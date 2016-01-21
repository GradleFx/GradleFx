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

import org.gradle.api.artifacts.Configuration
import org.gradlefx.configuration.sdk.SdkInitState
import org.gradlefx.configuration.sdk.SdkType
import org.gradlefx.configuration.sdk.states.AbstractCreateSdkInstallLocationState
import org.gradlefx.configuration.Configurations

class CreateAirSdkInstallLocationState extends AbstractCreateSdkInstallLocationState {
    CreateAirSdkInstallLocationState(Boolean isInstallationRequired) {
        super(SdkType.AIR, ".air.sentry", Configurations.AIRSDK_CONFIGURATION_NAME.configName(), isInstallationRequired)
    }

    @Override
    SdkInitState nextState() {
        //if lib/adt.jar is found in the SDK installation, it will mark this project as AIR-enabled.
        if (isInstalled || isInstallationRequired) {
            flexConvention.sdkTypes.add(SdkType.AIR);
        }

        if (!isInstalled && isInstallationRequired) {
            Configuration flexSdkConfiguration = project.configurations.getByName(configName)
            return new InstallAirSdkState(installLocation, flexSdkConfiguration.singleFile, sentryFilename)
        } else {
            return null;
        }
    }
}
