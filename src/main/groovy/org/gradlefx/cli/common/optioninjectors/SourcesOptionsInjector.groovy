package org.gradlefx.cli.common.optioninjectors

import groovy.io.FileType
import org.gradlefx.cli.compiler.CompilerOption
import org.gradlefx.cli.common.requirements.CompilerOptionsRequirement
import org.gradlefx.cli.common.requirements.GradleFxConventionRequirement
import org.gradlefx.cli.common.requirements.ProjectRequirement
import org.gradlefx.cli.common.tools.SourceFileFilter

/**
 * Trait providing various methods to inject sources related options into the compiler options.
 */
trait SourcesOptionsInjector implements CompilerOptionsRequirement, ProjectRequirement, GradleFxConventionRequirement, SourceFileFilter {

    /** Adds the <code>-source-path</code> argument based on project.srcDirs and project.localeDir */
    void addSourceDirectories() {
        compilerOptions.addAll CompilerOption.SOURCE_PATH, filterValidSourcePaths(flexConvention.srcDirs)
    }

    public void addLibrarySourceFilesAndDirectories() {
        if (flexConvention.includeClasses) {
            compilerOptions.add CompilerOption.INCLUDE_CLASSES
            compilerOptions.addAll flexConvention.includeClasses
        }
        else if (flexConvention.includeSources) {
            Collection paths = flexConvention.includeSources.collect { project.file(it).path }
            compilerOptions.addAll CompilerOption.INCLUDE_SOURCES, paths
        }
        else {
            compilerOptions.addAll CompilerOption.INCLUDE_SOURCES, filterValidSourcePaths(flexConvention.srcDirs)
        }
    }

    void addLocaleSources() {
        //add locale path to source paths if any locales are defined
        if (flexConvention.locales && flexConvention.locales.size()) {
            compilerOptions.add CompilerOption.SOURCE_PATH, project.file(flexConvention.localeDir).path + '/{locale}'
        }
    }

    public void addResources() {
        flexConvention.resourceDirs.each {
            File resourceDir = project.file it

            if (resourceDir.exists()) {
                resourceDir.traverse(type: FileType.FILES) { File file ->
                    String relativePath = resourceDir.toURI().relativize(file.toURI()).getPath()

                    compilerOptions.add CompilerOption.INCLUDE_FILE
                    compilerOptions.addAll new ArrayList<String>([relativePath, file.path])
                }
            }
        }

    }
}
