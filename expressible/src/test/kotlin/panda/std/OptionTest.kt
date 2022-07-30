/*
 * Copyright (c) 2021 dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package panda.std

import java.util.Optional
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import panda.std.Option.none
import panda.std.Option.of
import kotlin.NoSuchElementException

class OptionTest {

    @Test
    fun shouldFilterValue() {
        val value = "value"
        val accept = true
        val reject = false

        assertTrue(none<Any>().filter { accept }.isEmpty)
        assertTrue(of(value).filter { reject }.isEmpty)
        assertEquals(value, of(value).filter { accept }.get())

        assertTrue(none<Any>().filter { accept }.isEmpty)
        assertTrue(of(value).filterNot { accept }.isEmpty)
        assertEquals(value, of(value).filterNot { reject }.get())
    }

    @Test
    fun shouldMapValue() {
        assertTrue(none<Any>().map { Object() }.isEmpty)
        assertEquals(3, of(1).map { it + 2 }.get())
    }

    @Test
    fun shouldFlatMapOption() {
        assertTrue(none<Any>().flatMap<Any> { of(Object()) }.isEmpty)
        assertEquals(3, of(1).flatMap { of(it + 2) }.get())
    }

    @Test
    fun shouldMatchTheGivenValueOrReturnEmptyOption() {
        assertTrue(none<Boolean>().match<Boolean>().isEmpty)
        assertTrue(of("test").match<Boolean>().isEmpty)

        assertEquals("b", of(2)
            .match(
                Case.of({ it == 1 }, { "a" }),
                Case.of({ it == 2 }, { "b" }),
                Case.of({ it == 3 }, { "c" })
            ).get())
    }

    @Test
    fun shouldExecuteClosureIfValueIsPresent() {
        var status = false

        none<Boolean>().peek { status = true }
        assertFalse(status)

        of(true).peek { status = it }
        assertTrue(status)
    }

    @Test
    fun shouldExecuteClosureIfEmpty () {
        var status = false

        of("value").onEmpty { status = true }
        assertFalse(status)

        none<Boolean>().onEmpty { status = true }
        assertTrue(status)
    }

    @Test
    fun shouldUseDefaultValueIfEmpty() {
        assertEquals("else", none<String>().orElse("else").get())
        assertEquals("value", of("value").orElse("else").get())

        assertEquals("else", none<Any>().orElseSupply { "else" }.get())
        assertEquals("value", of("value").orElseSupply { "else" }.get())

        assertEquals("else", none<String>().orElse(of("else")).get())
        assertEquals("value", of("value").orElse(of("else")).get())

        assertEquals("else", none<String>().orElse { of("else") }.get())
        assertEquals("value", of("value").orElse { of("else") }.get())
    }

    @Test
    fun shouldThrowIfEmpty() {
        assertThrows(RuntimeException::class.java) { none<Executable>().orThrow<Exception> { RuntimeException() } }
        assertDoesNotThrow { of("value").orThrow<Exception> { RuntimeException() } }
    }

    @Test
    fun shouldReturnAValueOrGivenDefaultValue() {
        assertEquals("else", none<String>().orElseGet("else"))
        assertEquals("value", of("value").orElseGet("else"))
        
        assertEquals("else", none<String>().orElseGet { "else" })
        assertEquals("value", of("value").orElseGet { "else" })
    }

    @Test
    fun shouldReturnAValueOrNull() {
        assertNull(none<Any>().orNull())
    }

    @Test
    fun shouldReturnAValueOrThrowIfEmpty() {
        assertEquals(0, of(0).get())
        assertThrows(NoSuchElementException::class.java) { none<Any>().get() }
    }

    @Test
    fun shouldInformAboutItsContent() {
        assertTrue(of(Object()).isDefined)
        assertTrue(of(Object()).isPresent)
        assertTrue(of(null).isEmpty)
    }

    @Test
    fun shouldBeConvertableToStream() {
        assertEquals(10, of("10").toJavaStream().mapToInt { it.toInt() }.findAny().orElse(-1))
        assertArrayEquals(arrayOfNulls(0), none<String>().toStream().toArray { arrayOfNulls<String>(it) })
    }

    @Test
    fun shouldBeConvertableToOptional() {
        assertTrue(of(Object()).toOptional().isPresent)
    }

    @Test
    fun shouldReturnTheSamePredefinedEmptyOption() {
        assertSame(none<Any>(), none<Any>())
        assertNull(none<Any>().orNull())
    }

    @Test
    fun shouldCreateOptionBasedOnGivenValue() {
        assertEquals("test", of("test").get())
    }

    @Test
    fun shouldCreateOptionBasedOnOptional() {
        assertTrue(Option.ofOptional(Optional.of(Object())).isDefined)
    }

    @Test
    fun shouldCreateOptionBasedOnCondition() {
        assertTrue(Option.`when`(true, Object()).isDefined)
        assertFalse(Option.`when`(false, Object()).isDefined)
    }

    @Test
    fun shouldCatchExceptionInCaseOfFailure() {
        assertTrue(Option.supplyThrowing(Throwable::class.java) { Object() }.isDefined)
        assertTrue(Option.supplyThrowing(RuntimeException::class.java) { throw RuntimeException() }.isEmpty)
        assertThrows(AttemptFailedException::class.java) { Option.supplyThrowing(IllegalAccessException::class.java) { throw RuntimeException("Gotcha") }}
    }

    @Test
    fun shouldIterateOverAValue() {
        assertFalse(none<Boolean>().iterator().hasNext())

        val iterator = of("test").iterator()
        assertEquals("test", iterator.next())
        assertFalse(iterator.hasNext())
    }

    @Test
    fun shouldBeConvertableToResult() {
        val result = of<String>("test").toResult("Not found")
        assertTrue(result.isOk)
        assertEquals("test", result.get())

        val errorResult = none<String>().toResult("Not found")
        assertTrue(errorResult.isErr)
        assertEquals("Not found", errorResult.error)
    }

    @Test
    fun shouldBeConvertableToPandaStream() {
        assertEquals(0, none<Int>().toStream<Any> { null }.count())
        assertEquals(2, of<List<Int>>(listOf(1, 2)).toStream { it.stream() }.count())
    }

    @Test
    fun shouldCreateOptionWithCompletable() {
        assertEquals("value", Option.withCompleted("value").get().get())
    }

    @Test
    @SuppressWarnings("ChangeToOperator")
    fun shouldImplementEqualsAndHashcode() {
        val base = of("value")

        assertEquals(base, base)
        assertFalse(base.equals(null))
        assertFalse(base.equals(Object()))

        val same = of("value")
        assertEquals(same, base)
        assertEquals(same.hashCode(), base.hashCode())

        val different = of("other")
        assertNotEquals(different, base)
        assertNotEquals(different.hashCode(), base.hashCode())
    }

    @Test
    fun shouldAssociate2OptionalValuesIntoAPair() {
        assertEquals(Pair.of("a", "b"), of("a").associateWith { of("b") }.get())
    }

}