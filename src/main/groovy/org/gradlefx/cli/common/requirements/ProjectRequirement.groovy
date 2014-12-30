package org.gradlefx.cli.common.requirements

import org.gradle.api.Project

/**
 * Requirement for implementing classes to provide a Project instance. Useful for traits and abstract classes where
 * you want to use a Project instance, but don't want to provide the implementation yet.
 */
interface ProjectRequirement {
    abstract Project getProject()
}
