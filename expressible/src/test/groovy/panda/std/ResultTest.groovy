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

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import panda.std.function.ThrowingSupplier

import java.util.concurrent.atomic.AtomicInteger

import static org.junit.jupiter.api.Assertions.*
import static panda.std.Result.error
import static panda.std.Result.ok

@CompileStatic
final class ResultTest {

    @Test
    void 'should map value' () {
        assertEquals(7, ok("7").map(value -> Integer.parseInt(value)).get())
    }

    @Test
    void 'should return alternative result if errored' () {
        assertEquals(7, error(-1).orElse(err -> ok(7)).get())
    }

    @Test
    void 'should return alternative value if errored' () {
        assertEquals(7, error(-1).orElseGet(err -> 7))
    }

    @Test
    void 'should catch exception in case of failure' () {
        def result = Result.attempt(Exception.class,() -> { throw new RuntimeException() } as ThrowingSupplier)
                .mapErr(ex -> "error")

        assertTrue result.isErr()
        assertEquals "error", result.getError()

        def resultOk = Result.attempt(NumberFormatException.class as Class<Throwable>,() -> Integer.parseInt("1"))

        assertTrue resultOk.isOk()
        assertEquals(1, resultOk.get())
    }

    @Test
    void 'should evaluate error closure if errored' () {
        def integer = new AtomicInteger(-1)
        error(integer.get()).onError(err -> integer.set(Math.abs(err)))
        assertEquals(1, integer.get())
    }

    @Test
    void 'should return proper ok status' () {
        assertTrue(ok("ok").isOk())
        assertFalse(error("err").isOk())
    }

    @Test
    void 'should return result value' () {
        assertEquals("value", ok("value").get())
    }

    @Test
    void 'should return proper error status' () {
        assertTrue(error("err").isErr())
        assertFalse(ok("ok").isErr())
    }

    @Test
    void 'should return error value' () {
        assertEquals("err", error("err").getError())
    }

    @Test
    @SuppressWarnings('ChangeToOperator')
    void 'should implement equals & hashcode' () {
        def base = ok("test")
        assertEquals base, base
        assertEquals base.hashCode(), base.hashCode()
        assertFalse base.equals(null)
        assertFalse base.equals(new Object())

        def same = ok("test")
        assertEquals base, same
        assertEquals base.hashCode(), same.hashCode()

        def different = ok("different")
        assertNotEquals base, different
        assertNotEquals base.hashCode(), different.hashCode()
    }

    @Test
    void 'should throw exception during an attempt of getting value from result representing error' () {
        assertThrows(NoSuchElementException.class, { error("Error").get() })
    }

    @Test
    void 'should return value or error as any value' () {
        assertEquals 'any', ok('any').getAnyAs()
        assertEquals 'any', error('any').getAnyAs()
    }

    @Test
    void 'should map result to option' () {
        assertEquals Option.of('value'), ok('value').toOption()
        assertEquals Option.none(), error('error').toOption()
    }

    @Test
    void 'should return null for orNull() in case of error' () {
        assertNull error('error').orNull()
    }

    @Test
    void 'should display formatted content through toString()' () {
        assertEquals 'Result{VALUE=value}', ok('value').toString()
        assertEquals 'Result{ERR=error}', error('error').toString()
    }

    @Test
    void 'should execute closure if ok' ()  {
        def status = false

        error(this).peek({ status = true })
        assertFalse status

        ok(this).peek({ status = true })
        assertTrue status
    }

    @Test
    void 'should throw if requested if result contains error' () {
        assertDoesNotThrow({ ok('value').orElseThrow({ new IllegalStateException() }) } as Executable)
        assertThrows(IllegalStateException.class, { error("error").orElseThrow({ new IllegalStateException() })} )
    }

    @Test
    void 'should filter value and return error if needed' () {
        assertEquals 'error', ok('value').filter({ false }, { 'error' }).getError()
        assertEquals 'error', ok('value').filterNot({ true }, { 'error' }).getError()
    }

    @Test
    void 'should flat map result value' () {
        assertEquals 'flat', ok('flat').flatMap(value -> ok(value)).get()
    }

    @Test
    void 'should map error' () {
        assertEquals 'mapped', error('error').mapErr(value -> 'mapped').getError()
    }

    @Test
    void 'should project value of error' () {
        assertThrows(IllegalStateException.class, { ok('value').projectToError() })

        assertDoesNotThrow({
            //noinspection UnnecessaryQualifiedReference
            Result<String, String> expected = Result<Object, String>.error('error').projectToError()
            assertEquals 'error', expected.getError()
        } as Executable)
    }

}