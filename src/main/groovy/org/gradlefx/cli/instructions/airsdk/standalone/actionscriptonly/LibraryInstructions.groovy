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

package org.gradlefx.cli.instructions.airsdk.standalone.actionscriptonly

import org.gradle.api.Project
import org.gradlefx.cli.common.optioninjectors.LibraryOptionsInjector
import org.gradlefx.cli.common.optioninjectors.SimpleConventionOptionsInjector
import org.gradlefx.cli.common.optioninjectors.SourcesOptionsInjector
import org.gradlefx.cli.instructions.CompilerInstructionsBuilder

/**
 * Compiler instructions for a library project that only uses the AIR SDK and is a pure actionscript project.
 */
class LibraryInstructions extends CompilerInstructionsBuilder implements SimpleConventionOptionsInjector, LibraryOptionsInjector, SourcesOptionsInjector {

    public LibraryInstructions(Project project) {
        super(project)
    }
    
    @Override
    public void configure() {
        // The AIR compiler (compc-cli) does not load air-config.xml/airmobile-config.xml/flexconfig.xml automatically,
        // thus we add it here
        loadDefaultConfig()
        
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
