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

import org.gradlefx.cli.CommandLineInstruction;
import org.gradlefx.conventions.GradleFxConvention
import org.gradle.api.Task;

abstract class CompileTaskDelegate implements CompileTask {

    Task task
    GradleFxConvention flexConvention
    CommandLineInstruction cli

    protected CompileTaskDelegate(Task task, CommandLineInstruction cli) {
        this.task = task
        this.cli = cli
        flexConvention = task.project.convention.plugins.flex
    }

}
