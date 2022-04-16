package panda.std.reactive

import org.junit.jupiter.api.Test

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertTrue
import static panda.std.reactive.Reference.reference

class ReferenceTest {

    @Test
    void 'should update computed value'() {
        def base = reference(1)
        def computed = base.computed(newValue -> Integer.toString(newValue))
        assertEquals "1", computed.get()

        base.set(2)
        assertEquals "2", computed.get()

        def await = new CountDownLatch(1)
        computed.subscribe(newValue -> await.countDown())
        base.set(3)
        assertTrue await.await(1, TimeUnit.SECONDS)
    }

}
