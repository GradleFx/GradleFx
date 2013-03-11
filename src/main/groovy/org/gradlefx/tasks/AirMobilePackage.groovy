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
    }

    @TaskAction
    def launch() {
        addArg '-package'
        addArg '-target'
        addArg flexConvention.airMobile.target

        addArgs "-storetype",
                "pkcs12",
                "-keystore",
                flexConvention.airMobile.air.keystore,
                "-storepass",
                flexConvention.airMobile.air.storepass

        addArgs project.file(project.buildDir.name + '/' + flexConvention.output).absolutePath

        addArgs project.file(flexConvention.airMobile.air.applicationDescriptor)

        addArgs project.file("${project.buildDir}/${flexConvention.output}.${FlexType.swf}")

        flexConvention.airMobile.air.includeFileTrees.each { ConfigurableFileTree fileTree ->
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

        super.launch()
    }


}
