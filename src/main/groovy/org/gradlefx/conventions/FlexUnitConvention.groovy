/*
* Copyright (c) 2011 the original author or authors
*
* Licensed under the Apache License Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http =//www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing software
* distributed under the License is distributed on an "AS IS" BASIS
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.gradlefx.conventions

import org.gradle.api.Project
import org.gradle.util.ConfigureUtil


/**
 * All FlexUnit conventions.
 * Documentation of all the options can be found here: http://docs.flexunit.org/index.php?title=Ant_Task
 */
class FlexUnitConvention {

    private String template //can be used for a custom template
    private String player           = 'flash'
    private String command          = System.getenv()['FLASH_PLAYER_EXE']
    private String toDir
    private String workingDir
    private Boolean haltOnFailure   = false
    private Boolean verbose         = false
    private Boolean localTrusted    = true
	private Boolean ignoreFailures  = true
    private int port                = 1024
    private int buffer              = 262144
    private int timeout             = 60000 //60 seconds
    private String failureProperty  = 'flexUnitFailed'
    private Boolean headless        = false
    private int display             = 99
    private List <String> includes  = ['**/*Test.as']
    private List <String> excludes  = []
    private String swfName          = 'TestRunner.swf'
    //list of additional compiler options as defined by the compc or mxmlc compiler
    private List <String> additionalCompilerOptions = []
    
    public FlexUnitConvention(Project project) {
        toDir       = "${project.buildDir}/reports"
        workingDir  = project.buildDir.path
    }

    void configure(Closure closure) {
        ConfigureUtil.configure(closure, this)
    }

    String getTemplate() {
        return template
    }

    void template(String template) {
        this.template = template
    }

    String getPlayer() {
        return player
    }

    void player(String player) {
        this.player = player
    }

    String getCommand() {
        return command
    }

    void command(String command) {
        this.command = command
    }

    String getToDir() {
        return toDir
    }

    void toDir(String toDir) {
        this.toDir = toDir
    }

    String getWorkingDir() {
        return workingDir
    }

    void workingDir(String workingDir) {
        this.workingDir = workingDir
    }

    Boolean getHaltOnFailure() {
        return haltOnFailure
    }

    void haltOnFailure(Boolean haltOnFailure) {
        this.haltOnFailure = haltOnFailure
    }

    Boolean getVerbose() {
        return verbose
    }

    void verbose(Boolean verbose) {
        this.verbose = verbose
    }

    Boolean getLocalTrusted() {
        return localTrusted
    }

    void localTrusted(Boolean localTrusted) {
        this.localTrusted = localTrusted
    }

	Boolean getIgnoreFailures() {
		return ignoreFailures
	}

	void ignoreFailures(Boolean ignoreFailures) {
		this.ignoreFailures = ignoreFailures
	}

    int getPort() {
        return port
    }

    void port(int port) {
        this.port = port
    }

    int getBuffer() {
        return buffer
    }

    void buffer(int buffer) {
        this.buffer = buffer
    }

    int getTimeout() {
        return timeout
    }

    void timeout(int timeout) {
        this.timeout = timeout
    }

    String getFailureProperty() {
        return failureProperty
    }

    void failureProperty(String failureProperty) {
        this.failureProperty = failureProperty
    }

    Boolean getHeadless() {
        return headless
    }

    void headless(Boolean headless) {
        this.headless = headless
    }

    int getDisplay() {
        return display
    }

    void display(int display) {
        this.display = display
    }

    List<String> getIncludes() {
        return includes
    }

    void includes(List<String> includes) {
        this.includes = includes
    }

    List<String> getExcludes() {
        return excludes
    }

    void excludes(List<String> excludes) {
        this.excludes = excludes
    }

    String getSwfName() {
        return swfName
    }

    void swfName(String name) {
        this.swfName = name
    }

    List<String> getAdditionalCompilerOptions() {
        return additionalCompilerOptions
    }

    void additionalCompilerOptions(List<String> options) {
        this.additionalCompilerOptions = options
    }
}
