package panda.std

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

internal class DestructuringDeclarationsTestKt {

    @Test
    fun destructuringDeclarationsMono() {
        val list = listOf(Mono.of("test1"), Mono.of("test2"))

        var i = 0
        for ((first) in list) {
            assertEquals("test" + ++i, first)
        }

        // without brackets destructuring declarations are not triggered
        i = 0
        for (first in list) {
            assertNotEquals("test" + ++i, first)
        }
    }

    @Test
    fun destructuringDeclarationsPair() {
        val list = listOf(Pair.of("test1", "test3"), Pair.of("test2", "test4"))

        var i = 0
        for ((first, second) in list) {
            assertEquals("test" + ++i, first)
            assertEquals("test" + (i + 2), second)
        }
    }

    @Test
    fun destructuringDeclarationsTriple() {
        val list = listOf(
                Triple.of("test1", "test3", "test5"),
                Triple.of("test2", "test4", "test6")
        )

        var i = 0
        for ((first, second, third) in list) {
            assertEquals("test" + ++i, first)
            assertEquals("test" + (i + 2), second)
            assertEquals("test" + (i + 4), third)
        }
    }

    @Test
    fun destructuringDeclarationsQuad() {
        val list = listOf(
                Quad.of("test1", "test3", "test5", "test7"),
                Quad.of("test2", "test4", "test6", "test8")
        )

        var i = 0
        for ((first, second, third, fourth) in list) {
            assertEquals("test" + ++i, first)
            assertEquals("test" + (i + 2), second)
            assertEquals("test" + (i + 4), third)
            assertEquals("test" + (i + 6), fourth)
        }
    }

}