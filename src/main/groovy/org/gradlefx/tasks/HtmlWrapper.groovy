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
import org.gradlefx.conventions.GradleFxConvention

class HtmlWrapper extends DefaultTask {

    GradleFxConvention flexConvention;

    public HtmlWrapper() {
        description = 'Creates an HTML wrapper for the generated swf'

        flexConvention = project.convention.plugins.flex
    }

	@TaskAction
	def generateHtmlWrapper() {
        createOutputDirectoryIfNotExists()

		ant.'html-wrapper'(
			title:               flexConvention.htmlWrapper.title,
			file:                flexConvention.htmlWrapper.file,
			height:              flexConvention.htmlWrapper.height,
			width:               flexConvention.htmlWrapper.width,
			application:         flexConvention.htmlWrapper.application,
			swf:                 flexConvention.htmlWrapper.swf,
			history:             flexConvention.htmlWrapper.history,
			'express-install':   flexConvention.htmlWrapper.'express-install',
			'version-detection': flexConvention.htmlWrapper.'version-detection',
			output:              flexConvention.htmlWrapper.output
		)
	}

    private def createOutputDirectoryIfNotExists() {
        if(!flexConvention.htmlWrapper.output.exists()) {
            flexConvention.htmlWrapper.output.mkdir()
        }
    }
}


