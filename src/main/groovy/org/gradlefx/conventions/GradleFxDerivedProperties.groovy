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

import org.gradlefx.configuration.sdk.SdkType

import static java.util.UUID.randomUUID
import org.gradle.api.artifacts.ProjectDependency;
import org.gradlefx.cli.CompilerOption;


class GradleFxDerivedProperties {

    public HashSet<SdkType> sdkTypes = new HashSet<SdkType>()

    public boolean usesFlex() {
        return sdkTypes.contains(SdkType.Flex);
    }

    /** A generated unique ID for this project */
    String uuid
    
    public String getUuid() {
        if (!uuid) uuid = uuid ?: randomUUID().toString()
        return uuid
    }
    
    /** A unique identifier for air and mobile applications */
    String applicationId
    
    public String getApplicationId() {
        if (!applicationId) applicationId = (getPackageName() ? getPackageName() + '.' : '') + project.name
        return applicationId
    }
    
    /** A version number for air and mobile applications */
    String version
    
    public String getVersion() {
        if (!version) version = toVersionNumber project.version
        return version
    }
    
    /** The class name of the main class */
    String className
    
    public String getClassName() {
        if (!className) className = toClassName new File(getMainClassPath())
        return className
    }
    
    /** The package of the main class */
    String packageName
    
    public String getPackageName() {
        if (!packageName) packageName = toPackage new File(getMainClassPath())
        return packageName
    }
    
    /** The default file extension for this configuration */
    String defaultExtension
    
    public String getDefaultExtension() {
        if (!defaultExtension) defaultExtension = usesFlex() ? '.mxml' : '.as'
        return defaultExtension
    }
    
    /** The physical path of the main class relative to src dirs */
    String mainClassPath
    
    public String getMainClassPath() {
        if (!mainClassPath) mainClassPath = createMainClassPath getMainClass(), getDefaultExtension()
        return mainClassPath
    }
    
    /** The path to the Flex SDK's configuration file */
    String configPath
    
    public String getConfigPath() {
        if (!configPath) configPath = "${getFlexHome()}/frameworks/${getType().configName}-config.xml"
        return configPath
    }
    
    /** A collection of all source directories */
    List allSrcDirs
    
    public List getAllSrcDirs() {
        if (!allSrcDirs) allSrcDirs = createAllSrcDirs()
        return allSrcDirs
    }
    
    /** A collection of additional compiler arguments for the IDE */
    List compilerArgs
    
    public List getCompilerArgs() {
        if (!compilerArgs) compilerArgs = createCompilerArgs()
        return compilerArgs
    }
    
    /** The default framework linkage for this project */
    FrameworkLinkage defaultFrameworkLinkage
    
    public getDefaultFrameworkLinkage() {
        if (!defaultFrameworkLinkage) defaultFrameworkLinkage = FrameworkLinkage.getCompilerDefault getType()
        return defaultFrameworkLinkage
    }
    
    /** A collection of {@link org.gradle.api.Project}s that are dependencies for this project */
    def dependencyProjects
    
    public getDependencyProjects() {
        if (!dependencyProjects) dependencyProjects = createDependencyProjects()       
        return dependencyProjects
    }
    
    
    /**
    * Creates a List of additional compiler arguments that the IDE will need in order to compile
    *
    * @return A List of additional compiler arguments for the IDE
    */
   private List createCompilerArgs() {
       List args = []
       
       if (getLocales().size()) {
           args.add "${CompilerOption.LOCALE}=${getLocales().join(',')}"
           args.add "${CompilerOption.SOURCE_PATH}=${getLocaleDir()}/{locale}"
       }
       
       args.addAll getAdditionalCompilerOptions()
       
       return args;
   }
    
    /**
    * Distills a class name from a {@link File}'s name
    *
    * @param file  The File we want a class name from
    * @return A class name
    */
   private String toClassName(File file) {
       //\.(mxml|as)$ : remove .as or .mxml extension
       return file.name.replaceAll(/\.(mxml|as)$/, '')
   }
   
   /**
    * Distills a package name from a {@link File}'s parent directory
    *
    * @param file  The File we want a package name from
    * @return A package name
    */
   private String toPackage(File file) {
       //replace back- and forward slashes with dots
       return file.parent ? file.parent.replaceAll(/\//, '.').replaceAll('\\\\', '.') : ''
   }
   
   /**
    * If mainClass already is a path, just returns that,
    * otherwise it converts the package notation into a path.
    * 
    * @return The physical path of the main class relative to src dirs
    */
   private static String createMainClassPath(String mainClass, String defaultExtension) {
       //^[\w\.]+$ : only dots and word characters allowed
       //^\w+\.(mxml|as)$ : Main.as or Main.mxml (without dir slashes) looks like a package notation, so we filter them out
       return mainClass.matches(/^[\w\.]+$/) && !mainClass.matches(/^\w+\.(mxml|as)$/) ?   
                  mainClass.replaceAll(/\./, '/') + defaultExtension : mainClass
   }
   
   /**
    * Creates an AIR descriptor compatible version (format <0-999>.<0-999>.<0-999>) number
    * from any kind of version string
    *
    * @param version The original version string
    * @return The version string formatted according to the <0-999>.<0-999>.<0-999> pattern
    */
   private String toVersionNumber(String version) {
       //[^\.\d] : remove anything that is not a dot or a number
       return !version || version == 'unspecified' ? '0.0.0' : version.replaceAll(/[^\.\d]/, '')
   }
   
   /** 
    * Creates a collection of all source directories 
    */
   private List createAllSrcDirs() {
       return [
            getSrcDirs(),
            getResourceDirs(),
            getTestDirs(),
            getTestResourceDirs()
        ].flatten()
   }
   
   /** 
    * Creates a collection of {@link Project}s that are dependencies for this project 
    */
   private createDependencyProjects() {
       return (
            project.configurations.internal.getDependencies() +
            project.configurations.merged.getDependencies() +
            project.configurations.external.getDependencies() +
            project.configurations.rsl.getDependencies() +
            project.configurations.theme.getDependencies()
        ).findAll { it instanceof ProjectDependency }
         .collect { it.getDependencyProject() }
   }

}
