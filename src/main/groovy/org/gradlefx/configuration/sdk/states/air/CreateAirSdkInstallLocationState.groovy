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

package org.gradlefx.configuration.sdk.states.air

import org.gradlefx.configuration.sdk.SdkInitState
import org.gradlefx.configuration.sdk.SdkType
import org.gradlefx.configuration.sdk.states.AbstractCreateSdkInstallLocationState
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.file.BaseDirFileResolver
import org.gradle.internal.nativeplatform.filesystem.FileSystems
import org.gradle.api.artifacts.Configuration
import org.gradlefx.configuration.sdk.SdkInitialisationContext
import org.gradle.api.Project
import org.gradlefx.configuration.Configurations

class CreateAirSdkInstallLocationState extends AbstractCreateSdkInstallLocationState {

    Project project

    CreateAirSdkInstallLocationState() {
        super(SdkType.AIR)
    }

    @Override
    void process(SdkInitialisationContext context) {
        super.process(context)

        project = context.project
    }

    @Override
    SdkInitState nextState() {
        if (!isAirSdkInstalled()) {
            Configuration flexSdkConfiguration = project.configurations.getByName(Configurations.AIRSDK_CONFIGURATION_NAME.configName())
            return new InstallAirSdkState(installLocation, flexSdkConfiguration.singleFile)
        } else {
            return null; //it's already installed
        }
    }

    private Boolean isAirSdkInstalled() {
        FileResolver sdkInstallDirectoryResolver = new BaseDirFileResolver(FileSystems.default, installLocation.directory)
        return sdkInstallDirectoryResolver.resolve("lib/adt.jar").exists()
    }
}
