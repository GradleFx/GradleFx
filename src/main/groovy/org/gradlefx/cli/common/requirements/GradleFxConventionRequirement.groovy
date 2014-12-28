package org.gradlefx.cli.common.requirements

import org.gradlefx.conventions.GradleFxConvention

/**
 * Requirement for implementing classes to provide a GradleFxConvention instance. Useful for traits and abstract classes where
 * you want to use a GradleFxConvention instance, but don't want to provide the implementation yet.
 */
interface GradleFxConventionRequirement {
    abstract GradleFxConvention getFlexConvention()
}