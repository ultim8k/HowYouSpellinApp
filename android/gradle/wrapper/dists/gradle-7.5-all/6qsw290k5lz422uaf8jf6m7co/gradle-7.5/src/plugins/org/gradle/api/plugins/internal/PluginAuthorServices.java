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
package org.gradle.api.plugins.internal;

import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.component.SoftwareComponentContainer;
import org.gradle.api.component.SoftwareComponentFactory;
import org.gradle.api.internal.tasks.DefaultSourceSetContainer;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.plugins.jvm.internal.DefaultJvmPluginServices;
import org.gradle.api.plugins.jvm.internal.JvmPluginServices;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.internal.Describables;
import org.gradle.internal.instantiation.InstanceGenerator;
import org.gradle.internal.instantiation.InstantiatorFactory;
import org.gradle.internal.reflect.Instantiator;
import org.gradle.internal.service.ServiceRegistration;
import org.gradle.internal.service.scopes.AbstractPluginServiceRegistry;

/**
 * Registers services that can be used by plugin authors to develop their plugins.
 */
public class PluginAuthorServices extends AbstractPluginServiceRegistry {
    @Override
    public void registerGlobalServices(ServiceRegistration registration) {
        registration.addProvider(new GlobalScopeServices());
    }

    @Override
    public void registerProjectServices(ServiceRegistration registration) {
        registration.addProvider(new ProjectScopeServices());
    }

    private static class GlobalScopeServices {
        SoftwareComponentFactory createSoftwareComponentFactory(Instantiator instantiator) {
            return new DefaultSoftwareComponentFactory(instantiator);
        }
    }

    private static class ProjectScopeServices {
        JvmPluginServices createJvmPluginServices(ConfigurationContainer configurations,
                                                  ObjectFactory objectFactory,
                                                  TaskContainer tasks,
                                                  SoftwareComponentContainer components,
                                                  InstantiatorFactory instantiatorFactory) {
            InstanceGenerator instantiator = instantiatorFactory.decorateScheme().instantiator();
            return instantiator.newInstanceWithDisplayName(DefaultJvmPluginServices.class,
                Describables.of("JVM Plugin Services"),
                configurations,
                objectFactory,
                tasks,
                components,
                instantiator);
        }

        SourceSetContainer createSourceSetContainer(ObjectFactory objectFactory) {
            return objectFactory.newInstance(DefaultSourceSetContainer.class);
        }
    }
}
