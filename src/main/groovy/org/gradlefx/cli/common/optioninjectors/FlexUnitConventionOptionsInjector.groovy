package org.gradlefx.cli.common.optioninjectors

import org.gradlefx.cli.common.requirements.CompilerOptionsRequirement
import org.gradlefx.cli.common.requirements.GradleFxConventionRequirement
import org.gradlefx.cli.common.tools.SourceFileFilter
import org.gradlefx.cli.compiler.CompilerOption
import org.gradlefx.util.ProjectFileSearcher
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Trait providing various methods to inject FlexUnit convention options into the compiler options.
 */
trait FlexUnitConventionOptionsInjector implements CompilerOptionsRequirement, GradleFxConventionRequirement, SourceFileFilter {

    protected static final Logger LOG = LoggerFactory.getLogger 'gradlefx'

    void addAdditionalFlexUnitCompilerOptions() {
        compilerOptions.addAll flexConvention.flexUnit.additionalCompilerOptions
    }

    void addTestSourceDirectories() {
        def sourcePaths = filterValidSourcePaths(flexConvention.testDirs)
        compilerOptions.addAll CompilerOption.SOURCE_PATH, sourcePaths
    }

    void addMainClass() {
        File mainClassFile = project.file("${flexConvention.flexUnit.toDir}/${getTemplateFileName()}")
        LOG.info("test main class file: " + mainClassFile.absolutePath)
        if (!mainClassFile.isAbsolute()) {
            ProjectFileSearcher projectFileSearcher = new ProjectFileSearcher(project)
            mainClassFile = projectFileSearcher.findFile flexConvention.srcDirs, flexConvention.mainClassPath
        }
        compilerOptions.add mainClassFile.absolutePath
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
}
