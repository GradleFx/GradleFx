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

import org.gradle.api.tasks.TaskAction
import org.slf4j.LoggerFactory

/*
 * Gradle task to execute Flex's MXMLC compiler for compiling a test runner for the FlexUnit testing framework.
 */
class TestCompile extends AbstractMxmlc {

	private static final String ANT_RESULT_PROPERTY = 'testCompileResult'
	private static final String ANT_OUTPUT_PROPERTY = 'testCompileOutput'

    private static final List<String> COMPC_COMPILER_OPTIONS = ['-compute-digest', '-directory', '-include-classes',
            '-include-file', '-include-lookup-only', '-include-namespaces', '-include-sources', '-include-stylesheet']
	
    public TestCompile() {
        description = 'Compiles test runner SWF for executing FlexUnit tests.'
    }

    @TaskAction
    def testCompile() {
        super.compileFlex(ANT_RESULT_PROPERTY, ANT_OUTPUT_PROPERTY, 'TestCompile', createCompilerArguments()) 
    }

    private List createCompilerArguments() {
        List compilerArguments = []

        //add every source directory
        project.srcDirs.each { sourcePath ->
            compilerArguments.add("-source-path+=" + project.file(sourcePath).path)
        }
		
		// additional directories for test code
		project.testDirs.each { sourcePath ->
			compilerArguments.add("-source-path+=" + project.file(sourcePath).path)
		}

		// external and test dependencies will be merged in
        addLibraries(project.configurations.internal.files, project.configurations.internal, "-include-libraries", compilerArguments)
		addLibraries(project.configurations.external.files - project.configurations.internal.files - project.configurations.merged.files - project.configurations.test.files, project.configurations.external, '-library-path', compilerArguments)
        addLibraries(project.configurations.merged.files, project.configurations.merged, "-library-path", compilerArguments)
		addLibraries(project.configurations.test.files, project.configurations.test, "-library-path", compilerArguments)
        addRsls(compilerArguments)

        //add all the other user specified compiler options
        project.additionalCompilerOptions.each { compilerOption ->
            if(!isCompcOption(compilerOption)) {
                compilerArguments.add(compilerOption)
            }
        }

        compilerArguments.add("-output=${project.buildDir.path}/${project.testOutput}.swf" )

        //add the target file
        File testClassFile = findFile(project.testDirs, project.testClass)
        compilerArguments.add(testClassFile.absolutePath)

        return compilerArguments
    }

    boolean isCompcOption(String compilerOption) {
        return COMPC_COMPILER_OPTIONS.contains(compilerOption.split(" |=")[0])
    }
}