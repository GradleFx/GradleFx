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
package org.gradlefx.tasks.adt

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradlefx.conventions.GradleFxConvention
import org.gradlefx.tasks.TaskGroups
import org.gradlefx.tasks.Tasks
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * £Task which launches ADT.
 */
public class AdtTask extends DefaultTask {

    protected static final Logger LOG = LoggerFactory.getLogger 'gradlefx'

    private static final String ANT_RESULT_PROPERTY = 'adtResult'
    private static final String ANT_OUTPUT_PROPERTY = 'adtOutput'
    private static final String ANT_ERROR_PROPERTY = 'adtError'

    GradleFxConvention flexConvention;

    List adtArguments

    String adtWorkDir;

    public AdtTask() {
        group = TaskGroups.BUILD
        description = 'a custom adt launch'

        flexConvention = (GradleFxConvention) project.convention.plugins.flex

        adtArguments = []

        adtWorkDir = project.projectDir.absolutePath

        dependsOn Tasks.COMPILE_TASK_NAME
    }

    @TaskAction
    def launch() {
        ant.java(jar: flexConvention.flexHome + '/lib/adt.jar',
                fork: true,
                dir: adtWorkDir,
                resultproperty: ANT_RESULT_PROPERTY,
                outputproperty: ANT_OUTPUT_PROPERTY,
                errorproperty: ANT_ERROR_PROPERTY,
                failOnError: false) {
            adtArguments.each { argument ->
                logger.info("adt args: {}", argument)
                arg(value: argument.toString())
            }
        }

        handleIfFailed ANT_RESULT_PROPERTY, ANT_OUTPUT_PROPERTY, ANT_ERROR_PROPERTY

        showAntOutput ant.properties[ANT_OUTPUT_PROPERTY], ant.properties[ANT_ERROR_PROPERTY]
    }

    def addArg(String arg) {
        adtArguments.add(arg)
    }

    def addArgs(...args) {
        adtArguments.addAll(args)
    }

    def handleIfFailed(String antResultProperty, String antOutputProperty, String antErrorProperty) {
        if (ant.properties[antResultProperty] != '0') {
            throw new Exception("${description} failed: ${ant.properties[antOutputProperty]}" +
                                " ${ant.properties[antErrorProperty]} Error code ${ant.properties[antResultProperty]}")
        }
    }

    def showAntOutput(antOutput, antError) {
        LOG.info "$antOutput $antError"
    }

}
