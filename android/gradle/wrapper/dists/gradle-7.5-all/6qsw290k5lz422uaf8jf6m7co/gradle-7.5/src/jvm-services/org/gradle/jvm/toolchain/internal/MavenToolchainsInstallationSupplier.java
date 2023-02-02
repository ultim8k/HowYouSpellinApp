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

package org.gradle.jvm.toolchain.internal;

import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.util.internal.MavenUtil;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MavenToolchainsInstallationSupplier extends AutoDetectingInstallationSupplier {

    private static final String PROPERTY_NAME = "org.gradle.java.installations.maven-toolchains-file";
    private static final String PARSE_EXPRESSION = "/toolchains/toolchain[type='jdk']/configuration/jdkHome//text()";
    private static final Logger LOGGER = Logging.getLogger(MavenToolchainsInstallationSupplier.class);

    private final Provider<String> toolchainLocation;
    private final XPathFactory xPathFactory;
    private final DocumentBuilderFactory documentBuilderFactory;
    private final FileResolver fileResolver;

    @Inject
    public MavenToolchainsInstallationSupplier(ProviderFactory factory, FileResolver fileResolver) {
        super(factory);
        toolchainLocation = factory.gradleProperty(PROPERTY_NAME).orElse(defaultMavenToolchainsDefinitionsLocation());
        xPathFactory = XPathFactory.newInstance();
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        this.fileResolver = fileResolver;
    }

    @Override
    protected Set<InstallationLocation> findCandidates() {
        File toolchainFile = fileResolver.resolve(toolchainLocation.get());
        if (toolchainFile.exists()) {
            try (FileInputStream toolchain = new FileInputStream(toolchainFile)) {
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                XPath xpath = xPathFactory.newXPath();
                XPathExpression expression = xpath.compile(PARSE_EXPRESSION);

                NodeList nodes = (NodeList) expression.evaluate(documentBuilder.parse(toolchain), XPathConstants.NODESET);
                Set<String> locations = new HashSet<>();
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node item = nodes.item(i);
                    if (item != null && item.getNodeType() == Node.TEXT_NODE) {
                        locations.add(item.getNodeValue().trim());
                    }
                }
                return locations.stream()
                    .map(jdkHome -> new InstallationLocation(new File(jdkHome), "Maven Toolchains"))
                    .collect(Collectors.toSet());
            } catch (IOException | ParserConfigurationException | SAXException | XPathExpressionException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("Java Toolchain auto-detection failed to parse Maven Toolchains located at %s", toolchainLocation), e);
                } else {
                    LOGGER.info(String.format("Java Toolchain auto-detection failed to parse Maven Toolchains located at %s", toolchainLocation));
                }
            }
        }
        return Collections.emptySet();
    }

    private String defaultMavenToolchainsDefinitionsLocation() {
        return new File(MavenUtil.getUserMavenDir(), "toolchains.xml").getAbsolutePath();
    }

}
