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

import org.gradlefx.cli.CompilerOption;
import org.gradlefx.conventions.FlexType;
import org.gradlefx.conventions.FrameworkLinkage;
import spock.lang.Specification


class FrameworkLinkageTest extends Specification {
    
    //external

    def "external refers to -external-library-path compiler option"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.external
            
        then:
            linkage.getCompilerOption() == CompilerOption.EXTERNAL_LIBRARY_PATH
    }
    
    def "external is default for FlexType.swc"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.external
            FlexType type = FlexType.swc
            
        then:
            linkage.getCompilerDefault(type) == FrameworkLinkage.external
            linkage.isCompilerDefault(type) == true
    }
    
    
    //merged
    
    def "merged refers to -library-path compiler option"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.merged
            
        then:
            linkage.getCompilerOption() == CompilerOption.LIBRARY_PATH
    }
    
    def "merged is not default for FlexType.swf"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.merged
            FlexType type = FlexType.swf
                    
        then:
            linkage.getCompilerDefault(type) == FrameworkLinkage.rsl
            linkage.isCompilerDefault(type) == false
    }
    
    def "merged is not default for FlexType.air"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.merged
            FlexType type = FlexType.air
                    
        then:
            linkage.getCompilerDefault(type) == FrameworkLinkage.rsl
            linkage.isCompilerDefault(type) == false
    }
    
    def "merged is not default for FlexType.mobile"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.merged
            FlexType type = FlexType.mobile
                    
        then:
            linkage.getCompilerDefault(type) == FrameworkLinkage.rsl
            linkage.isCompilerDefault(type) == false
    }
    
    def "merged is not default for FlexType.swc"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.merged
            FlexType type = FlexType.swc
                    
        then:
            linkage.getCompilerDefault(type) == FrameworkLinkage.external
            linkage.isCompilerDefault(type) == false
    }
    
    
    //RSL
    
    def "rsl refers to -runtime-shared-library-path compiler option"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.rsl
            
        then:
            linkage.getCompilerOption() == CompilerOption.RUNTIME_SHARED_LIBRARY_PATH
    }
    
    def "rsl is default for FlexType.swf"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.rsl
            FlexType type = FlexType.swf
                    
        then:
            linkage.getCompilerDefault(type) == FrameworkLinkage.rsl
            linkage.isCompilerDefault(type) == true
    }
    
    def "rsl is default for FlexType.air"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.rsl
            FlexType type = FlexType.air
                    
        then:
            linkage.getCompilerDefault(type) == FrameworkLinkage.rsl
            linkage.isCompilerDefault(type) == true
    }
    
    def "rsl is default for FlexType.mobile"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.rsl
            FlexType type = FlexType.mobile
                    
        then:
            linkage.getCompilerDefault(type) == FrameworkLinkage.rsl
            linkage.isCompilerDefault(type) == true
    }
    
}
