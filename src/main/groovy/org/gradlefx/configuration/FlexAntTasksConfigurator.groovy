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

package org.gradlefx.configuration

import org.gradle.api.Project
import org.gradlefx.validators.runner.FailOnErrorValidatorRunner
import org.gradlefx.validators.FlexSDKSpecifiedValidator

class FlexAntTasksConfigurator {

    private Project project

    FlexAntTasksConfigurator(Project project) {
        this.project = project;
    }

    public void configure() {
        new FailOnErrorValidatorRunner(project)
            .add(new FlexSDKSpecifiedValidator())
            .run()

        project.ant.property(name: 'FLEX_HOME', value: project.flexHome)
            project.ant.property(name: 'FLEX_LIB', value: '${FLEX_HOME}/frameworks/libs')
            project.ant.property(name: 'FLEX_ANT', value: '${FLEX_HOME}/ant')
            project.ant.property(name: 'FLEX_ANTLIB', value: '${FLEX_ANT}/lib')
            project.ant.property(name: 'FLEX_PLAYER_LIB', value: "\${FLEX_LIB}/player/${project.playerVersion}")

            project.ant.taskdef(resource: 'flexTasks.tasks') {
                classpath {
                    fileset(dir: '${FLEX_ANTLIB}') {
                        include(name: 'flexTasks.jar')
                    }
                }
            }
    }
}
