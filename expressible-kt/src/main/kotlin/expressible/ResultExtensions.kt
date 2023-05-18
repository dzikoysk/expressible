package expressible

fun <VALUE : Any, ERROR> VALUE.asSuccess(): expressible.Result<VALUE, ERROR> =
    expressible.Result.ok(this)

fun <VALUE, ERROR : Any> ERROR.asError(): expressible.Result<VALUE, ERROR> =
    expressible.Result.error(this)

fun <ERROR> expressible.Result<*, ERROR>.mapToUnit(): expressible.Result<Unit, ERROR> =
    map {}

fun <ERROR> ok(): expressible.Result<Unit, ERROR> =
    expressible.Result.ok(Unit)

@Deprecated("Inconsistent API method", ReplaceWith("Result.orThrow()"))
fun <ERROR : Exception> expressible.Result<*, ERROR>.orElseThrow() {
    orThrow { it }
}

fun <VALUE, ERROR : Exception> expressible.Result<VALUE, ERROR>.orThrow(): VALUE =
    orThrow { it }
