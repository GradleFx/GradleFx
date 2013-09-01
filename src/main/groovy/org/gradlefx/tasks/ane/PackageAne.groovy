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

package org.gradlefx.tasks.ane

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradlefx.cli.CompilerOption
import org.gradlefx.conventions.GradleFxConvention

/**
 * Task which packages an ANE extension.
 */
class PackageAne extends DefaultTask {

    def ANT_RESULT_PROPERTY = 'adtResult'
    def ANT_OUTPUT_PROPERTY = 'adtOutput'

    GradleFxConvention flexConvention;

    def PackageAne() {
        description = 'package ANE'
        flexConvention = (GradleFxConvention) project.convention.plugins.flex
        project.afterEvaluate {
            dependsOn project.subprojects.build;
        }
    }

    @TaskAction
    def action() {
        Project android_lib_proj = project.childProjects["android-lib"]
        Project common_api_proj = project.childProjects["common-api"]
        Project default_lib_proj = project.childProjects["default-lib"]

        prepareApi(common_api_proj, "Android-ARM")
        prepareApi(default_lib_proj, "default")

        project.copy {
            from "${default_lib_proj.buildDir}/${default_lib_proj.output}.swc"
            into project.buildDir
        }

        project.copy {
            from "${android_lib_proj.buildDir}/libs"
            into "${project.buildDir}/Android-ARM"
        }

        def adtArguments = []
        def buildDir = project.buildDir

        buildDir.mkdirs();
        addArgs adtArguments, CompilerOption.PACKAGE, CompilerOption.TARGET, "ane", "${buildDir}/${project.name}.ane", "ane-descriptor.xml", CompilerOption.SWC, "${buildDir}/default-lib.swc"
        addArgs adtArguments, CompilerOption.PLATFORM, "Android-ARM", CompilerOption.CHANGE_DIRECTORY, "${buildDir}/Android-ARM", "."
        addArgs adtArguments, CompilerOption.PLATFORM, "default", CompilerOption.CHANGE_DIRECTORY, "${buildDir}/default", "library.swf"

        ant.java(jar: flexConvention.flexHome + '/lib/adt.jar',
                fork: true,
                resultproperty: ANT_RESULT_PROPERTY,
                outputproperty: ANT_OUTPUT_PROPERTY) {
            adtArguments.each { argument ->
                logger.info("adt args: ${argument}")
                arg(value: argument.toString())
            }
        }

        handlePackageIfFailed ANT_RESULT_PROPERTY, ANT_OUTPUT_PROPERTY

        showAntOutput ant.properties[ANT_OUTPUT_PROPERTY]

    }

    def addArgs(List adtArguments, ...args) {
        adtArguments.addAll(args)
    }

    def handlePackageIfFailed(String antResultProperty, String antOutputProperty) {
        if (ant.properties[antResultProperty] != '0') {
            throw new Exception("Packaging failed: ${ant.properties[antOutputProperty]}\n")
        }
    }

    def showAntOutput(String antOutput) {
        logger.info antOutput
    }

    def prepareApi(Project libProject, String targetPlatform) {
        project.file("${project.buildDir}/${targetPlatform}/").mkdirs()
        project.zipTree("${libProject.buildDir}/${libProject.name}.swc").each { file -> if (file.name == "library.swf") {
            project.copy {
                from file.path
                into "${project.buildDir}/${targetPlatform}/"
            }
        } }
    }

}
