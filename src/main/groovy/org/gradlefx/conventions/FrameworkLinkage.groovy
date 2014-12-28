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

import org.gradlefx.cli.CompilerOption;

/**
 * Defines how the framework will be linked during compilation.
 */
public enum FrameworkLinkage {
    /**
     *  Indicates that this library will be externalized from the application, but it will not be used as an RSL.
     *  This excludes all the definitions in a library from an application but provides compile time link checking.
     */
    external(CompilerOption.EXTERNAL_LIBRARY_PATH),
    /**
     * Indicates that this library will be used as an RSL. When you compile your application, the classes and their
     * dependencies in this library are externalized, but you compile against them.
     * You must then make them available as an RSL at run time.
     */
    rsl(CompilerOption.RUNTIME_SHARED_LIBRARY_PATH),
    /**
     * Indicates that classes and their dependencies from this library are added to the SWF file at compile time.
     * They are not loaded at run time. The result is a larger SWF file, but no external dependencies.
     * This is the default selection for the framework.
     */
    merged(CompilerOption.LIBRARY_PATH)
    
    private CompilerOption compilerOption
    
    private FrameworkLinkage() {}
    
    private FrameworkLinkage(CompilerOption compilerOption) {
        this.compilerOption = compilerOption;
    }
    
    public CompilerOption getCompilerOption() {
        return compilerOption
    }

    /**
    * The framework linkage defaults to 'RSL' for Flex application projects ('swf', 'air' or 'mobile' {@link FlexType}),
    * and 'external' for library projects ('swc' {@link FlexType}).
    *
    * @return The default framework linkage
    */
    public static FrameworkLinkage getCompilerDefault(FlexType type) {
        if (type.isLib()) return external
        return rsl
    }

    public boolean isCompilerDefault(FlexType type) {
        FrameworkLinkage compilerDefault = getCompilerDefault(type)
        return this == compilerDefault
    }

}
