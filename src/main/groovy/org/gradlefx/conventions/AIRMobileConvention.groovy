package org.gradlefx.conventions

import org.gradle.api.Project

/**
 * @author <a href="mailto:drykovanov@wiley.com">Denis Rykovanov</a>
 * @since 11.03.13
 */
class AIRMobileConvention extends AIRConvention {

    private String target;

    AIRMobileConvention(Project project) {
        super(project)
        target = 'apk'
    }

    String getTarget() {
        return target
    }

    void target(String target) {
        this.target = target
    }
}
