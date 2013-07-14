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

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradlefx.configuration.Configurations
import org.gradlefx.conventions.GradleFxConvention
import org.junit.Before
import org.junit.Test

import static junit.framework.Assert.assertFalse
import static junit.framework.Assert.assertTrue
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.core.StringContains.containsString

class IdeaProjectTest {

    Project project
    IdeaProject _ideaFxProjectTask
    String imlFileContent

    @Test
    void should_create_iml_file() {
        given_project_name_is("AmandaHuggenkiss")
        when_I_create_project_config()
        then_an_iml_file_should_be_created_with_name("AmandaHuggenkiss.iml")
    }

    @Test
    void should_create_top_xml_tags() {
        given_project_name_is("AmandaHuggenkiss")
        when_I_create_project_config()
        then_the_iml_file_should_have_tag('<module type="Flex" version="4">')
    }

    @Test
    void should_create_correct_target_player_version() {
        given_project_name_is("AmandaHuggenkiss")
        given_player_version_is("12")
        when_I_create_project_config()
        then_the_iml_file_should_have_tag('<option name="TARGET_PLAYER_VERSION" value="12"/>')
        then_the_iml_file_should_have_tag('<dependencies target-player="12" framework-linkage="Runtime">')
    }

    @Test
    void should_have_correct_output_file() {
        given_project_name_is("AmandaHuggenkiss")
        given_project_type_is("swc")
        given_project_outputname_is("OliverClothesoff")
        when_I_create_project_config()
        then_the_iml_file_should_have_tag('<configuration name="AmandaHuggenkiss" pure-as="false" output-type="Library" output-file="OliverClothesoff.swc" output-folder="$MODULE_DIR$/bin-debug">')
    }

    @Test
    void should_be_only_one_content_url() {
        given_project_name_is("AmandaHuggenkiss")
        given_project_type_is("swc")
        given_src_dirs_is(['src'])
        when_I_create_project_config()
        then_the_iml_file_should_not_have_tag('<content url="file://$MODULE_DIR$"/>')

    }

    @Test
    void should_add_src_dirs() {
        given_project_name_is("AmandaHuggenkiss")
        given_src_dirs_is(['src', 'anotherSrc'])
        when_I_create_project_config()
        then_the_iml_file_should_have_tag('<sourceFolder url="file://$MODULE_DIR$/src" isTestSource="false"/>')
        then_the_iml_file_should_have_tag('<sourceFolder url="file://$MODULE_DIR$/anotherSrc" isTestSource="false"/>')
    }

    @Test
    void should_add_test_src_dirs() {
        given_project_name_is("AmandaHuggenkiss")
        given_test_src_dirs_is(['test', 'anotherTest'])
        when_I_create_project_config()
        then_the_iml_file_should_have_tag('<sourceFolder url="file://$MODULE_DIR$/test" isTestSource="true"/>')
        then_the_iml_file_should_have_tag('<sourceFolder url="file://$MODULE_DIR$/anotherTest" isTestSource="true"/>')
    }

    void given_project_name_is(String projectname) {
        File projectDir = new File(this.getClass().getResource("/stub-project-dir/intellij-dummy.xml").toURI())
        this.project = ProjectBuilder.builder().withProjectDir(projectDir.getParentFile()).withName(projectname).build()
        given_project_type_is("swc") //by default, type is required
        [       Configurations.INTERNAL_CONFIGURATION_NAME.configName(),
                Configurations.EXTERNAL_CONFIGURATION_NAME.configName(),
                Configurations.MERGE_CONFIGURATION_NAME.configName(),
                Configurations.RSL_CONFIGURATION_NAME.configName(),
                Configurations.THEME_CONFIGURATION_NAME.configName(),
                Configurations.TEST_CONFIGURATION_NAME.configName()
        ].each { project.configurations.create(it) }
    }

    void given_player_version_is(String version) {
        ideaProjectTask().flexConvention.playerVersion = version
    }

    void given_project_type_is(String type) {
        ideaProjectTask().flexConvention.type = type
    }

    void given_project_outputname_is(String outputname) {
        ideaProjectTask().flexConvention.output = outputname
    }

    void given_src_dirs_is(List<String> dirs) {
        ideaProjectTask().flexConvention.srcDirs = dirs
    }

    void given_test_src_dirs_is(List<String> dirs) {
        ideaProjectTask().flexConvention.testDirs = dirs
    }

    void when_I_create_project_config() {
        ideaProjectTask().createProjectConfig();
    }

    void then_an_iml_file_should_be_created_with_name(String filename) {
        File imlFile = project.file(filename)
        assertTrue(String.format("Iml-file %s was not created!", imlFile.absolutePath), imlFile.exists())
    }

    void then_the_iml_file_should_have_tag(String tag) {
        if (this.imlFileContent == null) {
            imlFileContent = project.file(project.name + ".iml").text
        }
        assertTrue(String.format("Could not find %s in %s", tag, imlFileContent), imlFileContent.contains(tag));
    }

    void then_the_iml_file_should_not_have_tag(String tag) {
        if (this.imlFileContent == null) {
            imlFileContent = project.file(project.name + ".iml").text
        }
        assertFalse(String.format("Could not find %s in %s", tag, imlFileContent), imlFileContent.contains(tag));
    }

    IdeaProject ideaProjectTask() {
        if (_ideaFxProjectTask == null) {
            _ideaFxProjectTask = project.tasks.create("ideafx", IdeaProject)
            GradleFxConvention pluginConvention = new GradleFxConvention(project)
            _ideaFxProjectTask.flexConvention = pluginConvention
            _ideaFxProjectTask.flexConvention.playerVersion = "11.5"
        }
        return _ideaFxProjectTask;
    }

}
