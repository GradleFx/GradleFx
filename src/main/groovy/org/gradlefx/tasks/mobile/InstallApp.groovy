package org.gradlefx.tasks.mobile

import org.gradlefx.conventions.GradleFxConvention
import org.gradlefx.tasks.AdtTask
import org.gradlefx.tasks.TaskGroups
import org.gradlefx.tasks.Tasks

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Chaos Encoder</a>
 */
class InstallApp extends AdtTask {

    public InstallApp() {
        super()
        description "install app to target device"
        group = TaskGroups.UPLOAD
        dependsOn uninstallTaskName
        dependsOn packageTaskName
    }

    @Override
    def launch() {
        addArgs "-installApp",
                "-platform",
                flexConvention.airMobile.platform,
                "-platformsdk",
                getPlatformSdk(),
                "-device", targetDevice,
                "-package", InstallAppUtils.getReleaseOutputPath(flexConvention, project)

        return super.launch()
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
