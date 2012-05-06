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
import org.gradlefx.FlexType
import org.gradle.api.logging.LogLevel
import org.gradlefx.options.CompilerOption
import org.gradlefx.conventions.GradleFxConvention

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
    protected void addLibraries(Set libraryFiles, Configuration configuration, String compilerArgument, List compilerArguments) {
        libraryFiles.each { dependency ->
            //only add swc dependencies, no use in adding pom dependencies
            if (dependency.name.endsWith(FlexType.swc.toString()) || dependency.isDirectory()) {
                if (!dependency.exists()) {
                    String errorMessage = "Couldn't find the ${dependency.name} file - are you sure the path is correct? "
                    errorMessage += "Dependency path: " + dependency.path
                    throw new ResolveException(configuration, new Throwable(errorMessage))
                }

                compilerArguments.add(compilerArgument + "+=" + dependency.path);
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
}
