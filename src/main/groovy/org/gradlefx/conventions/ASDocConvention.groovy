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

import org.gradle.util.ConfigureUtil


class ASDocConvention {
    
    private String outputDir = 'doc'
    private List <String> additionalASDocOptions = []

    void configure(Closure closure) {
        ConfigureUtil.configure(closure, this)
    }

    String getOutputDir() {
        return outputDir
    }

    void outputDir(String outputDir) {
        this.outputDir = outputDir
    }

    List<String> getAdditionalASDocOptions() {
        return additionalASDocOptions
    }

    void additionalASDocOptions(List<String> additionalASDocOptions) {
        this.additionalASDocOptions = additionalASDocOptions
    }
}
