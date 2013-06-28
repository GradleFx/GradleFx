package org.gradlefx.tasks.mobile

import org.gradlefx.tasks.AdtTask
import org.gradlefx.tasks.TaskGroups
import org.gradlefx.tasks.Tasks

import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathFactory

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Chaos Encoder</a>
 */
class UninstallApp extends AdtTask {

    public UninstallApp() {
        super()
        description "uninstall app to target device"
        group = TaskGroups.UPLOAD
        //dependsOn Tasks.PACKAGE_MOBILE_TASK_NAME
    }

    @Override
    def launch() {
        //flexConvention.airMobile.
        def appId = InstallAppUtils.getLaunchAppId(flexConvention, project)

        addArgs "-uninstallApp",
                "-platform",
                flexConvention.airMobile.platform,
                "-platformsdk",
                platformSdk,
                "-device", targetDevice,
                "-appid", appId

        return super.launch()
    }

    def getPlatformSdk() {
        flexConvention.airMobile.platformSdk
    }

    def getTargetDevice() {
        flexConvention.airMobile.targetDevice
    }
}
