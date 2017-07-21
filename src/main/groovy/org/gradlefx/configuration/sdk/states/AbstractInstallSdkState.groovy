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

package org.gradlefx.configuration.sdk.states

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradlefx.configuration.sdk.SdkInitState
import org.gradlefx.configuration.sdk.SdkInitialisationContext
import org.gradlefx.configuration.sdk.SdkInstallLocation
import org.gradlefx.configuration.sdk.states.unpacking.SdkDMGUnpacker
import org.gradlefx.configuration.sdk.states.unpacking.SdkTarGzUnpacker
import org.gradlefx.configuration.sdk.states.unpacking.SdkTbz2Unpacker
import org.gradlefx.configuration.sdk.states.unpacking.SdkZipUnpacker
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AbstractInstallSdkState implements SdkInitState {

    protected static final Logger LOG = LoggerFactory.getLogger 'gradlefx'

    SdkInstallLocation sdkInstallLocation
    File packagedSdkFile
    Project project
    String someSdkRootDirectoryName
    String sentryFilename

    /**
     *
     * @param sdkInstallLocation
     * @param packagedSdkFile
     * @param someSdkRootDirectoryName This should be the name of a directory which is located at the root of the SDK.
     *                             This is being used to determine the relative location of the SDK within the archive
     * @param sentryFilename the file to create after successful installation of the SDK
     */
    AbstractInstallSdkState(SdkInstallLocation sdkInstallLocation, File packagedSdkFile, String someSdkRootDirectoryName, String sentryFilename) {
        this.sdkInstallLocation = sdkInstallLocation
        this.packagedSdkFile = packagedSdkFile
        this.someSdkRootDirectoryName = someSdkRootDirectoryName
        this.sentryFilename = sentryFilename
    }

    void process(SdkInitialisationContext context) {
        this.project = context.project

        try {
            unpackSdk()
            downloadSdkDependencies()
            allowExecutionPermissionsOnSdkFiles()
            createSentryFile()
        } catch (Exception e) {
            revertInstall()

            throw e; //fail on purpose
        }
    }

    void unpackSdk() {
        if (packagedSdkFile.name.endsWith(".zip")) {
            LOG.info("Unpacking SDK...")
            new SdkZipUnpacker(project, sdkInstallLocation, packagedSdkFile, someSdkRootDirectoryName).unpack()
        } else if (packagedSdkFile.name.endsWith("tar.gz")) {
            LOG.info("Unpacking SDK...")
            new SdkTarGzUnpacker(project, sdkInstallLocation, packagedSdkFile, someSdkRootDirectoryName).unpack()
        } else if (packagedSdkFile.name.endsWith("tbz2")) {
            LOG.info("Unpacking SDK...")
            new SdkTbz2Unpacker(project, sdkInstallLocation, packagedSdkFile, someSdkRootDirectoryName).unpack()
        } else if (packagedSdkFile.name.endsWith("dmg")) {
            LOG.info("Unpacking SDK...")
            new SdkDMGUnpacker(project, sdkInstallLocation, packagedSdkFile, someSdkRootDirectoryName).unpack()
        } else {
            throw new RuntimeException("Unsupported sdk packaging type. Supported formats are zip, tar.gz, tbz2 and dmg")
        }
    }

    void revertInstall() {
        LOG.info("reverting SDK installation")
        sdkInstallLocation.directory.deleteDir()
    }

    SdkInitState nextState() {
        return null //the end
    }

    /**
     * Override this method when addition dependencies are required for the SDK.
     */
    protected void downloadSdkDependencies() {
    }

    /**
     * Set execution permissions on all SDK files to avoid problems with this.
     */
    void allowExecutionPermissionsOnSdkFiles() {
        sdkInstallLocation.directory.traverse { it.setExecutable(true, false) }
    }

    /**
     * Create the sentry file at the end of the SDK installation process.
     */
    void createSentryFile() {
        def sentryFile = new File(sdkInstallLocation.directory, sentryFilename)
        if (!sentryFile.createNewFile())
            throw new GradleException("Unable to create sentry file $sentryFile.absolutePath")
    }

}
