package panda.std.reactive

import panda.std.reactive.Reference.Dependencies

/* Convert object to reference */

fun <T : Any> T.toReference(): Reference<T> =
    Reference.reference(this)

fun <T : Any> reference(value: T): Reference<T> =
    value.toReference()

fun <T : Any> T.toMutableReference(): MutableReference<T> =
    MutableReference.mutableReference(this)

fun <T : Any> mutableReference(value: T): MutableReference<T> =
    value.toMutableReference()

/**
 * Executes the given block whenever any of the given references have been updated
 */
fun <T : Any> computed(vararg references: Reference<*>, block: () -> T): Reference<T> =
    Reference.computed(Dependencies(*references), block)