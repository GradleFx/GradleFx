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
import org.gradle.api.tasks.TaskAction;
import org.gradlefx.cli.CommandLineInstruction
import org.gradlefx.cli.CompilerOption
import org.gradlefx.configuration.Configurations
import org.gradlefx.configuration.sdk.SdkType
import org.gradlefx.tasks.Tasks
import org.gradlefx.validators.actions.ValidateCompcTaskPropertiesAction

/*
 * Gradle task to execute Flex's Compc compiler.
 */
class Compc extends CompileTaskDelegate {

    public Compc(Task task, CommandLineInstruction cli) {
        super(task, cli)
        task.description = 'Compiles Flex component (*.swc) using the compc compiler'

        if (flexConvention.fatSwc) {
            task.dependsOn Tasks.ASDOC_TASK_NAME
        }
    }

    @Override
    @TaskAction
    public void compileFlex() {
        new ValidateCompcTaskPropertiesAction().execute(this)

        cli.setConventionArguments()

        def taskName
        if (flexConvention.type.isAir() || !flexConvention.usesFlex()) {
            taskName = "compc-cli";
        } else {
            taskName = "compc";
        }
        cli.execute task.ant, taskName

        if (flexConvention.fatSwc) {
            addAsdocToSwc()
        }
    }

    protected void addAsdocToSwc() {
        task.ant.zip(destfile: new File(task.project.buildDir.absolutePath, "${flexConvention.output}.${flexConvention.type}"),
                update: true) {
            zipfileset(dir: task.project.file(flexConvention.asdoc.outputDir + "/tempdita"), prefix: 'docs') {
                exclude(name: 'ASDoc_Config.xml')
                exclude(name: 'overviews.xml')
            }
        }
    }

}
