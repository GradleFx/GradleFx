package org.gradlefx.conventions

import org.gradle.api.Project
import org.gradle.util.ConfigureUtil

/**
 * @author <a href="mailto:drykovanov@wiley.com">Denis Rykovanov</a>
 * @since 11.03.13
 */
class AIRMobileConvention  {

    private String target;

    private String extensionDir;
    private AIRConvention air;

    public AIRMobileConvention(Project project) {
        air = new AIRConvention(project)
        target = 'apk'
    }

    void configure(Closure closure) {
        ConfigureUtil.configure(closure, this)
    }

    def air(Closure closure) {
        air.configure(closure)
    }

    String getTarget() {
        return target
    }

    void target(String target) {
        this.target = target
    }

    AIRConvention getAir() {
        return air
    }

    void air(AIRConvention air) {
        this.air = air
    }

    String getExtensionDir() {
        return extensionDir
    }

    void extensionDir(String extensionDir) {
        this.extensionDir = extensionDir
    }
}
