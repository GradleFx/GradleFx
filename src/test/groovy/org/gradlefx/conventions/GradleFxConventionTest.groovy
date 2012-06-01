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

package org.gradlefx.conventions

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradlefx.FlexType;
import org.gradlefx.FrameworkLinkage;
import org.gradlefx.GradleFxPlugin;
import spock.lang.Specification
import org.gradlefx.conventions.GradleFxConvention

class GradleFxConventionTest extends Specification {

    Project project = ProjectBuilder.builder().build();
    GradleFxConvention flexConvention
    String flexHome = './src/test/resources/valid-flex-sdk'

    def setup() {
        new GradleFxPlugin().apply(project)
        flexConvention = project.convention.plugins.flex
        flexConvention.flexHome = flexHome
    }
    
    def "default output must be project name"() {
        when:
            flexConvention.type = FlexType.swf
            
        then:
            flexConvention.output == 'test'
    }

    def "default frameworkLinkage for swc type must be external"() {
        when:
            flexConvention.type = FlexType.swc

        then:
            flexConvention.frameworkLinkage == FrameworkLinkage.external
    }
    
    def "default frameworkLinkage for swf type must be rsl"() {
        when:
            flexConvention.type = FlexType.swf

        then:
            flexConvention.frameworkLinkage == FrameworkLinkage.rsl
    }
    
    def "default frameworkLinkage for air type must be rsl"() {
        when:
            flexConvention.type = FlexType.air

        then:
            flexConvention.frameworkLinkage == FrameworkLinkage.rsl
    }
    
    def "default frameworkLinkage for mobile type must be rsl"() {
        when:
            flexConvention.type = FlexType.mobile

        then:
            flexConvention.frameworkLinkage == FrameworkLinkage.rsl
    }
    
}
