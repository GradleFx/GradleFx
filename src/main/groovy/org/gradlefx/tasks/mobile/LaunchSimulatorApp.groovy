package org.gradlefx.tasks.mobile

import org.gradlefx.tasks.Tasks

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Chaos Encoder</a>
 */

class LaunchSimulatorApp extends LaunchApp {
    def getInstallAppTaskName() {
        Tasks.INSTALL_SIMULATOR_MOBILE_TASK_NAME
    }

    def getPlatformSdk() {
        flexConvention.airMobile.simulatorPlatformSdk
    }

    def getTargetDevice() {
        flexConvention.airMobile.simulatorTargetDevice
    }
}
