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

import org.gradlefx.cli.CompilerOption;


public enum FrameworkLinkage {
    external(CompilerOption.EXTERNAL_LIBRARY_PATH),
    rsl(CompilerOption.RUNTIME_SHARED_LIBRARY_PATH),
    merged(CompilerOption.LIBRARY_PATH),
    none
    
    private CompilerOption compilerOption
    
    private FrameworkLinkage() {}
    
    private FrameworkLinkage(CompilerOption compilerOption) {
        this.compilerOption = compilerOption;
    }
    
    public CompilerOption getCompilerOption() {
        return compilerOption
    }
    
    public boolean usesFlex() {
        return this != none
    }
    
    public boolean isCompilerDefault(FlexType type) {
        FrameworkLinkage compilerDefault = getCompilerDefault(type)
        return this == compilerDefault || (this == none && compilerDefault == merged)
    }
    
    public FrameworkLinkage getCompilerDefault(FlexType type) {
        return getCompilerDefault(this, type)
    }
    
    /**
    * The framework linkage defaults to 'RSL' for Flex application projects ('swf', 'air' or 'mobile' {@link FlexType}),
    * 'merged' for pure ActionScript application projects
    * and 'external' for library projects ('swc' {@link FlexType}).
    *
    * @return The default framework linkage
    */
    public static FrameworkLinkage getCompilerDefault(FrameworkLinkage linkage, FlexType type) {
        if ((linkage == external && type.isApp()) || (linkage == rsl && type.isLib()))
            throw new Exception('Applications cannot link externally')
            
        return getCompilerDefault(linkage.usesFlex(), type)
    }
    
    public static FrameworkLinkage getCompilerDefault(boolean useFlex, FlexType type) {
        if (type.isLib()) return external
        return useFlex ? rsl : merged
    }
    
}
