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

/**
 * All convention properties related to the HTML wrapper generation.
 */
class HtmlWrapperConvention {

    /**
     * Sets the value of the <title> tag in the head of the HTML page.
     */
    private String title
    /**
     * Sets the file name of the HTML output file. The default value is "{project.name}.html".
     */
    private String file
    /**
     * The width percentage the wrapper will use.
     */
    private int percentWidth  = 100
    /**
     * The height percentage the wrapper will use.
     */
    private int percentHeight = 100
    /**
     * The name of the SWF object in the HTML wrapper. You use this name to refer to the SWF object in JavaScript or
     * when using the ExternalInterface API. This value should not contain any spaces or special characters.
     * This attribute sets the value of the <embed> tag's name attribute and the <object> tag's id attribute.
     * Defaults to the project name.
     */
    private String application
    /**
     * Sets the name of the SWF file that the HTML wrapper embeds (for example, Main). Do not include the *.swf extension;
     * the extension is appended to the name for you.
     * This attribute sets the value of the attributes.name and attributes.id arguments in the embedSWF() method.
     * It also sets the value of the first argument in that method call.
     * Defaults to the project name.
     */
    private String swf
    /**
     * Set to true to include deep linking support (also referred to as history management) in the HTML wrapper.
     * Set to false to exclude deep linking from the wrapper.
     * When you set this attribute to true, it creates a history directory and stores the historyFrame.html, history.css, and history.js files in it.
     * The default value is false.
     */
    private Boolean history          = true
    /**
     * Determines whether to include Express Install logic in the HTML wrapper. When set to true, it copies the
     * playerProductInstall.swf file to the output directory and referenced in the wrapper file.
     * If you set this option to true, the versionDetection option is also assumed to be true.
     */
    private Boolean expressInstall   = true
    /**
     * Determines whether to include version detection logic in the wrapper. Set this value to false to disable all
     * Player version logic in the HTML wrapper.
     * The default value is true.
     */
    private Boolean versionDetection = true
    /**
     * Sets the directory that it writes the generated files to.
     * Defaults to the build directory.
     */
    private String output
    /**
     * The relative path to a custom html template
     */
    private String source
    /**
     * A map of tokens which will be replaced in your custom template.
     * The keys have to be specified as ${key} in your template
     */
    private Map tokenReplacements = [:]
    
    public HtmlWrapperConvention(Project project) {
        title       = project.description
        file        = "${project.name}.html"
        application = project.name
        swf         = project.name
        output      = project.buildDir.path
    }

    void configure(Closure closure) {
        ConfigureUtil.configure(closure, this)
    }

    String getTitle() {
        return title
    }

    void title(String title) {
        this.title = title
    }

    String getFile() {
        return file
    }

    void file(String file) {
        this.file = file
    }

    int getPercentWidth() {
        return percentWidth
    }

    void percentWidth(int percentWidth) {
        this.percentWidth = percentWidth
    }

    int getPercentHeight() {
        return percentHeight
    }

    void percentHeight(int percentHeight) {
        this.percentHeight = percentHeight
    }

    String getApplication() {
        return application
    }

    void application(String application) {
        this.application = application
    }

    String getSwf() {
        return swf
    }

    void swf(String swf) {
        this.swf = swf
    }

    Boolean getHistory() {
        return history
    }

    void history(Boolean history) {
        this.history = history
    }

    Boolean getExpressInstall() {
        return expressInstall
    }

    void expressInstall(Boolean expressInstall) {
        this.expressInstall = expressInstall
    }

    Boolean getVersionDetection() {
        return versionDetection
    }

    void versionDetection(Boolean versionDetection) {
        this.versionDetection = versionDetection
    }

    String getOutput() {
        return output
    }

    void output(String output) {
        this.output = output
    }

    void output(File output) {
        this.output = output
    }

    String getSource() {
        return source
    }

    void source(String source) {
        this.source = source
    }
    
    void source(File source) {
        this.source = source
    }
    
    Map getTokenReplacements() {
        return tokenReplacements
    }

    void tokenReplacements(Map tokenReplacements) {
        this.tokenReplacements = tokenReplacements
    }
}
