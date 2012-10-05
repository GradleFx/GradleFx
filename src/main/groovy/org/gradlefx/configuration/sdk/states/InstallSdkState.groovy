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

import org.gradlefx.configuration.sdk.SdkInitState
import org.gradlefx.configuration.sdk.SdkInitialisationContext
import org.gradlefx.configuration.sdk.SdkInstallLocation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.file.BaseDirFileResolver
import org.gradle.internal.nativeplatform.filesystem.FileSystems

class InstallSdkState implements SdkInitState {

    protected static final Logger LOG = LoggerFactory.getLogger 'gradlefx'

    SdkInstallLocation sdkInstallLocation
    File packagedSdkFile

    InstallSdkState(SdkInstallLocation sdkInstallLocation, File packagedSdkFile) {
        this.sdkInstallLocation = sdkInstallLocation
        this.packagedSdkFile = packagedSdkFile
    }

    void process(SdkInitialisationContext context) {
        unpackSdk()
        downloadSdkDependencies()

        context.processNextState(new SetFlexHomeBasedOnSdkInstallLocationState(sdkInstallLocation))
    }

    private void unpackSdk() {
        if(packagedSdkFile.name.endsWith(".zip")) {
            LOG.info("Unpacking SDK...")

            AntBuilder ant = new AntBuilder()
            ant.unzip(src: packagedSdkFile.absolutePath, dest: sdkInstallLocation.directory.absolutePath,  overwrite:"true")
        } else if(packagedSdkFile.name.endsWith("tar.gz")) {
            LOG.info("Unpacking SDK...")

            AntBuilder ant = new AntBuilder()
            ant.gunzip(src: packagedSdkFile.absolutePath)

            String tarFile = packagedSdkFile.absolutePath.replaceFirst(".gz", "")
            ant.untar(src: tarFile, dest: sdkInstallLocation.directory.absolutePath)

            //cleanup by removing the temporary tar archive
            new File(tarFile).delete()
        } else {
            throw new RuntimeException("Unsupported sdk packaging type. Supported formats are zip or tar.gz")
        }
    }

    /**
     * Starting from Apache Flex 4.8 additional dependencies are required
     * which can be downloaded by executing the %FLEX_HOME%/frameworks/downloads.xml ant script.
     */
    private void downloadSdkDependencies() {
         if(sdkRequiresAdditionalDownloads()) {
             AntBuilder ant = new AntBuilder()
             ant.ant antfile: getAdditionalDownloadsAntScriptFile(), dir: getAdditionalDownloadsAntScriptDirectory()
         }
    }

    private boolean sdkRequiresAdditionalDownloads() {
        return getAdditionalDownloadsAntScriptFile().exists()
    }

    private File getAdditionalDownloadsAntScriptFile() {
        FileResolver sdkInstallDirectoryResolver = new BaseDirFileResolver(FileSystems.default, sdkInstallLocation.directory)
        return sdkInstallDirectoryResolver.resolve("frameworks/downloads.xml")
    }

    private File getAdditionalDownloadsAntScriptDirectory() {
        FileResolver sdkInstallDirectoryResolver = new BaseDirFileResolver(FileSystems.default, sdkInstallLocation.directory)
        return sdkInstallDirectoryResolver.resolve("frameworks")
    }
}
