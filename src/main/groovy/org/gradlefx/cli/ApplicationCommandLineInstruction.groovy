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

package org.gradlefx.cli

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration


class ApplicationCommandLineInstruction extends CommandLineInstruction {

    public ApplicationCommandLineInstruction(Project project) {
        super(project)
    }
    
    @Override
    public void setConventionArguments() {
        //add framework
        addFramework()

        //add every source directory
        addSourcePaths()
        addLocales()

        //add dependencies
        addInternalLibraries()
        addExternalLibraries()
        addMergedLibraries()
        addTheme()
        addRSLs()
        addFrameworkRsls()

        addAdditionalCompilerOptions()
        addOutput()
        addMainClass()
    }
    
    @Override
    public void addExternalLibraries() {
        Configuration internal = project.configurations.internal
        Configuration external = project.configurations.external
        Configuration merged   = project.configurations.merged
        addLibraries external.files - internal.files - merged.files, external, CompilerOption.EXTERNAL_LIBRARY_PATH
    }
    
    public void addOutput() {
        addOutput "${project.buildDir.path}/${flexConvention.output}.swf"
    }
    
    public void addMainClass() {
        File mainClassFile = new File(flexConvention.mainClassPath)
        if (!mainClassFile.isAbsolute()) {
            mainClassFile = findFile flexConvention.srcDirs, flexConvention.mainClassPath
        }
        add mainClassFile.absolutePath
    }
    
    protected File findFile(dirs, fileName) {
        for (String dirName : dirs) {
            File dir = project.file dirName
            File desiredFile = new File(dir, fileName)
            if (desiredFile.exists()) {
                return desiredFile
            }
        }
        
        throw new Exception(
            "The file $fileName couldn't be found in directories $dirs; " +
            "note that if you used the 'flashbuilder' plugin this file may have been moved " +
            "to comply to FlashBuilder's restrictions (execute 'flashbuilder' and see if you get any warnings); " +
            "consider editing the 'mainClass' property or switching to a decent IDE"
        )
    }
    
}
