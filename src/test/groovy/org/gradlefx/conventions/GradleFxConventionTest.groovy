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

package org.gradlefx.conventions

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import org.gradlefx.plugins.GradleFxPlugin;

class GradleFxConventionTest extends Specification {

    Project project = ProjectBuilder.builder().build();
    GradleFxConvention flexConvention
    String flexHome = './src/test/resources/valid-flex-sdk'

    def setup() {
        new GradleFxPlugin().apply(project)
        flexConvention = project.convention.plugins.flex
        flexConvention.flexHome = flexHome
    }
    
    def "default output must be project name"() {
        when:
            flexConvention.type = FlexType.swf
            
        then:
            flexConvention.output == 'test'
    }

    def "default frameworkLinkage for swc type must be external"() {
        when:
            flexConvention.type = FlexType.swc

        then:
            flexConvention.frameworkLinkage == FrameworkLinkage.external
    }
    
    def "default frameworkLinkage for swf type must be rsl"() {
        when:
            flexConvention.type = FlexType.swf

        then:
            flexConvention.frameworkLinkage == FrameworkLinkage.rsl
    }
    
    def "default frameworkLinkage for air type must be rsl"() {
        when:
            flexConvention.type = FlexType.air

        then:
            flexConvention.frameworkLinkage == FrameworkLinkage.rsl
    }
    
    def "default frameworkLinkage for mobile type must be rsl"() {
        when:
            flexConvention.type = FlexType.mobile

        then:
            flexConvention.frameworkLinkage == FrameworkLinkage.rsl
    }

    def "all air properties should be set when defining all of them in the air closure"() {
        def keystoreValue = "keystoreName.p12"
        def storepassValue = "storepassCode"
        def applicationDescriptorValue = "appDescriptor.xml"
        def includeFileTreesValue = [ project.fileTree(dir: 'src/main/actionscript/', include: 'assets/appIcon.png') ]

        when:
            flexConvention.air {
                keystore                keystoreValue
                storepass               storepassValue
                applicationDescriptor   applicationDescriptorValue
                includeFileTrees        includeFileTreesValue
            }

        then:
            flexConvention.air.keystore == keystoreValue
            flexConvention.air.storepass == storepassValue
            flexConvention.air.applicationDescriptor == applicationDescriptorValue
            flexConvention.air.includeFileTrees == includeFileTreesValue
    }

    def "only applicationDescriptor air property should be set when defining it in the air closure"() {
        def oldKeystoreValue = flexConvention.air.keystore
        def oldStorepassValue = flexConvention.air.storepass
        def oldIncludeFileTreesValue = flexConvention.air.includeFileTrees
        def applicationDescriptorValue = "appDescriptor.xml"

        when:
            flexConvention.air {
                applicationDescriptor   applicationDescriptorValue
            }

        then:
            flexConvention.air.keystore == oldKeystoreValue
            flexConvention.air.storepass == oldStorepassValue
            flexConvention.air.includeFileTrees == oldIncludeFileTreesValue
            flexConvention.air.applicationDescriptor == applicationDescriptorValue
    }

    def "all asdoc properties should be set when defining all of them in the asdoc closure"() {
        def outputDirValue = 'customDocumentation'
        def additionalASDocOptionsValue = ['-strict=false']

        when:
            flexConvention.asdoc {
                outputDir = outputDirValue
                additionalASDocOptions = additionalASDocOptionsValue
            }

        then:
            flexConvention.asdoc.outputDir == outputDirValue
            flexConvention.asdoc.additionalASDocOptions == additionalASDocOptionsValue
    }

    def "only outputDirValue asdoc property should be set when defining it in the asdoc closure"() {
        def outputDirValue = 'customDocumentation'
        def oldAdditionalASDocOptionsValue = flexConvention.asdoc.additionalASDocOptions

        when:
            flexConvention.asdoc {
                outputDir = outputDirValue
            }

        then:
            flexConvention.asdoc.outputDir == outputDirValue
            flexConvention.asdoc.additionalASDocOptions == oldAdditionalASDocOptionsValue
    }

    def "all flexunit properties should be set when defining all of them in the flexunit closure"() {
        def playerValue = 'air'
        def commandValue = 'flashplayer.exe'
        def toDirValue = project.file('unittests').path + "/reports"
        def workingDirValue = project.file('unittests').path
        def haltOnFailureValue = !flexConvention.flexUnit.haltOnFailure
        def verboseValue = !flexConvention.flexUnit.verbose
        def localTrustedValue = !flexConvention.flexUnit.localTrusted
        def portValue = flexConvention.flexUnit.port + 100
        def bufferValue = flexConvention.flexUnit.buffer + 100
        def timeoutValue = flexConvention.flexUnit.timeout + 100
        def failurePropertyValue = 'customflexUnitFailed'
        def headlessValue = !flexConvention.flexUnit.headless
        def displayValue = flexConvention.flexUnit.display + 1
        def includesValue = ['**/*CustomTest.as']
        def excludesValue = ['**/*CustomIntegrationTest']

        when:
            flexConvention.flexUnit {
                player          playerValue
                command         commandValue
                toDir           toDirValue
                workingDir      workingDirValue
                haltOnFailure   haltOnFailureValue
                verbose         verboseValue
                localTrusted    localTrustedValue
                port            portValue
                buffer          bufferValue
                timeout         timeoutValue
                failureProperty failurePropertyValue
                headless        headlessValue
                display         displayValue
                includes        includesValue
                excludes        excludesValue
            }

        then:
            flexConvention.flexUnit.player == playerValue
            flexConvention.flexUnit.command == commandValue
            flexConvention.flexUnit.toDir == toDirValue
            flexConvention.flexUnit.workingDir == workingDirValue
            flexConvention.flexUnit.haltOnFailure == haltOnFailureValue
            flexConvention.flexUnit.verbose == verboseValue
            flexConvention.flexUnit.localTrusted == localTrustedValue
            flexConvention.flexUnit.port == portValue
            flexConvention.flexUnit.buffer == bufferValue
            flexConvention.flexUnit.timeout == timeoutValue
            flexConvention.flexUnit.failureProperty == failurePropertyValue
            flexConvention.flexUnit.headless == headlessValue
            flexConvention.flexUnit.display == displayValue
            flexConvention.flexUnit.includes == includesValue
            flexConvention.flexUnit.excludes == excludesValue
    }

    def "only playerValue flexunit property should be set when defining it in the flexunit closure"() {
        def playerValue = 'air'
        def oldCommandValue = flexConvention.flexUnit.command
        def oldToDirValue = flexConvention.flexUnit.toDir
        def oldWorkingDirValue = flexConvention.flexUnit.workingDir
        def oldHaltOnFailureValue = flexConvention.flexUnit.haltOnFailure
        def oldVerboseValue = flexConvention.flexUnit.verbose
        def oldLocalTrustedValue = flexConvention.flexUnit.localTrusted
        def oldPortValue = flexConvention.flexUnit.port
        def oldBufferValue = flexConvention.flexUnit.buffer
        def oldTimeoutValue = flexConvention.flexUnit.timeout
        def oldFailurePropertyValue = flexConvention.flexUnit.failureProperty
        def oldHeadlessValue = flexConvention.flexUnit.headless
        def oldDisplayValue = flexConvention.flexUnit.display
        def oldIncludesValue = flexConvention.flexUnit.includes
        def oldExcludesValue = flexConvention.flexUnit.excludes

        when:
            flexConvention.flexUnit {
                player          playerValue
            }

        then:
            flexConvention.flexUnit.player == playerValue
            flexConvention.flexUnit.command == oldCommandValue
            flexConvention.flexUnit.toDir == oldToDirValue
            flexConvention.flexUnit.workingDir == oldWorkingDirValue
            flexConvention.flexUnit.haltOnFailure == oldHaltOnFailureValue
            flexConvention.flexUnit.verbose == oldVerboseValue
            flexConvention.flexUnit.localTrusted == oldLocalTrustedValue
            flexConvention.flexUnit.port == oldPortValue
            flexConvention.flexUnit.buffer == oldBufferValue
            flexConvention.flexUnit.timeout == oldTimeoutValue
            flexConvention.flexUnit.failureProperty == oldFailurePropertyValue
            flexConvention.flexUnit.headless == oldHeadlessValue
            flexConvention.flexUnit.display == oldDisplayValue
            flexConvention.flexUnit.includes == oldIncludesValue
            flexConvention.flexUnit.excludes == oldExcludesValue
    }

    def "all htmlWrapper properties should be set when defining all of them in the htmlWrapper closure"() {
        def titleValue = 'customTitle'
        def fileValue = 'wrapper.html'
        def percentWidthValue  = flexConvention.htmlWrapper.percentWidth - 1
        def percentHeightValue = flexConvention.htmlWrapper.percentHeight -1
        def applicationValue = 'applicationName'
        def swfValue = 'swfName'
        def historyValue = !flexConvention.htmlWrapper.history
        def expressInstallValue = !flexConvention.htmlWrapper.expressInstall
        def versionDetectionValue = !flexConvention.htmlWrapper.versionDetection
        def outputValue = project.file('htmlWrapper').path

        when:
            flexConvention.htmlWrapper {
                title               titleValue
                file                fileValue
                percentWidth        percentWidthValue
                percentHeight       percentHeightValue
                application         applicationValue
                swf                 swfValue
                history             historyValue
                expressInstall      expressInstallValue
                versionDetection    versionDetectionValue
                output              outputValue
            }

        then:
            flexConvention.htmlWrapper.title == titleValue
            flexConvention.htmlWrapper.file == fileValue
            flexConvention.htmlWrapper.percentWidth == percentWidthValue
            flexConvention.htmlWrapper.percentHeight == percentHeightValue
            flexConvention.htmlWrapper.application == applicationValue
            flexConvention.htmlWrapper.swf == swfValue
            flexConvention.htmlWrapper.history == historyValue
            flexConvention.htmlWrapper.expressInstall == expressInstallValue
            flexConvention.htmlWrapper.versionDetection == versionDetectionValue
            flexConvention.htmlWrapper.output == outputValue
    }

    def "only title htmlWrapper property should be set when defining it in the htmlWrapper closure"() {
        def titleValue = 'customTitle'
        def oldFileValue = flexConvention.htmlWrapper.file
        def oldPercentWidthValue  = flexConvention.htmlWrapper.percentWidth
        def oldPercentHeightValue = flexConvention.htmlWrapper.percentHeight
        def oldApplicationValue = flexConvention.htmlWrapper.application
        def oldSwfValue = flexConvention.htmlWrapper.swf
        def oldHistoryValue = flexConvention.htmlWrapper.history
        def oldExpressInstallValue = flexConvention.htmlWrapper.expressInstall
        def oldVersionDetectionValue = flexConvention.htmlWrapper.versionDetection
        def oldOutputValue = flexConvention.htmlWrapper.output

        when:
            flexConvention.htmlWrapper {
                title               titleValue
            }

        then:
            flexConvention.htmlWrapper.title == titleValue
            flexConvention.htmlWrapper.file == oldFileValue
            flexConvention.htmlWrapper.percentWidth == oldPercentWidthValue
            flexConvention.htmlWrapper.percentHeight == oldPercentHeightValue
            flexConvention.htmlWrapper.application == oldApplicationValue
            flexConvention.htmlWrapper.swf == oldSwfValue
            flexConvention.htmlWrapper.history == oldHistoryValue
            flexConvention.htmlWrapper.expressInstall == oldExpressInstallValue
            flexConvention.htmlWrapper.versionDetection == oldVersionDetectionValue
            flexConvention.htmlWrapper.output == oldOutputValue
    }


    def "default target for mobile type must be apk"() {
        when:
        flexConvention.type = FlexType.mobile

        then:
        flexConvention.airMobile.target == "apk"
    }

    def "default simulatorTarget for mobile type must be apk"() {
        when:
        flexConvention.type = FlexType.mobile

        then:
        flexConvention.airMobile.simulatorTarget == "apk"
    }

    def "all airmobile properties should be set when defining all of them in the air closure"() {
        when:
        flexConvention.airMobile {
            target = 'apk'
            extensionDir = 'anedir'
            platformSdk = 'platformSdk'
            targetDevice = 'targetDevice'
        }

        then:
        flexConvention.airMobile.target == 'apk'
        flexConvention.airMobile.extensionDir == 'anedir'
        flexConvention.airMobile.platformSdk == 'platformSdk'
        flexConvention.airMobile.targetDevice == 'targetDevice'
    }

}
