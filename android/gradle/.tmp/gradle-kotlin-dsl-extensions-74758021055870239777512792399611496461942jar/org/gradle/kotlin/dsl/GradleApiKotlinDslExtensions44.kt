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
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.tooling.BuildController.getModel].
 *
 * @see org.gradle.tooling.BuildController.getModel
 */
inline fun <T : Any> org.gradle.tooling.BuildController.`getModel`(`modelType`: kotlin.reflect.KClass<T>): T =
    `getModel`(`modelType`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.tooling.BuildController.findModel].
 *
 * @see org.gradle.tooling.BuildController.findModel
 */
inline fun <T : Any> org.gradle.tooling.BuildController.`findModel`(`modelType`: kotlin.reflect.KClass<T>): T? =
    `findModel`(`modelType`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.tooling.BuildController.getModel].
 *
 * @see org.gradle.tooling.BuildController.getModel
 */
inline fun <T : Any> org.gradle.tooling.BuildController.`getModel`(`target`: org.gradle.tooling.model.Model, `modelType`: kotlin.reflect.KClass<T>): T =
    `getModel`(`target`, `modelType`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.tooling.BuildController.findModel].
 *
 * @see org.gradle.tooling.BuildController.findModel
 */
inline fun <T : Any> org.gradle.tooling.BuildController.`findModel`(`target`: org.gradle.tooling.model.Model, `modelType`: kotlin.reflect.KClass<T>): T? =
    `findModel`(`target`, `modelType`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.tooling.BuildController.getModel].
 *
 * @see org.gradle.tooling.BuildController.getModel
 */
inline fun <T : Any, P : Any> org.gradle.tooling.BuildController.`getModel`(`modelType`: kotlin.reflect.KClass<T>, `parameterType`: kotlin.reflect.KClass<P>, `parameterInitializer`: org.gradle.api.Action<in P>): T =
    `getModel`(`modelType`.java, `parameterType`.java, `parameterInitializer`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.tooling.BuildController.findModel].
 *
 * @see org.gradle.tooling.BuildController.findModel
 */
inline fun <T : Any, P : Any> org.gradle.tooling.BuildController.`findModel`(`modelType`: kotlin.reflect.KClass<T>, `parameterType`: kotlin.reflect.KClass<P>, `parameterInitializer`: org.gradle.api.Action<in P>): T? =
    `findModel`(`modelType`.java, `parameterType`.java, `parameterInitializer`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.tooling.BuildController.getModel].
 *
 * @see org.gradle.tooling.BuildController.getModel
 */
inline fun <T : Any, P : Any> org.gradle.tooling.BuildController.`getModel`(`target`: org.gradle.tooling.model.Model, `modelType`: kotlin.reflect.KClass<T>, `parameterType`: kotlin.reflect.KClass<P>, `parameterInitializer`: org.gradle.api.Action<in P>): T =
    `getModel`(`target`, `modelType`.java, `parameterType`.java, `parameterInitializer`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.tooling.BuildController.findModel].
 *
 * @see org.gradle.tooling.BuildController.findModel
 */
inline fun <T : Any, P : Any> org.gradle.tooling.BuildController.`findModel`(`target`: org.gradle.tooling.model.Model, `modelType`: kotlin.reflect.KClass<T>, `parameterType`: kotlin.reflect.KClass<P>, `parameterInitializer`: org.gradle.api.Action<in P>): T? =
    `findModel`(`target`, `modelType`.java, `parameterType`.java, `parameterInitializer`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.tooling.BuildController.getCanQueryProjectModelInParallel].
 *
 * @see org.gradle.tooling.BuildController.getCanQueryProjectModelInParallel
 */
@org.gradle.api.Incubating
inline fun org.gradle.tooling.BuildController.`getCanQueryProjectModelInParallel`(`modelType`: kotlin.reflect.KClass<*>): Boolean =
    `getCanQueryProjectModelInParallel`(`modelType`.java)

