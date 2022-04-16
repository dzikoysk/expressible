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

import java.util.function.IntFunction
import java.util.function.Supplier

import static org.junit.jupiter.api.Assertions.*
import static panda.std.Option.none
import static panda.std.Option.of

@CompileStatic
final class OptionTest {

    @Test
    void 'should filter value' () {
        def value = 'value'
        def accept = { true }
        def reject = { false }

        assertTrue none().filter(accept).isEmpty()
        assertTrue of(value).filter(reject).isEmpty()
        assertEquals value, of(value).filter(accept).get()

        assertTrue none().filter(accept).isEmpty()
        assertTrue of(value).filterNot(accept).isEmpty()
        assertEquals value, of(value).filterNot(reject).get()
    }

    @Test
    void 'should map value' () {
        assertTrue none().map({ new Object() }).isEmpty()
        assertEquals 3, of(1).map(value -> value + 2).get()
    }

    @Test
    void 'should flat map option' () {
        assertTrue none().flatMap({ of(new Object()) }).isEmpty()
        assertEquals 3, of(1).flatMap(value -> of(value + 2)).get()
    }

    @Test
    void 'should match the given value or return empty option' () {
        assertTrue none().match().isEmpty()
        assertTrue of('test').match().isEmpty()

        assertEquals 'b', of(2)
                .match([
                        Case.of({ value -> value == 1 }, { value -> 'a' }),
                        Case.of({ value -> value == 2 }, { value -> 'b' }),
                        Case.of({ value -> value == 3 }, { value -> 'c' })
                ] as Case[])
                .get()
    }

    @Test
    void 'should execute closure if value is present'() {
        def status = false

        none().peek(value -> status = true)
        assertFalse status

        of(true).peek(value -> status = value)
        assertTrue status
    }

    @Test
    void 'should execute closure if empty' () {
        def status = false

        of('value').onEmpty({ status = true })
        assertFalse status

        none().onEmpty({ status = true })
        assertTrue status
    }

    @Test
    void 'should use default value if empty' () {
        assertEquals 'else', none().orElse('else').get()
        assertEquals 'value', of('value').orElse('else').get()

        assertEquals 'else', none().orElseSupply({ 'else' } as Supplier).get()
        assertEquals 'value', of('value').orElseSupply({ 'else' } as Supplier).get()

        assertEquals 'else', none().orElse(of('else')).get()
        assertEquals 'value', of('value').orElse(of('else')).get()

        assertEquals 'else', none().orElse({ of('else') } as Supplier).get()
        assertEquals 'value', of('value').orElse({ of('else') } as Supplier).get()
    }

    @Test
    void 'should throw if empty' () {
        assertThrows RuntimeException.class, () -> none().orThrow(() -> new RuntimeException())
        assertDoesNotThrow({ of('value').orThrow(() -> new RuntimeException()) } as Executable)
    }

    @Test
    void 'should return a value or given default value' () {
        assertEquals 'else', none().orElseGet('else')
        assertEquals 'value', of('value').orElseGet('else')
        
        assertEquals 'else', none().orElseGet({ 'else' } as Supplier)
        assertEquals 'value', of('value').orElseGet({ 'else' } as Supplier)
    }

    @Test
    void 'should return a value or null' () {
        assertNull none().orNull()
    }

    @Test
    void 'should return a value or throw if empty' () {
        assertEquals 0, of(0).get()
        assertThrows NoSuchElementException.class, () -> none().get()
    }

    @Test
    void 'should inform about its content' () {
        assertTrue of(new Object()).isDefined()
        assertTrue of(new Object()).isPresent()
        assertTrue of(null).isEmpty()
    }

    @Test
    void 'should be convertable to stream' () {
        assertEquals 10, of("10").toJavaStream().mapToInt(value -> Integer.parseInt(value)).findAny().orElse(-1)
        assertArrayEquals new String[0], none().toStream().toArray({ length -> new String[length] } as IntFunction)
    }

    @Test
    void 'should be convertable to optional' () {
        assertTrue of(new Object()).toOptional().isPresent()
    }

    @Test
    void 'should return the same predefined empty option' () {
        assertSame none(), none()
        assertNull none().orNull()
    }

    @Test
    void 'should create option based on given value' () {
        assertEquals "test", of("test").get()
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
        assertTrue Option.attempt(Throwable.class, { new Object() }).isDefined()
        assertTrue Option.attempt(RuntimeException.class, { throw new RuntimeException() } as ThrowingSupplier).isEmpty()
        assertThrows AttemptFailedException.class, { Option.attempt(IllegalAccessException.class, { throw new RuntimeException("Gotcha") }) }
    }

    @Test
    void 'should iterate over a value' () {
        assertFalse none().iterator().hasNext()

        def iterator = of('test').iterator()
        assertEquals 'test', iterator.next()
        assertFalse iterator.hasNext()
    }

    @Test
    void 'should be convertable to result' () {
        def result = of('test').toResult("Not found")
        assertTrue result.isOk()
        assertEquals 'test', result.get()

        def errorResult = none().toResult("Not found")
        assertTrue errorResult.isErr()
        assertEquals "Not found", errorResult.getError()
    }

    @Test
    void 'should be convertable to panda stream' () {
        assertEquals 0, none().toStream(value -> null).count()
        assertEquals 2, of([1, 2]).toStream(list -> list.stream()).count()
    }

    @Test
    void 'should crete option with completable' () {
        assertEquals 'value', Option.withCompleted('value').get().get()
    }

    @Test
    @SuppressWarnings('ChangeToOperator')
    void 'should implement equals & hashcode' () {
        def base =  of('value')

        assertEquals base, base
        assertFalse base.equals(null)
        assertFalse base.equals(new Object())

        def same = of('value')
        assertEquals same, base
        assertEquals same.hashCode(), base.hashCode()

        def different = of('other')
        assertNotEquals different, base
        assertNotEquals different.hashCode(), base.hashCode()
    }

    @Test
    void 'should associate 2 optional values into a pair'() {
        assertEquals(Pair.of("a", "b"), of("a").associateWith({ of("b") }).get())
    }

}