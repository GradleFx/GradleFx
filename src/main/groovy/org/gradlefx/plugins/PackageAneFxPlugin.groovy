package org.gradlefx.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradlefx.tasks.ane.PackageAne

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Chaos Encoder</a>
 */
class PackageAneFxPlugin implements Plugin<Project> {
    protected Project project;


    @Override
    protected void configure(Project project) {

    }

    @Override
    public void apply(Project project) {
        this.project = project

        applyPlugins()

        addTasks()
        project.afterEvaluate {
            configure(project)
        }
    }

    def addTasks() {
        project.tasks.add('package', PackageAne)
    }

    protected void applyPlugins() {
        applyPlugin 'base'
    }

    protected void applyPlugin(String name) {
        project.apply(plugin: name)
    }


}
