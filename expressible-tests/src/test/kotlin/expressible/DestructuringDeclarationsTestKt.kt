package expressible

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DestructuringDeclarationsTestKt {

    @Test
    fun destructuringDeclarationsMono() {
        val mono = Mono.of("a")
        val (first) = mono
        assertEquals(mono, Mono.of(first))
    }

    @Test
    fun destructuringDeclarationsPair() {
        val pair = Pair.of("a", "b")
        val (first, second) = pair
        assertEquals(pair, Pair.of(first, second))
    }

    @Test
    fun destructuringDeclarationsTriple() {
        val triple = Triple.of("a", "b", "c")
        val (first, second, third) = triple
        assertEquals(triple, Triple.of(first, second, third))
    }

    @Test
    fun destructuringDeclarationsQuad() {
        val triple = Quad.of("a", "b", "c", "d")
        val (first, second, third, fourth) = triple
        assertEquals(triple, Quad.of(first, second, third, fourth))
    }

}