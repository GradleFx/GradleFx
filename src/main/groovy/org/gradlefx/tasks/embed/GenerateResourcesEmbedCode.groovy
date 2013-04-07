package org.gradlefx.tasks.embed

import org.apache.commons.io.FilenameUtils
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.FileTreeElement
import org.gradle.api.tasks.TaskAction

import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author <a href="mailto:denis.rykovanov@gmail.com">Chaos Encoder</a>
 */
class GenerateResourcesEmbedCode extends DefaultTask {

    def GenerateResourcesEmbedCode() {
        description = 'generate embed code'
    }

    @TaskAction
    def action() {
        ConfigurableFileTree soundsFolder = project.fileTree('sounds')


        def srcFolder = project.file('src/main/actionscript')
        srcFolder.mkdirs();

        def embedRefClass = new File(srcFolder, 'EmbedSourceRefClass.as');

        Path embedClassPath = Paths.get(embedRefClass.parentFile.path)
        Path projectPath = Paths.get(project.projectDir.path)

        embedRefClass.withWriter { out ->
            out.println("package {")
            out.println("import flash.utils.Dictionary;")
            out.println("//generated code")
            out.println("\tpublic class EmbedSourceRefClass {")

            def classKeyRefMap = [:];

            soundsFolder.visit { FileTreeElement file ->
                Path soundFilePath = Paths.get(file.getFile().absolutePath)
                Path soundToEmbClassRelativePath = embedClassPath.relativize(soundFilePath);
                String resourcePath = soundToEmbClassRelativePath.toString().replace('\\', '/');

                //String soundFileProjRelPath = projectPath.relativize(soundFilePath).toString().replace('\\', '/');

                def soundFileBaseName = FilenameUtils.getBaseName(file.name);
                def classRefName = "_${soundFileBaseName}_classRef";
                classKeyRefMap.put(soundFileBaseName, classRefName)

                out.println("\t\t[Embed(source=\"${resourcePath}\")]")
                out.println("\t\tpublic static var ${classRefName}:Class;");
            }

            out.println("//mapping to local resources");
            out.println("\tpublic static var keys:Dictionary;");
            out.println("{");
            out.println("\t\tkeys = new Dictionary()");
            classKeyRefMap.each { key, value ->
                out.println("\t\tkeys[\"$key\"] = $value;")
            }
            out.println("}");

            out.println("\t}")
            out.println("}")
        }

    }
}
