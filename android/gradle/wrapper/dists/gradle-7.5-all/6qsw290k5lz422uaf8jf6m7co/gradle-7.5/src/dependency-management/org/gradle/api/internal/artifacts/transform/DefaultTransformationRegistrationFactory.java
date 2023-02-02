/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.internal.artifacts.transform;

import com.google.common.collect.ImmutableMap;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.artifacts.transform.CacheableTransform;
import org.gradle.api.artifacts.transform.InputArtifact;
import org.gradle.api.artifacts.transform.InputArtifactDependencies;
import org.gradle.api.artifacts.transform.TransformAction;
import org.gradle.api.artifacts.transform.TransformParameters;
import org.gradle.api.internal.DocumentationRegistry;
import org.gradle.api.internal.DomainObjectContext;
import org.gradle.api.internal.artifacts.ArtifactTransformRegistration;
import org.gradle.api.internal.attributes.AttributeContainerInternal;
import org.gradle.api.internal.attributes.ImmutableAttributes;
import org.gradle.api.internal.file.FileCollectionFactory;
import org.gradle.api.internal.file.FileLookup;
import org.gradle.api.internal.tasks.properties.FileParameterUtils;
import org.gradle.api.internal.tasks.properties.InputFilePropertyType;
import org.gradle.api.internal.tasks.properties.PropertyValue;
import org.gradle.api.internal.tasks.properties.PropertyVisitor;
import org.gradle.api.internal.tasks.properties.PropertyWalker;
import org.gradle.api.internal.tasks.properties.TypeMetadata;
import org.gradle.api.internal.tasks.properties.TypeMetadataStore;
import org.gradle.api.tasks.FileNormalizer;
import org.gradle.internal.exceptions.DefaultMultiCauseException;
import org.gradle.internal.execution.fingerprint.InputFingerprinter;
import org.gradle.internal.fingerprint.DirectorySensitivity;
import org.gradle.internal.fingerprint.LineEndingSensitivity;
import org.gradle.internal.hash.ClassLoaderHierarchyHasher;
import org.gradle.internal.instantiation.InstantiationScheme;
import org.gradle.internal.isolation.IsolatableFactory;
import org.gradle.internal.model.CalculatedValueContainerFactory;
import org.gradle.internal.operations.BuildOperationExecutor;
import org.gradle.internal.reflect.DefaultTypeValidationContext;
import org.gradle.internal.reflect.PropertyMetadata;
import org.gradle.internal.reflect.validation.Severity;
import org.gradle.internal.service.ServiceLookup;
import org.gradle.model.internal.type.ModelType;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.stream.Collectors;

public class DefaultTransformationRegistrationFactory implements TransformationRegistrationFactory {

    private final BuildOperationExecutor buildOperationExecutor;
    private final IsolatableFactory isolatableFactory;
    private final ClassLoaderHierarchyHasher classLoaderHierarchyHasher;
    private final TransformerInvocationFactory transformerInvocationFactory;
    private final PropertyWalker parametersPropertyWalker;
    private final ServiceLookup internalServices;
    private final TypeMetadataStore actionMetadataStore;
    private final FileCollectionFactory fileCollectionFactory;
    private final FileLookup fileLookup;
    private final InputFingerprinter inputFingerprinter;
    private final CalculatedValueContainerFactory calculatedValueContainerFactory;
    private final DomainObjectContext owner;
    private final InstantiationScheme actionInstantiationScheme;
    private final InstantiationScheme legacyActionInstantiationScheme;
    private final DocumentationRegistry documentationRegistry;

    public DefaultTransformationRegistrationFactory(
        BuildOperationExecutor buildOperationExecutor,
        IsolatableFactory isolatableFactory,
        ClassLoaderHierarchyHasher classLoaderHierarchyHasher,
        TransformerInvocationFactory transformerInvocationFactory,
        FileCollectionFactory fileCollectionFactory,
        FileLookup fileLookup,
        InputFingerprinter inputFingerprinter,
        CalculatedValueContainerFactory calculatedValueContainerFactory,
        DomainObjectContext owner,
        ArtifactTransformParameterScheme parameterScheme,
        ArtifactTransformActionScheme actionScheme,
        ServiceLookup internalServices,
        DocumentationRegistry documentationRegistry
    ) {
        this.buildOperationExecutor = buildOperationExecutor;
        this.isolatableFactory = isolatableFactory;
        this.classLoaderHierarchyHasher = classLoaderHierarchyHasher;
        this.transformerInvocationFactory = transformerInvocationFactory;
        this.fileCollectionFactory = fileCollectionFactory;
        this.fileLookup = fileLookup;
        this.inputFingerprinter = inputFingerprinter;
        this.calculatedValueContainerFactory = calculatedValueContainerFactory;
        this.owner = owner;
        this.actionInstantiationScheme = actionScheme.getInstantiationScheme();
        this.actionMetadataStore = actionScheme.getInspectionScheme().getMetadataStore();
        this.legacyActionInstantiationScheme = actionScheme.getLegacyInstantiationScheme();
        this.parametersPropertyWalker = parameterScheme.getInspectionScheme().getPropertyWalker();
        this.internalServices = internalServices;
        this.documentationRegistry = documentationRegistry;
    }

    @Override
    public ArtifactTransformRegistration create(ImmutableAttributes from, ImmutableAttributes to, Class<? extends TransformAction<?>> implementation, @Nullable TransformParameters parameterObject) {
        TypeMetadata actionMetadata = actionMetadataStore.getTypeMetadata(implementation);
        boolean cacheable = implementation.isAnnotationPresent(CacheableTransform.class);
        DefaultTypeValidationContext validationContext = DefaultTypeValidationContext.withoutRootType(documentationRegistry, cacheable);
        actionMetadata.visitValidationFailures(null, validationContext);

        // Should retain this on the metadata rather than calculate on each invocation
        Class<? extends FileNormalizer> inputArtifactNormalizer = null;
        Class<? extends FileNormalizer> dependenciesNormalizer = null;
        DirectorySensitivity artifactDirectorySensitivity = DirectorySensitivity.DEFAULT;
        DirectorySensitivity dependenciesDirectorySensitivity = DirectorySensitivity.DEFAULT;
        LineEndingSensitivity artifactLineEndingSensitivity = LineEndingSensitivity.DEFAULT;
        LineEndingSensitivity dependenciesLineEndingSensitivity = LineEndingSensitivity.DEFAULT;
        for (PropertyMetadata propertyMetadata : actionMetadata.getPropertiesMetadata()) {
            Class<? extends Annotation> propertyType = propertyMetadata.getPropertyType();
            if (propertyType.equals(InputArtifact.class)) {
                // Should ask the annotation handler to figure this out instead
                NormalizerCollectingVisitor visitor = new NormalizerCollectingVisitor();
                actionMetadata.getAnnotationHandlerFor(propertyMetadata).visitPropertyValue(propertyMetadata.getPropertyName(), null, propertyMetadata, visitor, null);
                inputArtifactNormalizer = visitor.normalizer;
                artifactDirectorySensitivity = visitor.directorySensitivity;
                artifactLineEndingSensitivity = visitor.lineEndingSensitivity;
                DefaultTransformer.validateInputFileNormalizer(propertyMetadata.getPropertyName(), inputArtifactNormalizer, cacheable, validationContext);
            } else if (propertyType.equals(InputArtifactDependencies.class)) {
                NormalizerCollectingVisitor visitor = new NormalizerCollectingVisitor();
                actionMetadata.getAnnotationHandlerFor(propertyMetadata).visitPropertyValue(propertyMetadata.getPropertyName(), null, propertyMetadata, visitor, null);
                dependenciesNormalizer = visitor.normalizer;
                dependenciesDirectorySensitivity = visitor.directorySensitivity;
                dependenciesLineEndingSensitivity = visitor.lineEndingSensitivity;
                DefaultTransformer.validateInputFileNormalizer(propertyMetadata.getPropertyName(), dependenciesNormalizer, cacheable, validationContext);
            }
        }
        ImmutableMap<String, Severity> validationMessages = validationContext.getProblems();
        if (!validationMessages.isEmpty()) {
            String formatString = validationMessages.size() == 1
                ? "A problem was found with the configuration of %s."
                : "Some problems were found with the configuration of %s.";
            throw new DefaultMultiCauseException(
                String.format(formatString, ModelType.of(implementation).getDisplayName()),
                validationMessages.keySet().stream()
                    .sorted()
                    .map(InvalidUserDataException::new)
                    .collect(Collectors.toList())
            );
        }
        Transformer transformer = new DefaultTransformer(
            implementation,
            parameterObject,
            from,
            FileParameterUtils.normalizerOrDefault(inputArtifactNormalizer),
            FileParameterUtils.normalizerOrDefault(dependenciesNormalizer),
            cacheable,
            artifactDirectorySensitivity,
            dependenciesDirectorySensitivity,
            artifactLineEndingSensitivity,
            dependenciesLineEndingSensitivity,
            buildOperationExecutor,
            classLoaderHierarchyHasher,
            isolatableFactory,
            fileCollectionFactory,
            fileLookup,
            parametersPropertyWalker,
            actionInstantiationScheme,
            owner,
            calculatedValueContainerFactory,
            internalServices,
            documentationRegistry);

        return new DefaultArtifactTransformRegistration(from, to, new TransformationStep(transformer, transformerInvocationFactory, owner, inputFingerprinter));
    }

    @Override
    @SuppressWarnings("deprecation")
    public ArtifactTransformRegistration create(ImmutableAttributes from, ImmutableAttributes to, Class<? extends org.gradle.api.artifacts.transform.ArtifactTransform> implementation, Object[] params) {
        Transformer transformer = new LegacyTransformer(implementation, params, legacyActionInstantiationScheme, from, classLoaderHierarchyHasher, isolatableFactory);
        return new DefaultArtifactTransformRegistration(from, to, new TransformationStep(transformer, transformerInvocationFactory, owner, inputFingerprinter));
    }

    private static class DefaultArtifactTransformRegistration implements ArtifactTransformRegistration {
        private final ImmutableAttributes from;
        private final ImmutableAttributes to;
        private final TransformationStep transformationStep;

        public DefaultArtifactTransformRegistration(ImmutableAttributes from, ImmutableAttributes to, TransformationStep transformationStep) {
            this.from = from;
            this.to = to;
            this.transformationStep = transformationStep;
        }

        @Override
        public AttributeContainerInternal getFrom() {
            return from;
        }

        @Override
        public AttributeContainerInternal getTo() {
            return to;
        }

        @Override
        public TransformationStep getTransformationStep() {
            return transformationStep;
        }

        @Override
        public String toString() {
            return transformationStep + " transform from " + from + " to " + to;
        }
    }

    private static class NormalizerCollectingVisitor extends PropertyVisitor.Adapter {
        private Class<? extends FileNormalizer> normalizer;
        private DirectorySensitivity directorySensitivity = DirectorySensitivity.DEFAULT;
        private LineEndingSensitivity lineEndingSensitivity = LineEndingSensitivity.DEFAULT;

        @SuppressWarnings("deprecation")
        @Override
        public void visitInputFileProperty(
            String propertyName,
            boolean optional,
            boolean skipWhenEmpty,
            DirectorySensitivity directorySensitivity,
            LineEndingSensitivity lineEndingSensitivity,
            boolean incremental,
            @Nullable Class<? extends FileNormalizer> fileNormalizer,
            PropertyValue value,
            InputFilePropertyType filePropertyType
        ) {
            this.normalizer = fileNormalizer;
            this.directorySensitivity = directorySensitivity == DirectorySensitivity.UNSPECIFIED
                ? DirectorySensitivity.DEFAULT
                : directorySensitivity;
            this.lineEndingSensitivity = lineEndingSensitivity;
        }
    }
}
