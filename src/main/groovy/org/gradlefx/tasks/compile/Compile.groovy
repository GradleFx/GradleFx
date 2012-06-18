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

import org.gradle.api.DefaultTask
import org.gradle.api.Task;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.tasks.TaskAction;
import org.gradlefx.FlexType;
import org.gradlefx.conventions.GradleFxConvention;

class Compile extends DefaultTask implements CompileTask {
    
    GradleFxConvention flexConvention

    public Compile() {
        logging.setLevel(LogLevel.INFO)
    }

    @Override
    @TaskAction
    public void compileFlex() {
        flexConvention = project.convention.plugins.flex
        
        initInputDirectory()
        initOutputDirectory()
        
        createDelegate().compileFlex()
    }
    
    private void initInputDirectory() {
        flexConvention.srcDirs.each { sourceDirectory ->
            inputs.dir sourceDirectory
        }
    }

    private void initOutputDirectory() {
        outputs.dir project.buildDir
    }
    
    private CompileTask createDelegate() {
        FlexType type = flexConvention.type
        
        if (type.isLib()) return new Compc(this)
        if (type.isWebApp()) return new Mxmlc(this)
        if (type.isNativeApp()) return new Amxmlc(this)
        
        throw new Exception("Unhandled FlexType ($type)! This should never happen.")
    }
    
}
