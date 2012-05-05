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

package org.gradlefx.tasks.compile

import groovy.io.FileType
import org.gradle.api.tasks.TaskAction
import org.gradlefx.options.CompilerOption
import org.gradlefx.validators.actions.ValidateCompcTaskPropertiesAction

/*
 * Gradle task to execute Flex's Compc compiler.
 */
class Compc extends AbstractCompileTask {

	private static final String ANT_RESULT_PROPERTY = 'compcCompileResult'
	private static final String ANT_OUTPUT_PROPERTY = 'compcCompileOutput'
	
    public Compc() {
        description = 'Compiles Flex component (*.swc) using the compc compiler'
    }

    @TaskAction
    def compileFlex() {
        new ValidateCompcTaskPropertiesAction().execute(this)

        List compilerArguments = createCompilerArguments()

        ant.java(jar: project.flexHome + '/lib/compc.jar',
                dir: project.flexHome + '/frameworks',
                fork: true,
                resultproperty: ANT_RESULT_PROPERTY,
                outputproperty: ANT_OUTPUT_PROPERTY) {

            project.jvmArguments.each { jvmArgument ->
                jvmarg(value: jvmArgument)
            }

            compilerArguments.each { compilerArgument ->
                arg(value: compilerArgument)
            }
        }

        handleBuildIfFailed ANT_RESULT_PROPERTY, ANT_OUTPUT_PROPERTY, 'Compc'
		
		showAntOutput ant.properties[ANT_OUTPUT_PROPERTY]
    }

    private List createCompilerArguments() {
        List compilerArguments = []

        //add every source path
        addSourcePaths(compilerArguments)
        addSourceFilesAndDirectories(compilerArguments)
        addResources(compilerArguments)
        addLocales(compilerArguments)

        //add dependencies
        addLibraries(project.configurations.internal.files, project.configurations.internal, "-include-libraries", compilerArguments)
        addLibraries(project.configurations.external.files, project.configurations.external, "-external-library-path", compilerArguments)
        addLibraries(project.configurations.merged.files, project.configurations.merged, "-library-path", compilerArguments)

        //add all the other user specified compiler options
        project.additionalCompilerOptions.each { compilerOption ->
            compilerArguments.add(compilerOption)
        }

        compilerArguments.add("-output=${project.buildDir.path}/${project.output}.swc")
        return compilerArguments
    }

    private def addResources(List compilerArguments) {
        project.resourceDirs.each { String resourceDirString ->
            File resourceDir = project.file(resourceDirString)

            if(resourceDir.exists()) {
                resourceDir.traverse(type: FileType.FILES) {
                    String relativePath = resourceDir.toURI().relativize(it.toURI()).getPath();

                    println "relative resource path: " + relativePath

                    compilerArguments.add(CompilerOption.INCLUDE_FILE)
                    compilerArguments.add(relativePath)
                    compilerArguments.add(it.path)
                }
            }

        }
    }

    private def addSourceFilesAndDirectories(List compilerArguments) {
        if (project.includeClasses == null && project.includeSources == null) {
            project.srcDirs.each { sourcePath ->
                File sourceDir = project.file(sourcePath)

                //don't allow non existing source paths unless they contain a token (e.g. {locale})
                if(sourceDir.exists() || sourcePath.contains('{')) {
                    compilerArguments.add("${CompilerOption.INCLUDE_SOURCES}+=${sourceDir.path}")
                }
            }
        } else {
            if (project.includeClasses != null) {
                compilerArguments.add(CompilerOption.INCLUDE_CLASSES)
                project.includeClasses.each { classToInclude ->
                    compilerArguments.add(classToInclude)
                }
            }

            if (project.includeSources != null) {
                project.includeSources.each { classOrDirectoryToInclude ->
                    compilerArguments.add("${CompilerOption.INCLUDE_SOURCES}+=${project.file(classOrDirectoryToInclude).path}")
                }
            }
        }
    }

}
