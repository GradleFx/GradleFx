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

class HtmlWrapperConvention {
    
    private String title
    private String file
    private int percentWidth  = 100
    private int percentHeight = 100
    private String application
    private String swf
    private Boolean history          = true
    private Boolean expressInstall   = true
    private Boolean versionDetection = true
    private String output
    
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
}
