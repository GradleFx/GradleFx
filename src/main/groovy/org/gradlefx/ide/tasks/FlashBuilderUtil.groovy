package org.gradlefx.ide.tasks

import org.gradle.api.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class FlashBuilderUtil {
    public static final String eclipseProject = '.project'
    public static final String actionScriptProperties = '.actionScriptProperties'
    public static final String flexLibProperties = '.flexLibProperties'
    public static final String flexProperties = '.flexProperties'
    
    public String getOutputDir(Project project) {
        String outputDir = project.convention.plugins.flex.type.isLib() ? 'bin' : 'bin-debug';
        
        if (project.file(actionScriptProperties).exists()) {
            def props = new XmlSlurper().parse(project.projectDir.path + '/' + actionScriptProperties)
            outputDir = props.compiler.@outputFolderPath.text()
        }
        
        return outputDir
    }    

}
