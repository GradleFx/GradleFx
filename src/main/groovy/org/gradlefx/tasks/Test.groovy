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

import groovy.text.SimpleTemplateEngine
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.FileTree
import org.gradle.api.file.FileTreeElement
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction
import org.gradlefx.cli.compiler.AntBasedCompilerProcess
import org.gradlefx.cli.compiler.CompilerJar
import org.gradlefx.cli.compiler.CompilerProcess
import org.gradlefx.cli.compiler.DefaultCompilerResultHandler
import org.gradlefx.cli.instructions.CompilerInstructionsBuilder
import org.gradlefx.cli.instructions.flexsdk.FlexUnitAppInstructions as FlexSDKFlexUnitAppInstructions
import org.gradlefx.cli.instructions.airsdk.standalone.actionscriptonly.FlexUnitAppInstructions as NoFlexSDKFlexUnitAppInstructions
import org.gradlefx.configuration.FlexUnitAntTasksConfigurator
import org.gradlefx.conventions.FlexUnitConvention
import org.gradlefx.conventions.GradleFxConvention
import org.gradlefx.util.PathToClassNameExtractor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
/*
 * A Gradle task to execute FlexUnit tests.
 */
class Test extends DefaultTask {

    private static final Logger LOG = LoggerFactory.getLogger Test

    GradleFxConvention flexConvention
    FlexUnitConvention flexUnit
    PathToClassNameExtractor pathToClassNameExtractor

    public Test() {
        group = TaskGroups.VERIFICATION
        description = "Run the FlexUnit tests."

        logging.setLevel LogLevel.INFO

        flexConvention = project.convention.plugins.flex
        flexUnit = flexConvention.flexUnit

        dependsOn Tasks.COPY_TEST_RESOURCES_TASK_NAME

        pathToClassNameExtractor = new PathToClassNameExtractor()
    }

    @TaskAction
    def runFlexUnit() {
        if(hasTests()) {
            createFlexUnitRunnerFromTemplate()

            compileTestRunner()

            configureAntWithFlexUnit()
            runTests()
        } else {
            LOG.info "Skipping tests since no tests exist"
        }
    }

    private void compileTestRunner() {
        def compilerInstructions = createCompilerInstructionsBuilder().buildInstructions()
        def compilerJar = flexConvention.hasFlexSDK() ? CompilerJar.mxmlc : CompilerJar.mxmlc_cli

        CompilerProcess compilerProcess = new AntBasedCompilerProcess(ant, compilerJar, new File(flexConvention.flexHome))
        compilerProcess.with {
            jvmArguments = flexConvention.jvmArguments
            compilerOptions = compilerInstructions
            compilerResultHandler = new DefaultCompilerResultHandler()
        }
        compilerProcess.compile()
    }

    /**
     * Determines which compiler instructions will be used for this project.
     */
    private CompilerInstructionsBuilder createCompilerInstructionsBuilder() {
        if(flexConvention.hasFlexSDK()) {
            new FlexSDKFlexUnitAppInstructions(project)
        } else {
            new NoFlexSDKFlexUnitAppInstructions(project)
        }
    }

    private boolean hasTests() {
        String nonEmptyTestDir = flexConvention.testDirs.find { String testDir ->
            if(project.file(testDir).exists()) {
                FileTree fileTree = project.fileTree(testDir)
                fileTree.includes = flexConvention.flexUnit.includes
                fileTree.excludes = flexConvention.flexUnit.excludes

                return !fileTree.empty
            } else {
                return false
            }
        }

        return nonEmptyTestDir != null
    }

    private void createFlexUnitRunnerFromTemplate() {
        String templateText = retrieveTemplateText()
        String templateFileName = getTemplateFileName()

        Set<String> fullyQualifiedNames = gatherFullyQualifiedTestClassNames()
        Set<String> classNames = gatherTestClassNames()
        def binding = [
            "fullyQualifiedNames": fullyQualifiedNames,
            "testClasses": classNames,
            "flexUnitPort": flexUnit.port
        ]
        def engine = new SimpleTemplateEngine()
        def template = engine.createTemplate(templateText).make(binding)

        // you can't write to a directory that doesn't exist
        def outputDir = project.file(flexConvention.flexUnit.toDir)
        if (!outputDir.exists()) outputDir.mkdirs()

        File destinationFile = project.file("${flexConvention.flexUnit.toDir}/${templateFileName}")
        destinationFile.createNewFile()
        destinationFile.write(template.toString())
    }

    private String retrieveTemplateText() {
        def templateText
        if(flexConvention.flexUnit.template == null) {
            //use the standard template
            String templatePath = "/templates/flexunit/FlexUnitRunner.mxml"
            templateText = getClass().getResourceAsStream(templatePath).text
        } else {
            templateText = project.file(flexConvention.flexUnit.template).text
        }

        templateText
    }

    private String getTemplateFileName() {
        def name
        if(flexConvention.flexUnit.template != null && flexConvention.flexUnit.template.endsWith(".as")) {
            name = "FlexUnitRunner.as"
        } else {
            name = "FlexUnitRunner.mxml"
        }

        name
    }

    def Set<String> gatherFullyQualifiedTestClassNames() {
        List<String> paths = []
        flexConvention.testDirs.each { String testDir ->
            FileTree fileTree = project.fileTree(testDir)
            fileTree.includes = flexConvention.flexUnit.includes
            fileTree.excludes = flexConvention.flexUnit.excludes

            fileTree.visit { FileTreeElement includedFile ->
                if(!includedFile.isDirectory()) {
                    def fullyQualifiedClassname =
                        pathToClassNameExtractor.convertPathStringToFullyQualifiedClassName(includedFile.relativePath.pathString)
                    paths.add(fullyQualifiedClassname)
                }
            }
        }

        return paths
    }

    def Set<String> gatherTestClassNames() {
        //fully qualified test class names are required because test 
        //classes can have the same name but in different package structures.
        gatherFullyQualifiedTestClassNames()
    }

    private void runTests() {
        File reportDir = project.file flexUnit.toDir

        // you can't write to a directory that doesn't exist
        if (!reportDir.exists()) reportDir.mkdirs()

        ant.flexunit (
            swf:             "${flexConvention.flexUnit.toDir}/${flexConvention.flexUnit.swfName}",
            player:          flexUnit.player,
            command:         getCommand(),
            toDir:           flexUnit.toDir,
            workingDir:      flexUnit.workingDir,
            haltonfailure:   flexUnit.haltOnFailure,
            verbose:         flexUnit.verbose,
            localTrusted:    flexUnit.localTrusted,
            port:            flexUnit.port,
            buffer:          flexUnit.buffer,
            timeout:         flexUnit.timeout,
            failureproperty: flexUnit.failureProperty,
            headless:        flexUnit.headless,
            display:         flexUnit.display)

        if (ant.properties[flexUnit.failureProperty] == "true") {
            def msg = 'Tests failed'
            if (flexUnit.ignoreFailures) { LOG.warn(msg) }
            else { throw new Exception(msg) }
        }
    }

    private String getCommand() {
        if (flexUnit.command == null)
            return findFlexUnitCommand()
        return flexUnit.command
    }

    private String findFlexUnitCommand() {
        if (flexUnit.player == 'air')
            return getAdlPath()
        throwMissingCommandException("The Flash player executable is not found")
    }

    private String getAdlPath() {
        def filename = 'bin/adl'
        if (Os.isFamily(Os.FAMILY_WINDOWS))
            filename += '.exe'
        def adlPath = new File(flexConvention.flexHome, filename)
        if (!adlPath.canExecute())
            throwMissingCommandException("Unable to locate the ADL executable at $adlPath.absolutePath")
        return adlPath
    }

    private static void throwMissingCommandException(String message) {
        throw new GradleException("${message}. Either set the FLASH_PLAYER_EXE " +
                "environment variable or set the 'flexUnit.command' property.")
    }

    private void configureAntWithFlexUnit() {
        new FlexUnitAntTasksConfigurator(project).configure()
    }

}
