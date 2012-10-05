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

package org.gradlefx.configuration.sdk

import org.gradle.api.Project
import org.gradle.api.internal.file.BaseDirFileResolver
import org.gradle.api.internal.file.FileResolver
import org.gradle.internal.nativeplatform.filesystem.FileSystems
import org.gradlefx.conventions.GradleFxConvention

import java.security.MessageDigest

class SdkInstallLocationFactory {

    private static final String SDKS_BASE_DIR_NAME = "sdks"

    File sdksInstallBaseDirectory

    SdkInstallLocationFactory(Project project) {
        File gradleFxUserHomeDir = ((GradleFxConvention) project.convention.plugins.flex).gradleFxUserHomeDir
        FileResolver sdksBaseDirResolver = new BaseDirFileResolver(FileSystems.default, gradleFxUserHomeDir)
        sdksInstallBaseDirectory = sdksBaseDirResolver.resolve(SDKS_BASE_DIR_NAME)
    }

    SdkInstallLocation createFromPackagedSdkFile(SdkType type, File packagedSdkFile) {
        FileResolver sdkTypeDirResolver = new BaseDirFileResolver(FileSystems.default, sdksInstallBaseDirectory)
        File sdkTypeDirectory = sdkTypeDirResolver.resolve(type.name().toLowerCase());

        FileResolver installBaseDirResolver = new BaseDirFileResolver(FileSystems.default, sdkTypeDirectory)
        String hashedSdkDirectoryName = fileToHash(packagedSdkFile)
        File sdkInstallDirectory = installBaseDirResolver.resolve(hashedSdkDirectoryName)

        SdkInstallLocation location = new SdkInstallLocation(type, sdkInstallDirectory)

        return location
    }

    private String fileToHash(File file) {
        def messageDigest = MessageDigest.getInstance("SHA1")
        messageDigest.update( file.absolutePath.getBytes() );

        return new BigInteger(1, messageDigest.digest()).toString(16).padLeft(40, '0')
    }
}
