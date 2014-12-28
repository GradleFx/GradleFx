package org.gradlefx.cli.instructions.airsdk.standalone.actionscriptonly

import org.gradle.api.Project
import org.gradlefx.cli.common.optioninjectors.FlexUnitConventionOptionsInjector
import org.gradlefx.cli.common.optioninjectors.FlexUnitLibraryOptionsInjector
import org.gradlefx.cli.common.optioninjectors.SourcesOptionsInjector
import org.gradlefx.cli.instructions.CompilerInstructionsBuilder

/**
 * Compiler instructions for a FlexUnit runner application that only uses the AIR SDK (actionscript only)
 */
class FlexUnitAppInstructions extends CompilerInstructionsBuilder implements FlexUnitLibraryOptionsInjector, FlexUnitConventionOptionsInjector, SourcesOptionsInjector {

    FlexUnitAppInstructions(Project project) {
        super(project)
    }

    @Override
    public void configure() {
        // The AIR compiler (mxmlc-cli) does not load air-config.xml/airmobile-config.xml/flexconfig.xml automatically,
        // thus we add it here
        loadDefaultConfig()

        //add every source directory
        addSourceDirectories()
        addTestSourceDirectories()
        addLocaleSources()
        addLocales()

        //add dependencies
        addInternalLibraries()
        addExternalLibrariesForLib()
        addMergedLibraries()
        addTestLibraries()
        addTheme()
        addRSLs()
        addFrameworkRsls()

        addAdditionalFlexUnitCompilerOptions()
        addOutput()
        addMainClass()
    }

    public void addOutput() {
        addOutput "${flexConvention.flexUnit.toDir}/${flexConvention.flexUnit.swfName}"
    }

}
