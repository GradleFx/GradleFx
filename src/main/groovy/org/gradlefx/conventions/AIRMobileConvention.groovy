package org.gradlefx.conventions

import org.gradle.api.Project
import org.gradle.util.ConfigureUtil

/**
 * @author <a href="mailto:drykovanov@wiley.com">Denis Rykovanov</a>
 * @since 11.03.13
 */
class AIRMobileConvention  {

    private String target

    private String extensionDir
    //private AIRConvention air
    private String platformSdk

    private String targetDevice

    public AIRMobileConvention(Project project) {
        target = 'apk'
    }

    void configure(Closure closure) {
        ConfigureUtil.configure(closure, this)
    }

    String getTargetDevice() {
        return targetDevice
    }

    void targetDevice(String targetDevice) {
        this.targetDevice = targetDevice
    }

    String getPlatformSdk() {
        return platformSdk
    }

    void platformSdk(String platformSdk) {
        this.platformSdk = platformSdk
    }


    String getTarget() {
        return target
    }

    void target(String target) {
        this.target = target
    }

    String getExtensionDir() {
        return extensionDir
    }

    void extensionDir(String extensionDir) {
        this.extensionDir = extensionDir
    }
}
