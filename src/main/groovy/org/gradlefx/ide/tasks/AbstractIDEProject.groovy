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

import groovy.xml.XmlUtil
import org.gradle.api.AntBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction
import org.gradlefx.configuration.Configurations
import org.gradlefx.conventions.GradleFxConvention
import org.gradlefx.ide.validators.actions.ValidateIDEProjectTaskAction
import org.gradlefx.tasks.TaskGroups
import org.gradlefx.templates.tasks.Scaffold
import org.gradlefx.util.TemplateUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AbstractIDEProject extends DefaultTask implements ProjectTask, TemplateUtil {

    protected static final Logger LOG = LoggerFactory.getLogger 'gradlefx'

    /** Convention properties */
    protected GradleFxConvention flexConvention

    /** The name of the targeted IDE */
    protected String ideName

    /**
     * Constructor
     * @param ideName The name of the targeted IDE
     */
    public AbstractIDEProject(String ideName) {
        this.ideName = ideName
        group = TaskGroups.IDE
        description = "Generate $ideName project"

        ant.setLifecycleLogLevel(AntBuilder.AntMessagePriority.INFO)
        flexConvention = project.convention.plugins.flex

        dependsOn(Scaffold.NAME)
    }

    @Override
    @TaskAction
    public void generateProject() {
        new ValidateIDEProjectTaskAction().execute(this)

        LOG.info "Verifying project properties compatibility with $ideName"
        invalidateConventions()

        LOG.info "Creating $ideName project files"
        createProjectConfig()
    }

    /**
     *
     */
    abstract protected void invalidateConventions()

    /**
     * Creates the configuration files required by the targeted IDE
     */
    abstract protected void createProjectConfig()

    /**
     * Opens an XML file, allows you to edit it in a {@link Closure}, and writes the modified data to the file
     *
     * @param path The path to the XML file to edit
     * @param closure A Closure in which to edit the XML data; takes the root node as an argument
     */
    protected void editXmlFile(String path, Closure closure) {
        def xml = new XmlParser().parse project.projectDir.absolutePath + '/' + path

        closure xml

        project.file(path).withWriter { out ->
            XmlUtil.serialize xml, out
        }
    }

    /**
     * Loops over all dependency files and allows you to use them through a {@link Closure}
     *
     * @param closure A Closure in which to use the dependency file; takes the file and its {@link Configurations} as arguments
     */
    protected void eachDependencyFile(Closure closure) {
        [
                Configurations.INTERNAL_CONFIGURATION_NAME.configName(),
                Configurations.EXTERNAL_CONFIGURATION_NAME.configName(),
                Configurations.MERGE_CONFIGURATION_NAME.configName(),
                Configurations.RSL_CONFIGURATION_NAME.configName(),
                Configurations.THEME_CONFIGURATION_NAME.configName()
        ].each() { type ->
            project.configurations[type].files.each() {
                closure it, type
            }
        }
    }

}
