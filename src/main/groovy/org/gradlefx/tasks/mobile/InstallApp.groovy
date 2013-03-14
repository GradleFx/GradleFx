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
        dependsOn Tasks.UNINSTALL_MOBILE_TASK_NAME
        dependsOn Tasks.PACKAGE_MOBILE_TASK_NAME
    }

    @Override
    def launch() {
        addArgs "-installApp",
                "-platform",
                //fixme it must be custom
                "android",
                "-platformsdk",
                flexConvention.airMobile.platformSdk,
                "-device", flexConvention.airMobile.targetDevice,
                //todo fix package extension, it can be different
                "-package", project.file("${project.buildDir.name}/${flexConvention.output}").absolutePath + ".${flexConvention.airMobile.target}"

        return super.launch()
    }
}
