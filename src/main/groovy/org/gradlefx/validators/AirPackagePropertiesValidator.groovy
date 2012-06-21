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

package org.gradlefx.validators

import org.gradlefx.conventions.AIRConvention

class AirPackagePropertiesValidator extends AbstractProjectPropertyValidator {
    
    void execute() {
        validateApplicationDescriptorSpecified()
        validateRequiredSigningOptionsSpecified()
    }

    private void validateApplicationDescriptorSpecified() {
        AIRConvention air = flexConvention.air
        if (air.applicationDescriptor == null || !project.file(air.applicationDescriptor).exists()) {
            addError "No Application descriptor has been specified. " + 
                "This can be done as follows -> air.applicationDescriptor: '/src/main/flex/airdescriptor.xml'"
        }
    }

    private void validateRequiredSigningOptionsSpecified() {
        if (flexConvention.air.keystore == null) {
            addError "A certificate needs to be specified. " +
                "This can be done as follows -> air.keystore: 'certificate.p12'"
        }
    }
}
