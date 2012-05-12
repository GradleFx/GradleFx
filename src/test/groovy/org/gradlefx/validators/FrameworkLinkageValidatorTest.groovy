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
import org.gradlefx.FlexType;
import org.gradlefx.FrameworkLinkage;
import spock.lang.Specification
import org.gradlefx.conventions.GradleFxConvention

class FrameworkLinkageValidatorTest extends Specification {

    FrameworkLinkageValidator validator = new FrameworkLinkageValidator();

    Project project = ProjectBuilder.builder().build();

    def setup() {
        validator.project = project
        validator.flexConvention = new GradleFxConvention(project)
    }

    def "a combination of library project and RSL linkage should add error message"() {
        when:
            validator.flexConvention.type = FlexType.swc
            validator.flexConvention.frameworkLinkage = FrameworkLinkage.rsl
            validator.execute();

        then:
            validator.getErrorMessages().size() == 1
    }
    
    def "a combination of webapp project and external linkage should add error message"() {
        when:
            validator.flexConvention.type = FlexType.swf
            validator.flexConvention.frameworkLinkage = FrameworkLinkage.external
            validator.execute();

        then:
            validator.getErrorMessages().size() == 1
    }
    
    def "a combination of AIR project and external linkage should add error message"() {
        when:
            validator.flexConvention.type = FlexType.air
            validator.flexConvention.frameworkLinkage = FrameworkLinkage.external
            validator.execute();

        then:
            validator.getErrorMessages().size() == 1
    }
    
    def "any other combination should not add error messages"() {
        when:
            //swf with none, RSL or merged
            validator.flexConvention.type = FlexType.swf
            validator.flexConvention.frameworkLinkage = FrameworkLinkage.none
            validator.execute();
            
            validator.flexConvention.frameworkLinkage = FrameworkLinkage.rsl
            validator.execute();
            
            validator.flexConvention.frameworkLinkage = FrameworkLinkage.merged
            validator.execute();
            
            //AIR with none, RSL or merged
            validator.flexConvention.type = FlexType.air
            validator.flexConvention.frameworkLinkage = FrameworkLinkage.none
            validator.execute();
            
            validator.flexConvention.frameworkLinkage = FrameworkLinkage.rsl
            validator.execute();
            
            validator.flexConvention.frameworkLinkage = FrameworkLinkage.merged
            validator.execute();
            
            //swc with none, external or merged
            validator.flexConvention.type = FlexType.swc
            validator.flexConvention.frameworkLinkage = FrameworkLinkage.none
            validator.execute();
            
            validator.flexConvention.frameworkLinkage = FrameworkLinkage.external
            validator.execute();
            
            validator.flexConvention.frameworkLinkage = FrameworkLinkage.merged
            validator.execute();

        then:
            validator.getErrorMessages().size() == 0
    }
    
}
