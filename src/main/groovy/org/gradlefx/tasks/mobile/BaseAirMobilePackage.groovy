package org.gradlefx.tasks.mobile

import org.apache.commons.lang.StringUtils
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.FileTreeElement
import org.gradle.api.tasks.TaskAction
import org.gradlefx.conventions.AIRMobileConvention
import org.gradlefx.conventions.FlexType
import org.gradlefx.tasks.AdtTask

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Chaos Encoder</a>
 */
class BaseAirMobilePackage extends AdtTask {
    public BaseAirMobilePackage() {
        super()
        description = "Packages the generated swf file into an mobile package";
        adtWorkDir = flexConvention.air.packageWorkDir
    }

    def AIRMobileConvention getAirMobile() {
        flexConvention.airMobile
    }

    def getTarget() {
        airMobile.target
    }

    def getOutputPath() {
        return InstallAppUtils.getReleaseOutputPath(flexConvention, project)
    }

    @TaskAction
    def launch() {
        addArg '-package'
        addArg '-target'
        addArg target

        if (StringUtils.isNotEmpty(flexConvention.airMobile.provisioning_profile)) {
            addArgs "-provisioning-profile", flexConvention.airMobile.provisioning_profile
        }


        addArgs "-storetype",
                "pkcs12",
                "-keystore",
                flexConvention.air.keystore,
                "-storepass",
                flexConvention.air.storepass

        addArgs outputPath
        addArgs project.file(flexConvention.air.applicationDescriptor)

        addArgs "-C", "${project.buildDir.absolutePath}", "${flexConvention.output}.${FlexType.swf}"

        flexConvention.air.includeFileTrees.each { ConfigurableFileTree fileTree ->
            addArgs "-C"
            addArgs fileTree.dir.absolutePath

            fileTree.visit { FileTreeElement file ->
                if (!file.isDirectory()) {
                    addArgs file.relativePath
                }
            }
        }

        if (StringUtils.isNotEmpty(flexConvention.airMobile.extensionDir)) {
            addArg("-extdir")
            addArg(flexConvention.airMobile.extensionDir)
        }

        addPlatformSdkParams()

        super.launch()
    }

    def addPlatformSdkParams() {
        addArgs "-platformsdk", flexConvention.airMobile.platformSdk
    }
}
