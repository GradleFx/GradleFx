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

import org.gradlefx.conventions.FlexType
import org.gradlefx.conventions.FrameworkLinkage


class FrameworkLinkageValidator extends AbstractProjectPropertyValidator {

    @Override
    public void execute() {
        if (flexConvention.usesFlex()) {
            if (isLinkageIncompatibleWithFlexType()) {
                addError("The frameworkLinkage '$flexConvention.frameworkLinkage' is incompatible with " +
                        "Flex project type '$flexConvention.type'")
            }
        }
        else {
            if (!flexConvention.frameworkLinkage.isCompilerDefault(flexConvention.type)) {
                addWarning('For non-flex projects the frameworkLinkage parameter has no effect')
            }
        }
    }
    
    /**
     * External linkage cannot be used in an application project; 
     * RSL linkage cannot be used in a library project.
     * 
     * @return Whether the selected {@link FrameworkLinkage} is incompatible with the selected {@link FlexType}
     */
    private boolean isLinkageIncompatibleWithFlexType() {
        FlexType type = flexConvention.type
        FrameworkLinkage linkage = flexConvention.frameworkLinkage
        
        return  (type.isLib() && linkage == FrameworkLinkage.rsl) || 
                (type.isApp() && linkage == FrameworkLinkage.external)
    }

}
