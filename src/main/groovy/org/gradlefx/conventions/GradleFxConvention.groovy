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
import org.gradlefx.FlexType
import org.gradlefx.FrameworkLinkage


class GradleFxConvention {

    private Project project

    String output

    // the home directory of the Flex SDK
    def flexHome = System.getenv()['FLEX_HOME'] //default to FLEX_HOME environment variable

    // which directories to look into for source code
    def srcDirs = ['src/main/actionscript']

    //test directories
    def testDirs = ['src/test/actionscript']

    //resource directories
    def resourceDirs = ['src/main/resources']

    //test resource directories
    def testResourceDirs = ['src/test/resources']
    
    //locale root directory
    def localeDir = 'src/main/locale'
    
    //array of locales; maps to the locale compiler option
    def locales = []

    //equivalent of the include-classes compiler option
    List includeClasses;

    //equivalent of the include-sources compiler option
    List includeSources;

    // what type of Flex project are we?  either SWF or SWC
    FlexType type
    
    //how the Flex framework will be linked in the project: external, RSL, merged or none
    //default: RSL for swf, external for swc
    FrameworkLinkage frameworkLinkage

    // the directory where we should publish the build artifacts
    String publishDir = 'publish'

    //the root class which is used by the mxmlc compiler to create a swf
    def mainClass = 'Main.mxml'

    //array of additional compiler options as defined by the compc or mxmlc compiler
    def additionalCompilerOptions = []

    def jvmArguments = []

    // player version
    def playerVersion = '10.0'

    // HTML wrapper options
    def htmlWrapper
	
	// FlexUnit properties
	def flexUnit

    // AIR packaging properties
    def air


    def GradleFxConvention(Project project) {
        this.project = project

        htmlWrapper = [
            title:               project.description,
            file:                "${project.name}.html",
            height:              '100%',
            width:               '100%',
            application:         project.name,
            swf:                 project.name,
            history:             'true',
            'express-install':   'true',
            'version-detection': 'true',
            output:              project.buildDir
        ]

		flexUnit = [
			player:          'flash',
			command:         System.getenv()['FLASH_PLAYER_EXE'],
			toDir:           "${project.buildDir}/reports",
			workingDir:      project.buildDir,
			haltonfailure:   'false',
			verbose:         'false',
			localTrusted:    'true',
			port:            '1024',
			buffer:          '262144',
			timeout:         '60000', //60 seconds
			failureproperty: 'flexUnitFailed',
			headless:        'false',
			display:         '99',
            includes:         ['**/*Test.as'],
            excludes:         []
		]

        air = [
            keystore:               "${project.name}.p12",
            storepass:              null,
            applicationDescriptor:  "/src/main/actionscript/${project.name}.xml",
            includeFileTrees:       null
        ]
		
        project.afterEvaluate {
            initializeEmptyProperties()
        }
    }

    public def initializeEmptyProperties() {
		output = output ?: project.name
        frameworkLinkage = getFrameworkLinkage()
    }
    
    /**
     * The framework linkage defaults to 'RSL' for application projects ('air' or 'swf' {@link FlexType}) 
     * and 'external' for library projects ('swc' {@link FlexType}). 
     * 
     * @return The project's framework linkage
     */
    private FrameworkLinkage getFrameworkLinkage() {
        FrameworkLinkage defaultLinkage;
        
        switch (type) {
            case FlexType.air:
            case FlexType.swf: defaultLinkage = FrameworkLinkage.rsl; break
            case FlexType.swc: defaultLinkage = FrameworkLinkage.external; break
            default: defaultLinkage = FrameworkLinkage.none; break
        }
        
        return frameworkLinkage ? frameworkLinkage : defaultLinkage
    }
    
}

