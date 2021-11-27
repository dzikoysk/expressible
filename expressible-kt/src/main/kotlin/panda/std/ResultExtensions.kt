package panda.std

fun <VALUE : Any, ERROR> VALUE.asSuccess(): Result<VALUE, ERROR> =
    Result.ok(this)

fun <VALUE, ERROR : Any> ERROR.asError(): Result<VALUE, ERROR> =
    Result.error(this)