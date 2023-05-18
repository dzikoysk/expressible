package expressible

fun <VALUE : Any, ERROR> VALUE.asOk(): Result<VALUE, ERROR> =
    Result.ok(this)

fun <VALUE, ERROR : Any> ERROR.asError(): Result<VALUE, ERROR> =
    Result.error(this)

fun <ERROR> Result<*, ERROR>.mapToUnit(): Result<Unit, ERROR> =
    map {}

fun <ERROR> ok(): Result<Unit, ERROR> =
    Unit.asOk()

fun <VALUE, ERROR : Exception> Result<VALUE, ERROR>.orThrow(): VALUE =
    orThrow { it }
