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

package org.gradlefx.configuration.sdk.states.unpacking

import org.gradle.api.Project
import org.gradlefx.configuration.sdk.SdkInstallLocation

class SdkTbz2Unpacker extends AbstractSdkUnpacker {

    SdkTbz2Unpacker(Project project, SdkInstallLocation sdkInstallLocation, File packagedSdkFile, String someSdkRootDirectoryName) {
        super(project, sdkInstallLocation, packagedSdkFile, someSdkRootDirectoryName)
    }

    void unpackArchive() {
        if (execute([ "tar", "xf", packagedSdkFile.absolutePath ], sdkInstallLocation.directory))
            return

        project.ant.untar(src: packagedSdkFile.absolutePath, dest: sdkInstallLocation.directory.absolutePath, overwrite: "true", compression: "bzip2")
    }
}
