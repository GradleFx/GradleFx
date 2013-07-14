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
package org.gradlefx.conventions.embed

import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.util.ConfigureUtil

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Chaos Encoder</a>
 */
public class EmbedConvention {
    private Project project
    def List<EmbedRefClass> embedRefClasses;

    public EmbedConvention(Project project) {
        embedRefClasses = [];
        this.project = project
        //embed = this;
    }

    void configure(Closure closure) {
        ConfigureUtil.configure(closure, this)
    }

    public embed(Closure closure) {
        configure(closure)
    }

    EmbedRefClass embedRefClass(Closure closure) {
        def result = new EmbedRefClass()
        result.configure(closure)
        embedRefClasses.add(result)
        return result;
    }


}
