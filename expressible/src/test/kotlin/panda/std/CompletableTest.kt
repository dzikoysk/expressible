package panda.std

import org.junit.jupiter.api.Test
import panda.std.reactive.Completable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue

class CompletableTest {

    @Test
    fun `should be not ready if not completed`() {
        assertTrue(Completable.create<Boolean>().isUnprepared)
    }

    @Test
    fun `should be ready if completed`() {
        assertTrue(Completable.completed("value").isReady)
    }

    @Test
    fun `should return value or throw if not completed`() {
        assertEquals("value", Completable.completed("value").get())
        assertThrows(IllegalStateException::class.java) { Completable.create<Exception>().get() }
    }

    @Test
    fun `should complete option`() {
        assertEquals("value", Completable.create<String>().complete("value").complete("").get())
    }

    @Test
    fun `should return value or throw given error if not completed`() {
        assertThrows(RuntimeException::class.java) { Completable.create<Exception>().orThrow { RuntimeException() } }
        assertEquals("value", Completable.completed("value").orThrow { RuntimeException() })
    }

    @Test
    fun `should subscribe completable & receive provided value`() {
        var status = false
        Completable.create<Boolean>().subscribe { status = it }.complete(true)
        assertTrue(status)
    }

    @Test
    fun `should properly execute associated stages`() {
        var status = false
        val completable = Completable.create<String>()

        completable
            .thenApply { it.toBoolean() }
            .thenCompose { Completable.completed(!it) }
            .then { status = it }

        completable.complete("false")
        assertTrue(status)
    }

    @Test
    fun `should be convertable to future`() {
        assertEquals("value", Completable.completed("value").toFuture().get())
    }

}
