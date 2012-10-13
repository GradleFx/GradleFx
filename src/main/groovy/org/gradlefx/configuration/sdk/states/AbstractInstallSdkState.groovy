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

package org.gradlefx.configuration.sdk.states

import org.gradle.api.Project
import org.gradlefx.configuration.sdk.SdkInitState
import org.gradlefx.configuration.sdk.SdkInitialisationContext
import org.gradlefx.configuration.sdk.SdkInstallLocation

abstract class AbstractInstallSdkState implements SdkInitState {

    SdkInstallLocation sdkInstallLocation
    File packagedSdkFile
    Project project

    AbstractInstallSdkState(SdkInstallLocation sdkInstallLocation, File packagedSdkFile) {
        this.sdkInstallLocation = sdkInstallLocation
        this.packagedSdkFile = packagedSdkFile
    }

    void process(SdkInitialisationContext context) {
        this.project = context.project

        try {
            unpackSdk()
            downloadSdkDependencies()
        } catch (Exception e) {
            revertInstall()

            throw e; //fail on purpose
        }
    }

    abstract void unpackSdk()

    void revertInstall() {
        LOG.info("reverting SDK installation")
        sdkInstallLocation.directory.deleteDir()
    }


    /**
     * Override this method when addition dependencies are required for the SDK.
     */
    protected void downloadSdkDependencies() {
    }

}
