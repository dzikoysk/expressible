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

import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import panda.std.Result.error
import panda.std.Result.ok
import kotlin.math.abs

class ResultTest {

    @Test
    fun shouldMapValue() {
        assertEquals(7, ok<String, Any>("7").map { it.toInt() }.get())
    }

    @Test
    fun shouldReturnAlternativeResultIfErrored() {
        assertEquals(7, error<Int, Any>(-1).orElse { ok(7) }.get())
    }

    @Test
    fun shouldReturnAlternativeValueIfErrored() {
        assertEquals(7, error<Int, Any>(-1).orElseGet { 7 })
    }

    @Test
    fun shouldCatchExceptionInCaseOfFailure() {
        val result = Result.supplyThrowing<String, Throwable>(Exception::class.java) { throw RuntimeException() }.mapErr { "error" }
        assertTrue(result.isErr)
        assertEquals("error", result.error)

        val resultOk = Result.supplyThrowing(NumberFormatException::class.java) { Integer.parseInt("1") }
        assertTrue(resultOk.isOk)
        assertEquals(1, resultOk.get())

        assertThrows(AttemptFailedException::class.java) {
            Result.supplyThrowing(IllegalAccessException::class.java) { throw RuntimeException("Gotcha") }}
    }

    @Test
    fun shouldEvaluateErrorClosureIfErrored() {
        val integer = AtomicInteger(-1)
        error<Int, Int>(integer.get()).onError { err: Int -> integer.set(abs(err)) }
        assertEquals(1, integer.get())
    }

    @Test
    fun shouldReturnProperOkStatus() {
        assertTrue(ok<String, Any>("ok").isOk)
        assertFalse(error<String, Any>("err").isOk)
    }

    @Test
    fun shouldReturnResultValue() {
        assertEquals("value", ok<String, Any>("value").get())
    }

    @Test
    fun shouldReturnProperErrorStatus() {
        assertTrue(error<String, Any>("err").isErr)
        assertFalse(ok<String, Any>("ok").isErr)
    }

    @Test
    fun shouldReturnErrorValue() {
        assertEquals("err", error<String, Any>("err").error)
    }

    @Test
    @SuppressWarnings("ChangeToOperator")
    fun shouldImplementEqualsAndHashcode() {
        val base = ok<String, Any>("test")
        assertEquals(base, base)
        assertEquals(base.hashCode(), base.hashCode())
        assertFalse(base.equals(null))
        assertFalse(base == Object())

        val same = ok<String, Any>("test")
        assertEquals(base, same)
        assertEquals(base.hashCode(), same.hashCode())

        val different = ok<String, Any>("different")
        assertNotEquals(base, different)
        assertNotEquals(base.hashCode(), different.hashCode())
    }

    @Test
    fun shouldThrowExceptionDuringAnAttemptOfGettingValueFromResultRepresentingError() {
        assertThrows(NoSuchElementException::class.java) { error<String, Any>("Error").get() }
    }

    @Test
    fun shouldReturnValueOrErrorAsAnyValue() {
        assertEquals("any", ok<String, Any>("any").getAnyAs())
        assertEquals("any", error<String, Any>("any").getAnyAs())
    }

    @Test
    fun shouldMapResultToOption() {
        assertEquals(Option.of("value"), ok<String, Any>("value").toOption())
        assertEquals(Option.none<Any>(), error<String, Any>("error").toOption())
    }

    @Test
    fun shouldReturnNullForOrNullInCaseOfError() {
        assertNull(error<String, Any>("error").orNull())
    }

    @Test
    fun shouldDisplayFormattedContentThroughToString() {
        assertEquals("Result{VALUE=value}", ok<String, Any>("value").toString())
        assertEquals("Result{ERR=error}", error<String, Any>("error").toString())
    }

    @Test
    fun shouldExecuteClosureIfOk()  {
        var status = false

        error<Boolean, Any>(this).peek { status = true }
        assertFalse(status)

        ok<ResultTest, Any>(this).peek { status = true }
        assertTrue(status)
    }

    @Test
    fun shouldThrowIfRequestedIfResultContainsError() {
        assertDoesNotThrow { ok<String, Any>("value").orThrow { IllegalStateException() } }
        assertThrows(IllegalStateException::class.java) { error<String, Any>("error").orThrow { IllegalStateException() }}
    }

    @Test
    fun shouldFilterValueAndReturnErrorIfNeeded() {
        assertEquals("error", ok<String, Any>("value").filter( { false }, { "error" }).error)
        assertEquals("error", ok<String, Any>("value").filterNot( { true }, { "error" }).error)
    }

    @Test
    fun swapTest() {
        assertEquals("test", ok<String, Any>("test").swap().error)
    }

    @Test
    fun shouldFlatMapResultValue() {
        assertEquals("flat", ok<String, Any>("flat").flatMap { ok(it) }.get())
    }

    @Test
    fun shouldMapError() {
        assertEquals("mapped", error<String, Any>("error").mapErr { "mapped" }.error)
    }

    @Test
    fun shouldProjectValueOfError() {
        assertDoesNotThrow {
            //noinspection UnnecessaryQualifiedReference
            val expected = error<String, Any>("error").projectToError<Any>()
            assertEquals("error", expected.error)
        }
    }

}