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
package org.gradlefx.conventions

import org.gradle.api.Project
import org.gradle.util.ConfigureUtil

/**
 * All convention properties related to AIR for mobile.
 */
class AIRMobileConvention  {

    /**
     * Specifies the mobile platform for which the package is created.
     * ane —an AIR native extension package
     * Android package targets:
     *  apk — an Android package. A package produced with this target can only be installed on an Android device, not an emulator.
     *  apk‑captive‑runtime — an Android package that includes both the application and a captive version of the AIR runtime. A package produced with this target can only be installed on an Android device, not an emulator.
     *  apk-debug — an Android package with extra debugging information. (The SWF files in the application must also be compiled with debugging support.)
     *  apk-emulator — an Android package for use on an emulator without debugging support. (Use the apk-debug target to permit debugging on both emulators and devices.)
     *  apk-profile — an Android package that supports application performance and memory profiling.
     * iOS package targets:
     *  ipa-ad-hoc — an iOS package for ad hoc distribution.
     *  ipa-app-store — an iOS package for Apple App store distribution.
     *  ipa-debug — an iOS package with extra debugging information. (The SWF files in the application must also be compiled with debugging support.)
     *  ipa-test — an iOS package compiled without optimization or debugging information.
     *  ipa-debug-interpreter — functionally equivalent to a debug package, but compiles more quickly. However, the ActionScript bytecode is interpreted and not translated to machine code. As a result, code execution is slower in an interpreter package.
     *  ipa-debug-interpreter-simulator — functionally equivalent to ipa-debug-interpreter, but packaged for the iOS simulator. Macintosh-only. If you use this option, you must also include the -platformsdk option, specifying the path to the iOS Simulator SDK.
     *  ipa-test-interpreter — functionally equivalent to a test package, but compiles more quickly. However, the ActionScript bytecode is interpreted and not translated to machine code. As a result, code execution is slower in an interpreter package.
     *  ipa-test-interpreter-simulator — functionally equivalent to ipa-test-interpreter, but packaged for the iOS simulator. Macintosh-only. If you use this option, you must also include the -platformsdk option, specifying the path to the iOS Simulator SDK.
     */
    private String target

    /**
     * The name of a directory to search for native extensions (ANE files).
     * Either an absolute path or a relative path from the project directory.
     */
    private String extensionDir

    /**
     * Specify ios_simulator, the serial number (Android), or handle (iOS) of the connected device.
     * On iOS, this parameter is required; on Android, this paramater only needs to be specified when more than one
     * Android device or emulator is attached to your computer and running.
     * If the specified device is not connected, ADT returns exit code 14: Device error (Android) or Invalid device specified (iOS).
     * If more than one device or emulator is connected and a device is not specified, ADT returns exit code 2: Usage error
     */
    private String targetDevice

    /**
     * The path to your iOS provisioning profile. Relative from your project directory.
     */
    private String provisioningProfile

    /**
     * The extension of the packaged application.
     */
    private String outputExtension

    /**
     * The name of the platform of the device. Specify ios or android.
     */
    private String platform

    /**
     * The path to the platform SDK for the target device:
     *  Android - The AIR 2.6+ SDK includes the tools from the Android SDK needed to implement the relevant ADT commands.
     *  Only set this value to use a different version of the Android SDK. Also, the platform SDK path does not need to
     *  be supplied if the AIR_ANDROID_SDK_HOME environment variable is already set.
     *  iOS - The AIR SDK ships with a captive iOS SDK. The platformsdk option lets you package applications with an
     *  external SDK so that you are not restricted to using the captive iOS SDK.
     *  For example, if you have built an extension with the latest iOS SDK, you can specify that SDK when packaging
     *  your application. Additionally, when using ADT with the iOS Simulator, you must always include the platformsdk
     *  option, specifying the path to the iOS Simulator SDK.
     */
    private String platformSdk

    /**
     * The path to the platform SDK for the simulator.
     */
    private String simulatorPlatformSdk

    /**
     * Specifies the mobile platform of the simulator. See the target property for more information.
     */
    private String simulatorTarget

    /**
     * Specifies the device of the simulator. See the device property for more information.
     */
    private String simulatorTargetDevice

    /**
     * Specifies whether -sampler option is used at the packaging phase
     */
    private Boolean sampler

    /**
     * Specifies whether -useLegacyAOT with flag 'no' option is used at the packaging phase
     */
    private Boolean nonLegacyCompiler

    /**
     * (Android only, AIR 14 and higher) Application developers can use this argument to create APK for x86 platforms,
     * it takes following values:
     * armv7 - ADT packages APK for the Android armv7 platform. This is the default value when no value is specified.
     * x86 - ADT packages APK for the Android x86 platform.
     */
    private String arch

    public AIRMobileConvention(Project project) {
        target = 'apk'
        simulatorTarget = 'apk'
        outputExtension = 'apk'
        platform = 'android'
        sampler = false
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
        provisioningProfile
    }

    void provisioning_profile(String profile) {
        provisioningProfile = profile
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

    Boolean getSampler() {
        return sampler
    }

    void sampler(Boolean sampler) {
        this.sampler = sampler
    }

    Boolean getNonLegacyCompiler() {
        return nonLegacyCompiler
    }

    void setNonLegacyCompiler(Boolean nonLegacyCompiler) {
        this.nonLegacyCompiler = nonLegacyCompiler
    }

    String getArch() {
        return arch
    }

    void setArch(String arch) {
        this.arch = arch
    }
}
