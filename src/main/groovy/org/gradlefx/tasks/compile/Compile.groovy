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

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction
import org.gradlefx.cli.CommandLineInstruction
import org.gradlefx.conventions.FlexType
import org.gradlefx.conventions.GradleFxConvention
import org.gradlefx.validators.RequiredProjectPropertiesValidator
import org.gradlefx.validators.runner.FailOnErrorValidatorRunner

class Compile extends DefaultTask implements CompileTask {

    CompileTask delegate
    GradleFxConvention flexConvention

    public Compile() {
        logging.captureStandardOutput LogLevel.INFO

        project.afterEvaluate {
            flexConvention = project.convention.plugins.flex
            new FailOnErrorValidatorRunner(project).add(new RequiredProjectPropertiesValidator()).run()

            initInputDirectory()
            initOutputDirectory()

            //delegate is also a default Closure argument, hence the explicit 'this' scope
            this.delegate = createDelegate()
            addCompilationDependencies()
        }
    }

    @Override
    @TaskAction
    public void compileFlex() {
        delegate.compileFlex()
    }

    protected void initInputDirectory() {
        flexConvention.srcDirs.each { sourceDirectory ->
            inputs.dir sourceDirectory
        }
    }

    protected void initOutputDirectory() {
        outputs.dir project.buildDir
    }

    protected CompileTask createDelegate() {
        FlexType type = flexConvention.type
        CommandLineInstruction cli = type.cliClass.newInstance project
        return type.compileClass.newInstance(this, cli)
    }

    protected void addCompilationDependencies() {
        dependsOn {
            Set dependentTasks = new HashSet()
            project.configurations.each { Configuration configuration ->
                configuration.getDependencies().withType(ProjectDependency).each { ProjectDependency dependency ->
                    dependentTasks.add dependency.dependencyProject.path + ':' + name
                }
            }
            dependentTasks
        }
    }

}
