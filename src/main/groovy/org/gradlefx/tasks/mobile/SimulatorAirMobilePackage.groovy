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
        return "${project.buildDir}/${flexConvention.output}-${airMobile.simulatorTarget}.${flexConvention.airMobile.outputExtension}"
    }
}

