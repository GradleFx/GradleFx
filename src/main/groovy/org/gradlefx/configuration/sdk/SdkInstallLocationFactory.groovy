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

package org.gradlefx.configuration.sdk

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradlefx.configuration.Configurations
import org.gradlefx.conventions.GradleFxConvention
import org.gradlefx.validators.FlexSDKSpecifiedValidator
import org.gradlefx.validators.runner.FailOnErrorValidatorRunner

import java.security.MessageDigest

class SdkInstallLocationFactory {

    private static final String SDKS_BASE_DIR_NAME = "sdks/"

    GradleFxConvention gradleFxConvention
    File sdksInstallBaseDirectory
    Project project
    String sentryFilename

    SdkInstallLocationFactory(Project project, String sentryFilename) {
        this.project = project
        this.sentryFilename = sentryFilename
        gradleFxConvention = (GradleFxConvention) project.convention.plugins.flex
        sdksInstallBaseDirectory = new File(gradleFxConvention.gradleFxUserHomeDir, SDKS_BASE_DIR_NAME)
    }

    SdkInstallLocation createSdkLocation(SdkType sdkType) {
        Boolean isFlexSdkDeclaredAsDependency = isFlexSdkDeclaredAsDependency()
        Boolean isAirSdkDeclaredAsDependency = isAirSdkDeclaredAsDependency()

        File flexSdkArchive = getSdkArchiveForConfiguration(Configurations.FLEXSDK_CONFIGURATION_NAME)
        File airSdkArchive = getSdkArchiveForConfiguration(Configurations.AIRSDK_CONFIGURATION_NAME)

        File sdkInstallDirectory = null
        if(isFlexSdkDeclaredAsDependency && isAirSdkDeclaredAsDependency) {
            //hash of both flex and air sdk archives define the directory name
            String hashedSdkDirectoryName = filesToHash(flexSdkArchive, airSdkArchive)
            sdkInstallDirectory = new File(sdksInstallBaseDirectory, hashedSdkDirectoryName)
        } else if (isFlexSdkDeclaredAsDependency && !isAirSdkDeclaredAsDependency) {
            //air SDK is assumed to be in the flex sdk archive
            String hashedSdkDirectoryName = filesToHash(flexSdkArchive)
            sdkInstallDirectory = new File(sdksInstallBaseDirectory, hashedSdkDirectoryName)
        } else if (!isFlexSdkDeclaredAsDependency && isAirSdkDeclaredAsDependency) {
            //air SDK is assumed not to be in the flex sdk archive
            String hashedSdkDirectoryName = filesToHash(airSdkArchive)
            sdkInstallDirectory = new File(sdksInstallBaseDirectory, hashedSdkDirectoryName)
        } else {
            //custom path
            new FailOnErrorValidatorRunner(project)
                    .add(new FlexSDKSpecifiedValidator())
                    .run()

            //create the sentry file if it doesn't exist yet, to indicate it has already been installed manually by the user
            new File(gradleFxConvention.flexHome, sentryFilename).createNewFile()

            sdkInstallDirectory = new File(gradleFxConvention.flexHome)
        }

        return new SdkInstallLocation(sdkType, sdkInstallDirectory)
    }

    private Boolean isFlexSdkDeclaredAsDependency() {
        return getSdkArchiveForConfiguration(Configurations.FLEXSDK_CONFIGURATION_NAME) != null
    }

    private Boolean isAirSdkDeclaredAsDependency() {
        return getSdkArchiveForConfiguration(Configurations.AIRSDK_CONFIGURATION_NAME) != null
    }

    private File getSdkArchiveForConfiguration(Configurations configurationName) {
        Configuration sdkConfiguration = project.configurations.getByName(configurationName.configName())
        if(!sdkConfiguration.files.empty){
            return sdkConfiguration.singleFile
        } else {
            return null;
        }
    }

    private String filesToHash(File ...files) {
        String filePathsCombined = ""
        files.each {
            filePathsCombined += it.absolutePath
        }

        def messageDigest = MessageDigest.getInstance("SHA1")
        messageDigest.update( filePathsCombined.getBytes() );

        return new BigInteger(1, messageDigest.digest()).toString(16).padLeft(40, '0')
    }
}
