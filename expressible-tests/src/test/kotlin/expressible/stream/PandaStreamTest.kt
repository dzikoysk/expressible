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

package expressible.stream

import java.util.stream.Stream
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout

class PandaStreamTest {

    private val values = listOf("1", "2", "3")
    private val numbers = arrayOf(1, 2, 3)

    @Test
    fun stream() {
        assertEquals(1, expressible.stream.PandaStream.of(values)
                .stream { stream -> stream.filter { it == "2" }}
                .count())
    }

    @Test
    fun concat() {
        val list = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3")

        assertEquals(list, expressible.stream.PandaStream.of(values)
                .concat(Stream.of("4", "5", "6"))
                .concat("7", "8", "9")
                .concat(expressible.stream.PandaStream.of("10"))
                .concat(values)
                .toList())
    }

    @Test
    fun map() {
        assertArrayEquals(numbers, expressible.stream.PandaStream.of(values).map { it.toInt() }
                .sorted()
                .toArray { arrayOfNulls<Int>(it) })
    }

    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    @Test
    fun `is`() {
        assertArrayEquals(numbers, expressible.stream.PandaStream.of(1, null, 2, "3", 3, "4")
                .`is`(Integer::class.java)
                .toArray { arrayOfNulls<Integer>(it) })

        assertArrayEquals(arrayOf(null, "3", "4"), expressible.stream.PandaStream.of(1, null, 2, "3", 3, "4")
                .isNot(Integer::class.java)
                .toArray { arrayOfNulls<String>(it) })
    }

    @Test
    fun `is primitive`() {
        assertArrayEquals(numbers, expressible.stream.PandaStream.of(1, null, 2, "3", 3, "4")
            .`is`(Int::class.java)
            .toArray { arrayOfNulls<Int>(it) })
    }

    @Test
    fun `flat map`() {
        assertArrayEquals(
            arrayOf(
                11, 22, 33,
                111, 222, 333
            ),
            expressible.stream.PandaStream.of(values)
                .flatMap { listOf("$it$it", "$it$it$it") }
                .map { it.toInt() }
                .sorted()
                .toArray { arrayOfNulls<Int>(it) }
        )
    }

    @Test
    fun filter() {
        assertTrue(
            expressible.stream.PandaStream.of(values)
                .filter { it == "2" }
                .head()
                .isDefined)
    }

    @Test
    fun `shuffle has same elements`() {
        val shuffled = expressible.stream.PandaStream.of(values).shuffle().toList()

        assertEquals(values.size, shuffled.size)
        assertTrue(shuffled.containsAll(values))
    }

    @Timeout(1)
    @Test
    fun `shuffle is shuffling`() {
        while (expressible.stream.PandaStream.of(values).shuffle().toList() == values) {}
    }

    @Test
    fun skip() {
        assertEquals(2L, expressible.stream.PandaStream.of(values)
                .skip(1L)
                .count())
    }

    @Test
    fun find() {
        assertTrue(
            expressible.stream.PandaStream.of(values)
            .find { it == "2" }
            .isDefined)
    }

    @Test
    fun head() {
        assertEquals("1", expressible.stream.PandaStream.of(values).head().get())
    }

    @Test
    fun count() {
        assertEquals(3, expressible.stream.PandaStream.of(values).count())
        assertEquals(1, expressible.stream.PandaStream.of(values).count { it == "2" })
    }

    @Test
    fun `take while`() {
        assertArrayEquals(arrayOf(1, 2), expressible.stream.PandaStream.of(1, 2, 3, 4, 5)
            .takeWhile { it  < 3 }
            .toArray { arrayOfNulls<Int>(it) })
    }

    @Test
    fun `to shuffled list has same elements`() {
        val shuffled = expressible.stream.PandaStream.of(values).toShuffledList()

        assertEquals(values.size, shuffled.size)
        assertTrue(shuffled.containsAll(values))
    }

    @Timeout(1)
    @Test
    fun `to shuffled list is shuffled`() {
        while (expressible.stream.PandaStream.of(values).toShuffledList() == values) {}
    }

    @Test
    fun `to map`() {
        val map = expressible.stream.PandaStream.of(values)
            .toMapByPair { expressible.Pair(it, it.toInt()) }

        assertEquals(1, map["1"])
        assertEquals(2, map["2"])
        assertEquals(3, map["3"])
    }

    @Test
    fun `to stream`() {
        assertArrayEquals(numbers, expressible.stream.PandaStream.of(values)
                .map { it.toInt() }
                .toStream()
                .sorted()
                .toArray { arrayOfNulls<Int>(it) })
    }

    @Test
    fun of() {
        val stream = values.stream()
        assertEquals(stream, expressible.stream.PandaStream.of(stream).toStream())
    }

    @Test
    fun `flat of`() {
        val testList = listOf(1, 2, 3)
        val testSet = setOf(3, 4, 5)
        val resultList = listOf(1, 2, 3, 3, 4, 5)

        // collection
        val collectionOfIterables = expressible.stream.PandaStream.flatOf(listOf(
            testList,
            testSet
        )).toList()
        assertEquals(resultList, collectionOfIterables)

        // iterable
        val iterableOfIterables = expressible.stream.PandaStream.flatOf(listOf(
            testList,
            testSet
        ).asIterable()).toList()
        assertEquals(resultList, iterableOfIterables)

        // array
        val arrayOfIterables = expressible.stream.PandaStream.flatOf(
            testList,
            testSet
        ).toList()
        assertEquals(resultList, arrayOfIterables)
    }

    @Test
    fun empty() {
        assertTrue(expressible.stream.PandaStream.empty<Any>().toList().isEmpty())
    }

    @Test
    fun search() {
        val success = expressible.stream.PandaStream.of("a", "b", "c").search { expressible.Result.`when`(it == "b", it, it) }
        assertTrue(success.isOk)
        assertEquals("b", success.get())

        val error = expressible.stream.PandaStream.of("a", "b", "c").search { expressible.Result.`when`(it == "d", it, it) }
        assertTrue(error.isErr)
        assertEquals(listOf("a", "b", "c"), error.error)
    }

}