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

import java.util.List;

class Amxmlc extends Mxmlc {

     @Override
     protected List createCompilerArguments() {
         List arguments = super.createCompilerArguments()
         arguments.add(0,"+configname=air")
         return arguments
     }
     
     @Override
     protected void addPlayerLibrary(List compilerArguments) {
         String libPath = "${project.flexHome}/frameworks/libs/air/airglobal.swc"
         compilerArguments.add("${CompilerOption.EXTERNAL_LIBRARY_PATH}+=${libPath}");
     }
     
     @Override
     protected boolean canAddLibrary(String libPath) {
         return !libPath.matches(/^.*(\/|\\)(player)(\/|\\).*$/)
     }
     
}
