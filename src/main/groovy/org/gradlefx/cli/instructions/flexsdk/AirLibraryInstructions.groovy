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

package org.gradlefx.cli.instructions.flexsdk

import org.gradle.api.Project
import org.gradlefx.cli.common.optioninjectors.LibraryOptionsInjector
import org.gradlefx.cli.common.optioninjectors.SourcesOptionsInjector
import org.gradlefx.cli.instructions.CompilerInstructionsBuilder

/**
 * Compiler instructions for a library project that uses the Flex SDK and Flex and also AIR.
 */
class AirLibraryInstructions extends CompilerInstructionsBuilder implements LibraryOptionsInjector, SourcesOptionsInjector {

    public AirLibraryInstructions(Project project) {
        super(project)
    }
    
    @Override
    public void configure() {
        loadDefaultConfig()

        /* By default, the *-config.xml files will cause the RSLs to be added to the -runtime-shared-library-path compiler
           option. For libraries however, we don't want to use RSLs. Therefore we first need to remove those instructions
           set by the config file and the reapply those rsls on their other compiler option (e.g. -external-library-path for 'external',
           which is typically used for library projects).
         */
        reapplyFrameworkRslsAccordingToFrameworkLinkage()
        
        //add every source path
        addSourceDirectories()
        addLocaleSources()
        addLibrarySourceFilesAndDirectories()
        addResources()
        addLocales()
        
        //add dependencies
        addInternalLibraries()
        addExternalLibrariesForLib()
        addMergedLibraries()
        
        addAdditionalCompilerOptions()
        addOutput()
    }
    
    public void addOutput() {
        addOutput "${project.buildDir.path}/${flexConvention.output}.swc"
    }
    
}
