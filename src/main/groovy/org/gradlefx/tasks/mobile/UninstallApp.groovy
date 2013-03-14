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
    def launch() {                                          //todo remove duplicate with LaunchApp
        //flexConvention.airMobile.
        def builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        def doc = builder.parse(project.file(flexConvention.air.applicationDescriptor).newInputStream())
        def xpath = XPathFactory.newInstance().newXPath()

        def appId = xpath.evaluate("/application/id", doc)

        addArgs "-uninstallApp",
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
