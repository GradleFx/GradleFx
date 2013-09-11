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

import org.gradlefx.cli.CompilerOption
import org.gradlefx.tasks.adt.AdtTask
import org.gradlefx.tasks.TaskGroups
import org.gradlefx.tasks.Tasks

/**
 * Installs the app to the device.
 */
class InstallApp extends AdtTask {

    public InstallApp() {
        description "install app to target device"
        group = TaskGroups.UPLOAD
        dependsOn uninstallTaskName
        dependsOn packageTaskName
    }

    @Override
    def launch() {
        addArgs CompilerOption.INSTALL_APP.optionName,
                CompilerOption.PLATFORM.optionName,
                flexConvention.airMobile.platform

        if(platformSdk != null) {
            addArgs CompilerOption.PLATFORM_SDK.optionName, platformSdk
        }

        if(targetDevice != null) {
            addArgs CompilerOption.DEVICE.optionName, targetDevice
        }

        addArgs CompilerOption.PACKAGE.optionName, packageOutputPath

        return super.launch()
    }

    def getPackageOutputPath() {
        return InstallAppUtils.getReleaseOutputPath(flexConvention, project)
    }

    def getPlatformSdk() {
        flexConvention.airMobile.platformSdk
    }

    def getTargetDevice() {
        flexConvention.airMobile.targetDevice
    }

    def getUninstallTaskName() {
        Tasks.UNINSTALL_MOBILE_TASK_NAME
    }

    def getPackageTaskName() {
        Tasks.PACKAGE_MOBILE_TASK_NAME
    }
}
