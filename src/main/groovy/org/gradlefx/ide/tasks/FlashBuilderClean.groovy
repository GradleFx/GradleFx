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

import org.gradle.api.AntBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction
import org.gradlefx.tasks.TaskGroups
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Mixin(FlashBuilderUtil)
class FlashBuilderClean extends DefaultTask implements CleanTask {
    public static final String NAME = 'flashbuilderClean'

    protected static final Logger LOG = LoggerFactory.getLogger 'gradlefx'

    /** The name of the targeted IDE */
    protected String ideName

    /**
     * Constructor
     */
    public FlashBuilderClean() {
        ideName = 'FlashBuilder'
        group = TaskGroups.IDE
        description = "Cleans $ideName project, i.e. removes $ideName configuration files and folders"

        ant.setLifecycleLogLevel(AntBuilder.AntMessagePriority.INFO)
    }

    @Override
    @TaskAction
    public void cleanProject() {
        LOG.info "Removing $ideName project files"

        boolean filesDeleted = false
        [
                FlashBuilderUtil.eclipseProject,
                FlashBuilderUtil.actionScriptProperties,
                FlashBuilderUtil.flexProperties,
                FlashBuilderUtil.flexLibProperties,
                FlashBuilderUtil.flexUnitSettings,
                FlashBuilderUtil.externalToolBuilders,
                getOutputDir(project),
                '.settings',
                'bin-release'
        ].each {
            File file = project.file it
            if (file.exists()) {
                LOG.info "\t$it"
                filesDeleted = true
                file.isFile() ? file.delete() : file.deleteDir()
            }
        }

        if (!filesDeleted) LOG.info "\tNothing to remove"
    }

}
