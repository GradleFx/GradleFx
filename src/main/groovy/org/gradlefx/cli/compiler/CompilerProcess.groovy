package org.gradlefx.cli.compiler

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Executes the compiler itself.
 */
interface CompilerProcess {

    CompilerResult compile()
}

/**
 * Ant-based compiler process implementation that executes a compiler's jar file with Ant.
 */
class AntBasedCompilerProcess implements CompilerProcess {

    private static final Logger LOG = LoggerFactory.getLogger 'gradlefx'

    private AntBuilder ant
    private CompilerJar compilerJar
    private File flexHome
    private List<String> jvmArguments = []
    private CompilerOptions compilerOptions = []
    private CompilerResultHandler compilerResultHandler = null

    AntBasedCompilerProcess(AntBuilder ant, CompilerJar compilerJar, File flexHome) {
        this.ant = ant
        this.compilerJar = compilerJar
        this.flexHome = flexHome
    }

    void setJvmArguments(List<String> jvmArguments) {
        this.jvmArguments = jvmArguments
        this
    }

    void setCompilerOptions(CompilerOptions compilerOptions) {
        this.compilerOptions = compilerOptions
        this
    }

    void setCompilerResultHandler(CompilerResultHandler compilerResultHandler) {
        this.compilerResultHandler = compilerResultHandler
    }

    @Override
    CompilerResult compile() {
        LOG.info "Compiling with " + compilerJar.name()
        compilerOptions.each {
            LOG.info "\t$it"
        }

        String antResultProperty = compilerJar.name() + 'Result'
        String antOutputProperty = compilerJar.name() + 'Output'
        String antErrorProperty = compilerJar.name() + 'Error'

        ant.java(
                jar: "$flexHome.absolutePath/lib/${compilerJar}",
                dir: "$flexHome.absolutePath/frameworks",
                fork: true,
                resultproperty: antResultProperty,
                outputproperty: antOutputProperty,
                errorproperty: antErrorProperty,
                failOnError: false) {
            jvmArguments.each {
                jvmarg(value: it)
            }
            compilerOptions.each {
                arg(value: it)
            }
        }

        boolean isSuccess = ant.properties[antResultProperty] == '0'
        String output = ant.properties[antOutputProperty]
        String errorStr =  ant.properties[antErrorProperty]
        def compilerResult = new CompilerResult(isSuccess, compilerJar, output, errorStr)

        if(compilerResultHandler) {
            compilerResultHandler.handleResult(compilerResult)
        }

        compilerResult
    }
}

/**
 * The result of a compilation process.
 */
class CompilerResult {

    boolean success
    CompilerJar compilerJar
    String compilerLog
    String errorLog

    CompilerResult(boolean success, CompilerJar compilerJar, String compilerLog, String errorLog) {
        this.success = success
        this.compilerJar = compilerJar
        this.compilerLog = compilerLog
        this.errorLog = errorLog
    }
}

/**
 * Handles the result of a compilation process.
 */
interface CompilerResultHandler {
    void handleResult(CompilerResult result)
}