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
import org.gradlefx.configuration.sdk.SdkInitialisationContext
import org.gradlefx.configuration.sdk.SdkInstallLocation
import org.gradlefx.conventions.GradleFxConvention
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SetAirHomeBasedOnSdkInstallLocationState implements SdkInitState {

    protected static final Logger LOG = LoggerFactory.getLogger 'gradlefx'

    SdkInstallLocation sdkInstallLocation

    SetAirHomeBasedOnSdkInstallLocationState(SdkInstallLocation sdkInstallLocation) {
        this.sdkInstallLocation = sdkInstallLocation
    }

    void process(SdkInitialisationContext context) {
        LOG.info("Setting airHome to " + sdkInstallLocation.directory.absolutePath)

        GradleFxConvention flexConvention = (GradleFxConvention) context.project.convention.plugins.flex

        flexConvention.airHome = sdkInstallLocation.directory.absolutePath
    }

    SdkInitState nextState() {
        return null //sdk initialization finished
    }
}
