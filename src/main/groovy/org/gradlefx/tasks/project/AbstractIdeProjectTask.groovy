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

package org.gradlefx.tasks.project

import org.gradlefx.configuration.Configurations;
import org.gradlefx.tasks.Tasks
import groovy.lang.Closure
import groovy.util.XmlParser
import groovy.xml.XmlUtil


abstract class AbstractIdeProjectTask extends AbstractProjectTask {
    
    /** The name of the targeted IDE */
    protected String ideName
    
    /**
     * Constructor
     * @param ideName The name of the targeted IDE
     */
    public AbstractIdeProjectTask(String ideName) {
        this.ideName = ideName
        description = "Generate $ideName project"
        
        dependsOn(Tasks.SKELETON_TASK_NAME)
    }
    
    @Override
    public void generateProject() {
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
     * @param path      The path to the XML file to edit
     * @param closure   A Closure in which to edit the XML data; takes the root node as an argument
     */
    protected void editXmlFile(String path, Closure closure) {
        def xml = new XmlParser().parse path
        
        closure xml
        
        toFile(path).withWriter { out ->
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
            Configurations.INTERNAL_CONFIGURATION_NAME,
            Configurations.EXTERNAL_CONFIGURATION_NAME,
            Configurations.MERGE_CONFIGURATION_NAME,
            Configurations.RSL_CONFIGURATION_NAME,
            Configurations.THEME_CONFIGURATION_NAME
        ].each() { type ->
            project.configurations[type].files.each() {
                closure it, type
            }
        }
    }

}
