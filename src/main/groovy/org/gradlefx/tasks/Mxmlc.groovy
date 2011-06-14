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

package org.gradlefx.tasks

import org.gradle.api.artifacts.ResolveException
import org.gradle.api.tasks.TaskAction

class Mxmlc extends AbstractCompileTask {

    public Mxmlc() {
        description = 'Compiles Flex application/module (*.swf) using the mxmlc compiler'
    }

    @TaskAction
    def compileFlex() {
        List compilerArguments = createCompilerArguments()

        ant.java(jar: project.flexHome + '/lib/mxmlc.jar',
                dir: project.flexHome + '/frameworks',
                fork: true,
                resultproperty: 'swcBuildResult',
                errorProperty: 'errorString') { javaTask ->

            compilerArguments.each { compilerArgument ->
                arg(value: compilerArgument)
            }
        }

        //handle failed compile
        if (ant.properties.swcBuildResult != '0') {
            throw new Exception("swc compilation failed: \n" + ant.properties.errorString);
        }
    }

    private List createCompilerArguments() {
        List compilerArguments = []

        //add every source directory
        project.srcDirs.each { sourcePath ->
            compilerArguments.add("-source-path+=" + project.file(sourcePath) )
        }

        //add dependencies
        addLibraries(project.configurations.internal, "-include-libraries", compilerArguments)
        addLibraries(project.configurations.merged, "-library-path", compilerArguments)
        addRsls(compilerArguments)

        //add all the other user specified compiler options
        project.additionalCompilerOptions.each { compilerOption ->
            compilerArguments.add(compilerOption)
        }

        compilerArguments.add("-output=" + project.buildDir.path + '/' + project.output)

        //add the target file
        for (def src : project.srcDirs) {
            File srcDir = project.file( src )
            File mcClazz = new File(srcDir, project.mainClass)
            if (mcClazz.exists()) {
                compilerArguments.add( mcClazz.absolutePath )
                break;
            }
        }

        return compilerArguments
    }

    def addRsls(List compilerArguments) {
        project.configurations.rsl.files.each { dependency ->
            if (dependency.exists()) {
                compilerArguments.add("-runtime-shared-library-path" + "=" + dependency.path + "," + dependency.name[0..-2] + 'f')
            }
            else {
                throw new ResolveException("Couldn't find the ${dependency.name} file - are you sure the path is correct?")
            }

        }
    }
}