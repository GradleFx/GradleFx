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
import org.gradlefx.tasks.AdtTask
import org.gradlefx.tasks.TaskGroups

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Chaos Encoder</a>
 */
class UninstallApp extends AdtTask {

    public UninstallApp() {
        super()
        description "uninstall app to target device"
        group = TaskGroups.UPLOAD
    }

    @Override
    def launch() {
        def appId = InstallAppUtils.getLaunchAppId(flexConvention, project)

        addArgs CompilerOption.UNINSTALL_APP.optionName,
                CompilerOption.PLATFORM.optionName,
                flexConvention.airMobile.platform,
                CompilerOption.PLATFORM_SDK.optionName,
                platformSdk,
                CompilerOption.DEVICE.optionName, targetDevice,
                CompilerOption.APP_ID.optionName, appId

        return super.launch()
    }

    def getPlatformSdk() {
        flexConvention.airMobile.platformSdk
    }

    def getTargetDevice() {
        flexConvention.airMobile.targetDevice
    }
}
