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

package org.gradlefx.configuration.sdk.states.flex

import org.gradle.api.artifacts.Configuration
import org.gradlefx.configuration.sdk.SdkInitState
import org.gradlefx.configuration.sdk.SdkInstallLocation
import org.gradlefx.configuration.sdk.SdkType
import org.gradlefx.configuration.sdk.states.AbstractCreateSdkInstallLocationState
import org.gradlefx.configuration.Configurations

class CreateFlexSdkInstallLocationState extends AbstractCreateSdkInstallLocationState {
    CreateFlexSdkInstallLocationState(Boolean isInstallationRequired) {
        super(SdkType.Flex, ".flex.sentry", Configurations.FLEXSDK_CONFIGURATION_NAME.configName(), isInstallationRequired)
    }

    @Override
    SdkInitState nextState() {
        //if lib/mxmlc.jar is found in the SDK installation, it will mark this project as Flex-enabled.
        if (isInstalled || isInstallationRequired) {
            flexConvention.sdkTypes.add(SdkType.Flex);
        }

        if (!isInstalled && isInstallationRequired) {
            Configuration flexSdkConfiguration = project.configurations.getByName(configName)
            return new InstallFlexSdkState(installLocation, flexSdkConfiguration.singleFile, sentryFilename)
        } else {
            return null;
        }
    }

    /**
     * Fallback to SDK specific file detection in case sentry file detection can't be used.
     */
    @Override
    protected boolean isSdkPresentFallback(SdkInstallLocation installLocation) {
        return new File(installLocation.directory, "lib/mxmlc.jar").exists()
    }
}
