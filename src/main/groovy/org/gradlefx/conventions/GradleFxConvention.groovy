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

class GradleFxConvention {

    private Project project

    String output

    // the home directory of the Flex SDK
    def flexHome = System.getenv()['FLEX_HOME'] //default to FLEX_HOME environment variable

    // which directories to look into for source code
    def srcDirs = ['/src/main/actionscript']

    //test directories
    def testDirs = ['/src/test/actionscript']

    //resource directories
    def resourceDirs = ['/src/main/resources']

    //test resource directories
    def testResourceDirs = ['/src/test/resources']

    //equivalent of the include-classes compiler option
    List includeClasses;

    //equivalent of the include-sources compiler option
    List includeSources;

    // what type of Flex project are we?  either SWF or SWC
    FlexType type

    // the directory where we should publish the build artifacts
    String publishDir = 'publish'

    //the root class which is used by the mxmlc compiler to create a swf
    def mainClass = 'Main.mxml'

    //array of additional compiler options as defined by the compc or mxmlc compiler
    def additionalCompilerOptions = []

    // player version
    def playerVersion = '10.0'

    // HTML wrapper options
    def htmlWrapper

    def GradleFxConvention(Project project) {
        this.project = project

        htmlWrapper = [
                title: project.description,
                file: "${project.name}.html",
                height: '100%',
                width: '100%',
                application: project.name,
                swf: project.name,
                history: 'true',
                'express-install': 'true',
                'version-detection': 'true',
                output: project.buildDir
        ]

        project.afterEvaluate {
            initializeEmptyProperties()
        }
    }

    public def initializeEmptyProperties() {
        if (output == null) {
            output = project.name
        }
    }
}

