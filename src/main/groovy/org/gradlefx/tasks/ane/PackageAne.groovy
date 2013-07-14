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

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Chaos Encoder</a>
 */
class PackageAne extends DefaultTask {

    def PackageAne() {
        description = 'package ane'
        project.gradle.projectsEvaluated {
            def dependsTasks = project.subprojects.build;
            dependsOn dependsTasks;
        }
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

    @TaskAction
    def action() {
        def FLEXSDK_HOME = System.getenv('FLEX_HOME')

        def Project android_lib_proj = project.childProjects["android-lib"]
        def Project common_api_proj = project.childProjects["common-api"]
        def Project default_lib_proj = project.childProjects["default-lib"]

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



        def ANT_RESULT_PROPERTY = 'adtResult'
        def ANT_OUTPUT_PROPERTY = 'adtOutput'

        adtArguments = []

        addArg = {String arg ->
            adtArguments.add(arg)
        }

        addArgs = {...args ->
            adtArguments.addAll(args)
        }

        handlePackageIfFailed = { antResultProperty, antOutputProperty ->
            if (ant.properties[antResultProperty] != '0') {
                throw new Exception("Packaging failed: ${ant.properties[antOutputProperty]}\n")
            }
        }

        showAntOutput = { antOutput->
            println antOutput
        }

        def buildDir = project.buildDir

        buildDir.mkdirs();
        addArgs "-package", "-target", "ane", "${buildDir}/${project.name}.ane", "ane-descriptor.xml", "-swc", "${buildDir}/default-lib.swc"
        addArgs "-platform", "Android-ARM", "-C", "${buildDir}/Android-ARM", "."
        addArgs "-platform", "default", "-C", "${buildDir}/default", "library.swf"
        ant.java(jar: FLEXSDK_HOME + '/lib/adt.jar',
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

}
