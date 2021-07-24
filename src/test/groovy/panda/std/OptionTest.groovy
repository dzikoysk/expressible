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
import panda.std.function.ThrowingSupplier

import java.util.function.IntFunction
import java.util.function.Supplier

import static org.junit.jupiter.api.Assertions.*
import static panda.std.Option.none

@CompileStatic
final class OptionTest {

    @Test
    void 'should filter value' () {
        assertTrue Option.of(true).filter(value -> false).isEmpty()
        assertTrue Option.of(true).filterNot(value -> true).isEmpty()
    }

    @Test
    void 'should map value' () {
        assertEquals 10, Option.of("10").map(value -> Integer.parseInt(value)).get()
    }

    @Test
    void 'should flat map option' () {
        assertEquals 10, Option.of("10").flatMap(value -> Option.of(Integer.parseInt(value))).get()
    }

    @Test
    void 'should match the given value or return empty option' () {
        assertEquals 'b', Option.of(2)
                .match([
                        Case.of({ value -> value == 1 }, { value -> 'a' }),
                        Case.of({ value -> value == 2 }, { value -> 'b' }),
                        Case.of({ value -> value == 3 }, { value -> 'c' })
                ] as Case[])
                .get()

        assertTrue Option.of('test').match().isEmpty()
    }

    @Test
    void 'should execute closure if value is present'() {
        def status = false
        Option.of("true").peek(value -> status = Boolean.parseBoolean(value))
        assertTrue status
    }

    @Test
    void 'should execute closure if empty' () {
        def status = false
        none().onEmpty(() -> status = true)
        assertTrue status
    }

    @Test
    void 'should use default value if empty' () {
        assertEquals "else", none().orElse("else").get()
        assertEquals "else", none().orElse(Option.of("else")).get()
        assertEquals "else", none().orElse({ Option.of("else") } as Supplier).get()
    }

    @Test
    void 'should throw if empty' () {
        assertThrows RuntimeException.class, () -> none().orThrow(() -> new RuntimeException())
    }

    @Test
    void 'should return a value or given default value' () {
        assertEquals "else", none().orElseGet("else")
        assertEquals "else", none().orElseGet({ "else" } as Supplier)
    }

    @Test
    void 'should return a value or null' () {
        assertNull none().getOrNull()
    }

    @Test
    void 'should return a value or throw if empty' () {
        assertEquals 0, Option.of(0).get()
        assertThrows NoSuchElementException.class, () -> none().get()
    }

    @Test
    void 'should inform about its content' () {
        assertTrue Option.of(new Object()).isDefined()
        assertTrue Option.of(new Object()).isPresent()
        assertTrue Option.of(null).isEmpty()
    }

    @Test
    void 'should be convertable to stream' () {
        assertEquals 10, Option.of("10").toJavaStream().mapToInt(value -> Integer.parseInt(value)).findAny().orElse(-1)
        assertArrayEquals new String[0], Option.<String> none().toStream().toArray({ length -> new String[length] } as IntFunction)
    }

    @Test
    void 'should be convertable to optional' () {
        assertTrue Option.of(new Object()).toOptional().isPresent()
    }

    @Test
    void 'should return the same predefined empty option' () {
        assertSame none(), none()
        assertNull none().getOrNull()
    }

    @Test
    void 'should create option based on given value' () {
        assertEquals "test", Option.of("test").get()
    }

    @Test
    void 'should create option based on optional' () {
        assertTrue Option.ofOptional(Optional.of(new Object())).isDefined()
    }

    @Test
    void 'should create option based on condition' () {
        assertTrue Option.when(true, new Object()).isDefined()
        assertFalse Option.when(false, new Object()).isDefined()
    }

    @Test
    void 'should catch exception in case of failure' () {
        assertTrue Option.attempt(Exception.class, { new Object() }).isDefined()
        assertTrue Option.attempt(RuntimeException.class, { throw new RuntimeException() } as ThrowingSupplier).isEmpty()
    }

    @Test
    void 'should iterate over a value' () {
        def iterator = Option.of('test').iterator()
        assertEquals 'test', iterator.next()
    }

}