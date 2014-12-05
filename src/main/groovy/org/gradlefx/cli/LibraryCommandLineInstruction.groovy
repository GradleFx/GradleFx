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

import groovy.io.FileType
import org.gradle.api.Project


class LibraryCommandLineInstruction extends CommandLineInstruction {

    public LibraryCommandLineInstruction(Project project) {
        super(project)
    }
    
    @Override
    public void setConventionArguments() {
        //add framework
        addFramework()
        
        //add every source path
        addSourcePaths()
        addSourceFilesAndDirectories()
        addResources()
        addLocales()
        
        //add dependencies
        addInternalLibraries()
        addExternalLibraries()
        addMergedLibraries()
        
        addAdditionalCompilerOptions()
        addOutput()
    }
    
    public void addResources() {
        flexConvention.resourceDirs.each { 
            File resourceDir = project.file it

            if (resourceDir.exists()) {
                resourceDir.traverse(type: FileType.FILES) { File file ->
                    String relativePath = resourceDir.toURI().relativize(file.toURI()).getPath()

                    add CompilerOption.INCLUDE_FILE
                    addAll new ArrayList<String>([relativePath, file.path])
                }
            }
        }
        
    }

    public void addSourceFilesAndDirectories() {
        if (flexConvention.includeClasses) {
            add CompilerOption.INCLUDE_CLASSES
            addAll flexConvention.includeClasses
        }
        else if (flexConvention.includeSources) {
            Collection paths = flexConvention.includeSources.collect { project.file(it).path }
            addAll CompilerOption.INCLUDE_SOURCES, paths
        }
        else {
            addAll CompilerOption.INCLUDE_SOURCES, filterValidSourcePaths(flexConvention.srcDirs)
        }
    }
    
    public void addOutput() {
        addOutput "${project.buildDir.name}/${flexConvention.output}.$flexConvention.type"
    }
    
}
