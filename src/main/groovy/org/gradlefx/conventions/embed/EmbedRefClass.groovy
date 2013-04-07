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

    void source(ConfigurableFileTree source) {
        this.sources.add(source)
    }

    List<ConfigurableFileTree> getSources() {
        return sources
    }


}
