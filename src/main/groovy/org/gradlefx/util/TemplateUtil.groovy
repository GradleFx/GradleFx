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

package org.gradlefx.util

import java.io.File;
import java.io.InputStream;


class TemplateUtil {

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
    *  <li>${playerVersion}: the target Flash player version</li>
    *  <li>${uuid}: a generated unique identifier</li>
    *  <li>${applicationId}: a generated air/mobile application identifier</li>
    *  <li>${version}: the application's version number</li>
    * </ul>
    *
    * @param source    The template on which the result will be based
    * @param target    The File that will be written
    * @param overwrite If the file already exists, do we replace it or leave it untouched?
    */
   public void writeContent(InputStream source, File target, boolean overwrite) {
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
     * Converts the path of the main class in to that of a descriptor file
     *
     * @param mainClassPath The path of the main class
     * @return The path of the descriptor file
     */
    public String toDescriptorPath(String mainClassPath) {
        return mainClassPath.replaceAll(/\.(mxml|as)$/, '-app.xml')
    }
   
}
