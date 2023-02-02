/*
 * Copyright 2018 the original author or authors.
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

@file:Suppress(
    "unused",
    "nothing_to_inline",
    "useless_cast",
    "unchecked_cast",
    "extension_shadowed_by_member",
    "redundant_projection",
    "RemoveRedundantBackticks",
    "ObjectPropertyName",
    "deprecation"
)
@file:org.gradle.api.Generated

/* ktlint-disable */

package org.gradle.kotlin.dsl


/**
 * Kotlin extension function for [org.gradle.api.artifacts.dsl.DependencyHandler.project].
 *
 * @see org.gradle.api.artifacts.dsl.DependencyHandler.project
 */
inline fun org.gradle.api.artifacts.dsl.DependencyHandler.`project`(vararg `notation`: Pair<String, Any?>): org.gradle.api.artifacts.Dependency =
    `project`(mapOf(*`notation`))


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.artifacts.dsl.DependencyHandler.registerTransform].
 *
 * @see org.gradle.api.artifacts.dsl.DependencyHandler.registerTransform
 */
inline fun <T : org.gradle.api.artifacts.transform.TransformParameters> org.gradle.api.artifacts.dsl.DependencyHandler.`registerTransform`(`actionType`: kotlin.reflect.KClass<out org.gradle.api.artifacts.transform.TransformAction<T>>, `registrationAction`: org.gradle.api.Action<in org.gradle.api.artifacts.transform.TransformSpec<T>>): Unit =
    `registerTransform`(`actionType`.java, `registrationAction`)

