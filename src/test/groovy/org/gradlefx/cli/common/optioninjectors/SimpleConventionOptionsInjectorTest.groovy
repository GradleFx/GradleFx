package org.gradlefx.cli.common.optioninjectors

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradlefx.cli.compiler.CompilerOption
import org.gradlefx.cli.compiler.CompilerOptions
import org.gradlefx.conventions.FlexType
import org.gradlefx.conventions.GradleFxConvention
import org.gradlefx.plugins.GradleFxPlugin
import spock.lang.Specification

class SimpleConventionOptionsInjectorTest extends Specification {

    Project project
    GradleFxConvention gradleFxConvention
    SimpleConventionOptionsInjector simpleConventionOptionsInjector

    def setup() {
        File projectDir = new File(getClass().getResource("/stub-project-dir").toURI())
        project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        new GradleFxPlugin().apply(project)
        gradleFxConvention = (GradleFxConvention) project.convention.plugins.flex
        simpleConventionOptionsInjector = new StubSimpleConventionOptionsInjector(project)
    }

    def "loadDefaultConfig should set the LOAD_CONFIG compiler argument to the configPath"() {
        gradleFxConvention.flexHome = './src/test/resources/valid-flex-sdk'
        gradleFxConvention.type = FlexType.swf

        when:
            simpleConventionOptionsInjector.loadDefaultConfig()
        then:
            CompilerOptions options = simpleConventionOptionsInjector.compilerOptions
            options.contains "$CompilerOption.LOAD_CONFIG+=$gradleFxConvention.configPath"
    }

    def "disableLoadConfig should set the LOAD_CONFIG compiler argument to an empty value"() {
        when:
            simpleConventionOptionsInjector.disableLoadConfig()
        then:
            CompilerOptions options = simpleConventionOptionsInjector.compilerOptions
            options.contains "$CompilerOption.LOAD_CONFIG="
    }
}

class StubSimpleConventionOptionsInjector implements SimpleConventionOptionsInjector {

    CompilerOptions compilerOptions = new CompilerOptions()
    Project project
    GradleFxConvention flexConvention

    public StubSimpleConventionOptionsInjector(Project project) {
        this.project = project
        flexConvention = project.convention.plugins.flex as GradleFxConvention
    }
}
