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

package org.gradlefx.tasks.compile

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ResolveException
import org.gradle.api.logging.LogLevel
import org.gradlefx.FlexType
import org.gradlefx.FrameworkLinkage;
import org.gradlefx.options.CompilerOption
import org.gradlefx.conventions.GradleFxConvention
import org.gradle.api.Project
import org.gradle.api.Task

abstract class AbstractCompileTask extends DefaultTask {

    GradleFxConvention flexConvention;

    protected AbstractCompileTask() {
        logging.setLevel(LogLevel.INFO)

        flexConvention = project.convention.plugins.flex

        initInputDirectory()
        initOutputDirectory()
    }

    private def initInputDirectory() {
        flexConvention.srcDirs.each { sourceDirectory ->
            inputs.dir sourceDirectory
        }
    }

    private def initOutputDirectory() {
        outputs.dir project.buildDir
    }
    
    /**
     * Adds all the source paths (project.srcDirs) including the locale path (project.localeDir) as compile arguments
     * @param compilerArguments
     */
    protected void addSourcePaths(List compilerArguments) {
        //add locale path to source paths if any locales are defined
        if (flexConvention.locales && flexConvention.locales.size()) {
            flexConvention.srcDirs.add flexConvention.localeDir
        }
        
        flexConvention.srcDirs.each { sourcePath ->
            File sourcePathDir = project.file(sourcePath)
            String path = project.file(sourcePath).path
            if (sourcePath == flexConvention.localeDir) path += '/{locale}'

            if (sourcePathDir.exists() || sourcePath.contains('{')) {
                compilerArguments.add("${CompilerOption.SOURCE_PATH}+=" + path)
            }
        }
    }
    
    protected void addLocales(List compilerArguments) {
        if (flexConvention.locales && flexConvention.locales.size()) {
            compilerArguments.add("${CompilerOption.LOCALE}=" + flexConvention.locales.join(','))
        }
    }

    /**
     * Adds all the dependencies for the given configuration as compile arguments
     * @param configuration
     * @param compilerArgument
     */
    protected void addLibraries(Set libraryFiles, Configuration configuration, CompilerOption compilerOption, List compilerArguments) {
        libraryFiles.each { dependency ->
            //only add swc dependencies, no use in adding pom dependencies
            if (dependency.name.endsWith(FlexType.swc.toString()) || dependency.isDirectory()) {
                if (!dependency.exists()) {
                    String errorMessage = "Couldn't find the ${dependency.name} file - are you sure the path is correct? "
                    errorMessage += "Dependency path: " + dependency.path
                    throw new ResolveException(configuration, new Throwable(errorMessage))
                }

                compilerArguments.add("${compilerOption}+=${dependency.path}");
            }
        }
    }
    
    protected void addPlayerLibrary(List compilerArguments) {
        String libPath = "${flexConvention.flexHome}/frameworks/libs/player/{targetPlayerMajorVersion}.{targetPlayerMinorVersion}/playerglobal.swc"
        compilerArguments.add("${CompilerOption.EXTERNAL_LIBRARY_PATH}+=${libPath}");
    }
    
    protected void addFramework(List compilerArguments) {
        FrameworkLinkage linkage = flexConvention.frameworkLinkage
        
        //ii's a pure AS project: we don't want to load the Flex configuration
        if (!linkage.usesFlex())
            compilerArguments.add("-load-config=")
        //when FrameworkLinkage is the default for this compiler, we don't have to do anything
        else if (!linkage.isCompilerDefault(flexConvention.type)) {
            //remove RSL's defined in config.xml
            compilerArguments.add("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH}=")
            
            //set the RSL's defined in config.xml on the library path
            def flexConfig = new XmlSlurper().parse(flexConvention.configPath)
            flexConfig['runtime-shared-library-path']['path-element'].each {
                compilerArguments.add("${linkage.getCompilerOption()}+=${flexConvention.flexHome}/frameworks/${it}")
            }
        }
    }
	
	def handleBuildIfFailed(antResultProperty, antOutputProperty, taskName) {
		if (ant.properties[antResultProperty] != '0') {
			throw new Exception("${taskName} compilation failed: ${ant.properties[antOutputProperty]}\n")
		}
	}
	
	def showAntOutput(antOutput) {
		println antOutput
	}

    /**
     * Adds a dependency on tasks with the specified name in other projects.  The other projects are determined from
     * project lib dependencies using the specified configuration name. These may be projects this project depends on or
     * projects that depend on this project based on the useDependOn argument.
     *
     * @param otherProjectTaskName name of task in other projects
     * @param configurationName name of configuration to use to find the other projects
     */
    protected void addDependsOnTaskInOtherProjects(String otherProjectTaskName, String configurationName) {
        final Configuration configuration = project.getConfigurations().getByName(configurationName);
        this.dependsOn(configuration.getTaskDependencyFromProjectDependency(true, otherProjectTaskName));
    }
}
