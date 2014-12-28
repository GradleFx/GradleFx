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

import org.gradlefx.cli.compiler.CompilerOption;


class CompcAdditionalPropertiesValidator extends AbstractCompilerPropertiesValidator {

    void execute() {
        if (usesCompilerOptionInAdditionalProperties(CompilerOption.INCLUDE_LIBRARIES)) {
            addCompilerOptionWarning(CompilerOption.INCLUDE_LIBRARIES, "specify the library as an 'internal' Gradle dependendency")
        }

        if (usesCompilerOptionInAdditionalProperties(CompilerOption.EXTERNAL_LIBRARY_PATH)) {
            addCompilerOptionWarning(CompilerOption.EXTERNAL_LIBRARY_PATH, "specify the library as an 'external' Gradle dependendency")
        }

        if (usesCompilerOptionInAdditionalProperties(CompilerOption.LIBRARY_PATH)) {
            addCompilerOptionWarning(CompilerOption.LIBRARY_PATH, "specify the library as a 'merged' Gradle dependendency")
        }

        if (usesCompilerOptionInAdditionalProperties(CompilerOption.OUTPUT)) {
            addCompilerOptionWarning(CompilerOption.OUTPUT, "specify the name of the output file in the 'output' flexConvention property")
        }

        if (usesCompilerOptionInAdditionalProperties(CompilerOption.INCLUDE_FILE)) {
            addCompilerOptionWarning(CompilerOption.INCLUDE_FILE, "put the resource files in the directories specified in the 'resourceDirs' flexConvention property")
        }

        if (usesCompilerOptionInAdditionalProperties(CompilerOption.SOURCE_PATH)) {
            addCompilerOptionWarning(CompilerOption.SOURCE_PATH, """specify the source directory as an entry in the 'srcDirs'
                                                                        flexConvention property. e.g. srcDirs.add('src/main/flex')""")
        }

        if (usesCompilerOptionInAdditionalProperties(CompilerOption.LOCALE)) {
            addCompilerOptionWarning(CompilerOption.LOCALE, "add the locale to the 'locales' flexConvention property. e.g. locales.add('EN_US')")
        }
    }

}
