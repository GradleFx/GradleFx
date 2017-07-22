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

package org.gradlefx.configuration.sdk.states.unpacking

import org.gradle.api.Project
import org.gradlefx.configuration.sdk.SdkInstallLocation

class SdkDMGUnpacker extends AbstractSdkUnpacker {
    protected final String ADOBE_AIR_SDK_VOLUME_NAME = "/Volumes/AIR SDK"
    protected String diskNameAfterMounting

    SdkDMGUnpacker(Project project, SdkInstallLocation sdkInstallLocation, File packagedSdkFile, String someSdkRootDirectoryName) {
        super(project, sdkInstallLocation, packagedSdkFile, someSdkRootDirectoryName)
    }

    void unpackArchive() {
        mount()
        copyFiles()
        unmount()
    }

    /**
     * Mount the dmg file through command "hdiutil attach" and store the disk path
     * of the mounted volume
     */
    private void mount() {
        def mountCommand = ["hdiutil", "attach", "-nobrowse", packagedSdkFile.absolutePath];
        LOG.info("Mounting via '{}'", mountCommand.join(' '))

        def process = mountCommand.execute()
        process.waitFor()

        diskNameAfterMounting = process.text.eachLine { line ->
            // check if the line contains path to AIR SDK
            if (line.contains(ADOBE_AIR_SDK_VOLUME_NAME)) {
                return line.substring(0, line.indexOf(' '))
            }
        }
    }

    /**
     * Copy files from mounted Volume to sdk install location
     */
    private void copyFiles() {
        def copyCommand = ["cp", "-r", ADOBE_AIR_SDK_VOLUME_NAME + "/", sdkInstallLocation.directory.absolutePath];
        LOG.info("Copy via '{}'", copyCommand.join(' '))

        def process = copyCommand.execute()
        process.waitFor()
    }

    /**
     * Unmount disk related to the dmg
     */
    private void unmount() {
        def unmountCommand = ["hdiutil", "detach", diskNameAfterMounting];
        LOG.info("Unmount via '{}'", unmountCommand.join(' '))

        def process = unmountCommand.execute()
        process.waitFor()
    }
}
