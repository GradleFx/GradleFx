package org.gradlefx.cli.common.optioninjectors

import org.gradlefx.cli.common.requirements.CompilerOptionsRequirement
import org.gradlefx.cli.common.requirements.GradleFxConventionRequirement
import org.gradlefx.cli.compiler.CompilerOption

/**
 * Trait providing various methods to inject Flex SDK resource related compiler options.
 */
trait FlexSDKResourcesOptionsInjector implements CompilerOptionsRequirement, GradleFxConventionRequirement {

    void addPlayerGlobalFromConfig() {
        def flexConfig = new XmlSlurper().parse(flexConvention.configPath)
        def relativeSwcPaths = flexConfig['compiler']['external-library-path']['path-element']

        def swcPathNode = relativeSwcPaths.find { it.text().contains 'playerglobal.swc' }
        compilerOptions.add CompilerOption.EXTERNAL_LIBRARY_PATH, "$flexConvention.flexHome/frameworks/${swcPathNode.text()}"
    }
}
