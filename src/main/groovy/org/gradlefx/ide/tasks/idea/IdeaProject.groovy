/*
* Copyright (c) 2011 the original author or authors
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

package org.gradlefx.ide.tasks.idea

import org.apache.commons.io.FilenameUtils
import org.gradle.api.artifacts.Dependency
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.FileTreeElement
import org.gradle.api.internal.artifacts.dependencies.DefaultProjectDependency
import org.gradle.api.internal.artifacts.dependencies.DefaultSelfResolvingDependency
import org.gradlefx.configuration.Configurations
import org.gradlefx.conventions.FlexType
import org.gradlefx.conventions.FrameworkLinkage
import org.gradlefx.ide.tasks.AbstractIDEProject

import static java.util.UUID.randomUUID

class IdeaProject extends AbstractIDEProject {

    public static final String NAME = 'idea'

    private String imlFilename

    public IdeaProject() {
        super('IntelliJ IDEA')
    }

    @Override
    protected void invalidateConventions() {
        //nothing to do here
    }

    @Override
    protected void createProjectConfig() {
        createIdeaDirectory();
        imlFilename = project.name + ".iml"
        createImlFile()
        addModule()
        updateConfiguration()
        addSourceDirs()
        addDependencies()
        updateFlexSdk()
        addCompilerOptions()
    }

    def addModule() {
        editXmlFile ".idea/modules.xml", { xml ->
            def moduleManager = xml.component.find { it.'@name' == 'ProjectModuleManager' }
            def modules = new Node(moduleManager, "modules")
            String imlFile = "${project.name}.iml"
            new Node(modules, "module", ['fileurl':"file://\$PROJECT_DIR\$/${imlFile}", 'filepath':"\$PROJECT_DIR\$/${imlFile}"])
        }
    }

    def createIdeaDirectory() {
        project.file(".idea").mkdir()
        ["modules.xml"].each { entry ->
            InputStream stream = getClass().getResourceAsStream("/templates/idea/template-${entry}")
            writeContent stream, project.file(".idea/${entry}"), true
        }
    }

    def addCompilerOptions() {
        editXmlFile imlFilename, { xml ->
            if (flexConvention.additionalCompilerOptions.empty) {
                return
            }

            def rootMgr = xml.component.find { it.'@name' == 'FlexBuildConfigurationManager' }
            def compilerOptions = rootMgr.configurations.configuration.'compiler-options'.first();
            def additionalOptions = new Node(compilerOptions, 'option', ['name': "additionalOptions"])
            additionalOptions.@'value' = flexConvention.additionalCompilerOptions.join(' ')
        }
    }

    def updateFlexSdk() {
        editXmlFile imlFilename, { xml ->

            def rootMgr = xml.component.find { it.'@name' == 'FlexBuildConfigurationManager' }
            def dependencies = rootMgr.configurations.configuration.dependencies.first()

            switch (flexConvention.frameworkLinkage) {
                case FrameworkLinkage.none:
                    dependencies.attributes().remove('framework-linkage')
                    break;
                case FrameworkLinkage.external:
                case FrameworkLinkage.rsl:
                    dependencies.@'framework-linkage' = 'Runtime'
                    break;
            }

            if (flexConvention.flexSdkName != null) {
                dependencies.sdk.@'name' = flexConvention.flexSdkName
                xml.component.find { it.'@name' == 'NewModuleRootManager' }.orderEntry.find { it.'@type' == 'jdk' }.@'jdkName' = flexConvention.flexSdkName
            }

        }
    }

    def addDependencies() {
        editXmlFile imlFilename, { xml ->
            def entries = xml.component.find { it.'@name' == 'FlexBuildConfigurationManager' }
                    .configurations.configuration.dependencies.entries.first()
            def rootMgr = xml.component.find { it.'@name' == 'NewModuleRootManager' }

            [
                    Configurations.INTERNAL_CONFIGURATION_NAME.configName(),
                    Configurations.EXTERNAL_CONFIGURATION_NAME.configName(),
                    Configurations.MERGE_CONFIGURATION_NAME.configName(),
                    Configurations.RSL_CONFIGURATION_NAME.configName(),
                    Configurations.TEST_CONFIGURATION_NAME.configName(),
                    Configurations.THEME_CONFIGURATION_NAME.configName()
            ].each { configType ->
                project.configurations[configType].allDependencies.each { Dependency dependency ->

                    if (dependency instanceof DefaultProjectDependency) {
                        def projectDependency = dependency as DefaultProjectDependency;
                        def entryProjectRef = new Node(entries, 'entry', ['module-name':projectDependency.dependencyProject.name, 'build-configuration-name':projectDependency.dependencyProject.name])
                        new Node(entryProjectRef, 'dependency', ['linkage': configTypeToLinkageType(configType) ])
                        new Node(rootMgr, 'orderEntry', [type:"module", 'module-name':projectDependency.dependencyProject.name]);
                    } else if (dependency instanceof DefaultSelfResolvingDependency) {
                        def selfDependency = dependency as DefaultSelfResolvingDependency;
                        selfDependency.source.files.each { file ->
                            def String uuid = file.name
                            def entry = new Node(entries, 'entry', ['library-id': uuid])
                            new Node(entry, 'dependency', ['linkage':configTypeToLinkageType(configType)])

                            def orderEntry = new Node(rootMgr, 'orderEntry', [type:"module-library"]);
                            def libNode = new Node(orderEntry, 'library', [name:file.name, type:"flex"])
                            new Node(libNode, 'properties', [id:uuid])
                            def classes = new Node(libNode, 'CLASSES')
                            new Node(classes, 'root', [url:"jar://\$MODULE_DIR\$/${FilenameUtils.separatorsToUnix(project.relativePath(file))}!/"]);
                            new Node(libNode, 'JAVADOC');
                            new Node(libNode, 'SOURCES');
                        }
                    }
                }

            }
        }
    }

    def configTypeToLinkageType(String configType) {
        switch (configType) {
            case Configurations.INTERNAL_CONFIGURATION_NAME.configName():
                "Include"
                break;
            case Configurations.EXTERNAL_CONFIGURATION_NAME.configName():
                "External"
                break;
            case Configurations.MERGE_CONFIGURATION_NAME.configName():
                "Merged"
                break;
            case Configurations.TEST_CONFIGURATION_NAME.configName():
                "Test"
                break;
        }
    }

    void createImlFile() {
        String path = "/templates/idea/template-iml.xml"
        InputStream stream = getClass().getResourceAsStream(path)

        writeContent stream, project.file(imlFilename), true
    }

    private void updateConfiguration() {
        editXmlFile imlFilename, { xml ->
            def configuration = xml.component.find { it.'@name' == 'FlexBuildConfigurationManager' }.configurations.configuration.first()
            configuration.@'pure-as' = flexConvention.frameworkLinkage == FrameworkLinkage.none;

            //setup main class
            switch (flexConvention.type) {
                case FlexType.swf:
                case FlexType.air:
                case FlexType.mobile:
                    configuration.@'main-class' = flexConvention.mainClassPath.replace('/', '.').replace('.as', '').replace('.mxml', '');
                    break;
            }

            //setup platform

            def isNativeLib = false;
            flexConvention.compilerArgs.every { String it ->
                isNativeLib = it == '+configname=air'
                return !isNativeLib
            }
            isNativeLib = (isNativeLib) || flexConvention.type.isNativeApp()

            switch (flexConvention.type) {
                case FlexType.swc:
                    if (isNativeLib) {
                        configuration.@'target-platform' = 'Desktop'
                    }
                    break;
                case FlexType.swf:
                    configuration.attributes().remove('output-type')
                    configuration.attributes().remove('target-platform')
                    break;
                case FlexType.air:
                    configuration.attributes().remove('output-type')
                    configuration.@'target-platform' = 'Desktop'
                    configuration.@'output-file' = "${flexConvention.output}.swf"
                    def packaging = configuration.'packaging-air-desktop'.first()
                    packaging.@'package-file-name' = flexConvention.output
                    packaging.@'use-generated-descriptor' = false
                    packaging.@'custom-descriptor-path' = "\$MODULE_DIR\$/${flexConvention.air.applicationDescriptor}"
                    new Node(packaging, 'AirSigningOptions',
                            ['keystore-path':"\$MODULE_DIR\$/${FilenameUtils.separatorsToUnix(project.relativePath(flexConvention.air.keystore))}", 'use-temp-certificate':false])
                    addFilesInPackage(packaging)
                    break;
                case FlexType.mobile:
                    configuration.attributes().remove('output-type')
                    configuration.@'target-platform' = 'Mobile'
                    configuration.'packaging-android'.@'package-file-name' = flexConvention.output
                    configuration.'packaging-ios'.@'package-file-name' = flexConvention.output
                    configuration.@'output-file' = "${flexConvention.output}.swf"

                    configuration.'packaging-android'.@'use-generated-descriptor' = false
                    configuration.'packaging-ios'.@'use-generated-descriptor' = false
                    configuration.'packaging-android'.@'custom-descriptor-path' = "\$MODULE_DIR\$/${flexConvention.air.applicationDescriptor}"
                    configuration.'packaging-ios'.@'custom-descriptor-path' = "\$MODULE_DIR\$/${flexConvention.air.applicationDescriptor}"

                    def packaging = flexConvention.airMobile.platform == 'android' ? configuration.'packaging-android'.first() : configuration.'packaging-ios'.first()
                    if (flexConvention.airMobile.platform == 'android') {
                        configuration.'packaging-android'.@'enabled' = true
                        new Node(packaging, 'AirSigningOptions',
                                ['keystore-path':"\$MODULE_DIR\$/${FilenameUtils.separatorsToUnix(project.relativePath(flexConvention.air.keystore))}", 'use-temp-certificate':false])
                    } else if (flexConvention.airMobile.platform == 'ios') {
                        configuration.'packaging-ios'.@'enabled' = true

                        def attrs = ['keystore-path':"\$MODULE_DIR\$/${FilenameUtils.separatorsToUnix(project.relativePath(flexConvention.air.keystore))}",
                                     'use-temp-certificate':false,
                                     sdk:flexConvention.airMobile.platformSdk
                                    ];
                        if (flexConvention.airMobile.provisioningProfile != null) {
                            attrs['provisioning-profile-path']  = "\$MODULE_DIR\$/${FilenameUtils.separatorsToUnix(project.relativePath(flexConvention.airMobile.provisioningProfile))}"
                        }

                        new Node(packaging, 'AirSigningOptions', attrs)

                    }

                    addFilesInPackage(packaging)
                    break;
            }

        }
    }

    private void addFilesInPackage(parent) {
        def filesParent = new Node(parent, 'files-to-package', [])
        flexConvention.air.includeFileTrees.each { ConfigurableFileTree fileTree ->
            fileTree.visit { FileTreeElement file ->
                if (!file.isDirectory()) {
                    new Node(filesParent, 'FilePathAndPathInPackage', ['file-path':file.file.absoluteFile, 'path-in-package':file.relativePath.toString()])
                }
            }
        }
    }

    private void addSourceDirs() {
        editXmlFile imlFilename, { xml ->
            def component = xml.component.find { it.'@name' == 'NewModuleRootManager' }

            def parent = new Node(component, 'content', [url: "file://\$MODULE_DIR\$"])

            def addSrcFolder = { isTest ->
                return {
                    new Node(parent, 'sourceFolder', [
                        url: "file://\$MODULE_DIR\$/" + it,
                        isTestSource: "$isTest"
                ]) }
            };

            flexConvention.srcDirs.each addSrcFolder(false)
            flexConvention.resourceDirs.each addSrcFolder(false)

            flexConvention.testDirs.each addSrcFolder(true)
            flexConvention.testResourceDirs.each addSrcFolder(true)
        }
    }
}
