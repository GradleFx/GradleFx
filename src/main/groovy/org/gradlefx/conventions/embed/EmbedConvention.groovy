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
