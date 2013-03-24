package org.gradlefx.tasks.mobile

import org.gradlefx.tasks.Tasks

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Chaos Encoder</a>
 */

class InstallSimulatorApp extends InstallApp {
    def getPlatformSdk() {
        flexConvention.airMobile.simulatorPlatformSdk
    }

    def getTargetDevice() {
        flexConvention.airMobile.simulatorTargetDevice
    }

    def getUninstallTaskName() {
        Tasks.UNINSTALL_SIMULATOR_MOBILE_TASK_NAME
    }

    def getPackageTaskName() {
        Tasks.PACKAGE_MOBILE_TASK_NAME
    }
}
