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

package org.gradlefx.cli.instructions

import org.gradle.api.Project
import org.gradlefx.cli.compiler.CompilerOption
import org.gradlefx.cli.common.optioninjectors.LibraryOptionsInjector
import org.gradlefx.cli.common.optioninjectors.SourcesOptionsInjector

/**
 * Compiler instructions for generating ASDoc.
 */
class ASDocInstructions extends CompilerInstructionsBuilder implements LibraryOptionsInjector, SourcesOptionsInjector {

    ASDocInstructions(Project project) {
        super(project)
    }
    
    @Override
    void configure() {
        //add every source directory
        addSourceDirectories()
        addLocaleSources()
        addDocSources()
        addLocales()

        //add dependencies
        addInternalLibraries()
        addExternalLibrariesForLib()
        addMergedLibraries()
        addTheme()
        addRSLs()

        addAdditionalCompilerOptions()
        addAdditionalAsdocOptions()

        // only generate the tempdita folder when having to create a fat swc
        if (flexConvention.fatSwc) keepXML()

        addOutput()
    }
    
    /** Adds the <code>-doc-sources</code> argument based on project.srcDirs */
    public void addDocSources() {
        flexConvention.srcDirs.each { sourcePath ->
            File sourcePathDir = project.file sourcePath
 
            if (sourcePathDir.exists()) {
                compilerOptions.add CompilerOption.DOC_SOURCES, sourcePathDir.path
            }
        }
    }
    
    public void addAdditionalAsdocOptions() {
        compilerOptions.addAll flexConvention.asdoc.additionalASDocOptions
    }
    
    public void keepXML() {
        compilerOptions.set CompilerOption.KEEP_XML, "true"
    }
    
    public void addOutput() {
        addOutput flexConvention.asdoc.outputDir
    }
    
}
