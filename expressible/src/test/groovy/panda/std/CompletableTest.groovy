package panda.std


import org.junit.jupiter.api.Test
import panda.std.reactive.Completable

import static java.lang.Boolean.parseBoolean
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertThrows
import static org.junit.jupiter.api.Assertions.assertTrue

class CompletableTest {

    @Test
    void 'should be not ready if not completed' () {
        assertTrue Completable.create().isUnprepared()
    }

    @Test
    void 'should be ready if completed' () {
        assertTrue Completable.completed('value').isReady()
    }

    @Test
    void 'should return value or throw if not completed' () {
        assertEquals 'value', Completable.completed('value').get()
        assertThrows IllegalStateException.class, { Completable.create().get() }
    }

    @Test
    void 'should complete option' () {
        assertEquals 'value', Completable.create().complete('value').complete('').get()
    }

    @Test
    void 'should return value or throw given error if not completed' () {
        assertThrows RuntimeException.class, { Completable.create().orThrow({ new RuntimeException() }) }
        assertEquals 'value', Completable.completed('value').orThrow({ new RuntimeException() })
    }

    @Test
    void 'should subscribe completable and receive provided value' () {
        boolean status = false
        Completable.create().subscribe(value -> { status = value}).complete(true)
        assertTrue status
    }

    @Test
    void 'should properly execute associated stages' () {
        boolean status = false
        Completable<String> completable = Completable.create()

        completable
            .thenApply(value -> parseBoolean(value))
            .thenCompose(value -> Completable.completed(!value))
            .then(value -> { status = value })

        completable.complete('false')
        assertTrue status
    }

    @Test
    void 'should be convertable to future' () {
        assertEquals 'value', Completable.completed('value').toFuture().get()
    }

}
