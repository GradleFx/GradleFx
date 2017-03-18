package org.gradlefx.cli.common.optioninjectors

import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ResolveException
import org.gradlefx.cli.compiler.CompilerOption
import org.gradlefx.cli.common.requirements.CompilerOptionsRequirement
import org.gradlefx.cli.common.requirements.ProjectRequirement
import org.gradlefx.conventions.FlexType

/**
 * Trait providing various methods to inject library options into the compiler options.
 */
trait LibraryOptionsInjector implements CompilerOptionsRequirement, ProjectRequirement {

    void addInternalLibraries() {
        Configuration internal = project.configurations.internal
        addLibraries internal.files, internal, CompilerOption.INCLUDE_LIBRARIES
    }

    void addExternalLibrariesForLib() {
        Configuration external = project.configurations.external
        addLibraries external.files, external, CompilerOption.EXTERNAL_LIBRARY_PATH
    }

    public void addExternalLibrariesForApp() {
        Configuration internal = project.configurations.internal
        Configuration external = project.configurations.external
        Configuration merged   = project.configurations.merged
        addLibraries external.files - internal.files - merged.files, external, CompilerOption.EXTERNAL_LIBRARY_PATH
    }

    void addMergedLibraries() {
        Configuration merged = project.configurations.merged
        addLibraries merged.files, merged, CompilerOption.LIBRARY_PATH
    }

    /** Adds the <code>-runtime-shared-library-path</code> argument based on RSL dependencies */
    void addRSLs() {
        Configuration rsl = project.configurations.rsl
        validateFilesExist rsl.files, rsl

        Collection<String> paths = rsl.files.collect { "$it.path,${it.name[0..-2]}f" }
        compilerOptions.addAll CompilerOption.RUNTIME_SHARED_LIBRARY_PATH, paths
    }

    void addTheme() {
        Configuration theme = project.configurations.theme
        addLibraries theme.files, theme, CompilerOption.THEME
    }

    /**
     * Adds all the dependencies for the given configuration as compile arguments
     *
     * @param libraryFiles
     * @param configuration
     * @param compilerOption
     */
    void addLibraries(Set<File> libraryFiles, Configuration configuration, CompilerOption compilerOption) {
        //only add swc or ane dependencies, no use in adding pom dependencies
        Collection<File> files = libraryFiles.findAll {
            it.name.endsWith(FlexType.swc.toString()) || it.name.endsWith(".ane") || it.isDirectory()
        }
        validateFilesExist files, configuration

        Collection paths = files.collect { it.path }
        compilerOptions.addAll compilerOption, paths
    }

    private static void validateFilesExist(Collection<File> files, Configuration configuration) {
        Collection<File> ghosts = files.findAll { !it.exists() }

        if (ghosts && ghosts.size()) {
            def errorMessage = "Couldn't find some dependency files - are you sure the path is correct? " +
                    "Unresolved dependency paths: ${ghosts.collect { it.path }}"
            
            throw new ResolveException(errorMessage, new Throwable(errorMessage))
            
        }
    }

}