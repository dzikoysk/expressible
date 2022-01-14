package panda.std

fun <VALUE : Any, ERROR> VALUE.asSuccess(): Result<VALUE, ERROR> =
    Result.ok(this)

fun <VALUE, ERROR : Any> ERROR.asError(): Result<VALUE, ERROR> =
    Result.error(this)

fun <VALUE, ERROR, MAPPED_ERROR> Sequence<Result<out VALUE, ERROR>>.firstSuccessOr(elseValue: () -> Result<out VALUE, MAPPED_ERROR>): Result<out VALUE, MAPPED_ERROR> =
    this.firstOrNull { it.isOk }
        ?.projectToValue()
        ?: elseValue()

fun <VALUE, ERROR> Sequence<Result<out VALUE, ERROR>>.firstOrErrors(): Result<out VALUE, Collection<ERROR>> {
    val collection: MutableCollection<ERROR> = ArrayList()

    return this
        .map { result -> result.onError { collection.add(it) } }
        .firstSuccessOr { Result.error(collection) }
}

fun <ERROR : Exception> Result<*, ERROR>.orElseThrow() {
    orElseThrow { it }
}