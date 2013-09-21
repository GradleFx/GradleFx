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

package org.gradlefx.plugins

import org.gradle.api.Project
import org.gradle.api.artifacts.PublishArtifact
import org.gradle.api.artifacts.dsl.ArtifactHandler
import org.gradle.api.internal.artifacts.publish.DefaultPublishArtifact
import org.gradlefx.configuration.Configurations
import org.gradlefx.configuration.FlexAntTasksConfigurator
import org.gradlefx.configuration.sdk.DefaultSdkInitialisationContext
import org.gradlefx.configuration.sdk.states.air.DetermineAirSdkDeclarationTypeState
import org.gradlefx.configuration.sdk.states.flex.DetermineFlexSdkDeclarationTypeState
import org.gradlefx.tasks.*
import org.gradlefx.tasks.adl.AdlTask
import org.gradlefx.tasks.adt.AdtTask
import org.gradlefx.tasks.compile.Compile
import org.gradlefx.tasks.mobile.InstallApp
import org.gradlefx.tasks.mobile.InstallSimulatorApp
import org.gradlefx.tasks.mobile.LaunchApp
import org.gradlefx.tasks.mobile.LaunchSimulatorApp
import org.gradlefx.tasks.mobile.ReleaseAirMobilePackage
import org.gradlefx.tasks.mobile.SimulatorAirMobilePackage
import org.gradlefx.tasks.mobile.UninstallApp
import org.gradlefx.tasks.mobile.UninstallSimulatorApp

class GradleFxPlugin extends AbstractGradleFxPlugin {

    @Override
    protected void addTasks() {
        //generic tasks
        addTask Tasks.BUILD_TASK_NAME, Build
        addTask Tasks.COPY_RESOURCES_TASK_NAME, CopyResources
        addTask Tasks.COPY_TEST_RESOURCES_TASK_NAME, CopyTestResources
        addTask Tasks.PUBLISH_TASK_NAME, Publish
        addTask Tasks.TEST_TASK_NAME, Test
        addTask Tasks.COMPILE_TASK_NAME, Compile
        addTask Tasks.CLEAN_SDKS, CleanSdks

        //conditional tasks
        addTask Tasks.ASDOC_TASK_NAME, ASDoc, { flexConvention.type?.isLib() }
        addTask Tasks.PACKAGE_TASK_NAME, AirPackage, { flexConvention.type?.isNativeApp() }
        addTask Tasks.PACKAGE_MOBILE_TASK_NAME, ReleaseAirMobilePackage, { flexConvention.type?.isMobile() }
        addTask Tasks.PACKAGE_SIMULATOR_MOBILE_TASK_NAME, SimulatorAirMobilePackage, { flexConvention.type?.isMobile() }
        addTask Tasks.INSTALL_MOBILE_TASK_NAME, InstallApp, { flexConvention.type?.isMobile() }
        addTask Tasks.INSTALL_SIMULATOR_MOBILE_TASK_NAME, InstallSimulatorApp, { flexConvention.type?.isMobile() }
        addTask Tasks.UNINSTALL_MOBILE_TASK_NAME, UninstallApp, { flexConvention.type?.isMobile() }
        addTask Tasks.UNINSTALL_SIMULATOR_MOBILE_TASK_NAME, UninstallSimulatorApp, { flexConvention.type?.isMobile() }
        addTask Tasks.LAUNCH_MOBILE_TASK_NAME, LaunchApp, { flexConvention.type?.isMobile() }
        addTask Tasks.LAUNCH_SIMULATOR_MOBILE_TASK_NAME, LaunchSimulatorApp, { flexConvention.type?.isMobile() }
        addTask Tasks.LAUNCH_ADL_TASK_NAME, AdlTask, { flexConvention.type?.isNativeApp() }
        addTask Tasks.CREATE_HTML_WRAPPER, HtmlWrapper, { flexConvention.type?.isWebApp() }
    }

    @Override
    protected void configure(Project project) {
        initializeSDKs()

        project.gradle.taskGraph.whenReady {
            if (!isCleanSdksGoingToRun()) {
                new FlexAntTasksConfigurator(project).configure()
            }
        }

        if (!flexConvention.type?.isNativeApp())
            addArtifactsToDefaultConfiguration project
    }

    private Boolean isCleanSdksGoingToRun() {
        project.gradle.taskGraph.hasTask((CleanSdks) project[Tasks.CLEAN_SDKS]);
    }

    private void initializeSDKs() {
        new DefaultSdkInitialisationContext(project, new DetermineFlexSdkDeclarationTypeState()).initSdk()
        new DefaultSdkInitialisationContext(project, new DetermineAirSdkDeclarationTypeState()).initSdk()
    }

    /**
     * Adds artifacts to the default configuration
     * @param project
     */
    private void addArtifactsToDefaultConfiguration(Project project) {
        String type = flexConvention.type.toString()
        File artifactFile = project.file project.buildDir.name + "/" + flexConvention.output + "." + type
        PublishArtifact artifact = new DefaultPublishArtifact(project.name, type, type, null, new Date(), artifactFile)

        project.artifacts { ArtifactHandler artifactHandler ->
            Configurations.ARTIFACT_CONFIGURATIONS.each { Configurations configuration ->
                String configName = configuration.configName()
                artifactHandler."$configName" artifact
            }
        }
    }

}
