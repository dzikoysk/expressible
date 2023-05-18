package expressible

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue

class CompletableTest {

    @Test
    fun `should be not ready if not completed`() {
        assertTrue(expressible.reactive.Completable.create<Boolean>().isUnprepared)
    }

    @Test
    fun `should be ready if completed`() {
        assertTrue(expressible.reactive.Completable.completed("value").isReady)
    }

    @Test
    fun `should return value or throw if not completed`() {
        assertEquals("value", expressible.reactive.Completable.completed("value").get())
        assertThrows(IllegalStateException::class.java) { expressible.reactive.Completable.create<Exception>().get() }
    }

    @Test
    fun `should complete option`() {
        assertEquals("value", expressible.reactive.Completable.create<String>().complete("value").complete("").get())
    }

    @Test
    fun `should return value or throw given error if not completed`() {
        assertThrows(RuntimeException::class.java) { expressible.reactive.Completable.create<Exception>().orThrow { RuntimeException() } }
        assertEquals("value", expressible.reactive.Completable.completed("value").orThrow { RuntimeException() })
    }

    @Test
    fun `should subscribe completable & receive provided value`() {
        var status = false
        expressible.reactive.Completable.create<Boolean>().subscribe { status = it }.complete(true)
        assertTrue(status)
    }

    @Test
    fun `should properly execute associated stages`() {
        var status = false
        val completable = expressible.reactive.Completable.create<String>()

        completable
            .thenApply { it.toBoolean() }
            .thenCompose { expressible.reactive.Completable.completed(!it) }
            .then { status = it }

        completable.complete("false")
        assertTrue(status)
    }

    @Test
    fun `should be convertable to future`() {
        assertEquals("value", expressible.reactive.Completable.completed("value").toFuture().get())
    }

}
