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
package org.gradlefx.tasks.adl

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradlefx.conventions.GradleFxConvention
import org.gradlefx.tasks.TaskGroups
import org.gradlefx.tasks.Tasks

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Chaos Encoder</a>
 */
class AdlTask extends DefaultTask {
    GradleFxConvention flexConvention;

    List adlArguments

    String adlWorkDir;

    public AdlTask() {
        group = TaskGroups.RUN
        description = 'an custom adl launch'

        flexConvention = (GradleFxConvention) project.convention.plugins.flex

        adlArguments = []

        adlWorkDir = project.projectDir

        dependsOn Tasks.COMPILE_TASK_NAME

    }

    @TaskAction
    def launch() {
        addArgs flexConvention.flexHome + '/bin/adl.exe'
        addArgs flexConvention.air.applicationDescriptor

        addArgThatNotNull '-profile', flexConvention.adl.profile
        addArgThatNotNull '-screensize', flexConvention.adl.screenSize

        addArgs project.buildDir
        def stdOut = new ByteArrayOutputStream();

        project.exec {
            commandLine adlArguments
            workingDir project.projectDir
            //store the output instead of printing to the console:
            standardOutput = stdOut
        }
        showAntOutput stdOut.toString()
    }

    def addArgThatNotNull(String command, String value) {
        if (value) {
            addArgs(command, value);
        }
    }

    def addArgs(...args) {
        adlArguments.addAll(args)
    }

    def showAntOutput(antOutput) {
        println antOutput
    }

}
