package org.gradlefx

import org.gradlefx.cli.CompilerOption;
import org.gradlefx.conventions.FlexType;
import org.gradlefx.conventions.FrameworkLinkage;
import spock.lang.Specification


class FrameworkLinkageTest extends Specification {
    
    //external

    def "external refers to -external-library-path compiler option"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.external
            
        then:
            linkage.getCompilerOption() == CompilerOption.EXTERNAL_LIBRARY_PATH
    }
    
    def "external uses the Flex framework"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.external
            
        then:
            linkage.usesFlex() == true
    }
    
    def "external and FlexType.swf can't be combined"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.external
            FlexType type = FlexType.swf
            linkage.getCompilerDefault(type)
            
        then:
            thrown Exception
    }
    
    def "external and FlexType.air can't be combined"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.external
            FlexType type = FlexType.air
            linkage.getCompilerDefault(type)
            
        then:
            thrown Exception
    }
    
    def "external and FlexType.mobile can't be combined"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.external
            FlexType type = FlexType.mobile
            linkage.getCompilerDefault(type)
            
        then:
            thrown Exception
    }
    
    def "external is default for FlexType.swc"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.external
            FlexType type = FlexType.swc
            
        then:
            linkage.getCompilerDefault(type) == FrameworkLinkage.external
            linkage.isCompilerDefault(type) == true
    }
    
    
    //merged
    
    def "merged refers to -library-path compiler option"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.merged
            
        then:
            linkage.getCompilerOption() == CompilerOption.LIBRARY_PATH
    }
    
    def "merged uses the Flex framework"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.merged
            
        then:
            linkage.usesFlex() == true
    }
    
    def "merged is not default for FlexType.swf"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.merged
            FlexType type = FlexType.swf
                    
        then:
            linkage.getCompilerDefault(type) == FrameworkLinkage.rsl
            linkage.isCompilerDefault(type) == false
    }
    
    def "merged is not default for FlexType.air"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.merged
            FlexType type = FlexType.air
                    
        then:
            linkage.getCompilerDefault(type) == FrameworkLinkage.rsl
            linkage.isCompilerDefault(type) == false
    }
    
    def "merged is not default for FlexType.mobile"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.merged
            FlexType type = FlexType.mobile
                    
        then:
            linkage.getCompilerDefault(type) == FrameworkLinkage.rsl
            linkage.isCompilerDefault(type) == false
    }
    
    def "merged is not default for FlexType.swc"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.merged
            FlexType type = FlexType.swc
                    
        then:
            linkage.getCompilerDefault(type) == FrameworkLinkage.external
            linkage.isCompilerDefault(type) == false
    }
    
    
    //RSL
    
    def "rsl refers to -runtime-shared-library-path compiler option"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.rsl
            
        then:
            linkage.getCompilerOption() == CompilerOption.RUNTIME_SHARED_LIBRARY_PATH
    }
    
    def "rsl uses the Flex framework"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.rsl
            
        then:
            linkage.usesFlex() == true
    }
    
    def "rsl is default for FlexType.swf"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.rsl
            FlexType type = FlexType.swf
                    
        then:
            linkage.getCompilerDefault(type) == FrameworkLinkage.rsl
            linkage.isCompilerDefault(type) == true
    }
    
    def "rsl is default for FlexType.air"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.rsl
            FlexType type = FlexType.air
                    
        then:
            linkage.getCompilerDefault(type) == FrameworkLinkage.rsl
            linkage.isCompilerDefault(type) == true
    }
    
    def "rsl is default for FlexType.mobile"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.rsl
            FlexType type = FlexType.mobile
                    
        then:
            linkage.getCompilerDefault(type) == FrameworkLinkage.rsl
            linkage.isCompilerDefault(type) == true
    }
    
    def "rsl and FlexType.swc can't be combined"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.rsl
            FlexType type = FlexType.swc
            linkage.getCompilerDefault(type)
                    
        then:
            thrown Exception
    }
    
    
    //none
    
    def "none refers to no compiler option"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.none
            
        then:
            linkage.getCompilerOption() == null
    }
    
    def "none doesn't use the Flex framework"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.none
            
        then:
            linkage.usesFlex() == false
    }
    
    def "none defaults to merged and is default for FlexType.swf"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.none
            FlexType type = FlexType.swf
                    
        then:
            linkage.getCompilerDefault(type) == FrameworkLinkage.merged
            linkage.isCompilerDefault(type) == true
    }
    
    def "none defaults to merged and is default for FlexType.air"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.none
            FlexType type = FlexType.air
                    
        then:
            linkage.getCompilerDefault(type) == FrameworkLinkage.merged
            linkage.isCompilerDefault(type) == true
    }
    
    def "none defaults to merged and is default for FlexType.mobile"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.none
            FlexType type = FlexType.mobile
                    
        then:
            linkage.getCompilerDefault(type) == FrameworkLinkage.merged
            linkage.isCompilerDefault(type) == true
    }
    
    def "none defaults to merged and is default for FlexType.swc"() {
        when:
            FrameworkLinkage linkage = FrameworkLinkage.none
            FlexType type = FlexType.swf
                    
        then:
            linkage.getCompilerDefault(type) == FrameworkLinkage.merged
            linkage.isCompilerDefault(type) == true
    }
    
}
