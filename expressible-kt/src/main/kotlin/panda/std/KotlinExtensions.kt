package panda.std

import java.util.concurrent.atomic.AtomicBoolean

fun <T> T.letIf(condition: Boolean, block: (T) -> T) =
    if (condition) block(this) else this

fun <T> T.letIf(condition: (T) -> Boolean, block: (T) -> T) =
    if (condition(this)) block(this) else this

fun AtomicBoolean.peek(block: () -> Unit) {
    if (this.get()) {
        block()
    }
}

fun <T, R> Iterable<T>.firstAndMap(transform: (T) -> R): R? =
    this.firstOrNull()?.let(transform)

fun <T> take(condition: Boolean, ifTrue: T, ifFalse: T): T =
    if (condition) ifTrue else ifFalse