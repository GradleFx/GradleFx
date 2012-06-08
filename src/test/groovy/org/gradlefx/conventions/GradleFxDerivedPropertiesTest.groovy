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
import spock.lang.Specification
import org.gradlefx.FlexType;
import org.gradlefx.FrameworkLinkage;
import org.gradlefx.conventions.GradleFxConvention
import org.gradlefx.options.CompilerOption;
import org.gradlefx.plugins.GradleFxPlugin;

class GradleFxDerivedPropertiesTest extends Specification {

    Project project = ProjectBuilder.builder().build();
    GradleFxConvention flexConvention
    String flexHome = './src/test/resources/valid-flex-sdk'

    def setup() {
        new GradleFxPlugin().apply(project)
        flexConvention = project.convention.plugins.flex
        flexConvention.flexHome = flexHome
    }
    
    def "default values derived from mainClass are: Main/Main.mxml/Main/[no package]"() {
        when:
            flexConvention.type = FlexType.swf
            project.evaluate()
            
        then:
            flexConvention.mainClass == 'Main'
            flexConvention.mainClassPath == 'Main.mxml'
            flexConvention.className == 'Main'
            flexConvention.packageName == ''
    }
    
    def "values derived from mainClass in a pure AS project are: Main/Main.as/Main/[no package]"() {
        when:
            flexConvention.type = FlexType.swf
            flexConvention.frameworkLinkage = FrameworkLinkage.none
            project.evaluate()
            
        then:
            flexConvention.mainClass == 'Main'
            flexConvention.mainClassPath == 'Main.as'
            flexConvention.className == 'Main'
            flexConvention.packageName == ''
    }
    
    def "a path as mainclass overrides default extension"() {
        when:
            flexConvention.type = FlexType.swf
            flexConvention.mainClass = 'MyApp.as'
            project.evaluate()
            
        then:
            flexConvention.mainClass == 'MyApp.as'
            flexConvention.mainClassPath == 'MyApp.as' //instead of default mxml
            flexConvention.className == 'MyApp'
            flexConvention.packageName == ''
    }
    
    def "a path as mainclass sets packageName"() {
        when:
            flexConvention.type = FlexType.swf
            flexConvention.mainClass = 'org/gradlefx/MyApp.mxml'
            project.evaluate()
            
        then:
            flexConvention.mainClass == 'org/gradlefx/MyApp.mxml'
            flexConvention.mainClassPath == 'org/gradlefx/MyApp.mxml'
            flexConvention.className == 'MyApp'
            flexConvention.packageName == 'org.gradlefx'
    }
    
    def "a class with package as mainclass sets packageName"() {
        when:
            flexConvention.type = FlexType.swf
            flexConvention.mainClass = 'org.gradlefx.MyApp'
            project.evaluate()
            
        then:
            flexConvention.mainClass == 'org.gradlefx.MyApp'
            flexConvention.mainClassPath == 'org/gradlefx/MyApp.mxml'
            flexConvention.className == 'MyApp'
            flexConvention.packageName == 'org.gradlefx'
    }
    
    def "defaultExtension is .as for pure AS projects"() {
        when:
            flexConvention.type = FlexType.swf
            flexConvention.frameworkLinkage = FrameworkLinkage.none
            
        then:
            flexConvention.defaultExtension == '.as'
    }
    
    def "defaultExtension is .mxml for Flex projects (and thus by default)"() {
        when:
            flexConvention.type = FlexType.swf
            
        then:
            flexConvention.defaultExtension == '.mxml'
    }
    
    def "applicationId is project name by default"() {
        when:
            flexConvention.type = FlexType.swf
            
        then:
            flexConvention.applicationId == 'test'
    }
    
    def "applicationId uses main class' package, if any"() {
        when:
            flexConvention.type = FlexType.swf
            flexConvention.mainClass = 'org/gradlefx/MyApp.mxml'
            
        then:
            flexConvention.applicationId == 'org.gradlefx.test'
    }
    
    def "version is '0.0.0' by default"() {
        when:
            flexConvention.type = FlexType.swf
            
        then:
            flexConvention.version == '0.0.0'
    }
    
    def "version is stripped of anything that is not a dot or a digit"() {
        when:
            project.version = '1.2.3-SNAPSHOT'
            flexConvention.type = FlexType.swf
            
        then:
            flexConvention.version == '1.2.3'
    }
    
    def "IDE gets compilerArgs if locales is defined"() {
        when:
            flexConvention.type = FlexType.swf
            flexConvention.locales = ['nl_BE', 'fr_BE']
        
        then:
            flexConvention.compilerArgs.size() == 2
            flexConvention.compilerArgs.containsAll(
                "${CompilerOption.LOCALE}=nl_BE,fr_BE", 
                "${CompilerOption.SOURCE_PATH}=src/main/locale/{locale}"
            )
    }
    
    
    def "IDE gets compilerArgs if additionalCompilerOptions are defined"() {
        when:
            flexConvention.type = FlexType.swf
            flexConvention.additionalCompilerOptions = ['-source-path=dummy']
        
        then:
            flexConvention.compilerArgs.size() == 1
            flexConvention.compilerArgs.contains('-source-path=dummy')
    }
    
    def "IDE gets no compilerArgs if locales and additionalCompilerOptions are not defined"() {
        when:
            flexConvention.type = FlexType.swf
        
        then:
            flexConvention.compilerArgs.size() == 0
    }
    
    def "defaultFrameworkLinkage for swc type must be external for Flex projects"() {
        when:
            flexConvention.type = FlexType.swc
        
        then:
            flexConvention.defaultFrameworkLinkage == FrameworkLinkage.external
    }
    
    def "defaultFrameworkLinkage for swc type must be external for pure AS projects"() {
        when:
            flexConvention.type = FlexType.swc
            flexConvention.frameworkLinkage = FrameworkLinkage.none
        
        then:
            flexConvention.defaultFrameworkLinkage == FrameworkLinkage.external
    }
    
    def "defaultFrameworkLinkage for Flex application projects must be rsl"() {
        when:
            flexConvention.type = FlexType.swf
        
        then:
            flexConvention.defaultFrameworkLinkage == FrameworkLinkage.rsl
    }
    
    def "defaultFrameworkLinkage for pure AS application projects must be merged"() {
        when:
            flexConvention.type = FlexType.swf
            flexConvention.frameworkLinkage = FrameworkLinkage.none
        
        then:
            flexConvention.defaultFrameworkLinkage == FrameworkLinkage.merged
    }
    
    def "allSrcDirs is a flat list of dirs from srcDirs/resourceDirs/testDirs/testResourceDirs"() {
        when:
            flexConvention.type = FlexType.swf
        
        then: 
            flexConvention.allSrcDirs.containsAll(
                'src/main/actionscript', 
                'src/test/actionscript', 
                'src/main/resources', 
                'src/test/resources'
            )
    }
    
    //TODO create a test for dependencyProjects
    
}
