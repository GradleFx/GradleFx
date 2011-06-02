package org.gradlefx

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

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.internal.artifacts.publish.DefaultPublishArtifact
import org.gradle.api.tasks.Delete
import org.gradlefx.FlexType
import org.gradlefx.conventions.GradleFxConvention
import org.gradlefx.tasks.Compc
import org.gradlefx.tasks.CopyResources
import org.gradlefx.tasks.Mxmlc
import org.gradlefx.tasks.Publish
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class GradleFxPlugin implements Plugin<Project> {

	public static final String COMPILE_TASK_NAME = 'compile'
    public static final String BUILD_TASK_NAME = 'build'
    public static final String PUBLISH_TASK_NAME = 'publish'
    public static final String COPY_RESOURCES_TASK_NAME = 'copyresources'
	public static final String CLEAN_TASK_NAME = 'clean'

    // configurations
    public static final String INTERNAL_CONFIGURATION_NAME = 'internal'
    public static final String EXTERNAL_CONFIGURATION_NAME = 'external'
    public static final String MERGE_CONFIGURATION_NAME = 'merged'
    public static final String RSL_CONFIGURATION_NAME = 'rsl'
    public static final String TEST_CONFIGURATION_NAME = 'test'

	Logger log = LoggerFactory.getLogger('flex')
	
	public void apply(Project project) {
		GradleFxConvention pluginConvention = new GradleFxConvention()
		project.convention.plugins.flex = pluginConvention

        addDefaultConfigurations(project)

		addBuild(project)
		addCompile(project, pluginConvention)
        addCopyResources(project)
		addClean(project)
        addPublish(project)

		addDependsOnOtherProjects(project)
		addDefaultArtifact(project)
	}

    private void addDefaultConfigurations(Project project) {
        project.configurations.add(INTERNAL_CONFIGURATION_NAME)
        project.configurations.add(EXTERNAL_CONFIGURATION_NAME)
        project.configurations.add(MERGE_CONFIGURATION_NAME)
        project.configurations.add(RSL_CONFIGURATION_NAME)
        project.configurations.add(TEST_CONFIGURATION_NAME)
    }
	
	private void addBuild(Project project) {
		DefaultTask buildTask = project.tasks.add(BUILD_TASK_NAME, DefaultTask)
		buildTask.setDescription("Assembles and tests this project.")
        buildTask.dependsOn(COMPILE_TASK_NAME)
	}
	
	private void addCompile(Project project, GradleFxConvention pluginConvention) {
		Task compileFlex = null
		if(project.type == FlexType.swc) {
			log.info "Adding ${COMPILE_TASK_NAME} task using compc to project ${project.name}" 
			compileFlex = project.tasks.add(COMPILE_TASK_NAME, Compc)
			compileFlex.outputs.dir project.buildDir
			pluginConvention.output = "${project.buildDir}/${project.name}.swc"
		}
		else if(project.type == FlexType.swf) {
			log.info "Adding ${COMPILE_TASK_NAME} task using mxmlc to project ${project.name}" 
			compileFlex = project.tasks.add(COMPILE_TASK_NAME, Mxmlc)
			pluginConvention.output = "${project.buildDir}/${project.name}.swf"
		}
		else {
			log.warn "Adding ${COMPILE_TASK_NAME} task using default implementation"
			compileFlex = project.tasks.add(COMPILE_TASK_NAME, DefaultTask)
			compileFlex.description = "Oops - we couldn't figure out if ${project.name} is a Flex component or a Flex application/module project."
		}

        compileFlex.dependsOn(COPY_RESOURCES_TASK_NAME)
	}

    private void addCopyResources(Project project) {
        project.tasks.add(COPY_RESOURCES_TASK_NAME, CopyResources)
    }
	
	private void addClean(final Project project) {
		Delete clean = project.tasks.add(CLEAN_TASK_NAME, Delete)
		clean.description = "Deletes the build directory."
		clean.delete { project.buildDir }
	}

    private void addPublish(Project project) {
        project.tasks.add(PUBLISH_TASK_NAME, Publish)
    }
	
	private void addDependsOnOtherProjects(Project project) {
		// dependencies need to be added as a closure as we don't have the information at the moment to wire them up
		project.tasks.compile.dependsOn {
			Set dependentTasks = new HashSet()
			['external', 'merge', 'rsl'].each { configuration ->
				Set deps = project.configurations."${configuration}".getDependencies(ProjectDependency)
				println "deps are: ${deps}"
		    	deps.each { projectDependency ->
					//def projectDependency = (ProjectDependency) dependency
					println "path to dependency: ${projectDependency.dependencyProject.path}"
					dependentTasks.add(projectDependency.dependencyProject.path + ':compile')
				}
			}
			dependentTasks
		}
	}

	private void addDefaultArtifact(Project project) {
		project.artifacts {
			libraries new DefaultPublishArtifact(project.name, project.type.toString(), project.type.toString(), null, new Date(), new File(project.output))
		}
	}
    
}