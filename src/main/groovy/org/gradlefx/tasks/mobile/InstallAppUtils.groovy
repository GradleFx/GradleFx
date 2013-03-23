package org.gradlefx.tasks.mobile

import org.gradle.api.Project
import org.gradlefx.conventions.GradleFxConvention

import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathFactory

class InstallAppUtils {

    static String getLaunchAppId(GradleFxConvention flexConvention, Project project) {
        def appId
        /*
        if (flexConvention.airMobile.platform == "android") {
            def builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
            def doc = builder.parse(project.file(flexConvention.air.applicationDescriptor).newInputStream())
            def xpath = XPathFactory.newInstance().newXPath()
            appId = xpath.evaluate("/application/id", doc)
        } else if (flexConvention.airMobile.platform == "ios") {
            appId = "${flexConvention.output}.${flexConvention.airMobile.outputExtension}"
        } else {
            throw new RuntimeException("unknow platform '${flexConvention.airMobile.platform}'")
        } */
        def builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        def doc = builder.parse(project.file(flexConvention.air.applicationDescriptor).newInputStream())
        def xpath = XPathFactory.newInstance().newXPath()
        appId = xpath.evaluate("/application/id", doc)
        return appId;
    }

}
