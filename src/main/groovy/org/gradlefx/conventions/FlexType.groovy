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

package org.gradlefx.conventions


import org.gradlefx.cli.AIRCommandLineInstruction
import org.gradlefx.tasks.compile.Compc;
import org.gradlefx.tasks.compile.Compile;
import org.gradlefx.tasks.compile.Mxmlc;

import org.gradlefx.cli.ApplicationCommandLineInstruction;
import org.gradlefx.cli.CommandLineInstruction;
import org.gradlefx.cli.LibraryCommandLineInstruction;

/**
 * Defines a certain type of Flex application.
 */
enum FlexType {
    swf('flex', Mxmlc, ApplicationCommandLineInstruction),
    swc('flex', Compc, LibraryCommandLineInstruction),
    air('air', Mxmlc, AIRCommandLineInstruction),
    mobile('airmobile', Mxmlc, AIRCommandLineInstruction)

    /**
     * The name of the type.
     */
    private String configName
    /**
     * The Compile task class which performs the compilation of that specific type.
     */
    private Class<Compile> compileClass
    /**
     * The class which calls the command line interface.
     */
    private Class<CommandLineInstruction> cliClass
    
    
    public FlexType(String configName, Class<Compile> compileClass, Class<CommandLineInstruction> cliClass) {
        this.configName = configName
        this.compileClass = compileClass
        this.cliClass = cliClass
    }
    
    public String getConfigName() {
        return configName
    }
    
    public Class<Compile> getCompileClass() {
        return compileClass
    }
    
    public Class<CommandLineInstruction> getCLIClass() {
        return cliClass
    }
    
    public boolean isApp() {
        return isWebApp() || isNativeApp()
    }
    
    public boolean isLib() {
        return this == swc
    }
    
    public boolean isWebApp() {
        return this == swf
    }
    
    public boolean isNativeApp() {
        return this == air || this == mobile
    }

    public boolean isMobile() {
        return this == mobile
    }
    
}