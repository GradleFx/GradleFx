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

package org.gradlefx.conventions.adl

import org.gradle.util.ConfigureUtil

/**
 * Defines the convention properties for ADL (AIR Debug Launcher).
 */
class AdlConvention {

    /**
     * ADL will debug the applicatino with the specified profile.
     * Can have the following values: desktop, extendedDesktop, mobileDevice
     */
    String profile;
    /**
     * The simulated screen size to use when running apps in the mobileDevice profile on the desktop.
     * To specify the screen size as a predefined screen type, look at the list provided here:
     * http://help.adobe.com/en_US/air/build/WSfffb011ac560372f-6fa6d7e0128cca93d31-8000.html
     * To specify the screen pixel dimensions directly, use the following format: widthXheight:fullscreenWidthXfullscreenHeight
     */
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
