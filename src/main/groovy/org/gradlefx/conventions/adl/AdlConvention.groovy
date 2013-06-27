package org.gradlefx.conventions.adl

import org.gradle.util.ConfigureUtil

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Chaos Encoder</a>
 */
class AdlConvention {
    private String profile;
    String screenSize

    public AdlConvention() {

    }

    String getScreenSize() {
        return screenSize
    }

    void screenSize(String screenSize) {
        this.screenSize = screenSize
    }

    String getProfile() {
        return profile
    }

    void profile(String profile) {
        this.profile = profile
    }

    def configure(Closure closure) {
        ConfigureUtil.configure(closure, this)
    }
}
