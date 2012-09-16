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

class ASDocCommandLineInstructionTest extends Specification {

    Project project
    ASDocCommandLineInstruction commandLineInstruction

    def setup() {
        project = ProjectBuilder.builder().build()
        new GradleFxPlugin().apply(project)
        commandLineInstruction = new ASDocCommandLineInstruction(project)
    }

    def "call keepXML, should add -keep-xml with value true to compiler arguments"() {
        when:
            commandLineInstruction.keepXML()
        then:
            commandLineInstruction.arguments.contains("${CompilerOption.KEEP_XML}=true")
    }
}
