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

package org.gradlefx

import org.gradlefx.options.CompilerOption;

public enum FrameworkLinkage {
    external(CompilerOption.EXTERNAL_LIBRARY_PATH),
    rsl(CompilerOption.RUNTIME_SHARED_LIBRARY_PATH),
    merged(CompilerOption.LIBRARY_PATH),
    none;
    
    private CompilerOption compilerOption;
    
    private FrameworkLinkage() {}
    
    private FrameworkLinkage(CompilerOption compilerOption) {
        this.compilerOption = compilerOption;
    }
    
    public CompilerOption getCompilerOption() {
        return compilerOption
    }
    
}
