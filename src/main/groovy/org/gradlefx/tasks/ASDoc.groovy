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
import org.gradlefx.tasks.compile.AbstractMxmlc
import org.gradlefx.FrameworkLinkage
import org.gradlefx.options.CompilerOption
import org.gradlefx.configuration.Configurations

class ASDoc extends AbstractMxmlc {

    private static final String ANT_RESULT_PROPERTY = 'asdocCompileResult'
    private static final String ANT_OUTPUT_PROPERTY = 'asdocCompileOutput'

    ASDoc() {
        description = 'Generates ASDoc documentation'

        addDependsOnTaskInOtherProjects(Tasks.COMPILE_TASK_NAME, Configurations.MERGE_CONFIGURATION_NAME)
        addDependsOnTaskInOtherProjects(Tasks.COMPILE_TASK_NAME, Configurations.EXTERNAL_CONFIGURATION_NAME)
        addDependsOnTaskInOtherProjects(Tasks.COMPILE_TASK_NAME, Configurations.INTERNAL_CONFIGURATION_NAME)

        initInputDirectory()
        initOutputDirectory()
    }

    private def initInputDirectory() {
        flexConvention.srcDirs.each { sourceDirectory ->
            inputs.dir sourceDirectory
        }
    }

    private def initOutputDirectory() {
        outputs.dir flexConvention.asdoc.outputDir
    }

    @Override
    @TaskAction
    public void compileFlex() {
        if(hasDocSources()) {
            super.compileFlex(ANT_RESULT_PROPERTY, ANT_OUTPUT_PROPERTY, 'asdoc', createCompilerArguments())
        }
    }

    @Override
    def compile(antResultProperty, antOutputProperty, compilerArguments) {
        ant.java(jar:            flexConvention.flexHome + '/lib/asdoc.jar',
                 dir:            flexConvention.flexHome + '/frameworks',
                 fork:           true,
                 resultproperty: antResultProperty,
                 outputproperty: antOutputProperty) { javaTask ->

            flexConvention.jvmArguments.each { jvmArgument ->
                jvmarg(value: jvmArgument)
            }

            compilerArguments.each { compilerArgument ->
                arg(value: compilerArgument)
            }
        }
    }

    protected FrameworkLinkage getDefaultFrameworkLinkage() {
        return FrameworkLinkage.merged
    }

    protected List createCompilerArguments() {
        List compilerArguments = []

        //add every source directory
        addSourcePaths(compilerArguments)
        addDocSources(compilerArguments)
        addLocales(compilerArguments)

        //add dependencies
        addLibraries(project.configurations.internal.files, project.configurations.internal, CompilerOption.INCLUDE_LIBRARIES, compilerArguments)
        addLibraries(project.configurations.external.files, project.configurations.external, CompilerOption.EXTERNAL_LIBRARY_PATH, compilerArguments)
        addLibraries(project.configurations.merged.files, project.configurations.merged, CompilerOption.LIBRARY_PATH, compilerArguments)
        addLibraries(project.configurations.theme.files, project.configurations.theme, CompilerOption.THEME, compilerArguments)
        addRsls(compilerArguments)

        //add all the other user specified compiler options
        flexConvention.additionalCompilerOptions.each { compilerOption ->
            compilerArguments.add(compilerOption)
        }

        flexConvention.asdoc.additionalASDocOptions.each { asdocOption ->
            compilerArguments.add(asdocOption)
        }

        // only generate the tempdita folder when having to create a fat swc
        if(flexConvention.fatSwc == true) {
            compilerArguments.add("-keep-xml=true")
        }

        compilerArguments.add("-output=${project.file(flexConvention.asdoc.outputDir).path}" )

        return compilerArguments
    }

    /**
     * Set the directories with sources for which asdoc should be generated.
     * @param compilerArguments
     */
    private void addDocSources(List compilerArguments) {
        flexConvention.srcDirs.each { sourcePath ->
            File sourcePathDir = project.file(sourcePath)

            if (sourcePathDir.exists()) {
                compilerArguments.add("-doc-sources+=" + sourcePathDir.path)
            }
        }
    }
    
    private boolean hasDocSources() {
        return flexConvention.srcDirs.any { sourcePath ->
            return project.file(sourcePath).exists()
        }
    }

}
