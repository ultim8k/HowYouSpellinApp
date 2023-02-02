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

package org.gradle.api.services.internal;

import com.google.common.collect.ImmutableList;
import org.gradle.api.artifacts.component.BuildIdentifier;
import org.gradle.api.internal.provider.AbstractMinimalProvider;
import org.gradle.api.services.BuildService;
import org.gradle.api.services.BuildServiceParameters;
import org.gradle.internal.Try;
import org.gradle.internal.instantiation.InstantiationScheme;
import org.gradle.internal.isolated.IsolationScheme;
import org.gradle.internal.isolation.IsolatableFactory;
import org.gradle.internal.service.ServiceLookup;
import org.gradle.internal.service.ServiceRegistry;
import org.gradle.internal.state.Managed;

import javax.annotation.Nullable;

// TODO:configuration-cache - complain when used at configuration time, except when opted in to this
@SuppressWarnings("rawtypes")
public class BuildServiceProvider<T extends BuildService<P>, P extends BuildServiceParameters> extends AbstractMinimalProvider<T> implements Managed {

    public interface Listener {
        Listener EMPTY = provider -> {
        };

        void beforeGet(BuildServiceProvider<?, ?> provider);
    }

    private final BuildIdentifier buildIdentifier;
    private final String name;
    private final Class<T> implementationType;
    private final IsolationScheme<BuildService, BuildServiceParameters> isolationScheme;
    private final InstantiationScheme instantiationScheme;
    private final IsolatableFactory isolatableFactory;
    private final ServiceRegistry internalServices;
    private final Listener listener;
    private final P parameters;
    private Try<T> instance;

    public BuildServiceProvider(
        BuildIdentifier buildIdentifier,
        String name,
        Class<T> implementationType,
        @Nullable P parameters,
        IsolationScheme<BuildService, BuildServiceParameters> isolationScheme,
        InstantiationScheme instantiationScheme,
        IsolatableFactory isolatableFactory,
        ServiceRegistry internalServices,
        Listener listener
    ) {
        this.buildIdentifier = buildIdentifier;
        this.name = name;
        this.implementationType = implementationType;
        this.parameters = parameters;
        this.isolationScheme = isolationScheme;
        this.instantiationScheme = instantiationScheme;
        this.isolatableFactory = isolatableFactory;
        this.internalServices = internalServices;
        this.listener = listener;
    }

    /**
     * Returns the identifier for the build that owns this service.
     */
    public BuildIdentifier getBuildIdentifier() {
        return buildIdentifier;
    }

    public String getName() {
        return name;
    }

    public Class<T> getImplementationType() {
        return implementationType;
    }

    @Nullable
    public P getParameters() {
        return parameters;
    }

    @Nullable
    @Override
    public Class<T> getType() {
        return implementationType;
    }

    @Override
    public boolean calculatePresence(ValueConsumer consumer) {
        return true;
    }

    @Override
    public boolean isImmutable() {
        return true;
    }

    @Override
    public Object unpackState() {
        throw new UnsupportedOperationException("Build services cannot be serialized.");
    }

    @Override
    protected Value<? extends T> calculateOwnValue(ValueConsumer consumer) {
        return Value.of(getInstance());
    }

    private T getInstance() {
        listener.beforeGet(this);
        synchronized (this) {
            if (instance == null) {
                instance = instantiate();
            }
        }
        return instance.get();
    }

    private Try<T> instantiate() {
        // TODO - extract some shared infrastructure to take care of instantiation (eg which services are visible, strict vs lenient, decorated or not?)
        // TODO - should hold the project lock to do the isolation. Should work the same way as artifact transforms (a work node does the isolation, etc)
        P isolatedParameters = isolatableFactory.isolate(parameters).isolate();
        // TODO - reuse this in other places
        ServiceLookup instantiationServices = instantiationServicesFor(isolatedParameters);
        try {
            return Try.successful(instantiate(instantiationServices));
        } catch (Exception e) {
            return Try.failure(instantiationException(e));
        }
    }

    private ServiceLifecycleException instantiationException(Exception e) {
        return new ServiceLifecycleException("Failed to create service '" + name + "'.", e);
    }

    private T instantiate(ServiceLookup instantiationServices) {
        return instantiationScheme.withServices(instantiationServices).instantiator().newInstance(implementationType);
    }

    private ServiceLookup instantiationServicesFor(@Nullable P isolatedParameters) {
        return isolationScheme.servicesForImplementation(
            isolatedParameters,
            internalServices,
            ImmutableList.of(),
            serviceType -> false
        );
    }

    @Override
    public ExecutionTimeValue<? extends T> calculateExecutionTimeValue() {
        return ExecutionTimeValue.changingValue(this);
    }

    public void maybeStop() {
        synchronized (this) {
            if (instance != null) {
                instance.ifSuccessful(t -> {
                    if (t instanceof AutoCloseable) {
                        try {
                            ((AutoCloseable) t).close();
                        } catch (Exception e) {
                            throw new ServiceLifecycleException("Failed to stop service '" + name + "'.", e);
                        }
                    }
                });
            }
        }
    }
}
