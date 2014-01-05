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

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradlefx.conventions.GradleFxConvention
import org.gradlefx.tasks.TaskGroups
import org.gradlefx.tasks.Tasks
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Task which launches ADL.
 */
class AdlTask extends DefaultTask {

    protected static final Logger LOG = LoggerFactory.getLogger 'gradlefx'

    GradleFxConvention flexConvention;

    List adlArguments

    String adlWorkDir;

    public AdlTask() {
        group = TaskGroups.RUN
        description = 'a custom adl launch'

        flexConvention = (GradleFxConvention) project.convention.plugins.flex

        adlArguments = []

        adlWorkDir = project.projectDir

        dependsOn Tasks.COMPILE_TASK_NAME

    }

    @TaskAction
    def launch() {
        def adlExecutablePath = (Os.isFamily(Os.FAMILY_WINDOWS))? '/bin/adl.exe' : '/bin/adl'
        addArgs flexConvention.flexHome + adlExecutablePath
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

        LOG.info stdOut.toString()
    }

    def addArgThatNotNull(String command, String value) {
        if (value) {
            addArgs(command, value);
        }
    }

    def addArgs(...args) {
        adlArguments.addAll(args)
    }

}
