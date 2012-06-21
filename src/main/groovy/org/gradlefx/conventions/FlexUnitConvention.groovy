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


class FlexUnitConvention {
    
    String player           = 'flash'
    String command          = System.getenv()['FLASH_PLAYER_EXE']
    String toDir
    String workingDir
    Boolean haltOnFailure   = false
    Boolean verbose         = false
    Boolean localTrusted    = true
    int port                = 1024
    int buffer              = 262144
    int timeout             = 60000 //60 seconds
    String failureProperty  = 'flexUnitFailed'
    Boolean headless        = false
    int display             = 99
    List <String> includes  = ['**/*Test.as']
    List <String> excludes  = []
    
    public FlexUnitConvention(Project project) {
        toDir       = "${project.buildDir}/reports"
        workingDir  = project.buildDir.path
    }
    
}
