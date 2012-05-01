package org.gradlefx.options

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
public enum CompilerOption {

    INCLUDE_FILE("-include-file"),
    INCLUDE_SOURCES("-include-sources"),
    INCLUDE_CLASSES("-include-classes"),
    INCLUDE_LIBRARIES("-include-libraries"),
    EXTERNAL_LIBRARY_PATH("-external-library-path"),
    LIBRARY_PATH("-library-path"),
    RUNTIME_SHARED_LIBRARY_PATH("-runtime-shared-library-path"),
    SOURCE_PATH("-source-path"),
    THEME("-theme"),
    OUTPUT("-output"),
    LOCALE("-locale");


    private String optionName;

    private CompilerOption(String optionName) {
        this.optionName = optionName;
    }

    String getOptionName() {
        return optionName
    }

    @Override
    String toString() {
        return optionName;
    }
}