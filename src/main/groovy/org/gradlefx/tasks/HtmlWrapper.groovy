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

import org.gradle.api.tasks.TaskAction
import org.gradle.api.DefaultTask
import org.gradlefx.conventions.HtmlWrapperConvention;

class HtmlWrapper extends DefaultTask {

    public HtmlWrapper() {
        description = 'Creates an HTML wrapper for the generated swf'
    }

	@TaskAction
	def generateHtmlWrapper() {
        HtmlWrapperConvention wrapper = project.convention.plugins.flex.htmlWrapper
        
        createOutputDirectoryIfNotExists wrapper.output
        
        File source = wrapper.source ? project.file(wrapper.source) : null
        
        if(source && source.exists()) {
            generateCustomWrapper(source, wrapper)
        }
        else {
            generateDefaultWrapper(wrapper)
        }
	}
    
    private def generateDefaultWrapper(HtmlWrapperConvention wrapper) {
        ant.'html-wrapper'(
            title:               wrapper.title,
            file:                wrapper.file,
            height:              "$wrapper.percentHeight%",
            width:               "$wrapper.percentWidth%",
            application:         wrapper.application,
            swf:                 wrapper.swf,
            history:             wrapper.history.toString(),
            'express-install':   wrapper.expressInstall.toString(),
            'version-detection': wrapper.versionDetection.toString(),
            output:              wrapper.output
        )
    }
    
    private def generateCustomWrapper(File source, HtmlWrapperConvention wrapper) {
        def defaultTokens = [
            application:    wrapper.application,
            percentHeight:  "$wrapper.percentHeight%",
            percentWidth:   "$wrapper.percentWidth%",
            swf:            wrapper.swf,
            title:          wrapper.title
        ]
        
        def tokens = defaultTokens + wrapper.tokenReplacements
        
        ant.copy(file: source, tofile: "${wrapper.output}/${wrapper.file}") {
            filterchain() {
                tokenfilter() {
                    replacestring(from: '${', to: '{')
                }
                replacetokens(begintoken: '{', endtoken: '}') {
                    tokens.each { key, value ->
                        token(key: key, value: value)
                    }
                }
            }
        }
    }
    
    private def createOutputDirectoryIfNotExists(String outputPath) {
        File output = project.file outputPath
        if (!output.exists()) output.mkdir()
    }
}
