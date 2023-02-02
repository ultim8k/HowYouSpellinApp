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
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.plugins.PluginContainer.apply].
 *
 * @see org.gradle.api.plugins.PluginContainer.apply
 */
inline fun <T : org.gradle.api.Plugin<*>> org.gradle.api.plugins.PluginContainer.`apply`(`type`: kotlin.reflect.KClass<T>): T =
    `apply`(`type`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.plugins.PluginContainer.hasPlugin].
 *
 * @see org.gradle.api.plugins.PluginContainer.hasPlugin
 */
inline fun org.gradle.api.plugins.PluginContainer.`hasPlugin`(`type`: kotlin.reflect.KClass<out org.gradle.api.Plugin<*>>): Boolean =
    `hasPlugin`(`type`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.plugins.PluginContainer.findPlugin].
 *
 * @see org.gradle.api.plugins.PluginContainer.findPlugin
 */
inline fun <T : org.gradle.api.Plugin<*>> org.gradle.api.plugins.PluginContainer.`findPlugin`(`type`: kotlin.reflect.KClass<T>): T? =
    `findPlugin`(`type`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.plugins.PluginContainer.getPlugin].
 *
 * @see org.gradle.api.plugins.PluginContainer.getPlugin
 */
inline fun <T : org.gradle.api.Plugin<*>> org.gradle.api.plugins.PluginContainer.`getPlugin`(`type`: kotlin.reflect.KClass<T>): T =
    `getPlugin`(`type`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.plugins.PluginContainer.getAt].
 *
 * @see org.gradle.api.plugins.PluginContainer.getAt
 */
inline fun <T : org.gradle.api.Plugin<*>> org.gradle.api.plugins.PluginContainer.`getAt`(`type`: kotlin.reflect.KClass<T>): T =
    `getAt`(`type`.java)

