package org.gradlefx.cli.instructions.flexsdk

import org.gradle.api.Project
import org.gradlefx.cli.common.optioninjectors.FlexUnitConventionOptionsInjector
import org.gradlefx.cli.common.optioninjectors.FlexUnitLibraryOptionsInjector
import org.gradlefx.cli.common.optioninjectors.SourcesOptionsInjector
import org.gradlefx.cli.instructions.CompilerInstructionsBuilder

/**
 * Compiler instructions for a FlexUnit runner application that uses the Flex SDK.
 */
class FlexUnitAppInstructions extends CompilerInstructionsBuilder implements FlexUnitLibraryOptionsInjector, FlexUnitConventionOptionsInjector, SourcesOptionsInjector {

    FlexUnitAppInstructions(Project project) {
        super(project)
    }

    @Override
    public void configure() {
        setDefaultConfigName()

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
