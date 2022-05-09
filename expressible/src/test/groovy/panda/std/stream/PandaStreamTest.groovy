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

package panda.std.stream

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test
import panda.std.Pair
import panda.std.Result

import java.util.function.IntFunction
import java.util.function.Predicate
import java.util.stream.Collectors
import java.util.stream.Stream

import static org.junit.jupiter.api.Assertions.*

@CompileStatic
class PandaStreamTest {

    private static final List<String> VALUES = Arrays.asList("1", "2", "3")
    private static final Integer[] NUMBERS = [ 1, 2, 3 ]

    @Test
    void stream() {
        assertEquals 1, PandaStream.of(VALUES)
                .stream(stream -> stream.filter(value -> "2" == value))
                .count()
    }

    @Test
    void concat() {
        def list = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3")

        assertEquals list, PandaStream.of(VALUES)
                .concat(Stream.of("4", "5", "6"))
                .concat("7", "8", "9")
                .concat(PandaStream.of("10"))
                .concat(VALUES)
                .toList()
    }

    @Test
    void map() {
        assertArrayEquals NUMBERS, PandaStream.of(VALUES).map(value -> Integer.parseInt(value))
                .sorted()
                .toArray({ length -> new Integer[length] } as IntFunction)
    }

    @Test
    void is() {
        assertArrayEquals NUMBERS, PandaStream.of(1, null, 2, "3", 3, "4")
                .is(Integer.class)
                .toArray({ length -> new Integer[length] } as IntFunction)
    }

    @Test
    void flatMap() {
        assertArrayEquals NUMBERS, PandaStream.of(VALUES)
                .flatMap({ string ->
                    string.chars()
                            .mapToObj({ c -> c as char } as IntFunction)
                            .collect(Collectors.toList())
                })
                .map(c -> Character.toString(c as char))
                .map(value -> Integer.parseInt(value.toString()))
                .sorted()
                .toArray({ length -> new Integer[length] } as IntFunction)
    }

    @Test
    void filter() {
        assertTrue PandaStream.of(VALUES)
                .filter(value -> "2" == value)
                .head()
                .isDefined()
    }

    @Test
    void skip() {
        assertEquals 2L, PandaStream.of(VALUES)
                .skip(1L)
                .count()
    }

    @Test
    void find() {
        assertTrue PandaStream.of(VALUES)
                .find({ value -> value == "2" } as Predicate)
                .isDefined()
    }

    @Test
    void head() {
        assertEquals "1", PandaStream.of(VALUES).head().get()
    }

    @Test
    void count() {
        assertEquals 3, PandaStream.of(VALUES).count()
        assertEquals 1, PandaStream.of(VALUES).count({ value -> value == "2" })
    }

    @Test
    void takeWhile() {
        assertArrayEquals([1, 2] as Integer[], PandaStream.of(1, 2, 3, 4, 5)
                .takeWhile(i -> i < 3)
                .toArray({ length -> new Integer[length] } as IntFunction))
    }

    @Test
    void toMap() {
        def map = PandaStream.of(VALUES)
                .toMapByPair((text) -> Pair.of(text, Integer.parseInt(text)))

        assertEquals 1, map.get("1")
        assertEquals 2, map.get("2")
        assertEquals 3, map.get("3")
    }

    @Test
    void toStream() {
        assertArrayEquals NUMBERS, PandaStream.of(VALUES)
                .map(value -> Integer.parseInt(value))
                .toStream()
                .sorted()
                .toArray({ length -> new Integer[length] } as IntFunction)
    }

    @Test
    void of() {
        Stream<String> stream = VALUES.stream()
        assertEquals stream, PandaStream.of(stream).toStream()
    }

    @Test
    void empty() {
        assertTrue PandaStream.empty().toList().isEmpty()
    }

    @Test
    void search() {
        def success = PandaStream.of("a", "b", "c").search(element -> Result.when(element == "b", element, element))
        assertTrue(success.isOk())
        assertEquals("b", success.get())

        def error = PandaStream.of("a", "b", "c").search(element -> Result.when(element == "d", element, element))
        assertTrue(error.isErr())
        assertEquals(["a", "b", "c"], error.getError())
    }

}