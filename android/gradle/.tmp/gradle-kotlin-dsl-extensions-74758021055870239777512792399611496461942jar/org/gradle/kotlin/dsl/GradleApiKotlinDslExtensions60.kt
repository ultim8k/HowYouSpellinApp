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
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.language.BinaryCollection.get].
 *
 * @see org.gradle.language.BinaryCollection.get
 */
inline fun <S : Any, T : org.gradle.api.component.SoftwareComponent> org.gradle.language.BinaryCollection<T>.`get`(`type`: kotlin.reflect.KClass<S>, `spec`: org.gradle.api.specs.Spec<in S>): org.gradle.language.BinaryProvider<S> =
    `get`(`type`.java, `spec`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.language.BinaryCollection.whenElementKnown].
 *
 * @see org.gradle.language.BinaryCollection.whenElementKnown
 */
inline fun <S : Any, T : org.gradle.api.component.SoftwareComponent> org.gradle.language.BinaryCollection<T>.`whenElementKnown`(`type`: kotlin.reflect.KClass<S>, `action`: org.gradle.api.Action<in S>): Unit =
    `whenElementKnown`(`type`.java, `action`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.language.BinaryCollection.whenElementFinalized].
 *
 * @see org.gradle.language.BinaryCollection.whenElementFinalized
 */
inline fun <S : Any, T : org.gradle.api.component.SoftwareComponent> org.gradle.language.BinaryCollection<T>.`whenElementFinalized`(`type`: kotlin.reflect.KClass<S>, `action`: org.gradle.api.Action<in S>): Unit =
    `whenElementFinalized`(`type`.java, `action`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.language.BinaryCollection.configureEach].
 *
 * @see org.gradle.language.BinaryCollection.configureEach
 */
inline fun <S : Any, T : org.gradle.api.component.SoftwareComponent> org.gradle.language.BinaryCollection<T>.`configureEach`(`type`: kotlin.reflect.KClass<S>, `action`: org.gradle.api.Action<in S>): Unit =
    `configureEach`(`type`.java, `action`)

