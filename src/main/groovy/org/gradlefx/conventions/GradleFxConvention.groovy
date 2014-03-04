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
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.file.BaseDirFileResolver
import org.gradle.internal.nativeplatform.filesystem.FileSystems
import org.gradlefx.configuration.sdk.SdkType
import org.gradlefx.conventions.adl.AdlConvention


@Mixin(GradleFxDerivedProperties)
class GradleFxConvention {
    
    private Project project

    File gradleFxUserHomeDir

    String output
    
    public String getOutput() {
        return output ?: project.name
    }

    // the home directory of the Flex SDK
    String flexHome = System.getenv()['FLEX_HOME'] //default to FLEX_HOME environment variable
    
    public void setFlexHome(String flexHome) {
        //convert relative paths to absolute ones to prevent ANT from freaking out
        this.flexHome = flexHome ? new File(flexHome).absolutePath : null
    }

    //The name you want to give to the SDK
    String flexSdkName

    // which directories to look into for source code
    List <String> srcDirs = ['src/main/actionscript']

    //test directories
    List <String> testDirs = ['src/test/actionscript']

    //resource directories
    List <String> resourceDirs = ['src/main/resources']

    //test resource directories
    List <String> testResourceDirs = ['src/test/resources']
    
    //locale root directory
    String localeDir = 'src/main/locale'
    
    //array of locales; maps to the locale compiler option
    List <String> locales = []

    //equivalent of the include-classes compiler option
    List <String> includeClasses;

    //equivalent of the include-sources compiler option
    List <String> includeSources;

    // what type of Flex project are we?  either SWF, SWC or AIR
    FlexType type
    
    //how the Flex framework will be linked in the project: external, RSL, merged or none
    //default: RSL for swf, external for swc
    FrameworkLinkage frameworkLinkage
    
    public FrameworkLinkage getFrameworkLinkage() {
        return frameworkLinkage ?: FrameworkLinkage.getCompilerDefault(true, type)
    }

    //Whether the asdocs should be merged into the swc for use in Flash Builder
    Boolean fatSwc

    // the directory where we should publish the build artifacts
    String publishDir = 'publish'

    //the root class which is used by the mxmlc compiler to create a swf
    String mainClass = 'Main'

    //array of additional compiler options as defined by the compc or mxmlc compiler
    List <String> additionalCompilerOptions = []

    List <String> jvmArguments = []

    // player version
    String playerVersion = '10.0'

    // To use debugRLS
    def useDebugRSLSwfs = false

    // HTML wrapper options
    HtmlWrapperConvention htmlWrapper
	
	// FlexUnit properties
	FlexUnitConvention flexUnit

    // AIR packaging properties
    AIRConvention air

    // AIR mobile packaging properties
    AIRMobileConvention airMobile

    // ASDoc properties
    ASDocConvention asdoc

    //SDK autoinstall properties
    SdkAutoInstallConvention sdkAutoInstall

    //adl convention propeperties
    AdlConvention adl

    def GradleFxConvention(Project project) {
        this.project = project

        FileResolver gradleFxUserHomeDirResolver = new BaseDirFileResolver(FileSystems.default, project.gradle.gradleUserHomeDir)
        gradleFxUserHomeDir = gradleFxUserHomeDirResolver.resolve("gradleFx")

        htmlWrapper     = new HtmlWrapperConvention(project)
        flexUnit        = new FlexUnitConvention(project)
        air             = new AIRConvention(project)
        airMobile       = new AIRMobileConvention(project)
        asdoc           = new ASDocConvention()
        sdkAutoInstall  = new SdkAutoInstallConvention()
        adl             = new AdlConvention()
    }

    /**
     * Complex convention setters
     */

    def htmlWrapper(Closure closure) {
        htmlWrapper.configure(closure)
    }

    def air(Closure closure) {
        air.configure(closure)
    }

    def airMobile(Closure closure) {
        airMobile.configure(closure)
    }

    def adl(Closure closure) {
        adl.configure(closure)
    }

    def flexUnit(Closure closure) {
        flexUnit.configure(closure)
    }

    def asdoc(Closure closure) {
        asdoc.configure(closure)
    }

    def sdkAutoInstall(Closure closure) {
        sdkAutoInstall.configure(closure)
    }
    
}

