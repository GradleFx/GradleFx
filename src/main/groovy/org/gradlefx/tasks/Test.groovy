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

package org.gradlefx.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileTree
import org.gradle.api.file.FileTreeElement
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction
import org.gradlefx.configuration.FlexUnitAntTasksConfigurator
import org.gradlefx.conventions.FlexUnitConvention
import org.gradlefx.conventions.GradleFxConvention
import org.slf4j.Logger
import org.slf4j.LoggerFactory


/*
 * A Gradle task to execute FlexUnit tests.
 */
class Test extends DefaultTask {

    private static final Logger LOG = LoggerFactory.getLogger Test

    GradleFxConvention flexConvention;

    public Test() {
        description = "Run the FlexUnit tests."
        logging.setLevel LogLevel.INFO

        flexConvention = project.convention.plugins.flex
    }

    @TaskAction
    def runFlexUnit() {
        if(hasTests()) {
            configureAntWithFlexUnit()
            runTests()
        } else {
            LOG.info "Skipping tests since no tests exist"
        }
    }

    private boolean hasTests() {
        String nonEmptyTestDir = flexConvention.testDirs.find { String testDir ->
            if(project.file(testDir).exists()) {
                FileTree fileTree = project.fileTree(testDir)
                fileTree.includes = flexConvention.flexUnit.includes
                fileTree.excludes = flexConvention.flexUnit.excludes

                return !fileTree.empty
            } else {
                return false
            }
        }

        return nonEmptyTestDir != null
    }

    private void runTests() {
        FlexUnitConvention flexUnit = flexConvention.flexUnit
        File reportDir = project.file flexUnit.toDir

        // you can't write to a directory that doesn't exist
        if (!reportDir.exists()) reportDir.mkdirs()

        Set<File> libraries = project.configurations.internal.files +
                project.configurations.external.files +
                project.configurations.merged.files +
                project.configurations.test.files

        ant.flexunit (
            player:          flexUnit.player,
            command:         flexUnit.command,
            toDir:           flexUnit.toDir,
            workingDir:      flexUnit.workingDir,
            haltonfailure:   flexUnit.haltOnFailure,
            verbose:         flexUnit.verbose,
            localTrusted:    flexUnit.localTrusted,
            port:            flexUnit.port,
            buffer:          flexUnit.buffer,
            timeout:         flexUnit.timeout,
            failureproperty: flexUnit.failureProperty,
            headless:        flexUnit.headless,
            display:         flexUnit.display) {

            flexConvention.srcDirs.each { String srcDir ->
                source(dir: project.file(srcDir).path)
            }

            flexConvention.testResourceDirs.each { String testResourceDir ->
                if(project.file(testResourceDir).exists()) {
                    source(dir: project.file(testResourceDir).path)
                }
            }

            flexConvention.testDirs.each { String testDir ->
                FileTree fileTree = project.fileTree(testDir)
                fileTree.includes = flexUnit.includes
                fileTree.excludes = flexUnit.excludes

                fileTree.visit { FileTreeElement includedFile ->
                    testSource(dir: project.file(testDir).path) {
                        include(name: includedFile.relativePath)
                    }
                }
            }

            libraries.each { File libraryFile ->
                library(dir: libraryFile.parent) {
                    include(name: libraryFile.name)
                }
            }

        }

        if (ant.properties[flexUnit.failureProperty] == "true") {
            throw new Exception("Tests failed");
        }
    }

    private void configureAntWithFlexUnit() {
        new FlexUnitAntTasksConfigurator(project).configure()
    }
    
}
