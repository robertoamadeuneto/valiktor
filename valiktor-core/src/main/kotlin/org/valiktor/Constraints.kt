package org.valiktor

/**
 * Represents a validation constraint
 *
 * @author Rodolpho S. Couto
 * @since 0.1.0
 *
 */
interface Constraint {

    /**
     * Specifies the name of the constraint, generally it will be the name of the class
     */
    val name: String
        get() = this.javaClass.simpleName

    /**
     * Specifies the name of the key in the message properties file
     */
    val messageKey: String
        get() = "org.valiktor.constraints.${this.name}.message"

    /**
     * Specifies the interpolation function to replace variables in the message
     */
    val interpolator: (String) -> String
        get() = { it }
}