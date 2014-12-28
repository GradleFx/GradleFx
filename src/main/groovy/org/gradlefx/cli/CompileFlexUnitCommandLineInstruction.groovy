package org.gradlefx.cli

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration

/**
 * Command line instruction which is used to compile a FlexUnit test application.
 */
class CompileFlexUnitCommandLineInstruction extends ApplicationCommandLineInstruction {

    CompileFlexUnitCommandLineInstruction(Project project) {
        super(project)
    }

    @Override
    public void setConventionArguments() {
        super.setConventionArguments()

        addTestLibraries()
    }

    @Override
    public void addSourcePaths() {
        super.addSourcePaths()

        def sourcePaths = filterValidSourcePaths(flexConvention.testDirs)
        addAll CompilerOption.SOURCE_PATH, sourcePaths
    }

    @Override
    public void addOutput() {
        addOutput "${flexConvention.flexUnit.toDir}/${flexConvention.flexUnit.swfName}"
    }

    @Override
    public void addMainClass() {
        File mainClassFile = project.file("${flexConvention.flexUnit.toDir}/${getTemplateFileName()}")
        LOG.warn("test main class file: " + mainClassFile.absolutePath)
        if (!mainClassFile.isAbsolute()) {
            mainClassFile = findFile flexConvention.srcDirs, flexConvention.mainClassPath
        }
        add mainClassFile.absolutePath
    }

    private String getTemplateFileName() {
        def name
        if(flexConvention.flexUnit.template != null && flexConvention.flexUnit.template.endsWith(".as")) {
            name = "FlexUnitRunner.as"
        } else {
            name = "FlexUnitRunner.mxml"
        }
        return name
    }

    @Override
    public void addAdditionalCompilerOptions() {
        addAll flexConvention.flexUnit.additionalCompilerOptions
    }

    @Override
    public void addExternalLibraries() {
        Configuration internal = project.configurations.internal
        Configuration external = project.configurations.external
        Configuration merged = project.configurations.merged
        addLibraries external.files - internal.files - merged.files, external, CompilerOption.LIBRARY_PATH
    }

    public void addTestLibraries() {
        Configuration test = project.configurations.test
        addLibraries test.files, test, CompilerOption.LIBRARY_PATH
    }
}
