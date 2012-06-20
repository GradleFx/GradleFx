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

package org.gradlefx.validators

import org.gradlefx.cli.CompilerOption;
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AbstractCompilerPropertiesValidator extends AbstractProjectPropertyValidator {

    private static final Logger LOG = LoggerFactory.getLogger('gradlefx')

    protected boolean usesCompilerOptionInAdditionalProperties(CompilerOption compilerOption) {
        return flexConvention.additionalCompilerOptions.any { String usedOption ->
            usedOption.startsWith compilerOption.optionName
        }
    }

    protected void addCompilerOptionWarning(CompilerOption compilerOption, String alternativeSolution) {
        addWarning "WARNING: The $compilerOption.optionName option is being used internally by GradleFx. " +
                   "Alternative: $alternativeSolution"
    }
}
