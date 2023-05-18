package expressible.reactive

import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import expressible.reactive.Reference.reference

class ReferenceTest {

    @Test
    fun `should update computed value`() {
        val base = reference(1)
        val computed = base.computed { it.toString() }
        assertEquals("1", computed.get())

        base.set(2)
        assertEquals("2", computed.get())

        val await = CountDownLatch(1)
        computed.subscribe { await.countDown() }
        base.set(3)
        assertTrue(await.await(1, TimeUnit.SECONDS))
    }

}
