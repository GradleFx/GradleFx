package org.gradlefx.cli.common.requirements

import org.gradlefx.cli.compiler.CompilerOptions

/**
 * Requires the implementing class to provide a CompilerOptions instance. Useful for traits and abstract classes where
 * you want to use a CompilerOptions instance, but don't want to provide the implementation yet.
 */
interface CompilerOptionsRequirement {
    abstract CompilerOptions getCompilerOptions()
}