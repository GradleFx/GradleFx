package org.gradlefx.cli.compiler

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DefaultCompilerResultHandler implements CompilerResultHandler {

    protected static final Logger LOG = LoggerFactory.getLogger 'gradlefx'

    @Override
    void handleResult(CompilerResult result) {
        if (result.success) LOG.info "[$result.compilerJar] $result.compilerLog"
        else handleError result.compilerJar, result.compilerLog + result.errorLog
    }

    private static void handleError(CompilerJar compilerJar, String message) {
        if (message.contains("FlexUnitTestRunnerUI")) {
            message += '\n[GradleFx tip] Chances are you\'re using the FlashBuilder IDE.' +
                    '\nWhen you run unit tests from within FlashBuilder ' +
                    '(by selecting "Execute FlexUnit Tests" from the project\'s context menu), ' +
                    'it will automatically create a test application for you called FlexUnitApplication.mxml ' +
                    'and there\'s nothing you can change about that (short of not using the feature). ' +
                    'What\'s more, this application has lots of dependencies you haven\'t defined ' +
                    'in your build script, hence the compiler error.' +
                    '\nThe easiest way to fix this, is to add the following code to your build script:' +
                    '\n\n\tcompileFlex.doFirst {' +
                    '\n\t\tproject.file(srcDirs[0] + \'/FlexUnitApplication.mxml\').delete()' +
                    '\n\t}' +
                    '\n\nThis will delete the file prior to compiling and FlashBuilder will not complain, ' +
                    'since it will automatically regenerate it when needed.';
        }

        throw new Exception(compilerJar.name() + " execution failed: $message\n")
    }
}
