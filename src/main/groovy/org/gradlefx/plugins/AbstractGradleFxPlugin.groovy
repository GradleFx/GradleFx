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
import org.gradle.api.artifacts.Configuration;
import org.gradlefx.configuration.Configurations;
import org.gradlefx.conventions.GradleFxConvention;


abstract class AbstractGradleFxPlugin implements Plugin<Project> {
    
    protected Project project
    protected GradleFxConvention flexConvention
    
    
    @Override
    public void apply(Project project) {
        this.project = project
        
        applyPlugins()
        addDefaultConfigurations()

        if (!project.convention.plugins.flex) {
            GradleFxConvention pluginConvention = new GradleFxConvention(project)
            project.convention.plugins.flex = pluginConvention
        }

        flexConvention = (GradleFxConvention) project.convention.plugins.flex
        
        addTasks()
        project.afterEvaluate {
            configure(project)
        }
    }
    
    protected void applyPlugins() {
        applyPlugin 'base'
    }
    
    protected void applyPlugin(String name) {
        project.apply(plugin: name)
    }
    
    protected void addDefaultConfigurations() {
        List names = project.configurations.collect { it.name }
        
        Configurations.DEPENDENCY_CONFIGURATIONS.each { Configurations configuration ->
            if (!names.contains(configuration.configName())) addConfiguration configuration.configName()
        }
    }
    
    abstract protected void addTasks()
    
    protected void configure(Project project) {}
    
    protected Task addTask(String name, Class taskClass) {
        return project.tasks.add(name, taskClass)
    }
    
    protected Task addTask(String name, Class taskClass, Closure condition) {
        //always add tasks to make sure they are immediately on the task graph,
        //but remove them after evaluation if it turns out we don't need them
        Task task = project.tasks.add name, taskClass
        
        project.afterEvaluate {
            if (!condition()) project.tasks.remove task
        }
        
        return task
    }
    
    protected Configuration addConfiguration(String name) {
        return project.configurations.add(name)
    }

}
