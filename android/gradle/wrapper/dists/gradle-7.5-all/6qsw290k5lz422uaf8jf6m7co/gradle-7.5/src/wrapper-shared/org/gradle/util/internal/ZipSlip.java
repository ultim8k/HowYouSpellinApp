/*
 * Copyright 2022 the original author or authors.
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

package org.gradle.util.internal;

import java.io.File;
import java.util.Locale;

import static java.lang.String.format;

public class ZipSlip {

    /**
     * Checks the entry name for zip-slip vulnerable sequences.
     *
     * <b>IMPLEMENTATION NOTE</b>
     * We do it this way instead of the way recommended in https://snyk.io/research/zip-slip-vulnerability
     * for performance reasons, calling {@link File#getCanonicalPath()} is too expensive.
     *
     * @throws IllegalArgumentException if the entry contains vulnerable sequences
     */
    public static String safeZipEntryName(String name) {
        if (isUnsafeZipEntryName(name)) {
            throw new IllegalArgumentException(format("'%s' is not a safe zip entry name.", name));
        }
        return name;
    }

    public static boolean isUnsafeZipEntryName(String name) {
        return name.isEmpty()
            || name.startsWith("/")
            || name.startsWith("\\")
            || name.contains("..")
            || (name.contains(":") && isWindows());
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase(Locale.US).contains("windows");
    }
}
