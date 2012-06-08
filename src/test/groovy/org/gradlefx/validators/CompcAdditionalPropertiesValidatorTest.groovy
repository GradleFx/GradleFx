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

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradlefx.options.CompilerOption
import spock.lang.Specification
import org.gradlefx.conventions.GradleFxConvention

class CompcAdditionalPropertiesValidatorTest extends Specification {

    CompcAdditionalPropertiesValidator validator = new CompcAdditionalPropertiesValidator();

    Project project = ProjectBuilder.builder().build();

    def setup() {
        validator.project = project
        validator.flexConvention = new GradleFxConvention(project)
    }

    def "use of compiler option used in Compc task should add warning message"() {
        when:
            validator.flexConvention.additionalCompilerOptions = compilerOption
            validator.execute();

        then:
            validator.getWarningMessages().size() == 1

        where:
            compilerOption << [
                                [CompilerOption.INCLUDE_LIBRARIES.optionName],
                                [CompilerOption.EXTERNAL_LIBRARY_PATH.optionName],
                                [CompilerOption.LIBRARY_PATH.optionName],
                                [CompilerOption.OUTPUT.optionName],
                                [CompilerOption.INCLUDE_FILE.optionName],
                                [CompilerOption.SOURCE_PATH.optionName],
                                [CompilerOption.LOCALE.optionName]
                              ]
    }
}
