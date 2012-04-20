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

import org.gradlefx.tasks.compile.AbstractMxmlc
import org.gradle.api.tasks.TaskAction

class ASDoc extends AbstractMxmlc {

    private static final String ANT_RESULT_PROPERTY = 'asdocCompileResult'
    private static final String ANT_OUTPUT_PROPERTY = 'asdocCompileOutput'

    ASDoc() {
        description = 'Generates ASDoc documentation'
    }

    @TaskAction
    def compileFlex() {
        if(hasDocSources()) {
            super.compileFlex(ANT_RESULT_PROPERTY, ANT_OUTPUT_PROPERTY, 'asdoc', createCompilerArguments())
        }
    }

    @Override
    def compile(antResultProperty, antOutputProperty, compilerArguments) {
        ant.java(jar:            project.flexHome + '/lib/asdoc.jar',
                 dir:            project.flexHome + '/frameworks',
                 fork:           true,
                 resultproperty: antResultProperty,
                 outputproperty: antOutputProperty) { javaTask ->

            project.jvmArguments.each { jvmArgument ->
                jvmarg(value: jvmArgument)
            }

            compilerArguments.each { compilerArgument ->
                arg(value: compilerArgument)
            }
        }
    }

    protected List createCompilerArguments() {
        List compilerArguments = []

        //add every source directory
        addSourcePaths(compilerArguments)
        addDocSources(compilerArguments)
        addLocales(compilerArguments)

        //add dependencies
        addLibraries(project.configurations.internal.files, project.configurations.internal, "-include-libraries", compilerArguments)
        addLibraries(project.configurations.external.files - project.configurations.internal.files - project.configurations.merged.files, project.configurations.external, '-external-library-path', compilerArguments)
        addLibraries(project.configurations.merged.files, project.configurations.merged, "-library-path", compilerArguments)
        addLibraries(project.configurations.theme.files, project.configurations.theme, "-theme", compilerArguments)
        addRsls(compilerArguments)

        //add all the other user specified compiler options
        project.additionalCompilerOptions.each { compilerOption ->
            compilerArguments.add(compilerOption)
        }

        compilerArguments.add("-output=${project.file(project.asdoc.outputDir).path}" )

        return compilerArguments
    }

    /**
     * Set the directories with sources for which asdoc should be generated.
     * @param compilerArguments
     */
    private void addDocSources(List compilerArguments) {
        project.srcDirs.each { sourcePath ->
            File sourcePathDir = project.file(sourcePath)

            if (sourcePathDir.exists()) {
                compilerArguments.add("-doc-sources+=" + sourcePathDir.path)
            }
        }
    }
    
    private boolean hasDocSources() {
        return project.srcDirs.any { sourcePath ->
            return project.file(sourcePath).exists()
        }
    }
}
