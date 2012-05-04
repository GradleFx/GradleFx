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

import org.gradlefx.options.CompilerOption
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MxmlcAdditionalPropertiesValidator extends AbstractCompilerPropertiesValidator {

    private static final Logger LOG = LoggerFactory.getLogger("gradlefx")

    void execute() {
        if(usesCompilerOptionInAdditionalProperties(CompilerOption.INCLUDE_LIBRARIES)) {
            addCompilerOptionWarning(CompilerOption.INCLUDE_LIBRARIES, "specify the library as an 'internal' Gradle dependendency")
        }

        if(usesCompilerOptionInAdditionalProperties(CompilerOption.EXTERNAL_LIBRARY_PATH)) {
            addCompilerOptionWarning(CompilerOption.EXTERNAL_LIBRARY_PATH, "specify the library as an 'external' Gradle dependendency")
        }

        if(usesCompilerOptionInAdditionalProperties(CompilerOption.LIBRARY_PATH)) {
            addCompilerOptionWarning(CompilerOption.LIBRARY_PATH, "specify the library as a 'merged' Gradle dependendency")
        }

        if(usesCompilerOptionInAdditionalProperties(CompilerOption.THEME)) {
            addCompilerOptionWarning(CompilerOption.THEME, "specify the library as a 'theme' Gradle dependendency")
        }

        if(usesCompilerOptionInAdditionalProperties(CompilerOption.RUNTIME_SHARED_LIBRARY_PATH)) {
            addCompilerOptionWarning(CompilerOption.RUNTIME_SHARED_LIBRARY_PATH, "specify the library as an 'rsl' Gradle dependendency")
        }

        if(usesCompilerOptionInAdditionalProperties(CompilerOption.OUTPUT)) {
            addCompilerOptionWarning(CompilerOption.OUTPUT, "specify the name of the output file in the 'output' convention property")
        }

        if(usesCompilerOptionInAdditionalProperties(CompilerOption.SOURCE_PATH)) {
            addCompilerOptionWarning(CompilerOption.SOURCE_PATH, """specify the source directory as an entry in the 'srcDirs'
                                                                convention property. e.g. srcDirs.add('src/main/flex')""")
        }

        if(usesCompilerOptionInAdditionalProperties(CompilerOption.LOCALE)) {
            addCompilerOptionWarning(CompilerOption.LOCALE, "add the locale to the 'locales' convention property. e.g. locales.add('EN_US')")
        }
    }
}
