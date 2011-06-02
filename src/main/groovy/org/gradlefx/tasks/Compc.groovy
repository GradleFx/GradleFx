package org.gradlefx.tasks
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

import org.gradle.api.tasks.TaskAction

class Compc extends AbstractCompileTask {

    public Compc() {
        description = 'Compiles Flex component (*.swc) using the compc compiler'
    }

	@TaskAction
	def compileFlex() {
        ant.java(jar: project.flexHome + '/lib/compc.jar',
             dir: project.path,
             fork: true,
             resultproperty: 'swcBuildResult',
             errorProperty: 'errorString') {

            //add every source directory
            project.srcDirs.each {
                dir -> arg(value: "-include-sources+=" + dir)
            }

            //add dependencies
            addLibraries(project.configurations.internal, "-include-libraries")
            addLibraries(project.configurations.external, "-external-library-path")
            addLibraries(project.configurations.merged, "-library-path")
            addLibraries(project.configurations.rsl, "-runtime-shared-library-path")

            //add all the other user specified compiler options
            project.additionalCompilerOptions.each {
                compilerOption -> arg(value: compilerOption)
            }

            arg(value: "-output=" + project.buildDir.path + '/' + project.output)
        }

        //handle failed compile
        if(ant.properties.swcBuildResult != '0') {
           throw new Exception("swc compilation failed: \n" + ant.properties.errorString);
        }
	}

}