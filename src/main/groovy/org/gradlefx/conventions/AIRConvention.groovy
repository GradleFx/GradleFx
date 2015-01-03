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
import org.gradle.util.ConfigureUtil
import org.gradle.api.file.ConfigurableFileTree


/**
 * All the convention properties related the AIR.
 */
class AIRConvention {

    /**
     * The name of the certificate which will be used to sign the air package. Uses the project name by convention.
     */
    private String keystore
    /**
     * The password required to access the keystore.
     */
    private String storepass = null
    /**
     * The location of the air descriptor file. Uses the project name by convention for this file.
     */
    private String applicationDescriptor
    /**
     * A list of FileTree objects which reference the files to include into the AIR package, like application icons
     * which are specified in your application descriptor.
     * This is primarily to include individual files, it won't add directories.
     * Can have a value such as this: [fileTree(dir: ‘src/main/actionscript/’, include: ‘assets/appIcon.png’)]
     */
    private List<ConfigurableFileTree> includeFileTrees = null
    /**
     * Similar purpose as the includeFileTrees property, but allows you more freedom without the convenience of a
     * FileTree. It allows you to specify files/directories that should be included into the AIR package.
     *
     * This allows you to easily define directories to include into the AIR package, something which isn't possible
     * with the includeFileTrees option. This is sometimes necessary when having to include many files, in which case
     * the adt task would possibly complain that the command length is too long, something which can be sircumvented
     * by adding them as directories instead of as individual files.
     *
     * fileOptions = [
     *   '-C',
     *   'release/bin',
     *   'images',
     *   '-C',
     *   'src/main/resources',
     *   'assets'
     * ]
     */
    private List<String> fileOptions = []
    /**
     * The directory in which the adt packager will be executed. By default uses the project directory.
     */
    private String packageWorkDir;
    /**
     * The directory in the package where the output swf file will be placed, for example 'foo/bar'
     * By default the output swf is placed at the root of the package.
     */
    private String mainSwfDir;

    public AIRConvention(Project project) {
        keystore = "${project.name}.p12"
        applicationDescriptor = "src/main/actionscript/${project.name}.xml"
        packageWorkDir = project.projectDir.path;
    }

    void configure(Closure closure) {
        ConfigureUtil.configure(closure, this)
    }

    String getKeystore() {
        return keystore
    }

    void keystore(String keystore) {
        this.keystore = keystore
    }

    String getStorepass() {
        return storepass
    }

    void storepass(String storepass) {
        this.storepass = storepass
    }

    String getApplicationDescriptor() {
        return applicationDescriptor
    }

    void applicationDescriptor(String applicationDescriptor) {
        this.applicationDescriptor = applicationDescriptor
    }

    List<ConfigurableFileTree> getIncludeFileTrees() {
        return includeFileTrees
    }

    void includeFileTrees(List<ConfigurableFileTree> includeFileTrees) {
        this.includeFileTrees = includeFileTrees
    }

    List<String> getFileOptions() {
        return fileOptions
    }

    void fileOptions(List<String> fileOptions) {
        this.fileOptions = fileOptions
    }

    String getPackageWorkDir() {
        return packageWorkDir
    }

    void packageWorkDir(String packageWorkDir) {
        this.packageWorkDir = packageWorkDir
    }

    String getMainSwfDir() {
        return mainSwfDir
    }

    void mainSwfDir(String mainSwfDir) {
        this.mainSwfDir = mainSwfDir
    }

}
