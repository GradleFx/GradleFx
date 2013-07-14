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

package org.gradlefx.tasks.embed

import org.apache.commons.io.FilenameUtils
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.FileTreeElement
import org.gradle.api.tasks.TaskAction
import org.gradlefx.conventions.GradleFxConvention
import org.gradlefx.conventions.embed.EmbedConvention

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
        def embedConvention = (EmbedConvention) project.convention.plugins.embedFlexResources

        def srcFolder = project.file('src/main/actionscript')
        srcFolder.mkdirs();

        embedConvention.embedRefClasses.each { embedRefInfo ->

            def embedRefClass = new File(srcFolder,  "${embedRefInfo.name}.as");


            Path embedClassPath = Paths.get(embedRefClass.parentFile.path)
            Path projectPath = Paths.get(project.projectDir.path)

            embedRefClass.withWriter { out ->
                out.println("package {")
                out.println("import flash.utils.Dictionary;")
                out.println("//generated code")
                out.println("\tpublic class ${embedRefInfo.name} {")

                def classKeyRefMap = [:];

                embedRefInfo.sources.each { ConfigurableFileTree resourceSrcFolder ->

                    resourceSrcFolder.visit { FileTreeElement file ->
                        if (file.directory) {
                            return;
                        }

                        Path resourceFilePath = Paths.get(file.getFile().absolutePath)
                        Path resourceToEmbClassRelativePath = embedClassPath.relativize(resourceFilePath);
                        String resourcePath = resourceToEmbClassRelativePath.toString().replace('\\', '/');

                        String resourceFileProjRelPath = projectPath.relativize(resourceFilePath).toString().replace('\\', '/');

                        def resourceFileBaseName = file.path.replaceAll(/(\.)|(\/)|(\-)/, '_');
                        def classRefName = "_${resourceFileBaseName}_classRef";
                        def key =  FilenameUtils.removeExtension(resourceFileProjRelPath)
                        classKeyRefMap.put(key, classRefName)

                        if (FilenameUtils.getExtension(file.file.path) != 'xml') {
                            out.println("\t\t[Embed(source=\"${resourcePath}\")]")
                        } else {
                            out.println("\t\t[Embed(source=\"${resourcePath}\", mimeType=\"application/octet-stream\")]")
                        }

                        out.println("\t\tpublic static var ${classRefName}:Class;");
                    }

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
}
