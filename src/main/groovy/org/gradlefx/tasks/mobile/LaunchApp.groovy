package org.gradlefx.tasks.mobile

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
        dependsOn Tasks.INSTALL_MOBILE_TASK_NAME
    }

    @Override
    def launch() {
        //flexConvention.airMobile.
        def builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        def doc = builder.parse(project.file(flexConvention.air.applicationDescriptor).newInputStream())
        def xpath = XPathFactory.newInstance().newXPath()

        def appId = xpath.evaluate("/application/id", doc)

        addArgs "-launchApp",
                "-platform",
                //fixme it must be custom
                "android",
                "-platformsdk",
                flexConvention.airMobile.platformSdk,
                "-device", flexConvention.airMobile.targetDevice,
                //todo fix package extension, it can be different
                "-appid", appId

        return super.launch()
    }
}
