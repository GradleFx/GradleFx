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

import groovy.io.FileType
import org.gradle.api.DefaultTask
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Mixin(IdeaUtil)
class IdeaClean extends DefaultTask implements CleanTask {
    public static final String NAME = 'ideaClean'

    protected static final Logger LOG = LoggerFactory.getLogger 'gradlefx'

    /** The name of the targeted IDE */
    protected String ideName

    /**
     * Constructor
     */
    public FlashBuilderClean() {
        ideName = 'IntelliJ Idea'
        description = "Cleans $ideName project, i.e. removes $ideName configuration files and folders"

        logging.setLevel LogLevel.INFO
    }

    @Override
    @TaskAction
    void cleanProject() {
        LOG.info "Removing $ideName project files"

        List<File> filesToDelete = [
                IdeaUtil.projectFolder,
                getOutputDir(project)
        ].collect {
            project.file it
        }

        project.projectDir.traverse(type: FileType.FILES, nameFilter: ~/.*${IdeaUtil.module}/) {
            filesToDelete.add it
        }

        boolean filesDeleted = false

        filesToDelete.each {
            if (it.exists()) {
                LOG.info "\t$it.name"
                filesDeleted = true
                //it.isFile() ? it.delete() : it.deleteDir()
            }
        }

        if (!filesDeleted) LOG.info "\tNothing to remove"
    }

}
