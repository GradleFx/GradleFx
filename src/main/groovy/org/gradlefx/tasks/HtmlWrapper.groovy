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

import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ResolveException
import org.gradle.api.internal.AbstractTask
import org.gradle.api.tasks.TaskAction
import org.gradlefx.FlexType
import org.gradle.api.DefaultTask

class HtmlWrapper extends DefaultTask {

    public HtmlWrapper() {
        description = 'Creates an HTML wrapper for the generated swf'
    }

	@TaskAction
	def generateHtmlWrapper() {
        createOutputDirectoryIfNotExists()

		ant.'html-wrapper'(
			title:               project.htmlWrapper.title,
			file:                project.htmlWrapper.file,
			height:              project.htmlWrapper.height,
			width:               project.htmlWrapper.width,
			application:         project.htmlWrapper.application,
			swf:                 project.htmlWrapper.swf,
			history:             project.htmlWrapper.history,
			'express-install':   project.htmlWrapper.'express-install',
			'version-detection': project.htmlWrapper.'version-detection',
			output:              project.htmlWrapper.output
		)
	}

    private def createOutputDirectoryIfNotExists() {
        if(!project.htmlWrapper.output.exists()) {
            project.htmlWrapper.output.mkdir()
        }
    }
}


