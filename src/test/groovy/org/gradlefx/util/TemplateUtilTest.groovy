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

package org.gradlefx.util

import spock.lang.Specification
import org.gradlefx.conventions.GradleFxConvention
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import org.gradlefx.plugins.GradleFxPlugin
import org.gradlefx.conventions.FlexType

class TemplateUtilTest extends Specification {

    StubTemplateUtilBase templateUtil
    Project project
    GradleFxConvention flexConvention

    def setup() {
        project = ProjectBuilder.builder().build()
        new GradleFxPlugin().apply(project)
        flexConvention = (GradleFxConvention) project.convention.plugins.flex
        flexConvention.type = FlexType.swf

        templateUtil = new StubTemplateUtilBase(project, flexConvention);
    }

    def "writeContent should retain backslashes"() {
        setup:
            InputStream template = getClass().getResourceAsStream("/templates/flashbuilder/swf-fx.actionScriptProperties")
            File destination = File.createTempFile("swf-fx", "actionScriptProperties")

        when:
            flexConvention.compilerArgs = ['\\my\\compiler\\arg\\with\\backslashes']
            templateUtil.writeContent(template, destination, true)

        then:
            Node xml = new XmlParser().parse(destination)
            xml.compiler.@additionalCompilerArguments == ['\\my\\compiler\\arg\\with\\backslashes']

        cleanup:
            destination.delete()
    }
}
