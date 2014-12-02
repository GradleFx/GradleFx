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

package org.gradlefx.cli

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.testfixtures.ProjectBuilder
import org.gradlefx.configuration.sdk.SdkType
import org.gradlefx.conventions.FlexType
import org.gradlefx.conventions.FrameworkLinkage
import org.gradlefx.conventions.GradleFxConvention
import org.gradlefx.plugins.GradleFxPlugin
import spock.lang.Specification

class CommandLineInstructionTest extends Specification {

    Project project = ProjectBuilder.builder().build();
    CommandLineInstruction commandLineInstruction

    def setup() {
        new GradleFxPlugin().apply(project)

        commandLineInstruction = new StubCommandLineInstruction(project)
    }

    def "call addFramework with FrameworkLinkage.none, and not using AIR removes the LOAD_CONFIG compiler argument"() {
        GradleFxConvention flexConvention = project.convention.plugins.flex
        flexConvention.flexHome = './src/test/resources/valid-flex-sdk'
        flexConvention.type = FlexType.swf
        flexConvention.frameworkLinkage = FrameworkLinkage.none
        flexConvention.sdkTypes.add(SdkType.Flex);

        when:
            commandLineInstruction.addFramework()
        then:
            List args = commandLineInstruction.arguments
            args.contains "$CompilerOption.LOAD_CONFIG="
    }

    def "call addFramework with FrameworkLinkage.none, and using AIR does not add the LOAD_CONFIG compiler argument"() {
        GradleFxConvention flexConvention = project.convention.plugins.flex
        flexConvention.flexHome = './src/test/resources/valid-flex-sdk'
        flexConvention.type = FlexType.mobile
        flexConvention.frameworkLinkage = FrameworkLinkage.none
        flexConvention.sdkTypes.add(SdkType.Flex);
        when:
            commandLineInstruction.addFramework()
        then:
            List args = commandLineInstruction.arguments
            !args.contains("$CompilerOption.LOAD_CONFIG=")
            !args.contains("$CompilerOption.CONFIGNAME+=$FlexType.mobile.configName")
    }

    def "call addFramework when using AIR and Flex adds flex config to the compiler arguments"() {
        GradleFxConvention flexConvention = project.convention.plugins.flex
        flexConvention.flexHome = './src/test/resources/valid-flex-sdk'
        flexConvention.type = FlexType.air
        flexConvention.frameworkLinkage = FrameworkLinkage.merged
        flexConvention.sdkTypes.add(SdkType.AIR);
        when:
            commandLineInstruction.addFramework()
        then:
            List args = commandLineInstruction.arguments
            !args.contains("$CompilerOption.LOAD_CONFIG=")
            args.contains "$CompilerOption.CONFIGNAME+=$FlexType.air.configName"
    }

    def "call linkPlayerGlobalIfNeeded links playerglobal if FrameworkLinkage is none and its a web project"() {
        GradleFxConvention flexConvention = project.convention.plugins.flex
        flexConvention.flexHome = './src/test/resources/valid-flex-sdk'
        flexConvention.type = FlexType.swf
        flexConvention.frameworkLinkage = FrameworkLinkage.none
        flexConvention.sdkTypes.add(SdkType.AIR);
        when:
            commandLineInstruction.linkPlayerGlobalIfNeeded()
        then:
            List args = commandLineInstruction.arguments
            args.contains "$CompilerOption.EXTERNAL_LIBRARY_PATH+=$flexConvention.flexHome/frameworks/libs/player/11.1"
    }

    def "call linkPlayerGlobalIfNeeded links airglobal if FrameworkLinkage is none and its an AIR project"() {
        GradleFxConvention flexConvention = project.convention.plugins.flex
        flexConvention.flexHome = './src/test/resources/valid-flex-sdk'
        flexConvention.type = FlexType.mobile
        flexConvention.frameworkLinkage = FrameworkLinkage.none
        flexConvention.sdkTypes.add(SdkType.AIR);
        when:
        commandLineInstruction.linkPlayerGlobalIfNeeded()
        then:
        List args = commandLineInstruction.arguments
        args.contains "$CompilerOption.EXTERNAL_LIBRARY_PATH+=$flexConvention.flexHome/frameworks/libs/air/airglobal.swc"
    }

    def "call addLibraries, should add valid swc locations to compiler arguments"() {
        Configuration configuration = Mock(Configuration)

        File utilSwc = new File(getClass().getResource("/dummy-swcs/util.swc").toURI())
        File domainSwc = new File(getClass().getResource("/dummy-swcs/domain.swc").toURI())
        Set<File> swcs = new HashSet<File>([utilSwc, domainSwc])

        when:
            commandLineInstruction.addLibraries(swcs, configuration, CompilerOption.INCLUDE_LIBRARIES)
        then:
            commandLineInstruction.arguments.contains("${CompilerOption.INCLUDE_LIBRARIES.optionName}+=${utilSwc.path}")
            commandLineInstruction.arguments.contains("${CompilerOption.INCLUDE_LIBRARIES.optionName}+=${domainSwc.path}")
    }

    def "calling addFrameworkRsls with an Adobe Flex SDK and useDebugRSLSwfs turned on should add framework rsls to compiler arguments"() {
        GradleFxConvention flexConvention = project.convention.plugins.flex
        flexConvention.flexHome = './src/test/resources/adobe-flex-rsl-sdk'
        flexConvention.type = FlexType.swf
        flexConvention.useDebugRSLSwfs = true

        when:
        commandLineInstruction.addFrameworkRsls()
        then:
        commandLineInstruction.arguments.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(flexConvention, "libs/framework.swc")},framework_4.6.0.23201.swf")
        commandLineInstruction.arguments.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(flexConvention, "libs/textLayout.swc")},textLayout_2.0.0.232.swf")
        commandLineInstruction.arguments.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(flexConvention, "libs/spark.swc")},spark_4.6.0.23201.swf")
        commandLineInstruction.arguments.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(flexConvention, "libs/sparkskins.swc")},sparkskins_4.6.0.23201.swf")
        commandLineInstruction.arguments.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(flexConvention, "libs/rpc.swc")},rpc_4.6.0.23201.swf")
        commandLineInstruction.arguments.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(flexConvention, "libs/charts.swc")},charts_4.6.0.23201.swf")
        commandLineInstruction.arguments.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(flexConvention, "libs/spark_dmv.swc")},spark_dmv_4.6.0.23201.swf")
        commandLineInstruction.arguments.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(flexConvention, "libs/osmf.swc")},osmf_1.0.0.16316.swf")
        commandLineInstruction.arguments.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(flexConvention, "libs/mx/mx.swc")},mx_4.6.0.23201.swf")
        commandLineInstruction.arguments.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(flexConvention, "libs/advancedgrids.swc")},advancedgrids_4.6.0.23201.swf")
    }

    def "calling addFrameworkRsls with an Apache Flex SDK and useDebugRSLSwfs turned on should add framework rsls to compiler arguments"() {
        GradleFxConvention flexConvention = project.convention.plugins.flex
        flexConvention.flexHome = './src/test/resources/apache-flex-rsl-sdk'
        flexConvention.type = FlexType.swf
        flexConvention.useDebugRSLSwfs = true

        when:
        commandLineInstruction.addFrameworkRsls()
        then:
        commandLineInstruction.arguments.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(flexConvention, "libs/framework.swc")},framework_4.9.0.1425567.swf")
        commandLineInstruction.arguments.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(flexConvention, "libs/textLayout.swc")},textLayout_4.9.0.1425567.swf")
        commandLineInstruction.arguments.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(flexConvention, "libs/spark.swc")},spark_4.9.0.1425567.swf")
        commandLineInstruction.arguments.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(flexConvention, "libs/sparkskins.swc")},sparkskins_4.9.0.1425567.swf")
        commandLineInstruction.arguments.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(flexConvention, "libs/rpc.swc")},rpc_4.9.0.1425567.swf")
        commandLineInstruction.arguments.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(flexConvention, "libs/charts.swc")},charts_4.9.0.1425567.swf")
        commandLineInstruction.arguments.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(flexConvention, "libs/spark_dmv.swc")},spark_dmv_4.9.0.1425567.swf")
        commandLineInstruction.arguments.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(flexConvention, "libs/osmf.swc")},osmf_4.9.0.1425567.swf")
        commandLineInstruction.arguments.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(flexConvention, "libs/mx/mx.swc")},mx_4.9.0.1425567.swf")
        commandLineInstruction.arguments.contains("${CompilerOption.RUNTIME_SHARED_LIBRARY_PATH.optionName}+=${toFullPath(flexConvention, "libs/advancedgrids.swc")},advancedgrids_4.9.0.1425567.swf")
    }

    def toFullPath(GradleFxConvention flexConvention, String frameworkLibPathFromFrameworkDir) {
        new File("${flexConvention.flexHome}/frameworks/${frameworkLibPathFromFrameworkDir}").path
    }

}
