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

package org.gradlefx.ide.tasks

import org.apache.commons.io.FilenameUtils
import org.gradle.api.artifacts.Dependency
import org.gradle.api.internal.artifacts.dependencies.DefaultProjectDependency
import org.gradle.api.internal.artifacts.dependencies.DefaultSelfResolvingDependency
import org.gradlefx.configuration.Configurations
import org.gradlefx.conventions.FlexType
import org.gradlefx.conventions.FrameworkLinkage
import static java.util.UUID.randomUUID

class IdeaProject extends AbstractIDEProject {
    public static final String NAME = 'idea'
    private String imlFilename

    public IdeaProject() {
        super('IntelliJ IDEA')
    }

    @Override
    protected void invalidateConventions() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void createProjectConfig() {
        imlFilename = project.name + ".iml"
        createImlFile()
        updateConfiguration()
        addSourceDirs()
        addDependencies()
        updateFlexSdk()
    }

    def updateFlexSdk() {
        editXmlFile imlFilename, { xml ->
            if (!project.hasProperty('ideaFxModuleSdkName')) {
                return
            }
            def rootMgr = xml.component.find { it.'@name' == 'FlexBuildConfigurationManager' }
            rootMgr.configurations.configuration.dependencies.sdk.@'name' = project.property('ideaFxModuleSdkName')
            xml.component.find { it.'@name' == 'NewModuleRootManager' }.orderEntry.find { it.'@type' == 'jdk' }.@'jdkName' = project.property('ideaFxModuleSdkName')
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
                    Configurations.THEME_CONFIGURATION_NAME.configName()
            ].each { type ->
                project.configurations[type].allDependencies.each { Dependency dependency ->

                    if (dependency instanceof DefaultProjectDependency) {
                        def projectDependency = dependency as DefaultProjectDependency;
                        def entryProjectRef = new Node(entries, 'entry', ['module-name':projectDependency.dependencyProject.name, 'build-configuration-name':projectDependency.dependencyProject.name])
                        new Node(entryProjectRef, 'dependency', ['linkage':'Merged'])
                        new Node(rootMgr, 'orderEntry', [type:"module", 'module-name':projectDependency.dependencyProject.name]);
                    } else if (dependency instanceof DefaultSelfResolvingDependency) {
                        def selfDependency = dependency as DefaultSelfResolvingDependency;
                        selfDependency.source.files.each { file ->
                            def String uuid = file.name
                            def entry = new Node(entries, 'entry', ['library-id': uuid])
                            new Node(entry, 'dependency', ['linkage':'Merged'])

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
            switch (flexConvention.type) {
                case FlexType.swf:
                    configuration.attributes().remove('target-platform')
                    break;
                case FlexType.air:
                    configuration.attributes().remove('output-type')
                    configuration.@'target-platform' = 'Desktop'
                    configuration.'packaging-air-desktop'.@'package-file-name' = flexConvention.output
                    configuration.@'output-file' = "${flexConvention.output}.swf"
                    break;
                case FlexType.mobile:
                    break;
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
