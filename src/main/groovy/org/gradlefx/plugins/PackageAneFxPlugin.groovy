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

package org.gradlefx.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradlefx.tasks.ane.PackageAne

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Chaos Encoder</a>
 */
class PackageAneFxPlugin implements Plugin<Project> {
    protected Project project;


    @Override
    protected void configure(Project project) {

    }

    @Override
    public void apply(Project project) {
        this.project = project

        applyPlugins()

        addTasks()
        project.afterEvaluate {
            configure(project)
        }
    }

    def addTasks() {
        project.tasks.create('package', PackageAne)
    }

    protected void applyPlugins() {
        applyPlugin 'base'
    }

    protected void applyPlugin(String name) {
        project.apply(plugin: name)
    }


}
