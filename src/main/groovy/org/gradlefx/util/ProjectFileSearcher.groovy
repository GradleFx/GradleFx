package org.gradlefx.util

import org.gradle.api.Project

/**
 * Allows to search a project for certain files.
 */
class ProjectFileSearcher {

    private Project project

    ProjectFileSearcher(Project project) {
        this.project = project
    }

    File findFile(dirs, fileName) {
        for (String dirName : dirs) {
            File dir = project.file dirName
            File desiredFile = new File(dir, fileName)
            if (desiredFile.exists()) {
                return desiredFile
            }
        }

        throw new Exception(
                "The file $fileName couldn't be found in directories $dirs; " +
                        "note that if you used the 'flashbuilder' plugin this file may have been moved " +
                        "to comply to FlashBuilder's restrictions (execute 'flashbuilder' and see if you get any warnings); " +
                        "consider editing the 'mainClass' property or switching to a decent IDE"
        )
    }
}
