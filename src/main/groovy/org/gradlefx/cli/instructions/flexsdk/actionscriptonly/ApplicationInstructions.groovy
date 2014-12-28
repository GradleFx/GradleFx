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

package org.gradlefx.cli.instructions.flexsdk.actionscriptonly

import org.gradle.api.Project
import org.gradlefx.cli.common.optioninjectors.FlexFrameworkRslOptionsInjector
import org.gradlefx.cli.common.optioninjectors.FlexSDKResourcesOptionsInjector
import org.gradlefx.cli.common.optioninjectors.LibraryOptionsInjector
import org.gradlefx.cli.common.optioninjectors.SimpleConventionOptionsInjector
import org.gradlefx.cli.common.optioninjectors.SourcesOptionsInjector
import org.gradlefx.cli.compiler.CompilerOption
import org.gradlefx.cli.instructions.CompilerInstructionsBuilder
import org.gradlefx.conventions.FrameworkLinkage

/**
 * Compiler instructions for an application that makes use of the Flex SDK for compilation, but is a pure actionscript project.
 */
class ApplicationInstructions
        extends CompilerInstructionsBuilder
        implements LibraryOptionsInjector, SourcesOptionsInjector,
                FlexFrameworkRslOptionsInjector, FlexSDKResourcesOptionsInjector {

    ApplicationInstructions(Project project) {
        super(project)
    }
    
    @Override
    void configure() {
        disableLoadConfig()
        addPlayerGlobalFromConfig()

        //add every source directory
        addSourceDirectories()
        addLocaleSources()
        addLocales()

        //add dependencies
        addInternalLibraries()
        addExternalLibrariesForApp()
        addMergedLibraries()
        addTheme()
        addRSLs()
        addFrameworkRsls()

        addAdditionalCompilerOptions()
        addOutput()
        addMainClass()
    }
    
    public void addOutput() {
        addOutput "${project.buildDir.path}/${flexConvention.output}.swf"
    }
    
}
