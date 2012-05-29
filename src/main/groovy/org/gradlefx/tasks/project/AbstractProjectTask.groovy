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

import static java.util.UUID.randomUUID
import groovy.lang.Closure
import java.io.File
import java.io.InputStream
import java.util.List
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction
import org.gradlefx.conventions.GradleFxConvention
import org.slf4j.Logger
import org.slf4j.LoggerFactory


abstract class AbstractProjectTask extends DefaultTask implements ProjectTask {
    
    protected static final Logger LOG = LoggerFactory.getLogger 'gradlefx'
    
    GradleFxConvention flexConvention
    
    
    /**
     * Constructor
     */
    protected AbstractProjectTask() {
        logging.setLevel LogLevel.INFO
        flexConvention = project.convention.plugins.flex
    }
    
    @TaskAction
    abstract public void generateProject()
    
    /**
     * Copies the content from a template file into a target file.
     * In this process the following tokens are being replaced:
     * <ul>
     *  <li>${class}: the main class' name</li>
     *  <li>${package}: the main class' package</li>
     *  <li>${project}: the project name</li>
     *  <li>${mainClass}: the main class' path</li>
     *  <li>${compilerArgs}: additional compiler arguments</li>
     *  <li>${mainSrc}: the main source folder</li>
     *  <li>${playerVersion}: the targert Flash player version</li>
     *  <li>${uuid}: a generated unique identifier</li>
     *  <li>${applicationId}: a generated air/mobile application identifier</li>
     *  <li>${version}: the application's version number</li>
     * </ul>
     * 
     * @param source    The template on which the result will be based
     * @param target    The File that will be written
     * @param overwrite If the file already exists, do we replace it or leave it untouched?
     */
    protected void writeContent(InputStream source, File target, boolean overwrite) {
        if (overwrite && target.exists()) target.delete()
        target.createNewFile()
        
        target.withWriter { out ->
            source.eachLine {
                out.println it.replaceAll(/\$\{class\}/, flexConvention.className)
                              .replaceAll(/\$\{package\}/, flexConvention.packageName)
                              .replaceAll(/\$\{project\}/, project.name)
                              .replaceAll(/\$\{mainClass\}/, flexConvention.mainClassPath)
                              .replaceAll(/\$\{compilerArgs\}/, flexConvention.compilerArgs.join('&#13;&#10;'))
                              .replaceAll(/\$\{mainSrc\}/, flexConvention.srcDirs[0])
                              .replaceAll(/\$\{playerVersion\}/, flexConvention.playerVersion)
                              .replaceAll(/\$\{uuid\}/, flexConvention.uuid)
                              .replaceAll(/\$\{appId\}/, flexConvention.applicationId)
                              .replaceAll(/\$\{version\}/, flexConvention.version)
            }
        }
    }
    
    /**
     * Distills a class name from a {@link File}'s name
     * 
     * @param file  The File we want a class name from
     * @return A class name
     */
    private String toClassName(File file) {
        return file.name.replaceAll(/\.(mxml|as)$/, '') 
    }
    
    /**
     * Distills a package name from a {@link File}'s parent directory
     * 
     * @param file  The File we want a package name from
     * @return A package name
     */
    private String toPackage(File file) {
        return file.parent ? file.parent.replaceAll(/\//, '.').replaceAll('\\\\', '.') : ''
    }
    
    /**
     * Creates an AIR descriptor compatible version (format <0-999>.<0-999>.<0-999>) number 
     * from any kind of version string
     * 
     * @param version The original version string
     * @return The version string formatted according to the <0-999>.<0-999>.<0-999> pattern
     */
    private String toVersionNumber(String version) {
        return !version || version == 'unspecified' ? '0.0.0' : version.replaceAll(/[^\.\d]/, '')
    }
    
    /**
     * Creates a {@link File} from a given relative path that can be used in multi-projects
     * 
     * @param relativePath  The relative path we want a File reference for
     * @return A File reference based on an absolute path
     */
    protected File toFile(String relativePath) {
        return new File(project.projectDir.absolutePath + '/' + relativePath)
    }

}
