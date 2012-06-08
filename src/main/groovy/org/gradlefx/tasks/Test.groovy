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
import org.gradle.api.tasks.TaskAction
import org.gradle.api.logging.LogLevel
import org.gradle.api.file.FileTree
import org.gradle.api.file.FileTreeElement
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.gradlefx.configuration.FlexUnitAntTasksConfigurator
import org.gradlefx.conventions.GradleFxConvention

/*
 * A Gradle task to execute FlexUnit tests.
 */

class Test extends DefaultTask {

    private static final Logger log = LoggerFactory.getLogger(Test)

    GradleFxConvention flexConvention;

    public Test() {
        description = "Run the FlexUnit tests."
        logging.setLevel(LogLevel.INFO)

        flexConvention = project.convention.plugins.flex
    }

    @TaskAction
    def runFlexUnit() {
        if(hasTests()) {
            configureAntWithFlexUnit()
            runTests()
        } else {
            log.info("Skipping tests since no tests exist")
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
        def reportDir = project.file(flexConvention.flexUnit.toDir)

        // you can't write to a directory that doesn't exist
        if(!reportDir.exists()) reportDir.mkdirs()

        Set<File> libraries = project.configurations.internal.files +
                project.configurations.external.files +
                project.configurations.merged.files +
                project.configurations.test.files

        ant.flexunit(
            player:          flexConvention.flexUnit.player,
            command:         flexConvention.flexUnit.command,
            toDir:           flexConvention.flexUnit.toDir,
            workingDir:      flexConvention.flexUnit.workingDir,
            haltonfailure:   flexConvention.flexUnit.haltOnFailure,
            verbose:         flexConvention.flexUnit.verbose,
            localTrusted:    flexConvention.flexUnit.localTrusted,
            port:            flexConvention.flexUnit.port,
            buffer:          flexConvention.flexUnit.buffer,
            timeout:         flexConvention.flexUnit.timeout,
            failureproperty: flexConvention.flexUnit.failureproperty,
            headless:        flexConvention.flexUnit.headless,
            display:         flexConvention.flexUnit.display) {

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
                fileTree.includes = flexConvention.flexUnit.includes
                fileTree.excludes = flexConvention.flexUnit.excludes

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

        if(ant.properties[flexConvention.flexUnit.failureproperty] == "true") {
            throw new Exception("Tests failed");
        }
    }

    private void configureAntWithFlexUnit() {
        new FlexUnitAntTasksConfigurator(project).configure()
    }
}