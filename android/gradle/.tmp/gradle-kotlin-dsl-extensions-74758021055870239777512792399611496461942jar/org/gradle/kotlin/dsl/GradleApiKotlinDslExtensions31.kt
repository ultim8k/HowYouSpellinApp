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
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.provider.ProviderFactory.of].
 *
 * @see org.gradle.api.provider.ProviderFactory.of
 */
@org.gradle.api.Incubating
inline fun <T : Any, P : org.gradle.api.provider.ValueSourceParameters> org.gradle.api.provider.ProviderFactory.`of`(`valueSourceType`: kotlin.reflect.KClass<out org.gradle.api.provider.ValueSource<T, P>>, `configuration`: org.gradle.api.Action<in org.gradle.api.provider.ValueSourceSpec<P>>): org.gradle.api.provider.Provider<T> =
    `of`(`valueSourceType`.java, `configuration`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.provider.ProviderFactory.credentials].
 *
 * @see org.gradle.api.provider.ProviderFactory.credentials
 */
inline fun <T : org.gradle.api.credentials.Credentials> org.gradle.api.provider.ProviderFactory.`credentials`(`credentialsType`: kotlin.reflect.KClass<T>, `identity`: String): org.gradle.api.provider.Provider<T> =
    `credentials`(`credentialsType`.java, `identity`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.provider.ProviderFactory.credentials].
 *
 * @see org.gradle.api.provider.ProviderFactory.credentials
 */
inline fun <T : org.gradle.api.credentials.Credentials> org.gradle.api.provider.ProviderFactory.`credentials`(`credentialsType`: kotlin.reflect.KClass<T>, `identity`: org.gradle.api.provider.Provider<String>): org.gradle.api.provider.Provider<T> =
    `credentials`(`credentialsType`.java, `identity`)

