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

import org.apache.commons.io.FileUtils
import org.apache.commons.lang.StringUtils
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.FileTreeElement
import org.gradle.api.tasks.TaskAction
import org.gradlefx.cli.compiler.CompilerOption
import org.gradlefx.conventions.AIRMobileConvention
import org.gradlefx.conventions.FlexType
import org.gradlefx.tasks.Tasks
import org.gradlefx.tasks.adt.AdtTask

/**
 * Base task for packaging mobile apps.
 */
class BaseAirMobilePackage extends AdtTask {

    public BaseAirMobilePackage() {
        description = "Packages the generated swf file into an mobile package";
        adtWorkDir = flexConvention.air.packageWorkDir
        dependsOn Tasks.COMPILE_TASK_NAME
    }

    @TaskAction
    def launch() {
        addArg CompilerOption.PACKAGE.optionName
        addArg CompilerOption.TARGET.optionName
        addArg target

        if(flexConvention.airMobile.sampler) {
            addArg CompilerOption.IOS_SAMPLER.optionName
        }

        if(flexConvention.airMobile.nonLegacyCompiler) {
            addArgs CompilerOption.USE_LEGACY_COMPILER.optionName, "no"
        }

        if (StringUtils.isNotEmpty(flexConvention.airMobile.provisioningProfile)) {
            addArgs CompilerOption.PROVISIONING_PROFILE.optionName, flexConvention.airMobile.provisioningProfile
        }

        addArgs CompilerOption.STORE_TYPE.optionName,
                "pkcs12",
                CompilerOption.KEYSTORE.optionName,
                flexConvention.air.keystore,
                CompilerOption.STOREPASS.optionName,
                flexConvention.air.storepass

        addArgs outputPath
        addArgs project.file(flexConvention.air.applicationDescriptor)

        addArg CompilerOption.CHANGE_DIRECTORY.optionName
        addArg project.buildDir.path
        if (flexConvention.air.mainSwfDir) {
            File swfDir = new File(project.buildDir, flexConvention.air.mainSwfDir);
            FileUtils.copyFileToDirectory(new File("${project.buildDir.path}/${flexConvention.output}.${FlexType.swf}"),swfDir)
            addArg "${flexConvention.air.mainSwfDir}/${flexConvention.output}.${FlexType.swf}"
        } else {
            addArg "${flexConvention.output}.${FlexType.swf}"
        }

        flexConvention.air.includeFileTrees.each { ConfigurableFileTree fileTree ->
            addArgs CompilerOption.CHANGE_DIRECTORY.optionName
            addArgs fileTree.dir.absolutePath

            fileTree.visit { FileTreeElement file ->
                if (!file.isDirectory()) {
                    addArgs file.relativePath
                }
            }
        }

        flexConvention.air.fileOptions.each {
            addArg it
        }

        if (StringUtils.isNotEmpty(flexConvention.airMobile.extensionDir)) {
            addArg(CompilerOption.EXTDIR.optionName)
            addArg(flexConvention.airMobile.extensionDir)
        }

        HashSet<String> aneFolders = new HashSet<String>()
        project.configurations.getAsMap().each {
            Set<File> files = it.value.files
            files.each {
                if (it.name.endsWith(".ane"))
                {
                    String folder = it.getParent()
                    if ( !aneFolders.contains(folder) ) {
                        aneFolders.add(it.getParent())
                        addArg CompilerOption.EXTDIR.optionName
                        addArg folder
                    }
                }
            }
        }

        addPlatformSdkParams()

        super.launch()
    }

    AIRMobileConvention getAirMobile() {
        flexConvention.airMobile
    }

    String getTarget() {
        airMobile.target
    }

    def getOutputPath() {
        return InstallAppUtils.getReleaseOutputPath(flexConvention, project)
    }

    def addPlatformSdkParams() {
        if(flexConvention.airMobile.platformSdk != null) {
            addArgs CompilerOption.PLATFORM_SDK.optionName, flexConvention.airMobile.platformSdk
        }
    }
}
