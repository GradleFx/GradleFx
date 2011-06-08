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

class Publish extends DefaultTask {

    public Publish() {
        description = "Publish build artifacts to specified directory."
    }

    @TaskAction
    def publishFlex() {
        // copy what we built to the publish directory
        project.copy {
            from project.buildDir
            into project.publishDir
            include '**/*'
        }
        // copy non-project RSL dependencies to the publish directory
        project.files(project.configurations.rsl) { libraries ->
            libraries?.files.each { library ->
                println "copying RSL dependency ${library.absolutePath} to publish directory ${project.publishDir}"
                project.copy {
                    from library.absolutePath
                    into project.publishDir
                }
            }
        }
    }
}