package org.gradlefx.cli.common.optioninjectors

import org.gradlefx.cli.common.requirements.CompilerOptionsRequirement
import org.gradlefx.cli.common.requirements.GradleFxConventionRequirement
import org.gradlefx.cli.common.requirements.ProjectRequirement
import org.gradlefx.util.ProjectFileSearcher

/**
 * Trait providing various methods to inject options in the compiler options of an application project type.
 */
trait ApplicationOptionsInjector implements ProjectRequirement, GradleFxConventionRequirement, CompilerOptionsRequirement {

    public void addMainClass() {
        File mainClassFile = new File(flexConvention.mainClassPath)
        if (!mainClassFile.isAbsolute()) {
            ProjectFileSearcher projectFileSearcher = new ProjectFileSearcher(project)
            mainClassFile = projectFileSearcher.findFile flexConvention.srcDirs, flexConvention.mainClassPath
        }
        compilerOptions.add mainClassFile.absolutePath
    }
}