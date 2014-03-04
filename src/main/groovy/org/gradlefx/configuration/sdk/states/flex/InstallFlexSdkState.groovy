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

package org.gradlefx.configuration.sdk.states.flex

import org.gradle.api.internal.file.BaseDirFileResolver
import org.gradle.api.internal.file.FileResolver
import org.gradle.internal.nativeplatform.filesystem.FileSystems
import org.gradlefx.configuration.sdk.SdkInstallLocation
import org.gradlefx.configuration.sdk.states.AbstractInstallSdkState
import org.gradlefx.conventions.GradleFxConvention
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class InstallFlexSdkState extends AbstractInstallSdkState {

    private static final Logger LOG = LoggerFactory.getLogger 'gradlefx'
    private static final String SDK_INSTALLER_CONFIG_URL = 'http://incubator.apache.org/flex/sdk-installer-config.xml'
    private static final String SOME_FLEX_SDK_ROOT_DIRECTORY = "frameworks"

    InstallFlexSdkState(SdkInstallLocation sdkInstallLocation, File packagedSdkFile) {
        super(sdkInstallLocation, packagedSdkFile, SOME_FLEX_SDK_ROOT_DIRECTORY)
    }

    /**
     * Starting from Apache Flex 4.8 additional dependencies are required
     * which can be downloaded by executing the %FLEX_HOME%/frameworks/downloads.xml ant script.
     */
    @Override
    void downloadSdkDependencies() {
        if (sdkRequiresAdditionalDownloads()) {
            executeSdkDownloadsScript()
            downloadPlayerGlobalSwc()
            updateFrameworkConfigFiles()
        }
    }

    /**
     * Copies the flashbuilder ide config files to the framework folder because these files contain the correct
     * path to playerglobal (it points to the playerglobal.swc installed in the flex install directory)
     */
    private void updateFrameworkConfigFiles() {
        FileResolver sdkInstallDirectoryResolver = new BaseDirFileResolver(FileSystems.default, sdkInstallLocation.directory)
        File ideConfigDir = sdkInstallDirectoryResolver.resolve("ide/flashbuilder/config")
        File frameworksDir = sdkInstallDirectoryResolver.resolve("frameworks")

        new AntBuilder().copy(toDir: frameworksDir) {
            fileset(dir: ideConfigDir)
        }
    }

    private boolean sdkRequiresAdditionalDownloads() {
        return getAdditionalDownloadsAntScriptFile().exists()
    }

    private File getAdditionalDownloadsAntScriptFile() {
        FileResolver sdkInstallDirectoryResolver = new BaseDirFileResolver(FileSystems.default, sdkInstallLocation.directory)
        return sdkInstallDirectoryResolver.resolve("frameworks/downloads.xml")
    }

    private void executeSdkDownloadsScript() {
        boolean showPrompts = ((GradleFxConvention) project.convention.plugins.flex).sdkAutoInstall.showPrompts

        AntBuilder ant = new AntBuilder()
        ant.ant(antfile: getAdditionalDownloadsAntScriptFile(), dir: getAdditionalDownloadsAntScriptDirectory()) {
            if(!showPrompts) {
                property(name: 'build.noprompt', value: true)
            }
        }
    }

    private File getAdditionalDownloadsAntScriptDirectory() {
        FileResolver sdkInstallDirectoryResolver = new BaseDirFileResolver(FileSystems.default, sdkInstallLocation.directory)
        return sdkInstallDirectoryResolver.resolve("frameworks")
    }

    private void downloadPlayerGlobalSwc() {
        FileResolver sdkInstallDirectoryResolver = new BaseDirFileResolver(FileSystems.default, sdkInstallLocation.directory)
        File playerGlobalSwcInstallLocation = sdkInstallDirectoryResolver.resolve("frameworks/libs/player/11.1")
        FileResolver playerGlobalSwcInstallLocationResolver = new BaseDirFileResolver(FileSystems.default, playerGlobalSwcInstallLocation)
        File playerGlobalSwcInstallFile = playerGlobalSwcInstallLocationResolver.resolve("playerglobal.swc")
        String playerGlobalSwcDownloadUrl = getPlayerGlobalSwcDownloadUrl()

        playerGlobalSwcInstallLocation.mkdirs()
        playerGlobalSwcInstallFile.createNewFile()
        playerGlobalSwcInstallFile.withOutputStream { out ->
            out << new URL(playerGlobalSwcDownloadUrl).openStream()
        }

    }

    private String getPlayerGlobalSwcDownloadUrl() {
        def sdkInstallerConfig = new XmlSlurper().parse(SDK_INSTALLER_CONFIG_URL)

        LOG.error(sdkInstallerConfig.toString())

        String playerGlobalBaseUrl = sdkInstallerConfig.files.file.find { it -> it.@name.text() == 'FlashPlayer' }.@path
        String playerGlobalFileUrl = sdkInstallerConfig.files.file.find { it -> it.@name.text() == 'FlashPlayer' }.@file

        LOG.error("player global url: " + playerGlobalBaseUrl + playerGlobalFileUrl)

        return playerGlobalBaseUrl + playerGlobalFileUrl
    }

}
