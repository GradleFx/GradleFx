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

package org.gradlefx.tasks.compile.factory

import org.gradle.api.Task
import org.gradlefx.FlexType
import org.gradlefx.tasks.compile.Compc
import org.gradlefx.tasks.compile.Mxmlc
import org.gradlefx.tasks.compile.NullCompileTask
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.gradlefx.tasks.compile.Amxmlc

class CompileTaskClassFactoryImpl implements CompileTaskClassFactory {

    Logger log = LoggerFactory.getLogger('flex')

    Class<Task> createCompileTaskClass(FlexType flexType) {
        if (flexType.isLib()) return Compc.class
        if (flexType.isWebApp()) return Mxmlc.class
        if (flexType.isNativeApp()) return Amxmlc.class
        
        log.warn "Adding compile task using an empty implementation"
        return NullCompileTask.class
    }
}
