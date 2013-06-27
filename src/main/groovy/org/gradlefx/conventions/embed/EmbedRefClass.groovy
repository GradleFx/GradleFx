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
