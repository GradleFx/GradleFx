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

package org.gradlefx.tasks.compile

import org.gradle.api.Task
import org.gradle.api.artifacts.ResolveException
import org.gradlefx.cli.CommandLineInstruction
import org.gradlefx.configuration.Configurations
import org.gradlefx.configuration.sdk.SdkType
import org.gradlefx.conventions.FrameworkLinkage
import org.gradlefx.tasks.Tasks
import org.gradlefx.validators.actions.ValidateMxmlcTaskPropertiesAction
import org.apache.commons.lang.StringUtils

class Mxmlc extends CompileTaskDelegate {

    public Mxmlc(Task task, CommandLineInstruction cli) {
        super(task, cli)
        task.description = 'Compiles Flex application/module (*.swf) using the mxmlc compiler'
        task.dependsOn Tasks.COPY_RESOURCES_TASK_NAME
    }

    void compileFlex() {
        new ValidateMxmlcTaskPropertiesAction().execute(this)

        cli.setConventionArguments()

        def taskName
        if (flexConvention.type.isAir()) {
            taskName = "mxmlc-cli";
        } else {
            taskName = "mxmlc";
        }
        cli.execute task.ant, taskName

        if (flexConvention.frameworkLinkage == FrameworkLinkage.rsl)
            copyFrameworkRSLs()
    }

    /**
     * Extracts the library swf's from the swc's and moves them to the build directory
     * in the location defined as failover RSL url.
     */
    private void copyFrameworkRSLs() {
        AntBuilder ant = new AntBuilder()
        ant.project.getBuildListeners()[0].setMessageOutputLevel(0)

        def flexConfig = new XmlSlurper().parse(flexConvention.configPath)

        flexConfig['runtime-shared-library-path'].each {
            String swcName = it['path-element'].text()
            File swc = new File("${flexConvention.flexHome}/frameworks/${swcName}")

            if (swc.exists()) {
                String rslUrl = StringUtils.isBlank(it['rsl-url'][1].text())? it['rsl-url'][0].text() : it['rsl-url'][1].text();
                String libName = flexConvention.useDebugRSLSwfs ? rslUrl[0..-2] + 'f' : rslUrl

                if(new File("${flexConvention.flexHome}/frameworks/rsls/${libName}").exists()) {
                    ant.copy(
                        file: "${flexConvention.flexHome}/frameworks/rsls/${libName}",
                        tofile:"${task.project.buildDir}/${libName}"
                    )
                }
            } else {
                throw new RuntimeException("Couldn't find the ${swc.name} file - are you sure the framework has all the files?")
            }
        }
    }

}
