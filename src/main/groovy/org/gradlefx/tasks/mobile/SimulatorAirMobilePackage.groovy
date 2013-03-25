package org.gradlefx.tasks.mobile

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Chaos Encoder</a>
 */

class SimulatorAirMobilePackage extends BaseAirMobilePackage {


    @Override
    def getTarget() {
        return airMobile.simulatorTarget
    }

    @Override
    def getOutputPath() {
        return InstallAppUtils.getSimulatorOutputPath(flexConvention, project)
    }

    def addPlatformSdkParams() {
        addArgs "-platformsdk", flexConvention.airMobile.simulatorPlatformSdk
    }
}

