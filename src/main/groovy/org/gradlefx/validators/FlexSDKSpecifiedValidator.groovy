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

package org.gradlefx.validators

import groovy.io.FileType
import org.apache.commons.lang.StringUtils

class FlexSDKSpecifiedValidator extends AbstractProjectPropertyValidator {
    
    private String flexConfigPath = "/frameworks/flex-config.xml"
    private String flexLibPath = "/frameworks/libs"
    
    void execute() {
        if(isFlexSDKHomeEmpty()) {
            addError("The Flex home location isn't specified. You can solve this by defining the FLEX_HOME" +
                " environment variable or by specifying the flexHome property in your build script")
        } else if(isFlexSDKHomeInvalid()) {
            addError("The path to the Flex home directory isn't valid (${flexConvention.flexHome}): " +
                "the directory doesn't exist")
        } else {
            if (hasNoFlexSDKlibs()) {
                addError("The path to the Flex home directory isn't valid (${flexConvention.flexHome}): " +
                    "can't find the Flex libraries in '${flexLibPath}'")
            }
            if (hasNoFlexConfigFile()) {
                addError("The path to the Flex home directory isn't valid (${flexConvention.flexHome}): " +
                    "can't find 'flex-config.xml' at '${flexConfigPath}'")
            }
        }
    }

    private boolean isFlexSDKHomeEmpty() {
        return StringUtils.isBlank(flexConvention.flexHome)
    }

    private boolean isFlexSDKHomeInvalid() {
        return !new File(flexConvention.flexHome).exists()
    }
    
    private boolean hasNoFlexSDKlibs() {
        File flexLibDir = new File(flexConvention.flexHome + flexLibPath)
        if (!flexLibDir.exists()) return true
        
        boolean hasSwc = false
        flexLibDir.traverse(type: FileType.FILES, nameFilter: ~/.*\.swc/) {
            hasSwc = true
        }
        return !hasSwc
    }
    
    private boolean hasNoFlexConfigFile() {
        File flexConfigFile = new File(flexConvention.flexHome + flexConfigPath)
        return !flexConfigFile.exists()
    }

}
