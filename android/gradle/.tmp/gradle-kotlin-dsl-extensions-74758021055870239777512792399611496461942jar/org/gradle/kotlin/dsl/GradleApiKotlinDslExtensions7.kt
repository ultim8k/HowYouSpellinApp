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
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.PolymorphicDomainObjectContainer.create].
 *
 * @see org.gradle.api.PolymorphicDomainObjectContainer.create
 */
inline fun <U : T, T : Any> org.gradle.api.PolymorphicDomainObjectContainer<T>.`create`(`name`: String, `type`: kotlin.reflect.KClass<U>): U =
    `create`(`name`, `type`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.PolymorphicDomainObjectContainer.maybeCreate].
 *
 * @see org.gradle.api.PolymorphicDomainObjectContainer.maybeCreate
 */
inline fun <U : T, T : Any> org.gradle.api.PolymorphicDomainObjectContainer<T>.`maybeCreate`(`name`: String, `type`: kotlin.reflect.KClass<U>): U =
    `maybeCreate`(`name`, `type`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.PolymorphicDomainObjectContainer.create].
 *
 * @see org.gradle.api.PolymorphicDomainObjectContainer.create
 */
inline fun <U : T, T : Any> org.gradle.api.PolymorphicDomainObjectContainer<T>.`create`(`name`: String, `type`: kotlin.reflect.KClass<U>, `configuration`: org.gradle.api.Action<in U>): U =
    `create`(`name`, `type`.java, `configuration`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.PolymorphicDomainObjectContainer.containerWithType].
 *
 * @see org.gradle.api.PolymorphicDomainObjectContainer.containerWithType
 */
inline fun <U : T, T : Any> org.gradle.api.PolymorphicDomainObjectContainer<T>.`containerWithType`(`type`: kotlin.reflect.KClass<U>): org.gradle.api.NamedDomainObjectContainer<U> =
    `containerWithType`(`type`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.PolymorphicDomainObjectContainer.register].
 *
 * @see org.gradle.api.PolymorphicDomainObjectContainer.register
 */
inline fun <U : T, T : Any> org.gradle.api.PolymorphicDomainObjectContainer<T>.`register`(`name`: String, `type`: kotlin.reflect.KClass<U>, `configurationAction`: org.gradle.api.Action<in U>): org.gradle.api.NamedDomainObjectProvider<U> =
    `register`(`name`, `type`.java, `configurationAction`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.PolymorphicDomainObjectContainer.register].
 *
 * @see org.gradle.api.PolymorphicDomainObjectContainer.register
 */
inline fun <U : T, T : Any> org.gradle.api.PolymorphicDomainObjectContainer<T>.`register`(`name`: String, `type`: kotlin.reflect.KClass<U>): org.gradle.api.NamedDomainObjectProvider<U> =
    `register`(`name`, `type`.java)

