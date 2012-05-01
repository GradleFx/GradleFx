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

package org.gradlefx.validators.runner

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.gradle.api.Project
import org.gradlefx.validators.ProjectPropertyValidator

class FailOnErrorValidatorRunner implements ValidatorRunner {

    private static final Logger LOG = LoggerFactory.getLogger("gradlefx")

    private Project project
    private List<ProjectPropertyValidator> validators = []

    public FailOnErrorValidatorRunner(Project project) {
        this.project = project
    }
    
    ValidatorRunner add(ProjectPropertyValidator validator) {
        validator.setProject(project)
        validators.add(validator)

        return this
    }

    void run() {
        validators.each { ProjectPropertyValidator validator ->
            validator.execute()

            if(validator.hasErrors()) {
                throw new IllegalStateException("Error: \n" + validator.getErrorMessages())
            }
            
            if(validator.hasWarnings()) {
                for(String warning : validator.getWarningMessages()) {
                    LOG.warn(warning)
                }
            }
        }
    }
}
