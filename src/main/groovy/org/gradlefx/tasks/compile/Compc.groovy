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
import org.gradlefx.FrameworkLinkage
import org.gradlefx.options.CompilerOption
import org.gradlefx.tasks.Tasks
import org.gradlefx.validators.actions.ValidateCompcTaskPropertiesAction

/*
 * Gradle task to execute Flex's Compc compiler.
 */
class Compc extends AbstractCompileTask {

	private static final String ANT_RESULT_PROPERTY = 'compcCompileResult'
	private static final String ANT_OUTPUT_PROPERTY = 'compcCompileOutput'
	
    public Compc() {
        description = 'Compiles Flex component (*.swc) using the compc compiler'

        if(flexConvention.fatSwc) {
            dependsOn(Tasks.ASDOC_TASK_NAME)
        }
    }

    @TaskAction
    def compileFlex() {
        new ValidateCompcTaskPropertiesAction().execute(this)

        List compilerArguments = createCompilerArguments()

        ant.java(jar: flexConvention.flexHome + '/lib/compc.jar',
                dir: flexConvention.flexHome + '/frameworks',
                fork: true,
                resultproperty: ANT_RESULT_PROPERTY,
                outputproperty: ANT_OUTPUT_PROPERTY) {

            flexConvention.jvmArguments.each { jvmArgument ->
                jvmarg(value: jvmArgument)
            }

            compilerArguments.each { compilerArgument ->
                arg(value: compilerArgument)
            }
        }

        handleBuildIfFailed ANT_RESULT_PROPERTY, ANT_OUTPUT_PROPERTY, 'Compc'
		
		showAntOutput ant.properties[ANT_OUTPUT_PROPERTY]

        if(flexConvention.fatSwc) {
            addAsdocToSwc()
        }
    }

    private List createCompilerArguments() {
        List compilerArguments = []
        
        //add framework
        addPlayerLibrary(compilerArguments)
        addFramework(compilerArguments)

        //add every source path
        addSourcePaths(compilerArguments)
        addSourceFilesAndDirectories(compilerArguments)
        addResources(compilerArguments)
        addLocales(compilerArguments)

        //add dependencies
        addLibraries(project.configurations.internal.files, project.configurations.internal, CompilerOption.INCLUDE_LIBRARIES, compilerArguments)
        addLibraries(project.configurations.external.files, project.configurations.external, CompilerOption.EXTERNAL_LIBRARY_PATH, compilerArguments)
        addLibraries(project.configurations.merged.files, project.configurations.merged, CompilerOption.LIBRARY_PATH, compilerArguments)
        
        //add all the other user specified compiler options
        flexConvention.additionalCompilerOptions.each { compilerOption ->
            compilerArguments.add(compilerOption)
        }

        compilerArguments.add("-output=${project.buildDir.path}/${flexConvention.output}.swc")
        return compilerArguments
    }
    
    protected FrameworkLinkage getDefaultFrameworkLinkage() {
        return FrameworkLinkage.merged
    }

    private def addResources(List compilerArguments) {
        flexConvention.resourceDirs.each { String resourceDirString ->
            File resourceDir = project.file(resourceDirString)

            if(resourceDir.exists()) {
                resourceDir.traverse(type: FileType.FILES) {
                    String relativePath = resourceDir.toURI().relativize(it.toURI()).getPath();

                    compilerArguments.add(CompilerOption.INCLUDE_FILE)
                    compilerArguments.add(relativePath)
                    compilerArguments.add(it.path)
                }
            }
        }
    }

    private def addSourceFilesAndDirectories(List compilerArguments) {
        if (flexConvention.includeClasses == null && flexConvention.includeSources == null) {
            flexConvention.srcDirs.each { sourcePath ->
                File sourceDir = project.file(sourcePath)

                //don't allow non existing source paths unless they contain a token (e.g. {locale})
                if(sourceDir.exists() || sourcePath.contains('{')) {
                    compilerArguments.add("${CompilerOption.INCLUDE_SOURCES}+=${sourceDir.path}")
                }
            }
        } else {
            if (flexConvention.includeClasses != null) {
                compilerArguments.add(CompilerOption.INCLUDE_CLASSES)
                flexConvention.includeClasses.each { classToInclude ->
                    compilerArguments.add(classToInclude)
                }
            }

            if (flexConvention.includeSources != null) {
                flexConvention.includeSources.each { classOrDirectoryToInclude ->
                    compilerArguments.add("${CompilerOption.INCLUDE_SOURCES}+=${project.file(classOrDirectoryToInclude).path}")
                }
            }
        }
    }

    def addAsdocToSwc() {
        ant.zip(destfile: new File(project.buildDir.absolutePath, "${flexConvention.output}.${flexConvention.type}"),
                update: true) {
            zipfileset(dir: project.file(flexConvention.asdoc.outputDir + "/tempdita"), prefix: 'docs') {
                exclude(name: 'ASDoc_Config.xml')
                exclude(name: 'overviews.xml')
            }
        }
    }

}
