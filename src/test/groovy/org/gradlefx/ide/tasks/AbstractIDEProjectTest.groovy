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

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import org.gradle.api.Project
import spock.lang.Shared

class AbstractIDEProjectTest extends Specification {

    private static final String ACTIONSCRIPT_PROPERTIES_FILENAME = "swf-fx.actionScriptProperties"
    private static final IDE_PROJECT_TASK_NAME = "ideProjectTask"
    private static final String COMPILER_ARGUMENT = "-link-report=C:/my/path"

    File actionscriptPropertiesSourceFile
    File projectDir = new File(getClass().getResource("/stub-project-dir").toURI())
    AbstractIDEProject ideProjectTask;

    def setup() {
        actionscriptPropertiesSourceFile = new File(getClass().getResource("/stub-project-dir/" + ACTIONSCRIPT_PROPERTIES_FILENAME).toURI())

        Project project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        ideProjectTask = project.tasks.add(IDE_PROJECT_TASK_NAME, StubAbstractIDEProject)
    }

    def "editXmlFile should save closure changes"() {
        when:
            ideProjectTask.editXmlFile ACTIONSCRIPT_PROPERTIES_FILENAME, { xml ->
                xml.compiler.@additionalCompilerArguments = COMPILER_ARGUMENT
            }

        then:
            Node xml = new XmlParser().parse(actionscriptPropertiesSourceFile)
            xml.compiler.@additionalCompilerArguments == [COMPILER_ARGUMENT]
    }
}
