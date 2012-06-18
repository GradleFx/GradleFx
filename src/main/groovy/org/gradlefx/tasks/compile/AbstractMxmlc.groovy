

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

import org.gradle.api.Task;
import org.gradle.api.artifacts.ResolveException
import org.gradlefx.options.CompilerOption

/*
 * Abstract base class capturing common functionality to execute Flex's MXMLC compiler. 
 */

abstract class AbstractMxmlc extends AbstractCompileTask {
    
    protected AbstractMxmlc(Task task) {
        super(task)
    }

	protected File findFile(dirs, fileName) {
		for (String dirName : dirs) {
			File dir = task.project.file(dirName)
			File desiredFile = new File(dir, fileName)
			if (desiredFile.exists()) {
				return desiredFile
			}
		}
        
		throw new Exception(
            "The file ${fileName} couldn't be found in directories ${dirs}; " +
            "note that if you used the 'flashbuilder' plugin this file may have been moved " +
            "to comply to FlashBuilder's restrictions (execute 'flashbuilder' and see if you get any warnings); " +
            "consider editing the 'mainClass' property or switching to a decent IDE"
        )
	}

	def compile(antResultProperty, antOutputProperty, compilerArguments) {
		task.ant.java(
            jar:            flexConvention.flexHome + '/lib/mxmlc.jar',
			dir:            flexConvention.flexHome + '/frameworks',
			fork:           true,
			resultproperty: antResultProperty,
			outputproperty: antOutputProperty
        ) {
            flexConvention.jvmArguments.each {
                jvmarg(value: it)
            }

			compilerArguments.each {
				arg(value: it)
			}
		}
	}

	def compileFlex(antResultProperty, antOutputProperty, taskName, compilerArguments) {
		compile antResultProperty, antOutputProperty, compilerArguments
	
		handleBuildIfFailed antResultProperty, antOutputProperty, 'taskName'
			
		showAntOutput task.ant.properties[antOutputProperty]
	}
	
    def addRsls(List compilerArguments) {
        task.project.configurations.rsl.files.each { dependency ->
            if (!dependency.exists()) {
				throw new ResolveException("Couldn't find the ${dependency.name} file - are you sure the path is correct?")
            }
			compilerArguments.add("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH}+=${dependency.path},${dependency.name[0..-2]}f")
        }
    }
}