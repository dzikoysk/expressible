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

package expressible

import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import expressible.Result.error
import expressible.Result.ok
import java.lang.IllegalArgumentException
import kotlin.math.abs

class ResultTest {

    @Test
    fun `should map value`() {
        assertEquals(7, ok<String, Any>("7").map { it.toInt() }.get())
    }

    @Test
    fun `should return alternative result if errored`() {
        assertEquals(7, error<Int, Any>(-1).orElse { ok(7) }.get())
    }

    @Test
    fun `should return alternative value if errored`() {
        assertEquals(7, error<Int, Any>(-1).orElseGet { 7 })
    }

    @Test
    fun `should catch exception in case of failure`() {
        val result = Result.supplyThrowing<String, Throwable>(Exception::class.java) { throw RuntimeException() }.mapErr { "error" }
        assertTrue(result.isErr)
        assertEquals("error", result.error)

        val resultOk = Result.supplyThrowing(NumberFormatException::class.java) { Integer.parseInt("1") }
        assertTrue(resultOk.isOk)
        assertEquals(1, resultOk.get())

        assertThrows(AttemptFailedException::class.java) {
            Result.supplyThrowing(IllegalAccessException::class.java) { throw RuntimeException("Gotcha") }
        }
    }

    @Test
    fun `should evaluate error closure if errored`() {
        val integer = AtomicInteger(-1)
        error<Int, Int>(integer.get()).onError { err: Int -> integer.set(abs(err)) }
        assertEquals(1, integer.get())
    }

    @Test
    fun `should return proper ok status`() {
        assertTrue(ok<String, Any>("ok").isOk)
        assertFalse(error<String, Any>("err").isOk)
    }

    @Test
    fun `should return result value`() {
        assertEquals("value", ok<String, Any>("value").get())
    }

    @Test
    fun `should return proper error status`() {
        assertTrue(error<String, Any>("err").isErr)
        assertFalse(ok<String, Any>("ok").isErr)
    }

    @Test
    fun `should return error value`() {
        assertEquals("err", error<String, Any>("err").error)
    }

    @Test
    @SuppressWarnings("ChangeToOperator")
    fun `should implement equals & hashcode`() {
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
    fun `should throw exception during an attempt of getting value from result representing error`() {
        assertThrows(IllegalStateException::class.java) { error<String, Any>("Error").get() }
    }

    @Test
    fun `should return value or error as any value`() {
        assertEquals("any", ok<String, Any>("any").getAnyAs())
        assertEquals("any", error<String, Any>("any").getAnyAs())
    }

    @Test
    fun `should map result to option`() {
        assertEquals(Option.of("value"), ok<String, Any>("value").toOption())
        assertEquals(Option.none<Any>(), error<String, Any>("error").toOption())
    }

    @Test
    fun `should return null for or null in case of error`() {
        assertNull(error<String, Any>("error").orNull())
    }

    @Test
    fun `should display formatted content through to string`() {
        assertEquals("Result{VALUE=value}", ok<String, Any>("value").toString())
        assertEquals("Result{ERR=error}", error<String, Any>("error").toString())
    }

    @Test
    fun `should execute closure if ok`()  {
        var status = false

        error<Boolean, Any>(this).peek { status = true }
        assertFalse(status)

        ok<ResultTest, Any>(this).peek { status = true }
        assertTrue(status)
    }

    @Test
    fun `should throw if requested if result contains error`() {
        assertDoesNotThrow { ok<String, Any>("value").orThrow { IllegalStateException() } }
        assertThrows(IllegalStateException::class.java) { error<String, Any>("error").orThrow { IllegalStateException() }}
    }

    @Test
    fun `should filter value & return error if needed`() {
        assertEquals("error", ok<String, Any>("value").filter({ false }, { "error" }).error)
        assertEquals("error", ok<String, Any>("value").filterNot({ true }, { "error" }).error)
    }

    @Test
    fun `should filter value & return error if needed using aggregated filters`() {
        assertEquals(
            "value",
            ok<String, Any>("value")
                .filter {
                    when {
                        it != "value" -> "err"
                        it.startsWith("test") -> "err"
                        else -> null // ok
                    }
                }
                .get()
        )

        assertEquals(
            "value",
            ok<String, Any>("value")
                .filterWithThrowing<IllegalArgumentException> {
                    require(it == "value") { "err" }
                    require(it.startsWith("v")) { "err" }
                }
                .mapFilterError { it.message ?: "err" }
                .get()
        )
    }

    @Test
    fun `swap test`() {
        assertEquals("test", ok<String, Any>("test").swap().error)
    }

    @Test
    fun `should flat map result value`() {
        assertEquals("flat", ok<String, Any>("flat").flatMap { ok(it) }.get())
    }

    @Test
    fun `should map error`() {
        assertEquals("mapped", error<String, Any>("error").mapErr { "mapped" }.error)
    }

    @Test
    fun `should project value of error`() {
        assertDoesNotThrow {
            val expected = error<String, Any>("error").projectToError<Any>()
            assertEquals("error", expected.error)
        }
    }

}