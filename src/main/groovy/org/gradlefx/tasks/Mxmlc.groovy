package org.gradlefx.tasks
/*
 * Copyright 2011 Infonic AG
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

import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ResolveException
import org.gradle.api.tasks.TaskAction

class Mxmlc extends AbstractCompileTask {

    public Mxmlc() {
        description = 'Compiles Flex application/module (*.swf) using the mxmlc compiler'
    }

	
	@TaskAction
	def compileFlex() {
        ant.java(jar: project.flexHome + '/lib/mxmlc.jar',
             dir: project.path,
             fork: true,
             resultproperty: 'swcBuildResult',
             errorProperty: 'errorString') {

            arg(value: "-output=" + project.output)

            //add every source directory
            project.srcDirs.each {
                dir -> arg(value: "-source-path+=" + project.projectDir.path + dir)
            }

            //add dependencies
            addLibraries(project.configurations.internal, "-include-libraries")
            addLibraries(project.configurations.merged, "-library-path")
            addRsls(project.configurations.rsl)

            //add all the other user specified compiler options
            project.additionalCompilerOptions.each {
                compilerOption -> arg(value: compilerOption)
            }

            //add the target file
            arg(value: project.projectDir.path + project.srcDirs.get(0) + '/' + project.mainClass)
        }

        //handle failed compile
        if(ant.properties.swcBuildResult != '0') {
           throw new Exception("swc compilation failed: \n" + ant.properties.errorString);
        }
	}
	
	def addRsls(Configuration configuration) {
		configuration.files.each { dependency ->
			if(dependency.exists()) {
                arg(value: "-runtime-shared-library-path" + "=" + dependency.path + "," + dependency.name[0..-2] + 'f')
			}
			else {
				throw new ResolveException("Couldn't find the ${dependency.name} file - are you sure the path is correct?")
			}
				
		}
	}
}