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

import org.gradle.api.tasks.TaskAction
import org.gradlefx.tasks.Tasks
import org.gradlefx.validators.actions.ValidateMxmlcTaskPropertiesAction
import org.gradlefx.options.CompilerOption

class Mxmlc extends AbstractMxmlc {

	private static final String ANT_RESULT_PROPERTY = 'mxmlcCompileResult'
	private static final String ANT_OUTPUT_PROPERTY = 'mxmlcCompileOutput'
	
    public Mxmlc() {
        description = 'Compiles Flex application/module (*.swf) using the mxmlc compiler'
        dependsOn(Tasks.COPY_RESOURCES_TASK_NAME)
    }

    @TaskAction
    def compileFlex() {
        new ValidateMxmlcTaskPropertiesAction().execute(this)

		super.compileFlex(ANT_RESULT_PROPERTY, ANT_OUTPUT_PROPERTY, 'Mxmlc', createCompilerArguments())
    }

    protected List createCompilerArguments() {
        List compilerArguments = []

        //add every source directory
        addSourcePaths(compilerArguments)
        addLocales(compilerArguments)

        //add dependencies
        addLibraries(project.configurations.internal.files, project.configurations.internal, CompilerOption.INCLUDE_LIBRARIES.optionName, compilerArguments)
		addLibraries(project.configurations.external.files - project.configurations.internal.files - project.configurations.merged.files, project.configurations.external, CompilerOption.EXTERNAL_LIBRARY_PATH.optionName, compilerArguments)
        addLibraries(project.configurations.merged.files, project.configurations.merged, CompilerOption.LIBRARY_PATH.optionName, compilerArguments)
        addLibraries(project.configurations.theme.files, project.configurations.theme, CompilerOption.THEME.optionName, compilerArguments)
        addRsls(compilerArguments)

        //add all the other user specified compiler options
        project.additionalCompilerOptions.each { compilerOption ->
            compilerArguments.add(compilerOption)
        }

        compilerArguments.add("${CompilerOption.OUTPUT}=${project.buildDir.path}/${project.output}.swf" )

        //add the target file
        File mainClassFile = findFile(project.srcDirs, project.mainClass)
        compilerArguments.add(mainClassFile.absolutePath)

        return compilerArguments
    }

}
