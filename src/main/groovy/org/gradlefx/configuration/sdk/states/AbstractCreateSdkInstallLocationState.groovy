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
import org.gradlefx.conventions.GradleFxConvention
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.gradlefx.configuration.sdk.*

abstract class AbstractCreateSdkInstallLocationState implements SdkInitState {

    protected static final Logger LOG = LoggerFactory.getLogger 'gradlefx'

    Project project
    GradleFxConvention flexConvention
    SdkType sdkType
    SdkInstallLocation installLocation
    String sentryFilename
    protected String configName
    Boolean isInstallationRequired
    Boolean isInstalled

    AbstractCreateSdkInstallLocationState(SdkType sdkType, String sentryFilename, String configName, Boolean isInstallationRequired) {
        this.sdkType = sdkType
        this.sentryFilename = sentryFilename
        this.configName = configName
        this.isInstallationRequired = isInstallationRequired
        this.isInstalled = false
    }

    void process(SdkInitialisationContext context) {
        LOG.debug("Determining SDK install location")

        SdkInstallLocationFactory locationFactory = new SdkInstallLocationFactory(context.project, sentryFilename)
        installLocation = locationFactory.createSdkLocation(sdkType)

        flexConvention = (GradleFxConvention) context.project.convention.plugins.flex
        flexConvention.flexHome = installLocation.directory.absolutePath

        project = context.project

        isInstalled = determineIsInstalled(installLocation)
    }

    /**
     * When an SDK is manually installed and specified via the flexHome property, it can't be
     * detected with sentry files. In this case we have to fall back to another detection mechanism.
     * @param installLocation location where the sdk should have been installed
     * @return true when the SDK has been found in the install location
     */
    abstract protected boolean isSdkPresentFallback(SdkInstallLocation installLocation);

    private boolean determineIsInstalled(SdkInstallLocation installLocation) {
        if(installLocation.manualInstall) {
            return isSdkPresentFallback(installLocation)
        } else {
            return new File(installLocation.directory, sentryFilename).exists()
        }
    }
}
