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

import org.gradlefx.tasks.compile.Compc
import org.gradlefx.tasks.compile.CompileTask
import org.gradlefx.tasks.compile.Mxmlc

/**
 * Defines a certain type of Flex application.
 */
enum FlexType {
    swf('flex', Mxmlc),
    swc('flex', Compc),
    swcAir('air', Compc),
    air('air', Mxmlc),
    mobile('airmobile', Mxmlc)

    /**
     * The name of the type.
     */
    private String configName
    /**
     * The Compile task class which performs the compilation of that specific type.
     */
    private Class<CompileTask> compileClass

    /**
     *
     * @param configName name of the Flex configuration. E.g. when you specify 'air' it will use the 'air-config.xml'.
     * @param compileClass the task that will compile the project.
     */
    public FlexType(String configName, Class<CompileTask> compileClass) {
        this.configName = configName
        this.compileClass = compileClass
    }
    
    public String getConfigName() {
        return configName
    }
    
    public Class<CompileTask> getCompileClass() {
        return compileClass
    }
    
    public boolean isApp() {
        return isWebApp() || isNativeApp()
    }
    
    public boolean isLib() {
        return this == swc || this == swcAir
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

    public boolean isAir() {
        return isNativeApp() || this == swcAir
    }
    
}