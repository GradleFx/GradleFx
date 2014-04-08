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

package org.gradlefx.cli

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ResolveException
import org.gradlefx.configuration.Configurations
import org.gradlefx.configuration.sdk.SdkType
import org.gradlefx.conventions.FlexType
import org.gradlefx.conventions.FrameworkLinkage
import org.gradlefx.conventions.GradleFxConvention
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class CommandLineInstruction {

    protected static final Logger LOG = LoggerFactory.getLogger 'gradlefx'

    Project project
    GradleFxConvention flexConvention

    List arguments

    public CommandLineInstruction(Project project) {
        this.project = project
        flexConvention = project.convention.plugins.flex as GradleFxConvention
        arguments = []
    }

    abstract public void setConventionArguments()

    /** Adds the framework libraries to the arguments based on the {@link FrameworkLinkage} */
    public void addFramework() {
        FrameworkLinkage linkage = flexConvention.frameworkLinkage

        if (!linkage.usesFlex()) {
            //it's a pure AS project: we don't want to load the Flex configuration
            if (flexConvention.sdkTypes.contains(SdkType.Flex)) {
                reset CompilerOption.LOAD_CONFIG
            }

            //but we need to add playerglobal.swc to the build path explicitly
            def flexConfig = new XmlSlurper().parse(flexConvention.configPath)
            def relativeSwcPaths = flexConfig['compiler']['external-library-path']['path-element']

            def swcPathNode = relativeSwcPaths.find { it.text().contains 'playerglobal.swc' }
            add CompilerOption.EXTERNAL_LIBRARY_PATH, "$flexConvention.flexHome/frameworks/${swcPathNode.text()}"
        }
        //when FrameworkLinkage is the default for this compiler and it's not a swc, we don't have to do anything
        else if (!linkage.isCompilerDefault(flexConvention.type) || (flexConvention.type == FlexType.swc)) {
            //remove RSL's defined in config.xml
            reset CompilerOption.RUNTIME_SHARED_LIBRARY_PATH

            //set the RSL's defined in config.xml on the library path
            def flexConfig = new XmlSlurper().parse(flexConvention.configPath)
            def relativeSwcPaths = flexConfig['runtime-shared-library-path']['path-element']

            Collection<String> paths = relativeSwcPaths.collect { "$flexConvention.flexHome/frameworks/$it" }
            addAll linkage.getCompilerOption(), paths
        }
    }

    /** Adds the <code>-source-path</code> argument based on project.srcDirs and project.localeDir */
    public void addSourcePaths() {
        addAll CompilerOption.SOURCE_PATH, filterValidSourcePaths(flexConvention.srcDirs)

        //add locale path to source paths if any locales are defined
        if (flexConvention.locales && flexConvention.locales.size()) {
            add CompilerOption.SOURCE_PATH, project.file(flexConvention.localeDir).path + '/{locale}'
        }
    }

    protected Collection<String> filterValidSourcePaths(List<String> sourcePaths) {
        //don't allow non existing source paths unless they contain a token (e.g. {locale})
        //TODO {} tokens can be validated earlier: locale paths should be in localeDir property
        return sourcePaths
                .findAll { String path -> project.file(path).exists() || path.contains('{') }
                .collect { project.file(it).path }
    }

    /** Adds the <code>-locale</code> argument based on {@link GradleFxConvention}.locales */
    public void addLocales() {
        if (flexConvention.locales && flexConvention.locales.size()) {
            set CompilerOption.LOCALE, flexConvention.locales
        }
    }

    public void addInternalLibraries() {
        Configuration internal = project.configurations.internal
        addLibraries internal.files, internal, CompilerOption.INCLUDE_LIBRARIES
    }

    public void addExternalLibraries() {
        Configuration external = project.configurations.external
        addLibraries external.files, external, CompilerOption.EXTERNAL_LIBRARY_PATH
    }

    public void addMergedLibraries() {
        Configuration merged = project.configurations.merged
        addLibraries merged.files, merged, CompilerOption.LIBRARY_PATH
    }

    public void addTheme() {
        Configuration theme = project.configurations.theme
        addLibraries theme.files, theme, CompilerOption.THEME
    }

    /**
     * Adds all the dependencies for the given configuration as compile arguments
     *
     * @param libraryFiles
     * @param configuration
     * @param compilerOption
     */
    protected void addLibraries(Set<File> libraryFiles, Configuration configuration, CompilerOption compilerOption) {
        //only add swc dependencies, no use in adding pom dependencies
        Collection<File> files = libraryFiles.findAll {
            it.name.endsWith(FlexType.swc.toString()) || it.isDirectory()
        }
        validateFilesExist files, configuration

        Collection paths = files.collect { it.path }
        addAll compilerOption, paths
    }

    /** Adds the <code>-runtime-shared-library-path</code> argument based on RSL dependencies */
    public void addRSLs() {
        Configuration rsl = project.configurations.rsl
        validateFilesExist rsl.files, rsl

        Collection<String> paths = rsl.files.collect { "$it.path,${it.name[0..-2]}f" }
        addAll CompilerOption.RUNTIME_SHARED_LIBRARY_PATH, paths
    }

    def addFrameworkRsls() {
        if (flexConvention.useDebugRSLSwfs) {
            def flexConfig = new XmlSlurper().parse(flexConvention.configPath)
            flexConfig['runtime-shared-library-path'].each {
                String swcName = "${flexConvention.flexHome}/frameworks/" + it['path-element'].text()
                String libName = it['rsl-url'][1].text()[0..-2] + 'f'

                File dependency = new File(swcName)
                if (!dependency.exists()) {
                    Configuration rsl = project.configurations.rsl
                    String errorMsg = "Couldn't find the ${dependency.name} file - are you sure the path is correct?"
                    throw new ResolveException(rsl, new Throwable(errorMsg))
                } else {
                    add CompilerOption.RUNTIME_SHARED_LIBRARY_PATH, "${dependency.path},${libName}"
                }
            }
        }
    }


    private void validateFilesExist(Collection<File> files, Configuration configuration) {
        Collection<File> ghosts = files.findAll { !it.exists() }

        if (ghosts && ghosts.size()) {
            throw new ResolveException(configuration, new Throwable(
                    "Couldn't find some dependency files - are you sure the path is correct? " +
                            "Unresolved dependency paths: ${ghosts.collect { it.path }}"
            ))
        }
    }

    public void addAdditionalCompilerOptions() {
        addAll flexConvention.additionalCompilerOptions
    }

    protected void addOutput(String dir) {
        set CompilerOption.OUTPUT, project.file(dir).path
    }

    public void execute(AntBuilder ant, String antTask) {
        LOG.info "Compiling with $antTask"
        arguments.each {
            LOG.info "\t$it"
        }

        String antResultProperty = antTask + 'Result'
        String antOutputProperty = antTask + 'Output'

        ant.java(
                jar: "$flexConvention.flexHome/lib/${antTask}.jar",
                dir: "$flexConvention.flexHome/frameworks",
                fork: true,
                resultproperty: antResultProperty,
                outputproperty: antOutputProperty
        ) {
            flexConvention.jvmArguments.each {
                jvmarg(value: it)
            }

            arguments.each {
                arg(value: it)
            }
        }

        String output = ant.properties[antOutputProperty]
        if (ant.properties[antResultProperty] == '0') LOG.info "[$antTask] $output"
        else handleError antTask, output
    }

    protected void handleError(String antTask, String message) {
        if (message.contains("FlexUnitTestRunnerUI")) {
            message += '\n[GradleFx tip] Chances are you\'re using the FlashBuilder IDE.' +
                    '\nWhen you run unit tests from within FlashBuilder ' +
                    '(by selecting "Execute FlexUnit Tests" from the project\'s context menu), ' +
                    'it will automatically create a test application for you called FlexUnitApplication.mxml ' +
                    'and there\'s nothing you can change about that (short of not using the feature). ' +
                    'What\'s more, this application has lots of dependencies you haven\'t defined ' +
                    'in your build script, hence the compiler error.' +
                    '\nThe easiest way to fix this, is to add the following code to your build script:' +
                    '\n\n\tcompileFlex.doFirst {' +
                    '\n\t\tproject.file(srcDirs[0] + \'/FlexUnitApplication.mxml\').delete()' +
                    '\n\t}' +
                    '\n\nThis will delete the file prior to compiling and FlashBuilder will not complain, ' +
                    'since it will automatically regenerate it when needed.';
        }

        throw new Exception("$antTask execution failed: $message\n")
    }

    public void set(CompilerOption option, String value) {
        arguments.add "$option.optionName=$value"
    }

    public void set(CompilerOption option, Collection<String> values) {
        arguments.add "$option.optionName=${values.join(',')}"
    }

    public void reset(CompilerOption option) {
        arguments.add "$option.optionName="
    }

    public void add(CompilerOption option, String value) {
        arguments.add "$option.optionName+=$value"
    }

    public void add(CompilerOption option) {
        arguments.add option.optionName
    }

    public void add(String value) {
        arguments.add value
    }

    public void setAll(CompilerOption option, Collection<String> values) {
        values.each { set option, it }
    }

    public void addAll(CompilerOption option, Collection<String> values) {
        values.each { add option, it }
    }

    public void addAll(Collection<String> values) {
        arguments.addAll values
    }

}
