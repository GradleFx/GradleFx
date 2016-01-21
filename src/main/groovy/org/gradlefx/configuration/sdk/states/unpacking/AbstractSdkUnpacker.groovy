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

import org.gradle.api.file.FileTree
import org.gradle.api.file.FileVisitDetails
import org.gradlefx.configuration.sdk.SdkInstallLocation
import org.gradle.api.Project
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AbstractSdkUnpacker implements Unpacker {

    protected static final Logger LOG = LoggerFactory.getLogger 'gradlefx'

    protected File packagedSdkFile
    protected String someSdkRootDirectoryName
    protected SdkInstallLocation sdkInstallLocation
    protected Project project

    /**
     * @param project
     * @param sdkInstallLocation
     * @param packagedSdkFile The SDK archive
     * @param someSdkRootDirectoryName This should be the name of a directory which is located at the root of the SDK.
     *        This is being used to determine the relative location of the SDK within the archive
     */
    AbstractSdkUnpacker(Project project, SdkInstallLocation sdkInstallLocation, File packagedSdkFile, String someSdkRootDirectoryName) {
        this.project = project
        this.sdkInstallLocation = sdkInstallLocation
        this.packagedSdkFile = packagedSdkFile
        this.someSdkRootDirectoryName = someSdkRootDirectoryName
    }

    final void unpack() {
        unpackArchive()
        moveSdkFilesToRootDirectoryWhenNeeded()
    }

    /**
     * Should simply extract the sdk archive to the install directory
     */
    abstract void unpackArchive();

    private void moveSdkFilesToRootDirectoryWhenNeeded() {
        AntBuilder ant = new AntBuilder()

        //move sdk files to the install root directory in case it has unpacked in subdirectories
        File sdkDirectoryInInstallLocation = findSdkLocation()
        if(!sdkDirectoryInInstallLocation.equals(sdkInstallLocation.directory)) {
            ant.copy(toDir: sdkInstallLocation.directory.absolutePath) {
                fileset(dir: sdkDirectoryInInstallLocation.absolutePath)
            }

            sdkDirectoryInInstallLocation.deleteDir()
        }
    }

    /**
     * Searches in the subdirectories of sdkInstallLocation for the sdk installation files (some archives put these
     * in subdirectories instead of directly under the root of the archive).
     * @return
     */
    private File findSdkLocation() {
        FileTree installLocationTree = project.fileTree(sdkInstallLocation.directory.absolutePath)

        File relativeSdkLocation = null;
        installLocationTree.visit { FileVisitDetails fileDetails ->
            if (fileDetails.isDirectory() && fileDetails.name.equals(someSdkRootDirectoryName)) {
                relativeSdkLocation = new File(fileDetails.file.parent)
                fileDetails.stopVisiting()
            }
        }

        if(relativeSdkLocation == null) {
            throw new IllegalStateException("No SDK found in this archive")
        }

        return relativeSdkLocation
    }

    protected static boolean execute(List args, File cwd) {
        try {
            def process = (args as String[]).execute((List)null, cwd)
            LOG.info("Unpacking via '{}'", args.join(' '))
            process.waitFor()
            return process.exitValue() == 0
        } catch (IOException) {
            return false
        }
    }
}
