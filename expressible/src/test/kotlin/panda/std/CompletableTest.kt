package panda.std

import org.junit.jupiter.api.Test
import panda.std.reactive.Completable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue

class CompletableTest {

    @Test
    fun shouldBeNotReadyIfNotCompleted() {
        assertTrue(Completable.create<Boolean>().isUnprepared)
    }

    @Test
    fun shouldBeReadyIfCompleted() {
        assertTrue(Completable.completed("value").isReady)
    }

    @Test
    fun shouldReturnValueOrThrowIfNotCompleted () {
        assertEquals("value", Completable.completed("value").get())
        assertThrows(IllegalStateException::class.java) { Completable.create<Exception>().get() }
    }

    @Test
    fun shouldCompleteOption() {
        assertEquals("value", Completable.create<String>().complete("value").complete("").get())
    }

    @Test
    fun shouldReturnValueOrThrowGivenErrorIfNotCompleted() {
        assertThrows(RuntimeException::class.java) { Completable.create<Exception>().orThrow { RuntimeException() } }
        assertEquals("value", Completable.completed("value").orThrow { RuntimeException() })
    }

    @Test
    fun shouldSubscribeCompletableAndReceiveProvidedValue() {
        var status = false
        Completable.create<Boolean>().subscribe { status = it }.complete(true)
        assertTrue(status)
    }

    @Test
    fun shouldProperlyExecuteAssociatedStages() {
        var status = false
        val completable = Completable.create<String>()

        completable
            .thenApply { it.toBoolean() }
            .thenCompose { value -> Completable.completed(!value) }
            .then { status = it }

        completable.complete("false")
        assertTrue(status)
    }

    @Test
    fun shouldBeConvertableToFuture() {
        assertEquals("value", Completable.completed("value").toFuture().get())
    }

}
