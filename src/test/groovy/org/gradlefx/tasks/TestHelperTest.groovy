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

package org.gradlefx.util

import spock.lang.Specification
import org.gradlefx.tasks.TestHelper

class TestHelperTest extends Specification {

    TestHelper testHelper

    def setup() {
        testHelper = new TestHelper()
    }

    def "convertPathStringToFullyQualifiedClassname parse .as path that does not contain package that starts with 'as' or 'mxml'"() {
        setup:
            String path = 'myproject/package/MyClass.as'
        when:
            String fullQualifiedClass = testHelper.convertPathStringToFullyQualifiedClassname(path)
        then:
            fullQualifiedClass == 'myproject.package.MyClass'
    }

    def "convertPathStringToFullyQualifiedClassname parse .mxml path that does not contain package that starts with 'as' or 'mxml'"() {
        setup:
            String path = 'myproject/package/MyView.mxml'
        when:
            String fullQualifiedClass = testHelper.convertPathStringToFullyQualifiedClassname(path)
        then:
            fullQualifiedClass == 'myproject.package.MyView'
    }

    def "convertPathStringToFullyQualifiedClassname parse .as path that contains package that starts with 'as'"() {
        setup:
            String path = 'myproject/asmallpackage/MyClass.as'
        when:
            String fullQualifiedClass = testHelper.convertPathStringToFullyQualifiedClassname(path)
        then:
            fullQualifiedClass == 'myproject.asmallpackage.MyClass'
    }

    def "convertPathStringToFullyQualifiedClassname parse .mxml path that contains package that starts with 'as'"() {
        setup:
            String path = 'myproject/asmallpackage/MyView.mxml'
        when:
            String fullQualifiedClass = testHelper.convertPathStringToFullyQualifiedClassname(path)
        then:
            fullQualifiedClass == 'myproject.asmallpackage.MyView'
    }

    def "convertPathStringToFullyQualifiedClassname parse .as path that contains package that starts with 'mxml'"() {
        setup:
            String path = 'myproject/mxmlviews/MyClass.as'
        when:
            String fullQualifiedClass = testHelper.convertPathStringToFullyQualifiedClassname(path)
        then:
            fullQualifiedClass == 'myproject.mxmlviews.MyClass'
    }

    def "convertPathStringToFullyQualifiedClassname parse .mxml path that contains package that starts with 'mxml'"() {
        setup:
            String path = 'myproject/mxmlviews/MyView.mxml'
        when:
            String fullQualifiedClass = testHelper.convertPathStringToFullyQualifiedClassname(path)
        then:
            fullQualifiedClass == 'myproject.mxmlviews.MyView'
    }
}
