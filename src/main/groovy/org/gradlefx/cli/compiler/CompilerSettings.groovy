package org.gradlefx.cli.compiler

/**
 * Settings that you would provide to the CompilerProcess.
 */
class CompilerSettings {
    CompilerOptions compilerOptions
    CompilerJar compilerJar

    CompilerSettings(CompilerOptions compilerOptions, CompilerJar compilerJar) {
        this.compilerOptions = compilerOptions
        this.compilerJar = compilerJar
    }
}