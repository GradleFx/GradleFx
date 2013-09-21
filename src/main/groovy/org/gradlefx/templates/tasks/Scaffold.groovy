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

package org.gradlefx.templates.tasks

import org.gradlefx.tasks.TaskGroups
import org.gradlefx.templates.validators.actions.ValidateScaffoldTaskPropertiesAction

import java.io.InputStream;
import org.gradle.api.DefaultTask;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.tasks.TaskAction;
import org.gradlefx.conventions.GradleFxConvention;
import org.gradlefx.util.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Mixin(TemplateUtil)
class Scaffold extends DefaultTask {
    public static final String NAME = 'scaffold'

    protected static final Logger LOG = LoggerFactory.getLogger 'gradlefx'

    /** Convention properties */
    GradleFxConvention flexConvention

    /**
     * Constructor
     */
    public Scaffold() {
        group = TaskGroups.GENERATING
        description = "Generate IDE independent project scaffold"
        logging.setLevel LogLevel.INFO
        flexConvention = project.convention.plugins.flex
    }

    @TaskAction
    public void generateProject() {
        new ValidateScaffoldTaskPropertiesAction().execute(this)

        createDirectories()
        createMainClass()
    }

    /**
    * Creates physical directories for all the directories defined in the
    * conventions if they don't exist yet. Does not remove any previous
    * contents. Also a directory is created for each defined locale.
    */
   protected void createDirectories() {
       LOG.info "Creating directory structure"

       flexConvention.allSrcDirs.each {
           LOG.info "\t" + it
           project.file(it).mkdirs()
       }

       flexConvention.locales.each {
           String dir = flexConvention.localeDir + '/' + it
           LOG.info "\t" + dir
           project.file(dir).mkdirs()
       }
   }

   /**
    * Creates the main application file in the main srcDir only if it doesn't exist yet.
    * If it's in a package structure, that will be created too.
    * For AIR and mobile projects a descriptor file will also be created.
    */
   protected void createMainClass() {
       if (flexConvention.type.isLib()) return

       String relativePath = flexConvention.srcDirs[0] + '/' + flexConvention.mainClassPath
       File file = project.file relativePath
       boolean needsDescriptor = flexConvention.type.isNativeApp()

       if (file.exists()) LOG.info "Main class already exists"
       else {
           LOG.info "Creating main class" + (needsDescriptor ? ' and application descriptor' : '')

           file.getParentFile().mkdirs()
           writeContent getTemplate(false), file, false

           if (needsDescriptor) {
               file = project.file(flexConvention.air.applicationDescriptor)
               writeContent getTemplate(true), file, false
               LOG.info "\t" + flexConvention.air.applicationDescriptor
           }
       }

       LOG.info "\t" + relativePath
   }

   /**
    * Returns an {@link InputStream} which contains a content template for creating new files.
    * This template is chosen based on the selected project {@link FlexType} and {@link FrameworkLinkage}.
    *
    * @param descriptor Whether to return a descriptor template or not.
    * @return A file content template based on the selected {@link FlexType} and {@link FrameworkLinkage}
    */
   private InputStream getTemplate(boolean descriptor) {
       String extension = flexConvention.frameworkLinkage.usesFlex() ? 'mxml' : 'as'
       String path = "/templates/${flexConvention.type}.${extension}"

       if (descriptor) path = toDescriptorPath path
       return getClass().getResourceAsStream(path)
   }

}
