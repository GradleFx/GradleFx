package org.gradlefx

import org.gradlefx.conventions.FlexType;
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
