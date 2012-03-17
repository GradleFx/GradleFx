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

package org.gradlefx

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.dsl.ArtifactHandler
import org.gradle.api.internal.artifacts.publish.DefaultPublishArtifact
import org.gradle.api.tasks.Delete
import org.gradlefx.conventions.GradleFxConvention
import org.gradlefx.tasks.CopyResources
import org.gradlefx.tasks.HtmlWrapper
import org.gradlefx.tasks.Publish
import org.gradlefx.tasks.Test
import org.gradlefx.tasks.compile.TestCompile

import org.gradlefx.tasks.compile.factory.CompileTaskClassFactoryImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.gradlefx.tasks.AirPackage
import org.gradlefx.configuration.FlexAntTasksConfigurator
import org.gradlefx.configuration.FlexUnitAntTasksConfigurator
import org.gradlefx.tasks.Tasks
import org.gradlefx.configuration.Configurations

class GradleFxPlugin implements Plugin<Project> {

    Logger log = LoggerFactory.getLogger('flex')

    private Project project

    public void apply(Project project) {
        this.project = project

        project.apply(plugin: 'base')

        GradleFxConvention pluginConvention = new GradleFxConvention(project)
        project.convention.plugins.flex = pluginConvention

        addDefaultConfigurations()

        addBuild()
        addCopyResources()
        addPublish()
		addTest()

        //do these tasks in the afterEvaluate phase because they need property access
        project.afterEvaluate {
            configureAntWithFlex()
			configureAntWithFlexUnit()
            addCompile(pluginConvention)
            addPackage()
            addHtmlWrapper()
            addDependsOnOtherProjects()
            addDefaultArtifact()
        }
    }

    private void configureAntWithFlex() {
        new FlexAntTasksConfigurator(project).configure()
    }

	private void configureAntWithFlexUnit() {
        new FlexUnitAntTasksConfigurator(project).configure()
	}
	
    private void addDefaultConfigurations() {
        project.configurations.add(Configurations.INTERNAL_CONFIGURATION_NAME)
        project.configurations.add(Configurations.EXTERNAL_CONFIGURATION_NAME)
        project.configurations.add(Configurations.MERGE_CONFIGURATION_NAME)
        project.configurations.add(Configurations.RSL_CONFIGURATION_NAME)
        project.configurations.add(Configurations.TEST_CONFIGURATION_NAME)
        project.configurations.add(Configurations.THEME_CONFIGURATION_NAME)
    }

    private void addBuild() {
        DefaultTask buildTask = project.tasks.add(Tasks.BUILD_TASK_NAME, DefaultTask)
        buildTask.setDescription("Assembles and tests this project.")
        buildTask.dependsOn(Tasks.TEST_TASK_NAME)
    }

    private void addCompile(GradleFxConvention pluginConvention) {
        Class<Task> compileClass = new CompileTaskClassFactoryImpl().createCompileTaskClass(project.type)
        project.tasks.add(Tasks.COMPILE_TASK_NAME, compileClass)
    }

    private void addPackage(GradleFxConvention pluginConvention) {
        if(project.type == FlexType.air) {
            Task packageTask = project.tasks.add(Tasks.PACKAGE_TASK_NAME, AirPackage)
            packageTask.dependsOn(Tasks.COMPILE_TASK_NAME)
        }
    }
	
	private void addTest() {
		Task test = project.tasks.add(Tasks.TEST_TASK_NAME, Test)
		test.description = 'Run the FlexUnit tests.'
	}

    private void addHtmlWrapper() {
        if (project.type == FlexType.swf) {
            project.tasks.add(Tasks.CREATE_HTML_WRAPPER, HtmlWrapper)
        }
    }

    private void addCopyResources() {
        project.tasks.add(Tasks.COPY_RESOURCES_TASK_NAME, CopyResources)
    }

    private void addPublish() {
        project.tasks.add(Tasks.PUBLISH_TASK_NAME, Publish)
    }

    private void addDependsOnOtherProjects() {
        // dependencies need to be added as a closure as we don't have the information at the moment to wire them up
        project.tasks.compile.dependsOn {
            Set dependentTasks = new HashSet()
            project.configurations.each { Configuration configuration ->
                Set deps = project.configurations."${configuration.name}".getDependencies().withType(ProjectDependency)
                deps.each { projectDependency ->
                    //def projectDependency = (ProjectDependency) dependency
                    println "path to dependency: ${projectDependency.dependencyProject.path}"
                    dependentTasks.add(projectDependency.dependencyProject.path + ':compile')
                }
            }
            dependentTasks
        }
    }

    /**
     * If this is an implementation project (compiles a swc of swf), it adds an artifact
     * of the given project to the default configuration.
     * @param project
     */
    private void addDefaultArtifact() {
        if (isImplementationProject()) {
            addProjectArtifactToDefaultConfiguration()
        }
    }

    /**
     * This project is an implementation project when its type is filled in.
     * @return
     */
    private Boolean isImplementationProject() {
        return project.type != null;
    }

    /**
     * Adds an artifact to the default configuration.
     * @param project
     */
    private void addProjectArtifactToDefaultConfiguration() {
        project.artifacts { ArtifactHandler artifactHandler ->
            File artifactFile = new File(project.buildDir.path + "/" + project.output + "." + project.type)
            def artifact = new DefaultPublishArtifact(project.name, project.type.toString(), project.type.toString(), null, new Date(), artifactFile)
            artifactHandler."${Configurations.ARCHIVES_CONFIGURATION_NAME}" artifact
            artifactHandler."${Configurations.DEFAULT_CONFIGURATION_NAME}" artifact
        }
    }

}
