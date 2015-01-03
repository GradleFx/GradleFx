package org.gradlefx.cli.instructions.airsdk.standalone.actionscriptonly

import org.gradle.api.Project
import org.gradlefx.cli.common.optioninjectors.ApplicationOptionsInjector
import org.gradlefx.cli.common.optioninjectors.FlexFrameworkRslOptionsInjector
import org.gradlefx.cli.common.optioninjectors.LibraryOptionsInjector
import org.gradlefx.cli.common.optioninjectors.SimpleConventionOptionsInjector
import org.gradlefx.cli.common.optioninjectors.SourcesOptionsInjector
import org.gradlefx.cli.instructions.CompilerInstructionsBuilder

/**
 * Compiler instructions for an application that only uses the AIR SDK, which also makes it a pure actionscript project.
 */
class ApplicationInstructions
        extends CompilerInstructionsBuilder
        implements SimpleConventionOptionsInjector, LibraryOptionsInjector, SourcesOptionsInjector,
                FlexFrameworkRslOptionsInjector, ApplicationOptionsInjector {

    ApplicationInstructions(Project project) {
        super(project)
    }

    @Override
    void configure() {
        // The AIR compiler (mxmlc-cli) does not load air-config.xml/airmobile-config.xml/flexconfig.xml automatically,
        // thus we add it here
        loadDefaultConfig()

        //add every source directory
        addSourceDirectories()
        addLocaleSources()
        addLocales()

        //add dependencies
        addInternalLibraries()
        addExternalLibrariesForApp()
        addMergedLibraries()
        addTheme()
        addRSLs()
        addFrameworkRsls()

        addAdditionalCompilerOptions()
        addOutput()
        addMainClass()
    }

    public void addOutput() {
        addOutput "${project.buildDir.path}/${flexConvention.output}.swf"
    }
}
