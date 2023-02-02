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
 * Kotlin extension function for [org.gradle.api.tasks.TaskContainer.create].
 *
 * @see org.gradle.api.tasks.TaskContainer.create
 */
inline fun org.gradle.api.tasks.TaskContainer.`create`(vararg `options`: Pair<String, Any?>): org.gradle.api.Task =
    `create`(mapOf(*`options`))


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.tasks.TaskContainer.create].
 *
 * @see org.gradle.api.tasks.TaskContainer.create
 */
inline fun <T : org.gradle.api.Task> org.gradle.api.tasks.TaskContainer.`create`(`name`: String, `type`: kotlin.reflect.KClass<T>): T =
    `create`(`name`, `type`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.tasks.TaskContainer.create].
 *
 * @see org.gradle.api.tasks.TaskContainer.create
 */
inline fun <T : org.gradle.api.Task> org.gradle.api.tasks.TaskContainer.`create`(`name`: String, `type`: kotlin.reflect.KClass<T>, vararg `constructorArgs`: Any): T =
    `create`(`name`, `type`.java, *`constructorArgs`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.tasks.TaskContainer.create].
 *
 * @see org.gradle.api.tasks.TaskContainer.create
 */
inline fun <T : org.gradle.api.Task> org.gradle.api.tasks.TaskContainer.`create`(`name`: String, `type`: kotlin.reflect.KClass<T>, `configuration`: org.gradle.api.Action<in T>): T =
    `create`(`name`, `type`.java, `configuration`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.tasks.TaskContainer.register].
 *
 * @see org.gradle.api.tasks.TaskContainer.register
 */
inline fun <T : org.gradle.api.Task> org.gradle.api.tasks.TaskContainer.`register`(`name`: String, `type`: kotlin.reflect.KClass<T>, `configurationAction`: org.gradle.api.Action<in T>): org.gradle.api.tasks.TaskProvider<T> =
    `register`(`name`, `type`.java, `configurationAction`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.tasks.TaskContainer.register].
 *
 * @see org.gradle.api.tasks.TaskContainer.register
 */
inline fun <T : org.gradle.api.Task> org.gradle.api.tasks.TaskContainer.`register`(`name`: String, `type`: kotlin.reflect.KClass<T>): org.gradle.api.tasks.TaskProvider<T> =
    `register`(`name`, `type`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.tasks.TaskContainer.register].
 *
 * @see org.gradle.api.tasks.TaskContainer.register
 */
inline fun <T : org.gradle.api.Task> org.gradle.api.tasks.TaskContainer.`register`(`name`: String, `type`: kotlin.reflect.KClass<T>, vararg `constructorArgs`: Any): org.gradle.api.tasks.TaskProvider<T> =
    `register`(`name`, `type`.java, *`constructorArgs`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.tasks.TaskContainer.replace].
 *
 * @see org.gradle.api.tasks.TaskContainer.replace
 */
inline fun <T : org.gradle.api.Task> org.gradle.api.tasks.TaskContainer.`replace`(`name`: String, `type`: kotlin.reflect.KClass<T>): T =
    `replace`(`name`, `type`.java)

