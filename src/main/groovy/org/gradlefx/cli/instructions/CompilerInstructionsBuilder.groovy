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
import org.gradlefx.cli.common.optioninjectors.FlexFrameworkRslOptionsInjector
import org.gradlefx.cli.common.optioninjectors.SimpleConventionOptionsInjector
import org.gradlefx.cli.compiler.CompilerOptions
import org.gradlefx.conventions.GradleFxConvention
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class CompilerInstructionsBuilder implements SimpleConventionOptionsInjector, FlexFrameworkRslOptionsInjector {

    protected static final Logger LOG = LoggerFactory.getLogger 'gradlefx'

    CompilerOptions compilerOptions = new CompilerOptions()
    Project project
    GradleFxConvention flexConvention

    public CompilerInstructionsBuilder(Project project) {
        this.project = project
        flexConvention = project.convention.plugins.flex as GradleFxConvention
    }

    final CompilerOptions buildInstructions() {
        compilerOptions = new CompilerOptions()
        configure()
        compilerOptions
    }

    /**
     * Populates the compiler options.
     */
    protected abstract void configure()

}
