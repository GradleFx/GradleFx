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

package org.gradlefx.validators.actions

import org.gradle.api.Action
import org.gradlefx.tasks.compile.Compc
import org.gradlefx.validators.CompcAdditionalPropertiesValidator
import org.gradlefx.validators.FlexSDKSpecifiedValidator
import org.gradlefx.validators.FrameworkLinkageValidator
import org.gradlefx.validators.RequiredProjectPropertiesValidator
import org.gradlefx.validators.runner.FailOnErrorValidatorRunner

class ValidateCompcTaskPropertiesAction implements Action<Compc> {

    void execute(Compc delegate) {
        new FailOnErrorValidatorRunner(delegate.task.project)
                .add(new RequiredProjectPropertiesValidator())
                .add(new FlexSDKSpecifiedValidator())
                .add(new FrameworkLinkageValidator())
                .add(new CompcAdditionalPropertiesValidator())
                .run()
    }
}
