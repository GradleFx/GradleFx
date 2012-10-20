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

package org.gradlefx.configuration

enum Configurations {
    DEFAULT_CONFIGURATION_NAME('default'),
    ARCHIVES_CONFIGURATION_NAME('archives'),
    INTERNAL_CONFIGURATION_NAME('internal'),
    EXTERNAL_CONFIGURATION_NAME('external'),
    MERGE_CONFIGURATION_NAME('merged'),
    RSL_CONFIGURATION_NAME('rsl'),
    TEST_CONFIGURATION_NAME('test'),
    THEME_CONFIGURATION_NAME('theme'),
    FLEXSDK_CONFIGURATION_NAME('flexSDK'),
    AIRSDK_CONFIGURATION_NAME('airSDK');

    private String configName;

    Configurations(String configName) {
        this.configName = configName
    }

    public String configName() {
        return configName
    }

    public static final List DEPENDENCY_CONFIGURATIONS = [
        INTERNAL_CONFIGURATION_NAME,
        EXTERNAL_CONFIGURATION_NAME,
        MERGE_CONFIGURATION_NAME,
        RSL_CONFIGURATION_NAME,
        TEST_CONFIGURATION_NAME,
        THEME_CONFIGURATION_NAME,
        FLEXSDK_CONFIGURATION_NAME,
        AIRSDK_CONFIGURATION_NAME
    ]
    
    public static final List ARTIFACT_CONFIGURATIONS = [
        DEFAULT_CONFIGURATION_NAME,
        ARCHIVES_CONFIGURATION_NAME
    ]
}
