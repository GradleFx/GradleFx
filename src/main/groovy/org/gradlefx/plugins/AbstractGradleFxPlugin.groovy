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

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradlefx.conventions.GradleFxConvention;


abstract class AbstractGradleFxPlugin implements Plugin<Project> {
    
    protected Project project
    protected GradleFxConvention flexConvention
    
    
    @Override
    public void apply(Project project) {
        this.project = project
        
        applyPlugins()
        
        if (!project.convention.plugins.flex) {
            GradleFxConvention pluginConvention = new GradleFxConvention(project)
            project.convention.plugins.flex = pluginConvention
        }
        
        flexConvention = project.convention.plugins.flex
        
        addTasks()
    }
    
    protected void applyPlugins() {
        applyPlugin 'base'
    }
    
    protected void applyPlugin(String name) {
        project.apply(plugin: name)
    }
    
    abstract protected void addTasks()
    
    protected Task addTask(String name, Class task) {
        return project.tasks.add(name, task)
    }

}
