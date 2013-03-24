package org.gradlefx.tasks

import org.apache.commons.lang.StringUtils
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.FileTreeElement
import org.gradle.api.tasks.TaskAction
import org.gradlefx.conventions.FlexType

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Chaos Encoder</a>
 */
class AirMobilePackage extends AdtTask {
    public AirMobilePackage() {
        super()
        description = "Packages the generated swf file into an mobile package";
        adtWorkDir = flexConvention.air.packageWorkDir
    }

    @TaskAction
    def launch() {
        addArg '-package'
        addArg '-target'
        addArg flexConvention.airMobile.target

        def outputPath = "${project.buildDir}/${flexConvention.output}.${flexConvention.airMobile.outputExtension}"

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

        if (flexConvention.airMobile.target.indexOf("-simulator") != -1) {
            addArgs "-platformsdk", flexConvention.airMobile.simulatorPlatformSdk
        } else if (flexConvention.airMobile.target.indexOf("ipa-") != -1) {
            addArgs "-platformsdk", flexConvention.airMobile.platformSdk
        }

        super.launch()
    }


}
