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

import java.util.List
import org.gradle.api.tasks.TaskAction
import org.gradlefx.options.CompilerOption
import org.gradlefx.tasks.Tasks
import org.gradlefx.validators.actions.ValidateMxmlcTaskPropertiesAction

class Mxmlc extends AbstractMxmlc {

	private static final String ANT_RESULT_PROPERTY = 'mxmlcCompileResult'
	private static final String ANT_OUTPUT_PROPERTY = 'mxmlcCompileOutput'
	
    public Mxmlc() {
        description = 'Compiles Flex application/module (*.swf) using the mxmlc compiler'
        dependsOn(Tasks.COPY_RESOURCES_TASK_NAME)
    }

    @TaskAction
    def compileFlex() {
        new ValidateMxmlcTaskPropertiesAction().execute(this)

		super.compileFlex(ANT_RESULT_PROPERTY, ANT_OUTPUT_PROPERTY, 'Mxmlc', createCompilerArguments())
    }

    protected List createCompilerArguments() {
        List compilerArguments = []
        
        //add framework
        addPlayerLibrary(compilerArguments)
        addFramework(compilerArguments)

        //add every source directory
        addSourcePaths(compilerArguments)
        addLocales(compilerArguments)

        //add dependencies
        addLibraries(project.configurations.internal.files, project.configurations.internal, CompilerOption.INCLUDE_LIBRARIES, compilerArguments)
		addLibraries(project.configurations.external.files - project.configurations.internal.files - project.configurations.merged.files, project.configurations.external, CompilerOption.EXTERNAL_LIBRARY_PATH, compilerArguments)
        addLibraries(project.configurations.merged.files, project.configurations.merged, CompilerOption.LIBRARY_PATH, compilerArguments)
        addLibraries(project.configurations.theme.files, project.configurations.theme, CompilerOption.THEME, compilerArguments)
        addRsls(compilerArguments)

        //add all the other user specified compiler options
        flexConvention.additionalCompilerOptions.each { compilerOption ->
            compilerArguments.add(compilerOption)
        }

        compilerArguments.add("${CompilerOption.OUTPUT}=${project.buildDir.path}/${flexConvention.output}.swf" )

        //add the target file
        File mainClassFile = findFile(flexConvention.srcDirs, flexConvention.mainClassPath)
        compilerArguments.add(mainClassFile.absolutePath)

        return compilerArguments
    }
    
    @Override
    protected void addFramework(List compilerArguments) {
        super.addFramework(compilerArguments)
        if (project.frameworkLinkage == getDefaultFrameworkLinkage()) copyFrameworkRSLs()
    }
    
    /**
     * Extracts the library swf's from the swc's and moves them to the build directory 
     * in the location defined as failover RSL url.
     */
    private void copyFrameworkRSLs() {
        AntBuilder ant = new AntBuilder()
        ant.project.getBuildListeners()[0].setMessageOutputLevel(0)
        
        def flexConfig = new XmlSlurper().parse("${flexConvention.flexHome}/frameworks/flex-config.xml")
        
        flexConfig['runtime-shared-library-path'].each {
            String swcName = it['path-element'].text()
            String libName = it['rsl-url'][1].text()[0..-2] + 'f'
            File swc = new File("${flexConvention.flexHome}/frameworks/${swcName}")
            
            if (swc.exists()) {
                ant.unzip(src: swc.path, dest: swc.parent) {
                    patternset(includes: 'library.swf')
                }
                ant.move(
                    file: "${swc.parent}/library.swf", 
                    tofile:"${project.buildDir}/${libName}"
                )
            }
        }
    }

}
