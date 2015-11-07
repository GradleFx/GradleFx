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
package org.gradlefx.tasks.mobile

import org.gradlefx.cli.compiler.CompilerOption
import org.gradlefx.tasks.adt.AdtTask
import org.gradlefx.tasks.TaskGroups

/**
 * Uninstalls the app from the target device.
 */
class UninstallApp extends AdtTask {

    public UninstallApp() {
        description "uninstall app from the target device"
        group = TaskGroups.UPLOAD
    }

    @Override
    def launch() {
        addArgs CompilerOption.UNINSTALL_APP.optionName,
                CompilerOption.PLATFORM.optionName,
                flexConvention.airMobile.platform

        if(platformSdk != null) {
            addArgs CompilerOption.PLATFORM_SDK.optionName, platformSdk
        }

        if(targetDevice != null) {
            addArgs CompilerOption.DEVICE.optionName, targetDevice
        }

        def appId = InstallAppUtils.getLaunchAppId(flexConvention, project)
        addArgs CompilerOption.APP_ID.optionName, appId

        return super.launch()
    }

    @Override
    def handleIfFailed(String antResultProperty, String antOutputProperty, String antErrorProperty) {
        if (ant.properties[antResultProperty] != '0') {
            LOG.error("${description} failed: ${ant.properties[antOutputProperty]}" +
                    " ${ant.properties[antErrorProperty]} Error code ${ant.properties[antResultProperty]}")
        }
    }

    def getPlatformSdk() {
        flexConvention.airMobile.platformSdk
    }

    def getTargetDevice() {
        flexConvention.airMobile.targetDevice
    }
}
