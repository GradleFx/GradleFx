package org.gradlefx.cli.common.optioninjectors

import groovy.util.slurpersupport.NodeChild
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ResolveException
import org.gradlefx.cli.compiler.CompilerOption
import org.gradlefx.cli.common.requirements.CompilerOptionsRequirement
import org.gradlefx.cli.common.requirements.GradleFxConventionRequirement
import org.gradlefx.cli.common.requirements.ProjectRequirement

/**
 * Trait providing various methods to inject rsl related compiler options.
 */
trait FlexFrameworkRslOptionsInjector implements ProjectRequirement, GradleFxConventionRequirement, CompilerOptionsRequirement {

    void removeFrameworkRslsFromRslLibraryPath() {
        //remove RSL's defined in flex-config.xml
        compilerOptions.reset CompilerOption.RUNTIME_SHARED_LIBRARY_PATH
    }

    void addFrameworkRslsToDefaultFrameworkLinkageLibraryPath() {
        //set the RSL's defined in *-config.xml on the library path default for the used framework linkage
        def flexConfig = new XmlSlurper().parse(flexConvention.configPath)
        def relativeSwcPaths = flexConfig['runtime-shared-library-path']['path-element']
        Collection<String> paths = relativeSwcPaths.collect { "$flexConvention.flexHome/frameworks/$it" }
        compilerOptions.addAll flexConvention.frameworkLinkage.getCompilerOption(), paths
    }

    void addFrameworkRsls() {
        if (flexConvention.useDebugRSLSwfs) {
            def flexConfig = new XmlSlurper().parse(flexConvention.configPath)
            flexConfig['runtime-shared-library-path'].each { NodeChild rslPathNode ->
                String swcName = "${flexConvention.flexHome}/frameworks/" + rslPathNode['path-element'].text()
                String libName = findRslLibNameInRuntimeSharedLibraryPath(rslPathNode)

                File dependency = new File(swcName)
                if (!dependency.exists()) {
                    Configuration rsl = project.configurations.rsl
                    String errorMsg = "Couldn't find the ${dependency.name} file - are you sure the path is correct?"
                    throw new ResolveException(rsl, new Throwable(errorMsg))
                } else {
                    compilerOptions.add CompilerOption.RUNTIME_SHARED_LIBRARY_PATH, "${dependency.path},${libName}"
                }
            }
        }
    }

    private static String findRslLibNameInRuntimeSharedLibraryPath(NodeChild runtimeSharedLibraryPathElement) {
        String primaryRslUrl = runtimeSharedLibraryPathElement['rsl-url'][0].text()
        if(primaryRslUrl.endsWith(".swf")) {
            //Apache version of the sdk detected which has no swz rsls anymore
            primaryRslUrl
        } else {
            //old version of the sdk which still supported swz rsls
            runtimeSharedLibraryPathElement['rsl-url'][1].text()[0..-2] + 'f'
        }
    }
}
