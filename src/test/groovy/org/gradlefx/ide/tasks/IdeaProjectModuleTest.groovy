package org.gradlefx.ide.tasks

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradlefx.configuration.Configurations
import org.gradlefx.conventions.FlexType
import org.gradlefx.conventions.FrameworkLinkage
import org.gradlefx.conventions.GradleFxConvention
import spock.lang.Specification

import static junit.framework.Assert.assertTrue

/**
 * @author <a href="mailto:drykovanov@wiley.com">Denis Rykovanov</a>
 * @since 21.05.13
 */
class IdeaProjectModuleTest extends Specification {

    IdeaProject getIdeaProjectTask() {
        if (_ideaFxProjectTask == null) {
            _ideaFxProjectTask = project.tasks.add("ideafx", IdeaProject)
            GradleFxConvention pluginConvention = new GradleFxConvention(project)
            _ideaFxProjectTask.flexConvention = pluginConvention
            _ideaFxProjectTask.flexConvention.playerVersion = "11.5"
        }
        return _ideaFxProjectTask;
    }

    Project project
    IdeaProject _ideaFxProjectTask
    String imlFileContent
    String testResourceDir = './src/test/resources/'

    def setup() {
    }

    def "test generation empty project"() {
        given:
            setupProjectWithName "test"
            ideaProjectTask.flexConvention.type = "swc"
        when:
            ideaProjectTask.createProjectConfig()
        then:
            File imlFile = project.file("${project.name}.iml")
            imlFile.exists() == true
    }

    def "config for pure web lib"() {
        given:
            setupProjectWithName "test"
            ideaProjectTask.flexConvention.type = "swc"
            ideaProjectTask.flexConvention.frameworkLinkage = FrameworkLinkage.none
        when:
            ideaProjectTask.createProjectConfig()
        then:
            def configuration = getModuleConfNode()
            configuration.'@name'.text() == 'test'
            configuration.'@output-type'.text() == "Library"
            configuration.'@pure-as'.text() == "true"
    }

    def "config for flex web lib"() {
        given:
            setupProjectWithName "test"
            ideaProjectTask.flexConvention.type = "swc"
            ideaProjectTask.flexConvention.frameworkLinkage = FrameworkLinkage.external
        when:
            ideaProjectTask.createProjectConfig()
        then:
            def configuration = getModuleConfNode()
            configuration.'@name'.text() == 'test'
            configuration.'@output-type'.text() == "Library"
            configuration.'@pure-as'.text() == "false"
    }

    def "config with swc dependency"() {
        given:
            setupProjectWithName "test"
            ideaProjectTask.flexConvention.type = 'swc'
            project.getDependencies().add(Configurations.MERGE_CONFIGURATION_NAME.configName(), project.files('lib/some.swc'))
        when:
            ideaProjectTask.createProjectConfig()
        then:
            def configuration = getModuleConfNode()
            def moduleId = configuration.dependencies.entries.entry.'@library-id'.text();
            moduleId != null
            configuration.dependencies.entries.entry.dependency.'@linkage'.text() == 'Merged'
            def moduleMgr = getModuleRootMgrNode()
            def orderEntry = moduleMgr.orderEntry.find { it.'@type' == "module-library" }

            orderEntry.library.'@type'.text() == 'flex'
            orderEntry.library.properties.'@id'.text() == moduleId
            orderEntry.library.CLASSES.root.'@url'.text() == 'jar://$MODULE_DIR$/lib/some.swc!/'
    }

    def "config with project dependency"() {
        given:
            setupProjectWithName "test"
            ideaProjectTask.flexConvention.type = 'swc'
            project.getDependencies().add(Configurations.MERGE_CONFIGURATION_NAME.configName(), project.project(':util'))
        when:
            ideaProjectTask.createProjectConfig()
        then:
            def configuration = getModuleConfNode()
            def entry = configuration.dependencies.entries.entry.first();

            entry.'@module-name' == 'util'
            entry.'@build-configuration-name' == 'util'
            entry.dependency.'@linkage'.text() == 'Merged'

            def moduleMgr = getModuleRootMgrNode()
            def orderEntry = moduleMgr.orderEntry.find { it.'@type' == "module" }
            //<orderEntry type="module" module-name="util" />
            orderEntry.'@module-name' == 'util'
    }


    def "setup dependency type"() {
        setup:
            setupProjectWithName "test"
            ideaProjectTask.flexConvention.type = 'swc'
            project.getDependencies().add(configName, project.project(':util'))
            ideaProjectTask.createProjectConfig()
        expect:
            def configuration = getModuleConfNode()
            def entry = configuration.dependencies.entries.entry.first();
            entry.dependency.'@linkage'.text() == linkageType
        where:
            configName << [Configurations.MERGE_CONFIGURATION_NAME.configName(),
                    Configurations.INTERNAL_CONFIGURATION_NAME.configName(),
                    Configurations.EXTERNAL_CONFIGURATION_NAME.configName(),
                    Configurations.TEST_CONFIGURATION_NAME.configName()]
            linkageType << ['Merged', 'Include', 'External', 'Test']
    }


    def "setup flex sdk"() {
        setup:
            setupProjectWithName "test"
            ideaProjectTask.flexConvention.frameworkLinkage = frameworkLinkage
            ideaProjectTask.createProjectConfig()
        expect:
            def configuration = getModuleConfNode()
            configuration.dependencies.sdk.'@name'.text() == 'default_flex_sdk'
            configuration.dependencies.'@framework-linkage'.text() == ideaSdkLinkage
            getModuleRootMgrNode().orderEntry.find { it.'@type' == "jdk" }.'@jdkName' == 'default_flex_sdk'
        where:
            frameworkLinkage << [FrameworkLinkage.merged, FrameworkLinkage.none, FrameworkLinkage.rsl]
            ideaSdkLinkage << ['Merged', '', 'Runtime']
    }

    def "setup flex sdk with custom name"() {
        given:
            setupProjectWithName "test"
            project.setProperty 'ideaFxModuleSdkName', 'customname_flex_sdk'
        when:
            ideaProjectTask.createProjectConfig()
        then:
            def configuration = getModuleConfNode()
            configuration.dependencies.sdk.'@name'.text() == 'customname_flex_sdk'
            getModuleRootMgrNode().orderEntry.find { it.'@type' == 'jdk' }.'@jdkName' == 'customname_flex_sdk'
    }

    def "setup web app project"() {
        given:
            setupProjectWithName "test"
            ideaProjectTask.flexConvention.type = 'swf'
            ideaProjectTask.flexConvention.mainClass = 'subpackage/WebContainer.as'
        when:
            ideaProjectTask.createProjectConfig();
        then:
            getModuleConfNode().'@main-class'.text() == "subpackage.WebContainer"
            getModuleConfNode().'@target-platform'.text() == ""
    }

    def "setup air app project"() {
        given:
            setupProjectWithName "test"
            ideaProjectTask.flexConvention.type = 'air'
            ideaProjectTask.flexConvention.mainClass = 'subpackage/AirContainer.mxml'
            ideaProjectTask.flexConvention.output = 'customOutput'
            ideaProjectTask.flexConvention.air.applicationDescriptor = 'src/main/actionscript/air.xml'
        when:
            ideaProjectTask.createProjectConfig()
        then:
            //todo files included in package
            getModuleConfNode().'@main-class'.text() == "subpackage.AirContainer"
            getModuleConfNode().'@target-platform'.text() == "Desktop"
            getModuleConfNode().'@output-type'.text() == ""
            getModuleConfNode().'@output-file'.text() == "customOutput.swf"
            getModuleConfNode().'packaging-air-desktop'.'@package-file-name'.text() == 'customOutput'
            getModuleConfNode().'packaging-air-desktop'.'@use-generated-descriptor'.text() == 'false'
            getModuleConfNode().'packaging-air-desktop'.'@custom-descriptor-path'.text() == '$MODULE_DIR$/src/main/actionscript/air.xml'
    }

    def "setup air mobile project"() { 
        setup:
            setupProjectWithName "test"
            ideaProjectTask.flexConvention.type = FlexType.mobile
            ideaProjectTask.flexConvention.mainClass = 'subpackage/AirContainer.mxml'
            ideaProjectTask.flexConvention.output = 'customOutput'
            ideaProjectTask.flexConvention.air.applicationDescriptor = 'src/main/actionscript/apk.xml'
            ideaProjectTask.flexConvention.airMobile.platform = platform
            ideaProjectTask.createProjectConfig()
        expect:
            //todo files included in package
            getModuleConfNode().'@main-class'.text() == "subpackage.AirContainer"
            getModuleConfNode().'@target-platform'.text() == "Mobile"
            getModuleConfNode().'@output-type'.text() == ""
            getModuleConfNode().'@output-file'.text() == "customOutput.swf"
            getModuleConfNode()["packaging-$packagin_suffix"].'@enabled'.text() == 'true'
            getModuleConfNode()["packaging-$packagin_suffix"].'@package-file-name'.text() == 'customOutput'
            getModuleConfNode()["packaging-$packagin_suffix"].'@use-generated-descriptor'.text() == 'false'
            getModuleConfNode()["packaging-$packagin_suffix"].'@custom-descriptor-path'.text() == '$MODULE_DIR$/src/main/actionscript/apk.xml'
        where:
            platform << ['android', 'ios']
            packagin_suffix << ['android', 'ios']
    }

    def "setup air lib project"() {
        given:
            setupProjectWithName "test"
            ideaProjectTask.flexConvention.type = FlexType.swc
            ideaProjectTask.flexConvention.additionalCompilerOptions << '+configname=air'
        when:
            ideaProjectTask.createProjectConfig()
        then:
            def configuration = getModuleConfNode()
            configuration.'@name'.text() == 'test'
            configuration.'@output-type'.text() == "Library"
            configuration.'@target-platform'.text() == "Desktop"
            configuration.'@pure-as'.text() == "false"
    }

    def "setup air certificate options for android and air"() { //todo cover ios
        setup:
            setupProjectWithName "test"
            ideaProjectTask.flexConvention.type = type
            ideaProjectTask.flexConvention.air.keystore = 'somecert.p12'
            ideaProjectTask.createProjectConfig()
        expect:
            def configuration = getModuleConfNode()
            configuration["packaging-$suffix"].AirSigningOptions.'@keystore-path'.text() == '$MODULE_DIR$/somecert.p12'
            configuration["packaging-$suffix"].AirSigningOptions.'@use-temp-certificate'.text() == 'false'
        where:
            type    << [FlexType.air, FlexType.mobile]
            suffix  << ['air-desktop', 'android']
        
    }

    def "include file in air package"() {
        setup:
            setupProjectWithName "test"
            ideaProjectTask.flexConvention.type = type
            ideaProjectTask.flexConvention.airMobile.platform = platform
            ideaProjectTask.flexConvention.air.includeFileTrees = [project.fileTree(dir: "sub-resource-dir", include: '**/*.*')]
            ideaProjectTask.createProjectConfig()
        expect:
//            project.fileTree(dir: "sub-resource-dir", include: '*.*').files.empty == false
//            def configuration = getModuleConfNode()
//            configuration["packaging-$suffix"].'files-to-package'.empty == false
        where:
                type            |   platform    |   suffix
                FlexType.air    |   null        |   'air-desktop'
                FlexType.mobile |   'android'   |   'android'
                FlexType.mobile |   'ios'       |   'ios'
    }
    
    def "additional compiler options"() {
        setup:
            setupProjectWithName "test"
            ideaProjectTask.flexConvention.type = FlexType.swc
            ideaProjectTask.flexConvention.additionalCompilerOptions << '+configname=air'
            ideaProjectTask.flexConvention.additionalCompilerOptions << '-tools-locale="en"' << '-default-background-color=0xcccccc'
        when:
            ideaProjectTask.createProjectConfig()
        then:
            def configuration = getModuleConfNode()
            configuration.'compiler-options'.'option'.find{ it.'@name' == "additionalOptions" }.'@value' == '+configname=air -tools-locale="en" -default-background-color=0xcccccc'
    }

    def setupProjectWithName(String projectName) {
        //todo extract
        File projectDir = new File(this.getClass().getResource("/stub-project-dir/intellij-dummy.xml").toURI())
        Project root = ProjectBuilder.builder().withProjectDir(projectDir.parentFile).withName('root').build()
        Project utilProject = ProjectBuilder.builder().withProjectDir(projectDir.getParentFile()).withParent(root).withName('util').build()
        this.project = ProjectBuilder.builder().withProjectDir(projectDir.getParentFile()).withParent(root).withName(projectName).build()
        ideaProjectTask.flexConvention.type = 'swc'
        [
                Configurations.INTERNAL_CONFIGURATION_NAME.configName(),
                Configurations.EXTERNAL_CONFIGURATION_NAME.configName(),
                Configurations.MERGE_CONFIGURATION_NAME.configName(),
                Configurations.RSL_CONFIGURATION_NAME.configName(),
                Configurations.THEME_CONFIGURATION_NAME.configName(),
                Configurations.TEST_CONFIGURATION_NAME.configName()
        ].each { project.configurations.add(it) }

    }

    def getModuleConfNode() {
        File imlFile = project.file("${project.name}.iml")
        def xml = new XmlParser().parse(imlFile);

        def configManager = xml.component.find { it ->
            it.@name == "FlexBuildConfigurationManager" }
        configManager != null

        return configManager.configurations.configuration
    }


    def getModuleRootMgrNode() {
        File imlFile = project.file("${project.name}.iml")
        def xml = new XmlParser().parse(imlFile);

        def configManager = xml.component.find { it ->
            it.@name == "NewModuleRootManager" }
        configManager != null

        return configManager
    }

}
