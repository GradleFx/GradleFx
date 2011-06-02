package org.gradlefx.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

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
class CopyResources extends DefaultTask {

    public CopyResources() {
        description = 'copies the resources to the build directory'
    }

    @TaskAction
    def copyResources() {
        logger.lifecycle('Copying resources to build directory...')

        project.resourceDirs.each { resourceDir ->
            def fromLocation = project.projectDir.path + resourceDir
            def toLocation = project.buildDir.path

            logger.info('from ' + fromLocation + ' to ' + toLocation)

            project.copy {
                from fromLocation
                into toLocation
            }
        }

    }
}
