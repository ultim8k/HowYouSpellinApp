/*
 * Copyright 2022 the original author or authors.
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

package org.gradle.api.internal.tasks.properties.annotations;

import org.gradle.api.artifacts.result.ResolvedArtifactResult;
import org.gradle.api.provider.Provider;
import org.gradle.api.reflect.TypeOf;
import org.gradle.internal.reflect.PropertyMetadata;
import org.gradle.internal.reflect.problems.ValidationProblemId;
import org.gradle.internal.reflect.validation.TypeValidationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.gradle.internal.reflect.validation.Severity.ERROR;

class PropertyAnnotationHandlerSupport {

    static void validateUnsupportedPropertyValueTypes(
        PropertyMetadata propertyMetadata,
        TypeValidationContext validationContext,
        Class<? extends Annotation> annotationType
    ) {
        validateUnsupportedPropertyValueType(
            annotationType, unpackValueTypesOf(propertyMetadata),
            propertyMetadata, validationContext,
            ResolvedArtifactResult.class,
            "Extract artifact metadata and annotate with @Input",
            "Extract artifact files and annotate with @InputFiles"
        );
    }

    private static void validateUnsupportedPropertyValueType(
        Class<? extends Annotation> annotationType,
        List<Class<?>> valueTypes,
        PropertyMetadata propertyMetadata,
        TypeValidationContext validationContext,
        Class<?> unsupportedType,
        String... possibleSolutions
    ) {
        if (valueTypes.stream().anyMatch(unsupportedType::isAssignableFrom)) {
            validationContext.visitPropertyProblem(problem -> {
                    problem.withId(ValidationProblemId.UNSUPPORTED_VALUE_TYPE)
                        .forProperty(propertyMetadata.getPropertyName())
                        .reportAs(ERROR)
                        .withDescription(() -> String.format("has @%s annotation used on property of type '%s'", annotationType.getSimpleName(), TypeOf.typeOf(propertyMetadata.getGetterMethod().getGenericReturnType()).getSimpleName()))
                        .happensBecause(() -> String.format("%s is not supported on task properties annotated with @%s", unsupportedType.getSimpleName(), annotationType.getSimpleName()));
                    for (String possibleSolution : possibleSolutions) {
                        problem.addPossibleSolution(possibleSolution);
                    }
                    problem.documentedAt("validation_problems", "unsupported_value_type");
                }
            );
        }
    }

    private static List<Class<?>> unpackValueTypesOf(PropertyMetadata propertyMetadata) {
        List<Class<?>> unpackedValueTypes = new ArrayList<>();
        Method getter = propertyMetadata.getGetterMethod();
        Class<?> returnType = getter.getReturnType();
        if (Provider.class.isAssignableFrom(returnType)) {
            List<TypeOf<?>> typeArguments = TypeOf.typeOf(getter.getGenericReturnType()).getActualTypeArguments();
            for (TypeOf<?> typeArgument : typeArguments) {
                unpackedValueTypes.add(typeArgument.getConcreteClass());
            }
        } else {
            unpackedValueTypes.add(returnType);
        }
        return unpackedValueTypes;
    }

    private PropertyAnnotationHandlerSupport() {
    }
}
