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

package org.gradlefx.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/*
 * A Gradle task to execute FlexUnit tests.
 */

class Test extends DefaultTask {

    public Test() {
        description = "Run the FlexUnit unit tests."
    }

    @TaskAction
    def runFlexUnit() {
		def reportDir = project.file(project.flexUnit.toDir)

		// you can't write to a directory that doesn't exist
		if(!reportDir.exists()) reportDir.mkdirs()
		
		project.flexUnit.swf = project.flexUnit.swf ?: project.testOutput 
		
		ant.flexunit(
			player:          project.flexUnit.player, 
			command:         project.flexUnit.command,
			swf:             project.flexUnit.swf,
			toDir:           project.flexUnit.toDir,
			workingDir:      project.flexUnit.workingDir,
			haltonfailure:   project.flexUnit.haltOnFailure,
			verbose:         project.flexUnit.verbose,
			localTrusted:    project.flexUnit.localTrusted,
			port:            project.flexUnit.port,
			buffer:          project.flexUnit.buffer,
			timeout:         project.flexUnit.timeout,
			failureproperty: project.flexUnit.failureProperty,
			headless:        project.flexUnit.headless,
			display:         project.flexUnit.display)
    }
}