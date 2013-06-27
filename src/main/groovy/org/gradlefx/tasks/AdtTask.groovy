package org.gradlefx.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradlefx.conventions.GradleFxConvention

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Chaos Encoder</a>
 */
public class AdtTask extends DefaultTask {
    GradleFxConvention flexConvention;

    private static final String ANT_RESULT_PROPERTY = 'adtResult'
    private static final String ANT_OUTPUT_PROPERTY = 'adtOutput'

    List adtArguments

    String adtWorkDir;

    public AdtTask() {
        group = TaskGroups.BUILD
        description = 'an Custom adt launch'

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
                outputproperty: ANT_OUTPUT_PROPERTY) {
            adtArguments.each { argument ->
                logger.info("adt args: {}", argument)
                arg(value: argument.toString())
            }
        }

        handlePackageIfFailed ANT_RESULT_PROPERTY, ANT_OUTPUT_PROPERTY

        showAntOutput ant.properties[ANT_OUTPUT_PROPERTY]
    }

    def addArg(String arg) {
        adtArguments.add(arg)
    }

    def addArgs(...args) {
        adtArguments.addAll(args)
    }

    def handlePackageIfFailed(antResultProperty, antOutputProperty) {
        if (ant.properties[antResultProperty] != '0') {
            throw new Exception("Packaging failed: ${ant.properties[antOutputProperty]}\n")
        }
    }

    def showAntOutput(antOutput) {
        println antOutput
    }

}
