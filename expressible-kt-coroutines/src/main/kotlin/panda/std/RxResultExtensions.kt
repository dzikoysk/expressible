package panda.std

import panda.std.Result.ok

suspend fun <VALUE, ERROR, MAPPED> Result<VALUE, ERROR>.map(mapper: suspend (VALUE) -> MAPPED): Result<MAPPED, ERROR> =
    if (isOk) ok(mapper(get())) else projectToError()

suspend fun <VALUE, ERROR, MAPPED> Result<VALUE, ERROR>.flatMap(mapper: suspend (VALUE) -> Result<MAPPED, ERROR>): Result<MAPPED, ERROR> =
    if (isOk) mapper(get()) else projectToError()

suspend fun <VALUE, ERROR> Result<VALUE, ERROR>.peek(consumer: suspend (VALUE) -> Unit): Result<VALUE, ERROR> =
    if (isOk) also { consumer(get()) } else this

suspend fun <VALUE, ERROR> Result<VALUE, ERROR>.onError(consumer: suspend (ERROR) -> Unit): Result<VALUE, ERROR> =
    if (isErr) also { consumer(error) } else this