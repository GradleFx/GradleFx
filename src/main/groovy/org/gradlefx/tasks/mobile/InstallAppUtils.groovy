/*
 * Copyright (c) 2011 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    static def getReleaseOutputPath(GradleFxConvention flexConvention, Project project) {
        return "${project.buildDir}/${flexConvention.output}.${flexConvention.airMobile.outputExtension}"
    }

    static def getSimulatorOutputPath(GradleFxConvention flexConvention, Project project) {
        return "${project.buildDir}/${flexConvention.output}-${flexConvention.airMobile.simulatorTarget}.${flexConvention.airMobile.outputExtension}"
    }
}
