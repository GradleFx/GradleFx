package org.gradlefx.cli.common.tools

import org.gradlefx.cli.common.requirements.ProjectRequirement

/**
 * Allows to filter source files.
 */
trait SourceFileFilter implements ProjectRequirement {

    Collection<String> filterValidSourcePaths(List<String> sourcePaths) {
        //don't allow non existing source paths unless they contain a token (e.g. {locale})
        //TODO {} tokens can be validated earlier: locale paths should be in localeDir property
        return sourcePaths
                .findAll { String path -> project.file(path).exists() || path.contains('{') }
                .collect { project.file(it).path }
    }
}
