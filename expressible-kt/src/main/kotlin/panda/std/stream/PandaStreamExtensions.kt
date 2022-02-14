package panda.std.stream

fun <T> Collection<T>.toPandaStream(): PandaStream<T> =
    PandaStream.of(this)