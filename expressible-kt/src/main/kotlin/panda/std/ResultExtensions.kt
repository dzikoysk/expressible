package panda.std

fun <VALUE : Any, ERROR> VALUE.asSuccess(): Result<VALUE, ERROR> =
    Result.ok(this)

fun <VALUE, ERROR : Any> ERROR.asError(): Result<VALUE, ERROR> =
    Result.error(this)

fun <ERROR> Result<*, ERROR>.mapToUnit(): Result<Unit, ERROR> =
    map {}

fun <ERROR> ok(): Result<Unit, ERROR> =
    Result.ok(Unit)

@Deprecated("Inconsistent API method", ReplaceWith("Result.orThrow()"))
fun <ERROR : Exception> Result<*, ERROR>.orElseThrow() {
    orThrow { it }
}

fun <VALUE, ERROR : Exception> Result<VALUE, ERROR>.orThrow(): VALUE =
    orThrow { it }