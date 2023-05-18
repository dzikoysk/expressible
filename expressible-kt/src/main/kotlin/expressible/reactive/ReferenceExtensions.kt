package expressible.reactive

import expressible.reactive.Reference.Dependencies.dependencies

/* Convert object to reference */

fun <T : Any> T.toReference(): expressible.reactive.Reference<T> =
    expressible.reactive.Reference.reference(this)

fun <T : Any> reference(value: T): expressible.reactive.Reference<T> =
    value.toReference()

fun <T : Any> T.toMutableReference(): expressible.reactive.MutableReference<T> =
    expressible.reactive.MutableReference.mutableReference(this)

fun <T : Any> mutableReference(value: T): expressible.reactive.MutableReference<T> =
    value.toMutableReference()

/**
 * Executes the given block whenever any of the given references have been updated
 */
fun <T : Any> computed(vararg references: expressible.reactive.Reference<*>, block: () -> T): expressible.reactive.Reference<T> =
    expressible.reactive.Reference.computed(dependencies(*references), block)