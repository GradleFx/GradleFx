package org.gradlefx.cli.common.optioninjectors

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradlefx.cli.compiler.CompilerOption
import org.gradlefx.cli.compiler.CompilerOptions
import org.gradlefx.conventions.FlexType
import org.gradlefx.conventions.GradleFxConvention
import org.gradlefx.plugins.GradleFxPlugin
import spock.lang.Specification

class FlexFrameworkRslOptionsInjectorTest extends Specification {

    Project project
    GradleFxConvention gradleFxConvention
    FlexFrameworkRslOptionsInjector flexFrameworkRslOptionsInjector

    def setup() {
        File projectDir = new File(getClass().getResource("/stub-project-dir").toURI())
        project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        new GradleFxPlugin().apply(project)
        gradleFxConvention = (GradleFxConvention) project.convention.plugins.flex
        flexFrameworkRslOptionsInjector = new StubFlexFrameworkRslOptionsInjector(project)
    }

    def "calling addFrameworkRsls with an Adobe Flex SDK and useDebugRSLSwfs turned on should add framework rsls to compiler arguments"() {
        gradleFxConvention.flexHome = './src/test/resources/adobe-flex-rsl-sdk'
        gradleFxConvention.type = FlexType.swf
        gradleFxConvention.useDebugRSLSwfs = true

        when:
        flexFrameworkRslOptionsInjector.addFrameworkRsls()
        then:
        def compilerOptions = flexFrameworkRslOptionsInjector.compilerOptions
        compilerOptions.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(gradleFxConvention, "libs/framework.swc")},framework_4.6.0.23201.swf")
        compilerOptions.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(gradleFxConvention, "libs/textLayout.swc")},textLayout_2.0.0.232.swf")
        compilerOptions.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(gradleFxConvention, "libs/spark.swc")},spark_4.6.0.23201.swf")
        compilerOptions.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(gradleFxConvention, "libs/sparkskins.swc")},sparkskins_4.6.0.23201.swf")
        compilerOptions.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(gradleFxConvention, "libs/rpc.swc")},rpc_4.6.0.23201.swf")
        compilerOptions.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(gradleFxConvention, "libs/charts.swc")},charts_4.6.0.23201.swf")
        compilerOptions.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(gradleFxConvention, "libs/spark_dmv.swc")},spark_dmv_4.6.0.23201.swf")
        compilerOptions.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(gradleFxConvention, "libs/osmf.swc")},osmf_1.0.0.16316.swf")
        compilerOptions.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(gradleFxConvention, "libs/mx/mx.swc")},mx_4.6.0.23201.swf")
        compilerOptions.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(gradleFxConvention, "libs/advancedgrids.swc")},advancedgrids_4.6.0.23201.swf")
    }

    def "calling addFrameworkRsls with an Apache Flex SDK and useDebugRSLSwfs turned on should add framework rsls to compiler arguments"() {
        gradleFxConvention.flexHome = './src/test/resources/apache-flex-rsl-sdk'
        gradleFxConvention.type = FlexType.swf
        gradleFxConvention.useDebugRSLSwfs = true

        when:
        flexFrameworkRslOptionsInjector.addFrameworkRsls()
        then:
        def compilerOptions = flexFrameworkRslOptionsInjector.compilerOptions
        compilerOptions.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(gradleFxConvention, "libs/framework.swc")},framework_4.9.0.1425567.swf")
        compilerOptions.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(gradleFxConvention, "libs/textLayout.swc")},textLayout_4.9.0.1425567.swf")
        compilerOptions.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(gradleFxConvention, "libs/spark.swc")},spark_4.9.0.1425567.swf")
        compilerOptions.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(gradleFxConvention, "libs/sparkskins.swc")},sparkskins_4.9.0.1425567.swf")
        compilerOptions.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(gradleFxConvention, "libs/rpc.swc")},rpc_4.9.0.1425567.swf")
        compilerOptions.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(gradleFxConvention, "libs/charts.swc")},charts_4.9.0.1425567.swf")
        compilerOptions.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(gradleFxConvention, "libs/spark_dmv.swc")},spark_dmv_4.9.0.1425567.swf")
        compilerOptions.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(gradleFxConvention, "libs/osmf.swc")},osmf_4.9.0.1425567.swf")
        compilerOptions.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(gradleFxConvention, "libs/mx/mx.swc")},mx_4.9.0.1425567.swf")
        compilerOptions.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(gradleFxConvention, "libs/advancedgrids.swc")},advancedgrids_4.9.0.1425567.swf")
    }

    def toFullPath(GradleFxConvention flexConvention, String frameworkLibPathFromFrameworkDir) {
        new File("${flexConvention.flexHome}/frameworks/${frameworkLibPathFromFrameworkDir}").path
    }
}

class StubFlexFrameworkRslOptionsInjector implements FlexFrameworkRslOptionsInjector {

    CompilerOptions compilerOptions = new CompilerOptions()
    Project project
    GradleFxConvention flexConvention

    public StubFlexFrameworkRslOptionsInjector(Project project) {
        this.project = project
        flexConvention = project.convention.plugins.flex as GradleFxConvention
    }
}
