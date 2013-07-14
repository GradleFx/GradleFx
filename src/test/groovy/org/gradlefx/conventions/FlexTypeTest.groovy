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

import spock.lang.Specification

class FlexTypeTest extends Specification {

    def "swf is an app and more specifically a web app"() {
        when:
            FlexType type = FlexType.swf
            
        then:
            type.isApp() == true
            type.isLib() == false
            type.isNativeApp() == false
            type.isWebApp() == true
    }
    
    def "swc is a library and not any kind of app"() {
        when:
            FlexType type = FlexType.swc
            
        then:
            type.isApp() == false
            type.isLib() == true
            type.isNativeApp() == false
            type.isWebApp() == false
    }
    
    def "air is an app and more specifically a native app"() {
        when:
            FlexType type = FlexType.air
            
        then:
            type.isApp() == true
            type.isLib() == false
            type.isNativeApp() == true
            type.isWebApp() == false
    }
    
    def "mobile is an app and more specifically a native app"() {
        when:
            FlexType type = FlexType.mobile
            
        then:
            type.isApp() == true
            type.isLib() == false
            type.isNativeApp() == true
            type.isWebApp() == false
    }
    
}
