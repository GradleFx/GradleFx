package org.gradlefx.cli.common.optioninjectors

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.testfixtures.ProjectBuilder
import org.gradlefx.cli.compiler.CompilerOption
import org.gradlefx.cli.compiler.CompilerOptions
import org.gradlefx.conventions.GradleFxConvention
import org.gradlefx.plugins.GradleFxPlugin
import spock.lang.Specification

class LibraryOptionsInjectorTest extends Specification {

    Project project
    GradleFxConvention gradleFxConvention
    LibraryOptionsInjector libraryOptionsInjector

    def setup() {
        File projectDir = new File(getClass().getResource("/stub-project-dir").toURI())
        project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        new GradleFxPlugin().apply(project)
        gradleFxConvention = (GradleFxConvention) project.convention.plugins.flex
        libraryOptionsInjector = new StubLibraryOptionsInjector(project)
    }

    def "call addLibraries, should add valid swc locations to compiler arguments"() {
        Configuration configuration = Mock(Configuration)

        File utilSwc = new File(getClass().getResource("/dummy-swcs/util.swc").toURI())
        File domainSwc = new File(getClass().getResource("/dummy-swcs/domain.swc").toURI())
        Set<File> swcs = new HashSet<File>([utilSwc, domainSwc])

        when:
        libraryOptionsInjector.addLibraries(swcs, configuration, CompilerOption.INCLUDE_LIBRARIES)
        then:
        libraryOptionsInjector.compilerOptions.contains("${CompilerOption.INCLUDE_LIBRARIES.optionName}+=${utilSwc.path}")
        libraryOptionsInjector.compilerOptions.contains("${CompilerOption.INCLUDE_LIBRARIES.optionName}+=${domainSwc.path}")
    }
}

class StubLibraryOptionsInjector implements LibraryOptionsInjector {

    CompilerOptions compilerOptions = new CompilerOptions()
    Project project

    public StubLibraryOptionsInjector(Project project) {
        this.project = project
    }
}