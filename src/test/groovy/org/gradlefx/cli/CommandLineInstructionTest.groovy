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

    def "call addFramework with FrameworkLinkage.none, should have playerglobal.swc on external classpath with FLEX and AIR dependency"() {
        GradleFxConvention flexConvention = project.convention.plugins.flex
        flexConvention.flexHome = './src/test/resources/valid-flex-sdk'
        flexConvention.type = FlexType.swf
        flexConvention.frameworkLinkage = FrameworkLinkage.none

        Set sdkTypes = project.getProperties().get("sdkTypes");
        sdkTypes.add(SdkType.Flex);
        sdkTypes.add(SdkType.AIR);

        when:
            commandLineInstruction.addFramework()
        then:
            List args = commandLineInstruction.arguments
            args.contains "$CompilerOption.LOAD_CONFIG="
            args.contains "$CompilerOption.EXTERNAL_LIBRARY_PATH+=$flexConvention.flexHome/frameworks/libs/player/{targetPlayerMajorVersion}.{targetPlayerMinorVersion}/playerglobal.swc"
    }

    def "call addFramework with FrameworkLinkage.none, should have playerglobal.swc on external classpath with AIR dependency only"() {
        GradleFxConvention flexConvention = project.convention.plugins.flex
        flexConvention.flexHome = './src/test/resources/valid-flex-sdk'
        flexConvention.type = FlexType.swf
        flexConvention.frameworkLinkage = FrameworkLinkage.none

        Set sdkTypes = project.getProperties().get("sdkTypes");
        sdkTypes.add(SdkType.AIR);

        when:
        commandLineInstruction.addFramework()
        then:
        List args = commandLineInstruction.arguments
        !args.contains("$CompilerOption.LOAD_CONFIG=")
        args.contains "$CompilerOption.EXTERNAL_LIBRARY_PATH+=$flexConvention.flexHome/frameworks/libs/player/{targetPlayerMajorVersion}.{targetPlayerMinorVersion}/playerglobal.swc"
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

}
