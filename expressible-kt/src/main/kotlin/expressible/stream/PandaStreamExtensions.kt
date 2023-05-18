package expressible.stream

fun <T> Collection<T>.toPandaStream(): expressible.stream.PandaStream<T> =
    expressible.stream.PandaStream.of(this)