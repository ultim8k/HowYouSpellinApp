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
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.model.ModelMap.withType].
 *
 * @see org.gradle.model.ModelMap.withType
 */
@org.gradle.api.Incubating
inline fun <S : Any, T : Any> org.gradle.model.ModelMap<T>.`withType`(`type`: kotlin.reflect.KClass<S>): org.gradle.model.ModelMap<S> =
    `withType`(`type`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.model.ModelMap.create].
 *
 * @see org.gradle.model.ModelMap.create
 */
@org.gradle.api.Incubating
inline fun <S : T, T : Any> org.gradle.model.ModelMap<T>.`create`(`name`: String, `type`: kotlin.reflect.KClass<S>): Unit =
    `create`(`name`, `type`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.model.ModelMap.create].
 *
 * @see org.gradle.model.ModelMap.create
 */
@org.gradle.api.Incubating
inline fun <S : T, T : Any> org.gradle.model.ModelMap<T>.`create`(`name`: String, `type`: kotlin.reflect.KClass<S>, `configAction`: org.gradle.api.Action<in S>): Unit =
    `create`(`name`, `type`.java, `configAction`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.model.ModelMap.named].
 *
 * @see org.gradle.model.ModelMap.named
 */
@org.gradle.api.Incubating
inline fun <T : Any> org.gradle.model.ModelMap<T>.`named`(`name`: String, `ruleSource`: kotlin.reflect.KClass<out org.gradle.model.RuleSource>): Unit =
    `named`(`name`, `ruleSource`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.model.ModelMap.beforeEach].
 *
 * @see org.gradle.model.ModelMap.beforeEach
 */
@org.gradle.api.Incubating
inline fun <S : Any, T : Any> org.gradle.model.ModelMap<T>.`beforeEach`(`type`: kotlin.reflect.KClass<S>, `configAction`: org.gradle.api.Action<in S>): Unit =
    `beforeEach`(`type`.java, `configAction`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.model.ModelMap.withType].
 *
 * @see org.gradle.model.ModelMap.withType
 */
@org.gradle.api.Incubating
inline fun <S : Any, T : Any> org.gradle.model.ModelMap<T>.`withType`(`type`: kotlin.reflect.KClass<S>, `configAction`: org.gradle.api.Action<in S>): Unit =
    `withType`(`type`.java, `configAction`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.model.ModelMap.withType].
 *
 * @see org.gradle.model.ModelMap.withType
 */
@org.gradle.api.Incubating
inline fun <S : Any, T : Any> org.gradle.model.ModelMap<T>.`withType`(`type`: kotlin.reflect.KClass<S>, `rules`: kotlin.reflect.KClass<out org.gradle.model.RuleSource>): Unit =
    `withType`(`type`.java, `rules`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.model.ModelMap.afterEach].
 *
 * @see org.gradle.model.ModelMap.afterEach
 */
@org.gradle.api.Incubating
inline fun <S : Any, T : Any> org.gradle.model.ModelMap<T>.`afterEach`(`type`: kotlin.reflect.KClass<S>, `configAction`: org.gradle.api.Action<in S>): Unit =
    `afterEach`(`type`.java, `configAction`)

