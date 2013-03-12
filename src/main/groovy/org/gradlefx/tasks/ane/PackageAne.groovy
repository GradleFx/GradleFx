package org.gradlefx.tasks.ane

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Chaos Encoder</a>
 */
class PackageAne extends DefaultTask {

    def PackageAne() {
        description = 'package ane'
    }

    @TaskAction
    def action() {
        def FLEXSDK_HOME = System.getenv('FLEX_HOME')

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
        addArgs "-platform", "default", "-C", "${buildDir}/default", "."
        ant.java(jar: FLEXSDK_HOME + '/lib/adt.jar',
                fork: true,
                resultproperty: ANT_RESULT_PROPERTY,
                outputproperty: ANT_OUTPUT_PROPERTY) {
            adtArguments.each { argument ->
                println("adt args: ${argument}")
                arg(value: argument.toString())
            }
        }

        handlePackageIfFailed ANT_RESULT_PROPERTY, ANT_OUTPUT_PROPERTY

        showAntOutput ant.properties[ANT_OUTPUT_PROPERTY]

    }

}
