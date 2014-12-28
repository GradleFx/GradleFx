package org.gradlefx.cli.common.optioninjectors

import org.gradle.api.artifacts.Configuration
import org.gradlefx.cli.compiler.CompilerOption

/**
 * Trait providing various methods to inject FlexUnit library options into the compiler options.
 */
trait FlexUnitLibraryOptionsInjector implements LibraryOptionsInjector {

    void addExternalLibrariesForLib() {
        Configuration internal = project.configurations.internal
        Configuration external = project.configurations.external
        Configuration merged = project.configurations.merged
        addLibraries external.files - internal.files - merged.files, external, CompilerOption.LIBRARY_PATH
    }

    void addTestLibraries() {
        Configuration test = project.configurations.test
        addLibraries test.files, test, CompilerOption.LIBRARY_PATH
    }
}
