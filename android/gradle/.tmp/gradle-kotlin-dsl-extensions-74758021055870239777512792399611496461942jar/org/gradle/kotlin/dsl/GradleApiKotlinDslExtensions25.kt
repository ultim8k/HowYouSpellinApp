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
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.plugins.ExtensionContainer.create].
 *
 * @see org.gradle.api.plugins.ExtensionContainer.create
 */
inline fun <T : Any> org.gradle.api.plugins.ExtensionContainer.`create`(`publicType`: org.gradle.api.reflect.TypeOf<T>, `name`: String, `instanceType`: kotlin.reflect.KClass<out T>, vararg `constructionArguments`: Any): T =
    `create`(`publicType`, `name`, `instanceType`.java, *`constructionArguments`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.plugins.ExtensionContainer.add].
 *
 * @see org.gradle.api.plugins.ExtensionContainer.add
 */
inline fun <T : Any> org.gradle.api.plugins.ExtensionContainer.`add`(`publicType`: kotlin.reflect.KClass<T>, `name`: String, `extension`: T): Unit =
    `add`(`publicType`.java, `name`, `extension`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.plugins.ExtensionContainer.create].
 *
 * @see org.gradle.api.plugins.ExtensionContainer.create
 */
inline fun <T : Any> org.gradle.api.plugins.ExtensionContainer.`create`(`publicType`: kotlin.reflect.KClass<T>, `name`: String, `instanceType`: kotlin.reflect.KClass<out T>, vararg `constructionArguments`: Any): T =
    `create`(`publicType`.java, `name`, `instanceType`.java, *`constructionArguments`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.plugins.ExtensionContainer.create].
 *
 * @see org.gradle.api.plugins.ExtensionContainer.create
 */
inline fun <T : Any> org.gradle.api.plugins.ExtensionContainer.`create`(`name`: String, `type`: kotlin.reflect.KClass<T>, vararg `constructionArguments`: Any): T =
    `create`(`name`, `type`.java, *`constructionArguments`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.plugins.ExtensionContainer.getByType].
 *
 * @see org.gradle.api.plugins.ExtensionContainer.getByType
 */
inline fun <T : Any> org.gradle.api.plugins.ExtensionContainer.`getByType`(`type`: kotlin.reflect.KClass<T>): T =
    `getByType`(`type`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.plugins.ExtensionContainer.findByType].
 *
 * @see org.gradle.api.plugins.ExtensionContainer.findByType
 */
inline fun <T : Any> org.gradle.api.plugins.ExtensionContainer.`findByType`(`type`: kotlin.reflect.KClass<T>): T? =
    `findByType`(`type`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.plugins.ExtensionContainer.configure].
 *
 * @see org.gradle.api.plugins.ExtensionContainer.configure
 */
inline fun <T : Any> org.gradle.api.plugins.ExtensionContainer.`configure`(`type`: kotlin.reflect.KClass<T>, `action`: org.gradle.api.Action<in T>): Unit =
    `configure`(`type`.java, `action`)

