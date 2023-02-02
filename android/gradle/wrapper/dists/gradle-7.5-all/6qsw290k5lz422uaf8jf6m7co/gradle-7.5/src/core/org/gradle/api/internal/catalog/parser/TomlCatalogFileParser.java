/*
 * Copyright 2021 the original author or authors.
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
package org.gradle.api.internal.catalog.parser;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Interners;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.initialization.dsl.VersionCatalogBuilder;
import org.gradle.api.internal.catalog.problems.VersionCatalogProblemBuilder;
import org.gradle.api.internal.catalog.problems.VersionCatalogProblemId;
import org.tomlj.Toml;
import org.tomlj.TomlArray;
import org.tomlj.TomlInvalidTypeException;
import org.tomlj.TomlParseError;
import org.tomlj.TomlParseResult;
import org.tomlj.TomlTable;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.gradle.api.internal.catalog.problems.DefaultCatalogProblemBuilder.buildProblem;
import static org.gradle.api.internal.catalog.problems.DefaultCatalogProblemBuilder.maybeThrowError;
import static org.gradle.problems.internal.RenderingUtils.oxfordListOf;

public class TomlCatalogFileParser {
    public static final String CURRENT_VERSION = "1.1";
    private static final Splitter SPLITTER = Splitter.on(":").trimResults();
    private static final String METADATA_KEY = "metadata";
    private static final String LIBRARIES_KEY = "libraries";
    private static final String BUNDLES_KEY = "bundles";
    private static final String VERSIONS_KEY = "versions";
    private static final String PLUGINS_KEY = "plugins";
    private static final Set<String> TOP_LEVEL_ELEMENTS = ImmutableSet.of(
        METADATA_KEY,
        LIBRARIES_KEY,
        BUNDLES_KEY,
        VERSIONS_KEY,
        PLUGINS_KEY
    );
    private static final Set<String> PLUGIN_COORDINATES = ImmutableSet.of(
        "id",
        "version"
    );
    private static final Set<String> LIBRARY_COORDINATES = ImmutableSet.of(
        "group",
        "name",
        "version",
        "module"
    );
    private static final Set<String> VERSION_KEYS = ImmutableSet.of(
        "ref",
        "require",
        "strictly",
        "prefer",
        "reject",
        "rejectAll"
    );

    public static void parse(InputStream in, VersionCatalogBuilder builder) throws IOException {
        StrictVersionParser strictVersionParser = new StrictVersionParser(Interners.newStrongInterner());
        TomlParseResult result = Toml.parse(in);
        assertNoParseErrors(result, builder);
        TomlTable metadataTable = result.getTable(METADATA_KEY);
        verifyMetadata(builder, metadataTable);
        TomlTable librariesTable = result.getTable(LIBRARIES_KEY);
        TomlTable bundlesTable = result.getTable(BUNDLES_KEY);
        TomlTable versionsTable = result.getTable(VERSIONS_KEY);
        TomlTable pluginsTable = result.getTable(PLUGINS_KEY);
        Sets.SetView<String> unknownTle = Sets.difference(result.keySet(), TOP_LEVEL_ELEMENTS);
        if (!unknownTle.isEmpty()) {
            throwVersionCatalogProblem(builder, VersionCatalogProblemId.TOML_SYNTAX_ERROR, spec ->
                spec.withShortDescription(() -> "Unknown top level elements " + unknownTle)
                    .happensBecause(() -> "TOML file contains an unexpected top-level element")
                    .addSolution(() -> "Make sure the top-level elements of your TOML file is one of " + oxfordListOf(TOP_LEVEL_ELEMENTS, "or"))
                    .documented());
        }
        parseLibraries(librariesTable, builder, strictVersionParser);
        parsePlugins(pluginsTable, builder, strictVersionParser);
        parseBundles(bundlesTable, builder);
        parseVersions(versionsTable, builder, strictVersionParser);
    }

    private static void assertNoParseErrors(TomlParseResult result, VersionCatalogBuilder builder) {
        if (result.hasErrors()) {
            List<TomlParseError> errors = result.errors();
            throwVersionCatalogProblem(builder, VersionCatalogProblemId.TOML_SYNTAX_ERROR, spec ->
                spec.withShortDescription(() -> "Parsing failed with " + errors.size() + " error" + (errors.size() > 1 ? "s" : ""))
                    .happensBecause(() -> {
                        StringBuilder reason = new StringBuilder();
                        for (TomlParseError error : errors) {
                            if (reason.length() > 0) {
                                reason.append("\n");
                            }
                            reason.append("At line ")
                                .append(error.position().line()).append(", column ")
                                .append(error.position().column())
                                .append(": ")
                                .append(error.getMessage());
                        }
                        return reason.toString();
                    })
                    .addSolution("Fix the TOML file according to the syntax described at https://toml.io")
                    .documented()
            );
        }
    }

    private static void verifyMetadata(VersionCatalogBuilder builder, @Nullable TomlTable metadataTable) {
        if (metadataTable != null) {
            String format = metadataTable.getString("format.version");
            if (format != null && !CURRENT_VERSION.equals(format)) {
                throwVersionCatalogProblem(builder, VersionCatalogProblemId.UNSUPPORTED_FORMAT_VERSION, spec ->
                    spec.withShortDescription(() -> "Unsupported version catalog format " + format)
                        .happensBecause(() -> "This version of Gradle only supports format version " + CURRENT_VERSION)
                        .addSolution(() -> "Try to upgrade to a newer version of Gradle which supports the catalog format version " + format)
                        .documented()
                );
            }
        }
    }

    private static void parseLibraries(@Nullable TomlTable librariesTable, VersionCatalogBuilder builder, StrictVersionParser strictVersionParser) {
        if (librariesTable == null) {
            return;
        }
        List<String> keys = librariesTable.keySet()
            .stream()
            .sorted(Comparator.comparing(String::length))
            .collect(Collectors.toList());
        for (String alias : keys) {
            parseLibrary(alias, librariesTable, builder, strictVersionParser);
        }
    }

    private static void parsePlugins(@Nullable TomlTable pluginsTable, VersionCatalogBuilder builder, StrictVersionParser strictVersionParser) {
        if (pluginsTable == null) {
            return;
        }
        List<String> keys = pluginsTable.keySet()
            .stream()
            .sorted(Comparator.comparing(String::length))
            .collect(Collectors.toList());
        for (String alias : keys) {
            parsePlugin(alias, pluginsTable, builder, strictVersionParser);
        }
    }

    private static void parseVersions(@Nullable TomlTable versionsTable, VersionCatalogBuilder builder, StrictVersionParser strictVersionParser) {
        if (versionsTable == null) {
            return;
        }
        List<String> keys = versionsTable.keySet()
            .stream()
            .sorted(Comparator.comparing(String::length))
            .collect(Collectors.toList());
        for (String alias : keys) {
            parseVersion(alias, versionsTable, builder, strictVersionParser);
        }
    }

    private static void parseBundles(@Nullable TomlTable bundlesTable, VersionCatalogBuilder builder) {
        if (bundlesTable == null) {
            return;
        }
        List<String> keys = bundlesTable.keySet().stream().sorted().collect(Collectors.toList());
        for (String alias : keys) {
            List<String> bundled = expectArray(builder, "bundle", alias, bundlesTable, alias).toList().stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
            builder.bundle(alias, bundled);
        }
    }

    @Nullable
    private static String expectString(VersionCatalogBuilder builder, String kind, String name, TomlTable table, @Nullable String element) {
        try {
            String path = name;
            if (element != null) {
                path += "." + element;
            }
            return notEmpty(builder, table.getString(path), element, name);
        } catch (TomlInvalidTypeException ex) {
            return throwUnexpectedTypeError(builder, kind, name, "a string", ex);
        }
    }

    private static <T> T throwUnexpectedTypeError(VersionCatalogBuilder builder, String kind, String name, String typeLabel, TomlInvalidTypeException ex) {
        return throwVersionCatalogProblem(builder, VersionCatalogProblemId.TOML_SYNTAX_ERROR, spec ->
            spec.withShortDescription(() -> "Unexpected type for " + kind + " '" + name + "'")
                .happensBecause(() -> "Expected " + typeLabel + " but " + StringUtils.uncapitalize(ex.getMessage()))
                .addSolution(() -> "Use " + typeLabel + " instead")
                .documented()
        );
    }

    @Nullable
    private static TomlArray expectArray(VersionCatalogBuilder builder, String kind, String alias, TomlTable table, String element) {
        try {
            return table.getArray(element);
        } catch (TomlInvalidTypeException ex) {
            return throwUnexpectedTypeError(builder, kind, alias, "an array", ex);
        }
    }

    @Nullable
    private static Boolean expectBoolean(VersionCatalogBuilder builder, String kind, String alias, TomlTable table, String element) {
        try {
            return table.getBoolean(element);
        } catch (TomlInvalidTypeException ex) {
            return throwUnexpectedTypeError(builder, kind, alias, "a boolean", ex);
        }
    }

    private static void expectedKeys(TomlTable table, Set<String> allowedKeys, String context) {
        Set<String> actualKeys = table.keySet();
        if (!allowedKeys.containsAll(actualKeys)) {
            Set<String> difference = Sets.difference(actualKeys, allowedKeys);
            throw new InvalidUserDataException("On " + context + " expected to find any of " + oxfordListOf(allowedKeys, "or")
                + " but found unexpected key" + (difference.size() > 1 ? "s " : " ") + oxfordListOf(difference, "and")
                + ".");
        }
    }

    private static void parseLibrary(String alias, TomlTable librariesTable, VersionCatalogBuilder builder, StrictVersionParser strictVersionParser) {
        Object gav = librariesTable.get(alias);
        if (gav instanceof String) {
            List<String> splitted = SPLITTER.splitToList((String) gav);
            if (splitted.size() == 3) {
                String group = notEmpty(builder, splitted.get(0), "group", alias);
                String name = notEmpty(builder, splitted.get(1), "name", alias);
                String version = notEmpty(builder, splitted.get(2), "version", alias);
                StrictVersionParser.RichVersion rich = strictVersionParser.parse(version);
                registerDependency(builder, alias, group, name, null, rich.require, rich.strictly, rich.prefer, null, null);
                return;
            } else {
                throwVersionCatalogProblem(builder, VersionCatalogProblemId.INVALID_DEPENDENCY_NOTATION, spec ->
                    spec.withShortDescription(() -> "On alias '" + alias + "' notation '" + gav + "' is not a valid dependency notation")
                        .happensBecause(() -> "When using a string to declare library coordinates, you must use a valid dependency notation")
                        .addSolution("Make sure that the coordinates consist of 3 parts separated by colons, eg: my.group:artifact:1.2")
                        .documented());
            }
        }
        if (gav instanceof TomlTable) {
            expectedKeys((TomlTable) gav, LIBRARY_COORDINATES, "library declaration '" + alias + "'");
        }
        String group = expectString(builder, "alias", alias, librariesTable, "group");
        String name = expectString(builder, "alias", alias, librariesTable, "name");
        Object version = librariesTable.get(alias + ".version");
        String mi = expectString(builder, "alias", alias, librariesTable, "module");
        if (mi != null) {
            List<String> splitted = SPLITTER.splitToList(mi);
            if (splitted.size() == 2) {
                group = notEmpty(builder, splitted.get(0), "group", alias);
                name = notEmpty(builder, splitted.get(1), "name", alias);
            } else {
                throwVersionCatalogProblem(builder, VersionCatalogProblemId.INVALID_MODULE_NOTATION, spec ->
                    spec.withShortDescription(() -> "On alias '" + alias + "' module '" + mi + "' is not a valid module notation")
                        .happensBecause(() -> "When using a string to declare library module coordinates, you must use a valid module notation")
                        .addSolution("Make sure that the module consist of 2 parts separated by colons, eg: my.group:artifact")
                        .documented());
            }
        }
        String versionRef = null;
        String require = null;
        String strictly = null;
        String prefer = null;
        List<String> rejectedVersions = null;
        Boolean rejectAll = null;
        if (version instanceof String) {
            require = (String) version;
            StrictVersionParser.RichVersion richVersion = strictVersionParser.parse(require);
            require = richVersion.require;
            prefer = richVersion.prefer;
            strictly = richVersion.strictly;
        } else if (version instanceof TomlTable) {
            TomlTable versionTable = (TomlTable) version;
            expectedKeys(versionTable, VERSION_KEYS, "version declaration of alias '" + alias + "'");
            versionRef = notEmpty(builder, versionTable.getString("ref"), "version reference", alias);
            require = notEmpty(builder, versionTable.getString("require"), "required version", alias);
            prefer = notEmpty(builder, versionTable.getString("prefer"), "preferred version", alias);
            strictly = notEmpty(builder, versionTable.getString("strictly"), "strict version", alias);
            TomlArray rejectedArray = expectArray(builder, "alias", alias, versionTable, "reject");
            rejectedVersions = rejectedArray != null ? rejectedArray.toList().stream()
                .map(String::valueOf)
                .map(v -> notEmpty(builder, v, "rejected version", alias))
                .collect(Collectors.toList()) : null;
            rejectAll = expectBoolean(builder, "alias", alias, versionTable, "rejectAll");
        } else if (version != null) {
            throwUnexpectedVersionSyntax(alias, builder, version);
        }
        if (group == null) {
            throwVersionCatalogProblem(builder, VersionCatalogProblemId.TOML_SYNTAX_ERROR, spec ->
                spec.withShortDescription(() -> "Alias definition '" + alias + "' is invalid")
                    .happensBecause(() -> "Group for alias '" + alias + "' wasn't set")
                    .addSolution("Add the 'group' element on alias '" + alias + "'")
                    .documented());
        }
        if (name == null) {
            throwVersionCatalogProblem(builder, VersionCatalogProblemId.TOML_SYNTAX_ERROR, spec ->
                spec.withShortDescription(() -> "Alias definition '" + alias + "' is invalid")
                    .happensBecause(() -> "Name for alias '" + alias + "' wasn't set")
                    .addSolution("Add the 'name' element on alias '" + alias + "'")
                    .documented());
        }
        registerDependency(builder, alias, group, name, versionRef, require, strictly, prefer, rejectedVersions, rejectAll);
    }

    private static void parsePlugin(String alias, TomlTable librariesTable, VersionCatalogBuilder builder, StrictVersionParser strictVersionParser) {
        Object coordinates = librariesTable.get(alias);
        if (coordinates instanceof String) {
            List<String> splitted = SPLITTER.splitToList((String) coordinates);
            if (splitted.size() == 2) {
                String id = notEmpty(builder, splitted.get(0), "id", alias);
                String version = notEmpty(builder, splitted.get(1), "version", alias);
                StrictVersionParser.RichVersion rich = strictVersionParser.parse(version);
                registerPlugin(builder, alias, id, null, rich.require, rich.strictly, rich.prefer, null, null);
                return;
            } else {
                throwVersionCatalogProblem(builder, VersionCatalogProblemId.INVALID_PLUGIN_NOTATION, spec ->
                    spec.withShortDescription(() -> "On alias '" + alias + "' notation '" + coordinates + "' is not a valid plugin notation")
                        .happensBecause(() -> "When using a string to declare plugin coordinates, you must use a valid plugin notation")
                        .addSolution("Make sure that the coordinates consist of 2 parts separated by colons, eg: my.plugin.id:1.2")
                        .documented());
            }
        }
        if (coordinates instanceof TomlTable) {
            expectedKeys((TomlTable) coordinates, PLUGIN_COORDINATES, "plugin declaration '" + alias + "'");
        }
        String id = expectString(builder, "alias", alias, librariesTable, "id");
        Object version = librariesTable.get(alias + ".version");
        String versionRef = null;
        String require = null;
        String strictly = null;
        String prefer = null;
        List<String> rejectedVersions = null;
        Boolean rejectAll = null;
        if (version instanceof String) {
            require = (String) version;
            StrictVersionParser.RichVersion richVersion = strictVersionParser.parse(require);
            require = richVersion.require;
            prefer = richVersion.prefer;
            strictly = richVersion.strictly;
        } else if (version instanceof TomlTable) {
            TomlTable versionTable = (TomlTable) version;
            expectedKeys(versionTable, VERSION_KEYS, "version declaration of alias '" + alias + "'");
            versionRef = notEmpty(builder, versionTable.getString("ref"), "version reference", alias);
            require = notEmpty(builder, versionTable.getString("require"), "required version", alias);
            prefer = notEmpty(builder, versionTable.getString("prefer"), "preferred version", alias);
            strictly = notEmpty(builder, versionTable.getString("strictly"), "strict version", alias);
            TomlArray rejectedArray = expectArray(builder, "alias", alias, versionTable, "reject");
            rejectedVersions = rejectedArray != null ? rejectedArray.toList().stream()
                .map(String::valueOf)
                .map(v -> notEmpty(builder, v, "rejected version", alias))
                .collect(Collectors.toList()) : null;
            rejectAll = expectBoolean(builder, "alias", alias, versionTable, "rejectAll");
        } else if (version != null) {
            throwUnexpectedVersionSyntax(alias, builder, version);
        }
        if (id == null) {
            throwVersionCatalogProblem(builder, VersionCatalogProblemId.TOML_SYNTAX_ERROR, spec ->
                spec.withShortDescription(() -> "Alias definition '" + alias + "' is invalid")
                    .happensBecause(() -> "Id for plugin alias '" + alias + "' wasn't set")
                    .addSolution("Add the 'id' element on alias '" + alias + "'")
                    .documented());
        }
        registerPlugin(builder, alias, id, versionRef, require, strictly, prefer, rejectedVersions, rejectAll);
    }

    private static void throwUnexpectedVersionSyntax(String alias, VersionCatalogBuilder builder, Object version) {
        throwVersionCatalogProblem(builder, VersionCatalogProblemId.TOML_SYNTAX_ERROR, spec ->
            spec.withShortDescription(() -> "Alias definition '" + alias + "' is invalid")
                .happensBecause(() -> "expected a version as a String or a table but got " + version.getClass().getSimpleName())
                .addSolution("Use a String notation, e.g version = \"1.1\"")
                .addSolution("Use a version reference, e.g version.ref = \"some-version\"")
                .addSolution("Use a rich version table, e.g version = { require=\"[1.0, 2.0[\", prefer=\"1.5\" }")
                .documented());
    }

    private static void parseVersion(String alias, TomlTable versionsTable, VersionCatalogBuilder builder, StrictVersionParser strictVersionParser) {
        String require = null;
        String strictly = null;
        String prefer = null;
        List<String> rejectedVersions = null;
        Boolean rejectAll = null;
        Object version = versionsTable.get(alias);
        if (version instanceof String) {
            require = notEmpty(builder, (String) version, "version", alias);
            StrictVersionParser.RichVersion richVersion = strictVersionParser.parse(require);
            require = richVersion.require;
            prefer = richVersion.prefer;
            strictly = richVersion.strictly;
        } else if (version instanceof TomlTable) {
            TomlTable versionTable = (TomlTable) version;
            require = notEmpty(builder, versionTable.getString("require"), "required version", alias);
            prefer = notEmpty(builder, versionTable.getString("prefer"), "preferred version", alias);
            strictly = notEmpty(builder, versionTable.getString("strictly"), "strict version", alias);
            TomlArray rejectedArray = expectArray(builder, "alias", alias, versionTable, "reject");
            rejectedVersions = rejectedArray != null ? rejectedArray.toList().stream()
                .map(String::valueOf)
                .map(v -> notEmpty(builder, v, "rejected version", alias))
                .collect(Collectors.toList()) : null;
            rejectAll = expectBoolean(builder, "alias", alias, versionTable, "rejectAll");
        } else if (version != null) {
            throwUnexpectedVersionSyntax(alias, builder, version);
        }
        registerVersion(builder, alias, require, strictly, prefer, rejectedVersions, rejectAll);
    }

    @Nullable
    private static String notEmpty(VersionCatalogBuilder builder, @Nullable String string, @Nullable String member, String alias) {
        if (string == null) {
            return null;
        }
        if (string.isEmpty()) {
            throwVersionCatalogProblem(builder, VersionCatalogProblemId.TOML_SYNTAX_ERROR, spec ->
                spec.withShortDescription(() -> "Alias definition '" + alias + "' is invalid")
                    .happensBecause(() -> (member == null ? "value" : StringUtils.capitalize(member)) + " for '" + alias + "' must not be empty")
                    .addSolution("Set a value for '" + member + "'")
                    .documented());
        }
        return string;
    }

    private static void registerDependency(VersionCatalogBuilder builder,
                                           String alias,
                                           String group,
                                           String name,
                                           @Nullable String versionRef,
                                           @Nullable String require,
                                           @Nullable String strictly,
                                           @Nullable String prefer,
                                           @Nullable List<String> rejectedVersions,
                                           @Nullable Boolean rejectAll) {
        VersionCatalogBuilder.LibraryAliasBuilder aliasBuilder = builder.library(alias, group, name);
        if (versionRef != null) {
            aliasBuilder.versionRef(versionRef);
            return;
        }
        aliasBuilder.version(v -> {
            if (require != null) {
                v.require(require);
            }
            if (strictly != null) {
                v.strictly(strictly);
            }
            if (prefer != null) {
                v.prefer(prefer);
            }
            if (rejectedVersions != null) {
                v.reject(rejectedVersions.toArray(new String[0]));
            }
            if (rejectAll != null && rejectAll) {
                v.rejectAll();
            }
        });
    }

    private static void registerPlugin(VersionCatalogBuilder builder,
                                           String alias,
                                           String id,
                                           @Nullable String versionRef,
                                           @Nullable String require,
                                           @Nullable String strictly,
                                           @Nullable String prefer,
                                           @Nullable List<String> rejectedVersions,
                                           @Nullable Boolean rejectAll) {
        VersionCatalogBuilder.PluginAliasBuilder aliasBuilder = builder.plugin(alias, id);
        if (versionRef != null) {
            aliasBuilder.versionRef(versionRef);
            return;
        }
        aliasBuilder.version(v -> {
            if (require != null) {
                v.require(require);
            }
            if (strictly != null) {
                v.strictly(strictly);
            }
            if (prefer != null) {
                v.prefer(prefer);
            }
            if (rejectedVersions != null) {
                v.reject(rejectedVersions.toArray(new String[0]));
            }
            if (rejectAll != null && rejectAll) {
                v.rejectAll();
            }
        });
    }

    private static void registerVersion(VersionCatalogBuilder builder,
                                        String alias,
                                        @Nullable String require,
                                        @Nullable String strictly,
                                        @Nullable String prefer,
                                        @Nullable List<String> rejectedVersions,
                                        @Nullable Boolean rejectAll) {
        builder.version(alias, v -> {
            if (require != null) {
                v.require(require);
            }
            if (strictly != null) {
                v.strictly(strictly);
            }
            if (prefer != null) {
                v.prefer(prefer);
            }
            if (rejectedVersions != null) {
                v.reject(rejectedVersions.toArray(new String[0]));
            }
            if (rejectAll != null && rejectAll) {
                v.rejectAll();
            }
        });
    }

    private static <T> T throwVersionCatalogProblem(VersionCatalogBuilder builder, VersionCatalogProblemId id, Consumer<? super VersionCatalogProblemBuilder.ProblemWithId> spec) {
        maybeThrowError("Invalid TOML catalog definition", ImmutableList.of(
            buildProblem(id, pb -> spec.accept(pb.inContext(() -> "version catalog " + builder.getName()))))
        );
        return null;
    }

}
