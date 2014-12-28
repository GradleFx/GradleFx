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
import spock.lang.Specification
import org.gradlefx.conventions.GradleFxConvention

class FlexSDKSpecifiedValidatorTest extends Specification {

    FlexSDKSpecifiedValidator validator = new FlexSDKSpecifiedValidator();

    Project project = ProjectBuilder.builder().build()
    String testResourceDir = './src/test/resources/'

    def setup() {
        validator.project = project
        validator.flexConvention = new GradleFxConvention(project)
        validator.flexConvention.type = FlexType.swf

    }

    def "if the 'flexHome' property isn't specified, add an error message"() {
        when:
            validator.flexConvention.flexHome = ''
            validator.execute()

        then:
            validator.getErrorMessages().size() == 1
    }
    
    def "if the 'flexHome' property contains an invalid path, add an error message"() {
        when:
            validator.flexConvention.flexHome = '/invalid/path/to/FlexSDK'
            validator.execute()

        then:
            validator.getErrorMessages().size() == 1
    }
    
    def "if the 'flexHome' dir contains no config-xml, add an error message"() {
        when:
            validator.flexConvention.flexHome = testResourceDir + 'flex-sdk-no-config'
            validator.execute()

        then:
            validator.getErrorMessages().size() == 1
    }
    
    def "if the 'flexHome' dir contains no libs, add an error message"() {
        when:
            validator.flexConvention.flexHome = testResourceDir + 'flex-sdk-no-libs'
            validator.execute()

        then:
            validator.getErrorMessages().size() == 1
    }
    
    def "if the 'flexHome' dir contains no config-xml and no libs, add 2 error messages"() {
        when:
            validator.flexConvention.flexHome = testResourceDir + 'invalid-flex-sdk'
            validator.execute()

        then:
            validator.getErrorMessages().size() == 2
    }
    
    def "if the 'flexHome' property is valid, add no error messages"() { 
        when:
            validator.flexConvention.flexHome = testResourceDir + 'valid-flex-sdk'
            validator.execute()

        then:
            validator.getErrorMessages().size() == 0
    }
    
}
