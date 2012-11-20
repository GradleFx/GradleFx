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

package org.gradlefx.ide.tasks

import org.gradle.api.Project

class FlashBuilderUtil {
    public static final String eclipseProject = '.project'
    public static final String actionScriptProperties = '.actionScriptProperties'
    public static final String flexLibProperties = '.flexLibProperties'
    public static final String flexProperties = '.flexProperties'
    public static final String flexUnitSettings = '.FlexUnitSettings'

    public String getOutputDir(Project project) {
        String outputDir = project.convention.plugins.flex.type.isLib() ? 'bin' : 'bin-debug';

        if (project.file(actionScriptProperties).exists()) {
            def props = new XmlSlurper().parse(project.projectDir.path + '/' + actionScriptProperties)
            outputDir = props.compiler.@outputFolderPath.text()
        }

        return outputDir
    }

}
