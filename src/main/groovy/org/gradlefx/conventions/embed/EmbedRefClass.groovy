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

import org.gradle.api.file.ConfigurableFileTree
import org.gradle.util.ConfigureUtil

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Chaos Encoder</a>
 */
class EmbedRefClass {

    def String name;

    private def List<ConfigurableFileTree> sources = [];

    void configure(Closure closure) {
        ConfigureUtil.configure(closure, this)
    }

    String getName() {
        return name
    }

    void name(String name) {
        this.name = name
    }

    ConfigurableFileTree getSource() {
        return source
    }

    void source(ConfigurableFileTree... sources) {
        this.sources.addAll(sources)
    }

    void source(Collection<ConfigurableFileTree> sources) {
        this.sources.addAll(sources)
    }

    List<ConfigurableFileTree> getSources() {
        return sources
    }


}
