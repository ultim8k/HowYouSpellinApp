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
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.model.ObjectFactory.named].
 *
 * @see org.gradle.api.model.ObjectFactory.named
 */
inline fun <T : org.gradle.api.Named> org.gradle.api.model.ObjectFactory.`named`(`type`: kotlin.reflect.KClass<T>, `name`: String): T =
    `named`(`type`.java, `name`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.model.ObjectFactory.newInstance].
 *
 * @see org.gradle.api.model.ObjectFactory.newInstance
 */
inline fun <T : Any> org.gradle.api.model.ObjectFactory.`newInstance`(`type`: kotlin.reflect.KClass<out T>, vararg `parameters`: Any): T =
    `newInstance`(`type`.java, *`parameters`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.model.ObjectFactory.domainObjectContainer].
 *
 * @see org.gradle.api.model.ObjectFactory.domainObjectContainer
 */
inline fun <T : Any> org.gradle.api.model.ObjectFactory.`domainObjectContainer`(`elementType`: kotlin.reflect.KClass<T>): org.gradle.api.NamedDomainObjectContainer<T> =
    `domainObjectContainer`(`elementType`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.model.ObjectFactory.domainObjectContainer].
 *
 * @see org.gradle.api.model.ObjectFactory.domainObjectContainer
 */
inline fun <T : Any> org.gradle.api.model.ObjectFactory.`domainObjectContainer`(`elementType`: kotlin.reflect.KClass<T>, `factory`: org.gradle.api.NamedDomainObjectFactory<T>): org.gradle.api.NamedDomainObjectContainer<T> =
    `domainObjectContainer`(`elementType`.java, `factory`)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.model.ObjectFactory.polymorphicDomainObjectContainer].
 *
 * @see org.gradle.api.model.ObjectFactory.polymorphicDomainObjectContainer
 */
inline fun <T : Any> org.gradle.api.model.ObjectFactory.`polymorphicDomainObjectContainer`(`elementType`: kotlin.reflect.KClass<T>): org.gradle.api.ExtensiblePolymorphicDomainObjectContainer<T> =
    `polymorphicDomainObjectContainer`(`elementType`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.model.ObjectFactory.domainObjectSet].
 *
 * @see org.gradle.api.model.ObjectFactory.domainObjectSet
 */
inline fun <T : Any> org.gradle.api.model.ObjectFactory.`domainObjectSet`(`elementType`: kotlin.reflect.KClass<T>): org.gradle.api.DomainObjectSet<T> =
    `domainObjectSet`(`elementType`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.model.ObjectFactory.namedDomainObjectSet].
 *
 * @see org.gradle.api.model.ObjectFactory.namedDomainObjectSet
 */
inline fun <T : Any> org.gradle.api.model.ObjectFactory.`namedDomainObjectSet`(`elementType`: kotlin.reflect.KClass<T>): org.gradle.api.NamedDomainObjectSet<T> =
    `namedDomainObjectSet`(`elementType`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.model.ObjectFactory.namedDomainObjectList].
 *
 * @see org.gradle.api.model.ObjectFactory.namedDomainObjectList
 */
inline fun <T : Any> org.gradle.api.model.ObjectFactory.`namedDomainObjectList`(`elementType`: kotlin.reflect.KClass<T>): org.gradle.api.NamedDomainObjectList<T> =
    `namedDomainObjectList`(`elementType`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.model.ObjectFactory.property].
 *
 * @see org.gradle.api.model.ObjectFactory.property
 */
inline fun <T : Any> org.gradle.api.model.ObjectFactory.`property`(`valueType`: kotlin.reflect.KClass<T>): org.gradle.api.provider.Property<T> =
    `property`(`valueType`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.model.ObjectFactory.listProperty].
 *
 * @see org.gradle.api.model.ObjectFactory.listProperty
 */
inline fun <T : Any> org.gradle.api.model.ObjectFactory.`listProperty`(`elementType`: kotlin.reflect.KClass<T>): org.gradle.api.provider.ListProperty<T> =
    `listProperty`(`elementType`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.model.ObjectFactory.setProperty].
 *
 * @see org.gradle.api.model.ObjectFactory.setProperty
 */
inline fun <T : Any> org.gradle.api.model.ObjectFactory.`setProperty`(`elementType`: kotlin.reflect.KClass<T>): org.gradle.api.provider.SetProperty<T> =
    `setProperty`(`elementType`.java)


/**
 * Kotlin extension function taking [kotlin.reflect.KClass] for [org.gradle.api.model.ObjectFactory.mapProperty].
 *
 * @see org.gradle.api.model.ObjectFactory.mapProperty
 */
inline fun <K : Any, V : Any> org.gradle.api.model.ObjectFactory.`mapProperty`(`keyType`: kotlin.reflect.KClass<K>, `valueType`: kotlin.reflect.KClass<V>): org.gradle.api.provider.MapProperty<K, V> =
    `mapProperty`(`keyType`.java, `valueType`.java)

