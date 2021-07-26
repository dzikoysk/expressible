package panda.std

import org.junit.jupiter.api.Test

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

}
