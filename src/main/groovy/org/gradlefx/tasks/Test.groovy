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

/*
 * A Gradle task to execute FlexUnit tests.
 */

class Test extends DefaultTask {

    private static final Logger log = LoggerFactory.getLogger(Test)

    public Test() {
        description = "Run the FlexUnit tests."
        logging.setLevel(LogLevel.INFO)
    }

    @TaskAction
    def runFlexUnit() {
        configureAntWithFlexUnit()

		if(hasTests()) {
            runTests()
        } else {
            log.info("Skipping tests since no tests exist")
        }
    }

    private boolean hasTests() {
        String nonEmptyTestDir = project.testDirs.find { String testDir ->
            if(project.file(testDir).exists()) {
                FileTree fileTree = project.fileTree(testDir)
                fileTree.includes = project.flexUnit.includes
                fileTree.excludes = project.flexUnit.excludes

                return !fileTree.empty
            } else {
                return false
            }
        }

        return nonEmptyTestDir != null
    }

    private void runTests() {
        def reportDir = project.file(project.flexUnit.toDir)

        // you can't write to a directory that doesn't exist
        if(!reportDir.exists()) reportDir.mkdirs()

        Set<File> libraries = project.configurations.internal.files +
                project.configurations.external.files +
                project.configurations.merged.files +
                project.configurations.test.files

        ant.flexunit(
            player:          project.flexUnit.player,
            command:         project.flexUnit.command,
            toDir:           project.flexUnit.toDir,
            workingDir:      project.flexUnit.workingDir,
            haltonfailure:   project.flexUnit.haltOnFailure,
            verbose:         project.flexUnit.verbose,
            localTrusted:    project.flexUnit.localTrusted,
            port:            project.flexUnit.port,
            buffer:          project.flexUnit.buffer,
            timeout:         project.flexUnit.timeout,
            failureproperty: project.flexUnit.failureproperty,
            headless:        project.flexUnit.headless,
            display:         project.flexUnit.display) {

            project.srcDirs.each { String srcDir ->
                source(dir: project.file(srcDir).path)
            }

            project.testResourceDirs.each { String testResourceDir ->
                if(project.file(testResourceDir).exists()) {
                    source(dir: project.file(testResourceDir).path)
                }
            }

            project.testDirs.each { String testDir ->
                FileTree fileTree = project.fileTree(testDir)
                fileTree.includes = project.flexUnit.includes
                fileTree.excludes = project.flexUnit.excludes

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

        if(ant.properties[project.flexUnit.failureproperty] == "true") {
            throw new Exception("Tests failed");
        }
    }

    private void configureAntWithFlexUnit() {
        new FlexUnitAntTasksConfigurator(project).configure()
    }
}