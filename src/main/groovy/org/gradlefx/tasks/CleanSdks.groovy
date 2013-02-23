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
import org.gradlefx.configuration.sdk.SdkInstallLocationFactory
import org.gradlefx.configuration.sdk.SdkInstallLocation
import org.gradlefx.configuration.sdk.SdkType

class CleanSdks extends DefaultTask {

    @TaskAction
    public void deleteSdksDir() {
        group = TaskGroups.FLEX_SDK
        description = 'Deletes the Flex SDK directories from the Gradle cache.'

        SdkInstallLocationFactory locationFactory = new SdkInstallLocationFactory(project)
        SdkInstallLocation installLocation = locationFactory.createSdkLocation(SdkType.Flex)

        if(installLocation.directory.exists()) {
            installLocation.directory.deleteDir()
        }
    }
}
