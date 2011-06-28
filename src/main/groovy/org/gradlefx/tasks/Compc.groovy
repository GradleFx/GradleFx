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

import org.gradle.api.tasks.TaskAction

class Compc extends AbstractCompileTask {

    public Compc() {
        description = 'Compiles Flex component (*.swc) using the compc compiler'
    }

    def includeSources

    def includeClasses

    @TaskAction
    def compileFlex() {
        List compilerArguments = createCompilerArguments()

        ant.java(jar: project.flexHome + '/lib/compc.jar',
                dir: project.flexHome + '/frameworks',
                fork: true,
                outputproperty: 'compcOutput',
                resultproperty: 'swcBuildResult',
                errorProperty: 'errorString') {

            compilerArguments.each { compilerArgument ->
                arg(value: compilerArgument)
            }
        }

        //handle failed compile
        if (ant.properties.swcBuildResult != '0') {
            throw new Exception("swc compilation failed: \n" + ant.properties.errorString);
        }

        // The output property
        //println ant.properties.compcOutput;
    }

    private List createCompilerArguments() {
        List compilerArguments = []

        //add every source directory
        project.srcDirs.each { sourcePath ->
            compilerArguments.add("-source-path+=" + project.file(sourcePath).path)
            if ((null == includeSources) && (null == includeClasses)) {
                compilerArguments.add("-include-sources+=" + project.file(sourcePath).path)
            }
        }

        //add dependencies
        addLibraries(project.configurations.internal, "-include-libraries", compilerArguments)
        addLibraries(project.configurations.external, "-external-library-path", compilerArguments)
        addLibraries(project.configurations.merged, "-library-path", compilerArguments)

        if (null != includeClasses) {
            if (includeClasses instanceof String) {
                compilerArguments.add('-include-classes')
                compilerArguments.add(includeClasses)
            } else if (includeClasses instanceof List) {
                compilerArguments.add('-include-classes')
                includeClasses.each { clazz ->
                    compilerArguments.add(clazz)
                }
            } else {
                println "Error: compc.includeClasses is set to an unrecognized type: " + includeClasses.getClass().getName()
            }
        }
        if (null != includeSources) {
            if (includeSources instanceof String) {
                compilerArguments.add('-include-sources+=' + project.file(includeSources).path)
            } else if (includeSources instanceof List) {
                includeSources.each { srcPath ->
                    compilerArguments.add('-include-sources+=' + project.file(srcPath).path)
                }
            } else {
                println "Error: compc.includeSources is set to an unrecognized type: " + includeSources.getClass().getName()
            }
        }

        //add all the other user specified compiler options
        project.additionalCompilerOptions.each { compilerOption ->
            compilerArguments.add(compilerOption)
        }

        compilerArguments.add("-output=${project.buildDir.path}/${project.output}.swc")
        return compilerArguments
    }

}
