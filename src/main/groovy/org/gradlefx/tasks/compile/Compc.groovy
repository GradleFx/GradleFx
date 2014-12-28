/*
 * Copyright (c) 2011 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradlefx.tasks.compile

import org.gradle.api.Task;
import org.gradle.api.tasks.TaskAction
import org.gradlefx.cli.compiler.AntBasedCompilerProcess
import org.gradlefx.cli.compiler.CompilerJar
import org.gradlefx.cli.compiler.CompilerProcess
import org.gradlefx.cli.compiler.DefaultCompilerResultHandler;
import org.gradlefx.cli.instructions.CompilerInstructionsBuilder
import org.gradlefx.cli.instructions.flexsdk.LibraryInstructions as FlexSDKLibraryInstructions
import org.gradlefx.cli.instructions.flexsdk.actionscriptonly.LibraryInstructions as FlexSDKPureASLibraryInstructions
import org.gradlefx.cli.instructions.airsdk.standalone.actionscriptonly.LibraryInstructions as NoFlexSDKLibraryInstructions
import org.gradlefx.tasks.Tasks
import org.gradlefx.validators.actions.ValidateCompcTaskPropertiesAction

/*
 * Gradle task to execute Flex's Compc compiler.
 */
class Compc extends CompileTaskDelegate {

    public Compc(Task task) {
        super(task)
        task.description = 'Compiles Flex component (*.swc) using the compc compiler'

        if (flexConvention.fatSwc) {
            task.dependsOn Tasks.ASDOC_TASK_NAME
        }
    }

    @Override
    @TaskAction
    public void compile() {
        new ValidateCompcTaskPropertiesAction().execute(this)

        def compilerInstructions = createCompilerInstructionsBuilder().buildInstructions()
        def compilerJar = flexConvention.hasFlexSDK()? CompilerJar.compc : CompilerJar.compc_cli

        CompilerProcess compilerProcess = new AntBasedCompilerProcess(task.ant, compilerJar, new File(flexConvention.flexHome))
        compilerProcess.with {
            jvmArguments = flexConvention.jvmArguments
            compilerOptions = compilerInstructions
            compilerResultHandler = new DefaultCompilerResultHandler()
        }
        compilerProcess.compile()

        if (flexConvention.fatSwc) {
            addAsdocToSwc()
        }
    }

    private CompilerInstructionsBuilder createCompilerInstructionsBuilder() {
        if(flexConvention.hasFlexSDK()) {
            if(flexConvention.usesFlex()) {
                new FlexSDKLibraryInstructions(task.project)
            } else {
                new FlexSDKPureASLibraryInstructions(task.project)
            }
        } else {
            new NoFlexSDKLibraryInstructions(task.project)
        }
    }

    private void addAsdocToSwc() {
        task.ant.zip(destfile: new File(task.project.buildDir.absolutePath, "${flexConvention.output}.${flexConvention.type}"),
                update: true) {
            zipfileset(dir: task.project.file(flexConvention.asdoc.outputDir + "/tempdita"), prefix: 'docs') {
                exclude(name: 'ASDoc_Config.xml')
                exclude(name: 'overviews.xml')
            }
        }
    }

}
