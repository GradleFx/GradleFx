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
    private String provisioning_profile
    private String outputExtension
    private String platform
    private String simulatorPlatformSdk
    private String simulatorTarget
    private String simulatorTargetDevice


    public AIRMobileConvention(Project project) {
        target = 'apk'
        outputExtension = 'apk'
        platform = 'android'
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

    String getSimulatorTarget() {
        return simulatorTarget
    }

    void simulatorTarget(String simulatorTarget) {
        this.simulatorTarget = simulatorTarget
    }



    String getSimulatorTargetDevice() {
        return simulatorTargetDevice
    }

    void simulatorTargetDevice(String simulatorTargetDevice) {
        this.simulatorTargetDevice = simulatorTargetDevice
    }


    String getExtensionDir() {
        return extensionDir
    }

    void extensionDir(String extensionDir) {
        this.extensionDir = extensionDir
    }

    String getProvisioning_profile() {
        provisioning_profile
    }

    void provisioning_profile(String profile) {
        provisioning_profile = profile
    }

    String getOutputExtension() {
        outputExtension
    }

    void outputExtension(String outputExtension) {
        this.outputExtension = outputExtension
    }

    String getPlatform() {
        platform
    }

    void platform(String platform) {
        this.platform = platform;
    }

    String getSimulatorPlatformSdk() {
        return simulatorPlatformSdk
    }

    void simulatorPlatformSdk(String platformSdk) {
        this.simulatorPlatformSdk = platformSdk
    }


}
