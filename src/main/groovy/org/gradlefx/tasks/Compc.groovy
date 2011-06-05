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
import org.gradle.api.Project

class Compc extends AbstractCompileTask {

    public Compc() {
        description = 'Compiles Flex component (*.swc) using the compc compiler'
    }

	@TaskAction
	def compileFlex() {
         List compilerArguments = createCompilerArguments(project)

        ant.java(jar: project.flexHome + '/lib/compc.jar',
             dir: project.flexHome + '/frameworks',
             fork: true,
             resultproperty: 'swcBuildResult',
             errorProperty: 'errorString') {

             compilerArguments.each { compilerArgument ->
                arg(value: compilerArgument)
            }
        }

        //handle failed compile
        if(ant.properties.swcBuildResult != '0') {
           throw new Exception("swc compilation failed: \n" + ant.properties.errorString);
        }
	}

    private List createCompilerArguments(Project project) {
        List compilerArguments = []

        //add every source directory
        project.srcDirs.each { sourcePath ->
            compilerArguments.add("-source-path+=" + project.projectDir.path + sourcePath)
            compilerArguments.add("-include-sources+=" + project.projectDir.path + sourcePath)
        }

        //add dependencies
        addLibraries(project.configurations.internal, "-include-libraries", compilerArguments)
        addLibraries(project.configurations.external, "-external-library-path", compilerArguments)
        addLibraries(project.configurations.merged, "-library-path", compilerArguments)

        //add all the other user specified compiler options
        project.additionalCompilerOptions.each { compilerOption ->
            compilerArguments.add(compilerOption)
        }

        compilerArguments.add("-output=" + project.buildDir.path + '/' + project.output)
        return compilerArguments
    }

}