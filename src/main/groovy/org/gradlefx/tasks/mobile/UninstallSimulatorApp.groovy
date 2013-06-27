package org.gradlefx.tasks.mobile

import org.gradlefx.tasks.Tasks

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Chaos Encoder</a>
 */

class UninstallSimulatorApp extends UninstallApp {
    def getPlatformSdk() {
        flexConvention.airMobile.simulatorPlatformSdk
    }

    def getTargetDevice() {
        flexConvention.airMobile.simulatorTargetDevice
    }

}
