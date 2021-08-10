package panda.std

fun <VALUE, ERROR> VALUE.asSuccess(): Result<VALUE, ERROR> =
    Result.ok(this)

fun <VALUE, ERROR> ERROR.asError(): Result<VALUE, ERROR> =
    Result.error(this)