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

package org.gradlefx.cli

import org.gradle.api.Project


class ASDocCommandLineInstruction extends CommandLineInstruction {

    public ASDocCommandLineInstruction(Project project) {
        super(project)
    }
    
    @Override
    public void setConventionArguments() {
        //add every source directory
        addSourcePaths()
        addDocSources()
        addLocales()

        //add dependencies
        addInternalLibraries()
        addExternalLibraries()
        addMergedLibraries()
        addTheme()
        addRSLs()

        addAdditionalCompilerOptions()
        addAdditionalAsdocOptions()

        // only generate the tempdita folder when having to create a fat swc
        if (flexConvention.fatSwc == true) keepXML()

        addOutput()
    }
    
    /** Adds the <code>-doc-sources</code> argument based on project.srcDirs */
    public void addDocSources() {
        flexConvention.srcDirs.each { sourcePath ->
            File sourcePathDir = project.file sourcePath
 
            if (sourcePathDir.exists()) {
                add CompilerOption.DOC_SOURCES, sourcePathDir.path
            }
        }
    }
    
    public void addAdditionalAsdocOptions() {
        addAll flexConvention.asdoc.additionalASDocOptions
    }
    
    public void keepXML() {
        set CompilerOption.KEEP_XML, "true"
    }
    
    public void addOutput() {
        addOutput flexConvention.asdoc.outputDir
    }
    
}
