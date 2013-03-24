package org.gradlefx.tasks.mobile

import org.gradle.api.Project
import org.gradlefx.conventions.GradleFxConvention

import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.*
import org.gradlefx.tasks.AdtTask
import org.gradlefx.tasks.TaskGroups
import org.gradlefx.tasks.Tasks

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Chaos Encoder</a>
 */
class LaunchApp extends AdtTask {

    public LaunchApp() {
        super()
        description "launch app to target device"
        group = TaskGroups.UPLOAD
        dependsOn installAppTaskName
    }

    @Override
    def launch() {
        //flexConvention.airMobile.
        def appId = InstallAppUtils.getLaunchAppId(flexConvention, project)

        addArgs "-launchApp",
                "-platform",
                flexConvention.airMobile.platform,
                "-platformsdk",
                platformSdk,
                "-device", targetDevice,
                "-appid", appId

        return super.launch()
    }

    def getInstallAppTaskName() {
        Tasks.INSTALL_MOBILE_TASK_NAME
    }

    def getPlatformSdk() {
        flexConvention.airMobile.platformSdk
    }

    def getTargetDevice() {
        flexConvention.airMobile.targetDevice
    }
}
