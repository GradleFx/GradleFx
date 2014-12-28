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

package org.gradlefx.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.TaskAction
import org.gradlefx.cli.compiler.AntBasedCompilerProcess
import org.gradlefx.cli.compiler.CompilerJar
import org.gradlefx.cli.compiler.CompilerProcess
import org.gradlefx.cli.compiler.DefaultCompilerResultHandler
import org.gradlefx.cli.instructions.ASDocInstructions
import org.gradlefx.cli.instructions.CompilerInstructionsBuilder
import org.gradlefx.configuration.Configurations
import org.gradlefx.conventions.GradleFxConvention

class ASDoc extends DefaultTask {

    GradleFxConvention flexConvention

    public ASDoc() {
        group = TaskGroups.DOCUMENTATION
        description = 'Generates ASDoc documentation'

        flexConvention = project.convention.plugins.flex

        [
            Configurations.MERGE_CONFIGURATION_NAME.configName(),
            Configurations.EXTERNAL_CONFIGURATION_NAME.configName(),
            Configurations.INTERNAL_CONFIGURATION_NAME.configName()
        ].each {
            addDependsOnTaskInOtherProjects Tasks.COMPILE_TASK_NAME, it
        }

        project.afterEvaluate {
            initInputDirectory()
            initOutputDirectory()
        }
    }

    /**
     * Adds a dependency on tasks with the specified name in other projects.  The other projects are determined from
     * project lib dependencies using the specified configuration name. These may be projects this project depends on or
     * projects that depend on this project based on the useDependOn argument.
     *
     * @param otherProjectTaskName name of task in other projects
     * @param configurationName name of configuration to use to find the other projects
     */
    protected void addDependsOnTaskInOtherProjects(String otherProjectTaskName, String configurationName) {
        Configuration configuration = project.getConfigurations().getByName(configurationName);
        dependsOn(configuration.getTaskDependencyFromProjectDependency(true, otherProjectTaskName));
    }

    protected void initInputDirectory() {
        flexConvention.srcDirs.each { sourceDirectory ->
            inputs.dir sourceDirectory
        }
    }

    protected void initOutputDirectory() {
        outputs.dir flexConvention.asdoc.outputDir
    }

    @TaskAction
    public void generateAsDoc() {
        if (hasDocSources()) {
            def compilerInstructions = new ASDocInstructions(project).buildInstructions()
            def compilerJar = flexConvention.hasFlexSDK()? CompilerJar.asdoc : CompilerJar.asdoc_legacy

            CompilerProcess compilerProcess = new AntBasedCompilerProcess(ant, compilerJar, new File(flexConvention.flexHome))
            compilerProcess.with {
                jvmArguments = flexConvention.jvmArguments
                compilerOptions = compilerInstructions
                compilerResultHandler = new DefaultCompilerResultHandler()
            }
            compilerProcess.compile()
        }
    }

    private boolean hasDocSources() {
        return flexConvention.srcDirs.any { sourcePath ->
            return project.file(sourcePath).exists()
        }
    }

}
