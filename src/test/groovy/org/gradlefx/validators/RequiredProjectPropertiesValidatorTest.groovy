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

package org.gradlefx.validators

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradlefx.conventions.FlexType
import org.gradlefx.conventions.GradleFxConvention
import spock.lang.Specification

class RequiredProjectPropertiesValidatorTest extends Specification {

    RequiredProjectPropertiesValidator validator = new RequiredProjectPropertiesValidator();

    Project project = ProjectBuilder.builder().build()

    def setup() {
        validator.project = project
        validator.flexConvention = new GradleFxConvention(project)
    }

    def "if the 'type' property isn't specified, add an error message"() {
        when:
            validator.flexConvention.type = null
            validator.execute()

        then:
            validator.getErrorMessages().size() == 1
    }

    def "if the 'type' property is specified, add no error messages"() {
        when:
            validator.flexConvention.type = FlexType.swf
            validator.execute()

        then:
            validator.getErrorMessages().size() == 0
    }

}
