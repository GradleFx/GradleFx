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
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.FileTreeElement
import org.gradle.api.tasks.TaskAction
import org.gradlefx.validators.actions.ValidateAirPackageTaskPropertiesAction
import org.gradlefx.cli.CompilerOption;
import org.gradlefx.conventions.FlexType;
import org.gradlefx.conventions.GradleFxConvention

class AirPackage extends DefaultTask {

    private static final String ANT_RESULT_PROPERTY = 'airPackageResult'
    private static final String ANT_OUTPUT_PROPERTY = 'airPackageOutput'

    GradleFxConvention flexConvention;

    public AirPackage() {
        group = TaskGroups.BUILD
        description = 'Packages the generated swf file into an .air package'

        flexConvention = (GradleFxConvention) project.convention.plugins.flex

        dependsOn Tasks.COMPILE_TASK_NAME
    }

    @TaskAction
    def packageAir() {
        new ValidateAirPackageTaskPropertiesAction().execute(this)

        List compilerArguments = createCompilerArguments()

        ant.java(jar: flexConvention.flexHome + '/lib/adt.jar',
                fork: true,
                dir: flexConvention.air.packageWorkDir,
                resultproperty: ANT_RESULT_PROPERTY,
                outputproperty: ANT_OUTPUT_PROPERTY) {

            compilerArguments.each { compilerArgument ->
                arg(value: compilerArgument)
            }
        }

        handlePackageIfFailed ANT_RESULT_PROPERTY, ANT_OUTPUT_PROPERTY

        showAntOutput ant.properties[ANT_OUTPUT_PROPERTY]
    }

    private List createCompilerArguments() {
        List airOptions = [CompilerOption.PACKAGE]

        addAirSigningOptions airOptions

        airOptions.addAll([
            project.file(project.buildDir.name + '/' + flexConvention.output).absolutePath,
            project.file(flexConvention.air.applicationDescriptor),
            project.file("${project.buildDir}/${flexConvention.output}.${FlexType.swf}")
        ])

        addFiles(airOptions)

        return airOptions
    }

    private void addFiles(List compilerOptions) {
        flexConvention.air.includeFileTrees.each { ConfigurableFileTree fileTree ->
            compilerOptions.add CompilerOption.CHANGE_DIRECTORY.optionName
            compilerOptions.add fileTree.dir.absolutePath

            fileTree.visit { FileTreeElement file ->
                if (!file.isDirectory()) {
                    compilerOptions.add file.relativePath
                }
            }
        }

        compilerOptions.addAll(flexConvention.air.fileOptions)
    }

    private void addAirSigningOptions(List compilerOptions) {
        compilerOptions.addAll ([
                CompilerOption.STORE_TYPE,
                "pkcs12",
                CompilerOption.KEYSTORE,
                flexConvention.air.keystore,
                CompilerOption.STOREPASS,
                flexConvention.air.storepass
        ])
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
