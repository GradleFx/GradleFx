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

package org.gradlefx.cli

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradlefx.conventions.GradleFxConvention
import org.gradlefx.plugins.GradleFxPlugin
import spock.lang.Specification

class LibraryCommandLineInstructionTest extends Specification {

    Project project
    GradleFxConvention flexConvention
    LibraryCommandLineInstruction commandLineInstruction

    def setup() {
        File projectDir = new File(getClass().getResource("/stub-project-dir").toURI())
        project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        new GradleFxPlugin().apply(project)
        flexConvention = project.convention.plugins.flex
        commandLineInstruction = new LibraryCommandLineInstruction(project)
    }

    def "call addResources, should add all resource file locations to compiler arguments with -include-file"() {
        File level1File = new File(getClass().getResource("/stub-project-dir/stub-resource-dir/level1file.txt").toURI())
        File level2File = new File(getClass().getResource("/stub-project-dir/stub-resource-dir/level2/level2file.txt").toURI())

        when:
            flexConvention.resourceDirs = ['stub-resource-dir']
            commandLineInstruction.addResources()
        then:
            commandLineInstruction.arguments.get(0) == CompilerOption.INCLUDE_FILE.optionName
            commandLineInstruction.arguments.get(1) == "level1file.txt"
            commandLineInstruction.arguments.get(2) == level1File.path
            commandLineInstruction.arguments.get(3) == CompilerOption.INCLUDE_FILE.optionName
            commandLineInstruction.arguments.get(4) == "level2/level2file.txt"
            commandLineInstruction.arguments.get(5) == level2File.path
    }
}
