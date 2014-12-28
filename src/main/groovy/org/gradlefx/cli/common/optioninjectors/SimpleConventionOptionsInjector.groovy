package org.gradlefx.cli.common.optioninjectors

import org.gradlefx.cli.compiler.CompilerOption
import org.gradlefx.cli.common.requirements.CompilerOptionsRequirement
import org.gradlefx.cli.common.requirements.GradleFxConventionRequirement
import org.gradlefx.cli.common.requirements.ProjectRequirement
import org.gradlefx.util.ProjectFileSearcher
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Trait providing various methods to inject some of the basic convention properties into the compiler options.
 */
trait SimpleConventionOptionsInjector implements CompilerOptionsRequirement, ProjectRequirement, GradleFxConventionRequirement {

    protected static final Logger LOG = LoggerFactory.getLogger 'gradlefx'

    void loadDefaultConfig() {
        compilerOptions.add CompilerOption.LOAD_CONFIG, flexConvention.configPath
    }

    void disableLoadConfig() {
        compilerOptions.reset CompilerOption.LOAD_CONFIG
    }

    void setDefaultConfigName() {
        compilerOptions.set CompilerOption.CONFIGNAME, flexConvention.type.configName
    }

    void addAdditionalCompilerOptions() {
        compilerOptions.addAll flexConvention.additionalCompilerOptions
    }

    void addOutput(String dir) {
        compilerOptions.set CompilerOption.OUTPUT, project.file(dir).path
    }

    /** Adds the <code>-locale</code> argument based on {@link org.gradlefx.conventions.GradleFxConvention}.locales */
    void addLocales() {
        if (flexConvention.locales && flexConvention.locales.size()) {
            compilerOptions.set CompilerOption.LOCALE, flexConvention.locales
        }
    }

    public void addMainClass() {
        File mainClassFile = new File(flexConvention.mainClassPath)
        if (!mainClassFile.isAbsolute()) {
            ProjectFileSearcher projectFileSearcher = new ProjectFileSearcher(project)
            mainClassFile = projectFileSearcher.findFile flexConvention.srcDirs, flexConvention.mainClassPath
        }
        compilerOptions.add mainClassFile.absolutePath
    }
}
