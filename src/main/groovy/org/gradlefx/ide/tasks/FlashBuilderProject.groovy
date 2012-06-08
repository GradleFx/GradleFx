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

package org.gradlefx.ide.tasks

import org.gradle.api.Project;
import org.gradlefx.FlexType
import org.gradlefx.FrameworkLinkage
import org.gradlefx.configuration.Configurations;
import org.gradlefx.templates.tasks.Scaffold;


@Mixin(FlashBuilderUtil)
class FlashBuilderProject extends AbstractIDEProject {
    public static final String NAME = 'flashbuilder'
    
    protected static int libtype_dir = 1
    protected static int libtype_swc = 3
    protected static int libtype_sdk = 4
    
    protected static int linktype_merged = 1
    protected static int linktype_external = 2
    protected static int linktype_rsl = 4
    
    protected static linkTypeMap = [
        (FrameworkLinkage.merged): linktype_merged,
        (FrameworkLinkage.external): linktype_external,
        (FrameworkLinkage.rsl): linktype_rsl
    ]
    
    protected String mainSrcDir
    
    
    /**
    * Constructor
    */
    public FlashBuilderProject() {
        super('FlashBuilder')
        mainSrcDir = flexConvention.srcDirs[0]
    }
    
    @Override
    protected void createProjectConfig() {
        createConfigFiles()
        addSourceDirs()
        addIncludedClasses()
        addDependencies()
    }
    
    @Override
    protected void invalidateConventions() {
        boolean isValid = !isApplicationFilesInvalid()
        if (!isValid) moveApplicationFiles()
        isValid = isValid && validateProjectDependencies()
        
        if (isValid) LOG.info '\tOK'
    }
    
    /**
     * Validates whether the application files are in a valid location for FlashBuilder and takes action if not.
     * Bottom line is: AIR and mobile application files must be located in the main source folder root (not in a package).
     * 
     * @return Whether the application files are in a invalid location for FlashBuilder
     */
    private boolean isApplicationFilesInvalid() {
        return flexConvention.type.isNativeApp() && !!flexConvention.packageName
    }
    
    private boolean validateProjectDependencies() {
        boolean isValid = true
        
        flexConvention.dependencyProjects.each {
            File buildDir = it.file getOutputDir(it)
            if (!buildDir.exists()) {
                isValid = false
                
                LOG.warn "\t[WARNING] Couldn't find a compiled library (.swc file) for dependency project '$it.name' " +
                                     "in $buildDir.path because the project wasn't compiled with $ideName yet: " +
                                     "please build '$it.name' with $ideName and run this task again to verify whether this warning disappears"
            
                return
            }
            
            File swcFile = new File(buildDir, "${it.name}.${FlexType.swc}")
            if (!swcFile.exists()) {
                isValid = false
                
                String msg = "Couldn't find a compiled library (.swc file) for dependency project $it.name at $swcFile.path"
                
                File alternativeSwcFile = buildDir.listFiles().find {
                    it.name.endsWith ".$FlexType.swc"
                }
                if (alternativeSwcFile) 
                    msg += "; however, we found '$alternativeSwcFile.name' in $buildDir.path " +
                           "and we'll use it to set up $ideName's project dependency"
                else msg += " so we scanned for other libraries in $buildDir.path, but couldn't find any"
                
                LOG.warn "\t[WARNING] $msg"
                LOG.warn "\t[WARNING] Verify whether $ideName's project name matches the dependency's Gradle project name; " +
                                     "$ideName caches the name of an existing project outside the project directory; " +
                                     "this means GradleFx cannot alter it for you, so you may want to do it manually."
            }
        }
        
        return isValid
    }
    
    /**
     * Moves the application files to the main source folder root and adjusts the related conventions.
     */
    private void moveApplicationFiles() {
        LOG.warn "\t[WARNING] The mainClass you defined is incompatible with $ideName"
        LOG.warn "\t[WARNING] GradleFX will automatically move the main application file " +
                 "and the descriptor to the root of the main source directory"
        
        //delete files generated by SkeletonProject
        String fullMainClassPath = mainSrcDir + '/' + flexConvention.mainClassPath
        project.file(fullMainClassPath).delete()
        project.file(toDescriptorPath(fullMainClassPath)).delete()
        
        //remove either the package or the path from mainClass
        String delimiter = flexConvention.mainClass == flexConvention.mainClassPath ? '/' : '.'
        List mainClassParts = flexConvention.mainClass.split(delimiter)
        flexConvention.mainClass = mainClassParts[mainClassParts.size() - 1]
        
        //reset derived properties
        flexConvention.mainClassPath = null
        flexConvention.className = null
        flexConvention.packageName = null
        flexConvention.applicationId = null
        
        //re-execute SkeletonProject#createMainClass() with new settings
        project.tasks.getByName(Scaffold.NAME).createMainClass()
        
        LOG.warn "\t[WARNING] $ideName conflict resolved; " + 
                 "if you wish to get rid of this message, edit the mainClass' location"
    }
    
    /**
     * Creates the FlashBuilder config files based on templates for the respective project types.
     * .project and .actionScriptProperties are always included;
     * if it's a library project we add a .flexLibProperties file (even if it's pure ActionScript);
     * if it's any other kind of Flex project we add a .flexProperties file
     */
    private void createConfigFiles() {
        List extensions = [FlashBuilderUtil.eclipseProject, FlashBuilderUtil.actionScriptProperties]
        if (flexConvention.type.isLib()) extensions.add FlashBuilderUtil.flexLibProperties
        else if (flexConvention.frameworkLinkage.usesFlex()) extensions.add FlashBuilderUtil.flexProperties
            
        extensions.each {
            LOG.info '\t' + it
            writeContent getTemplate(it), project.file(it), true
        }
    }
    
    /**
     * Adds all source dirs except the main source dir to the project's classpath
     */
    private void addSourceDirs() {
        editXmlFile FlashBuilderUtil.actionScriptProperties, { xml ->
            def parent = xml.compiler.compilerSourcePath[0]
            
            flexConvention.allSrcDirs.each {
                if (it != mainSrcDir)
                    new Node(parent, 'compilerSourcePathEntry', [
                        kind: libtype_dir, 
                        linkType: linktype_merged, 
                        path: it
                    ])
            }
        }
    }
    
    /**
     * If {@link GradleFxConvention}.includeClasses is defined, 
     * add the included classes to the project configuration
     */
    private void addIncludedClasses() {
        if (flexConvention.includeClasses == null) return

        editXmlFile FlashBuilderUtil.flexLibProperties, { xml ->
            def parent = xml.includeClasses[0]
            xml.@includeAllClasses = false
            
            flexConvention.includeClasses.each {
                new Node(parent, 'classEntry', [path: it])
            }
        }
    }
    
    /**
     * Sets the framework linkage and creates dependency nodes for all defined dependencies
     */
    private void addDependencies() {       
        editXmlFile FlashBuilderUtil.actionScriptProperties, { xml ->
            Node libNode = xml.compiler.libraryPath[0]
            libNode.@defaultLinkType = flexConvention.frameworkLinkage.isCompilerDefault(flexConvention.type) ? 0 : 1
            
            eachDependencyFile { file, type ->
                switch (type) {
                    case Configurations.INTERNAL_CONFIGURATION_NAME:
                    case Configurations.MERGE_CONFIGURATION_NAME:
                        createDependencyNode libNode, file, FrameworkLinkage.merged
                        break
                    case Configurations.EXTERNAL_CONFIGURATION_NAME:
                        createDependencyNode libNode, file, FrameworkLinkage.external
                        break                       
                    case Configurations.RSL_CONFIGURATION_NAME:
                        Node node = createDependencyNode libNode, file, FrameworkLinkage.rsl
                        node.@applicationDomain = 'default'
                        node.@forceLoad = false
                        
                        node = new Node(node, 'crossDomainRsls')
                        node = new Node(node, 'crossDomainRslEntry', [
                            autoExtract: true,
                            policyFileUrl: '',
                            rslUrl: file.name[0..-2] + 'f'
                        ])
                        break
                    case Configurations.THEME_CONFIGURATION_NAME:
                        String themeName = file.name[0..-5]
                        
                        if (!themeName.matches(/(\d|_|\w)+/)) {
                            LOG.warn "\t[WARNING] The file name $themeName is not supported by $ideName:"
                            LOG.warn "\t[WARNING] it can not contain anything else than letters, numbers and underscores."
                            LOG.warn "\t[WARNING] Because of this restriction the theme could not be imported."
                            LOG.warn "\t[WARNING] Consider renaming your theme artifact or switching to a decent IDE."
                            break
                        }
                        
                        new Node(xml, 'theme', [
                            themeIsDefault: false,
                            themeIsSDK: false,
                            themeLocation: '${EXTERNAL_THEME_DIR}/' + themeName
                        ])
                        
                        LOG.warn "\t[WARNING] GradleFx created a theme dependency with name '$themeName' for you; "
                        LOG.warn "\t[WARNING] however if $ideName does not know this name, it will not recognize your theme."
                        LOG.warn "\t[WARNING] Unfortunately GradleFx cannot access the list of themes stored in your $ideName installation to fix this."
                        LOG.warn "\t[WARNING] Verify that the proper theme is selected by going to 'Properties > Flex Theme':"
                        LOG.warn "\t[WARNING] if the Spark theme is selected instead of yours, you must import yours."
                        LOG.warn "\t[WARNING] Click on 'Import Theme...' and fill out the form with these data:"
                        LOG.warn "\t\tTheme file: ${toDependencyPath(file)}"
                        LOG.warn "\t\tTheme name: $themeName"
                        break
                }
            }
        }
    }
    
    /**
     * Creates an XML node for a dependency.
     * 
     * @param parent    The parent XML node of the node to be created
     * @param file      The dependency file (should be a swc)
     * @param linkage   How the dependency will be linked into the project
     * @return An XML node for a dependency
     */
    protected Node createDependencyNode(Node parent, File file, FrameworkLinkage linkage) {
        return new Node(parent, 'libraryPathEntry', [
            kind: libtype_swc, 
            linkType: linkTypeMap[linkage], 
            path: toDependencyPath(file), 
            useDefaultLinkType: flexConvention.frameworkLinkage == linkage
        ])
    }
    
    /**
     * Generates a template file stream for a given dot-file. 
     * These template file are located in the 'resources' directory.
     * 
     * @param extension The dot-file we want a template for
     * @return A template for a dot-file
     */
    protected InputStream getTemplate(String extension) {
        String asfx = flexConvention.frameworkLinkage.usesFlex() ? 'fx' : 'as'
        String path = "/templates/${getName()}/${flexConvention.type}-${asfx}${extension}"

        InputStream stream = getClass().getResourceAsStream(path)
        if (stream) return stream
        
        path = "/templates/${getName()}/template${extension}"
        return getClass().getResourceAsStream(path)
    }
    
    /**
    * Takes a swc {@link File} and returns a path formatted for FlashBuilder:
    * if it's a {@link Project} dependency, a relative path to the swc compiled by FlashBuiler;
    * in any other case, the absolute path
    *
    * @param swcFile A dependency swc file
    * @return A FlashBuilder formatted path
    */
   protected String toDependencyPath(File swcFile) {
       String path = swcFile.path
       
       flexConvention.dependencyProjects.findAll {
           swcFile.path.startsWith it.projectDir.path
       }.each {
           String buildDirPath = getOutputDir it
           File buildDir = it.file buildDirPath
           swcFile = new File(buildDir, "${it.name}.${FlexType.swc}")
           
           if (!swcFile.exists()) {
               File alternative = buildDir.listFiles().find {
                   it.name.endsWith ".$FlexType.swc"
               }
               if (alternative) swcFile = alternative
           }
           
           path = "/${swcFile.name[0..-5]}/$buildDirPath/$swcFile.name"
       }
       
       return path
   }

}
