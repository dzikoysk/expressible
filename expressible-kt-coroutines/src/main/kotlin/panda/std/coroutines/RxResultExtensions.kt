package panda.std.coroutines

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import panda.std.Result
import panda.std.Result.ok

suspend fun <VALUE, ERROR, MAPPED> Result<VALUE, ERROR>.rxMap(mapper: suspend (VALUE) -> MAPPED): Result<MAPPED, ERROR> =
    if (isOk) ok(mapper(get())) else projectToError()

suspend fun <VALUE, ERROR, MAPPED> Result<VALUE, ERROR>.rxFlatMap(mapper: suspend (VALUE) -> Result<MAPPED, ERROR>): Result<MAPPED, ERROR> =
    if (isOk) mapper(get()) else projectToError()

suspend fun <VALUE, ERROR> Result<VALUE, ERROR>.rxPeek(consumer: suspend (VALUE) -> Unit): Result<VALUE, ERROR> =
    if (isOk) also { consumer(get()) } else this

suspend fun <VALUE, ERROR> Result<VALUE, ERROR>.rxOnError(consumer: suspend (ERROR) -> Unit): Result<VALUE, ERROR> =
    if (isErr) also { consumer(error) } else this

suspend fun <VALUE, ERROR, MAPPED_ERROR> Flow<Result<out VALUE, ERROR>>.firstSuccessOr(elseValue: suspend () -> Result<out VALUE, MAPPED_ERROR>): Result<out VALUE, MAPPED_ERROR> =
    this.firstOrNull { it.isOk }
        ?.projectToValue()
        ?: elseValue()

suspend fun <VALUE, ERROR> Flow<Result<out VALUE, ERROR>>.firstOrErrors(): Result<out VALUE, Collection<ERROR>> {
    val collection: MutableCollection<ERROR> = ArrayList()

    return this
        .map { result -> result.onError { collection.add(it) } }
        .firstSuccessOr { Result.error(collection) }
}