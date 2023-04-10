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

import org.junit.jupiter.api.Test
import java.util.stream.Stream
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Timeout

class PandaStreamTest {

    private val values = listOf("1", "2", "3")
    private val numbers = arrayOf(1, 2, 3)

    @Test
    fun stream() {
        assertEquals(1, PandaStream.of(values)
                .stream { stream -> stream.filter { it == "2" }}
                .count())
    }

    @Test
    fun concat() {
        val list = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3")

        assertEquals(list, PandaStream.of(values)
                .concat(Stream.of("4", "5", "6"))
                .concat("7", "8", "9")
                .concat(PandaStream.of("10"))
                .concat(values)
                .toList())
    }

    @Test
    fun map() {
        assertArrayEquals(numbers, PandaStream.of(values).map { it.toInt() }
                .sorted()
                .toArray { arrayOfNulls<Int>(it) })
    }

    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    @Test
    fun `is`() {
        assertArrayEquals(numbers, PandaStream.of(1, null, 2, "3", 3, "4")
                .`is`(Integer::class.java)
                .toArray { arrayOfNulls<Integer>(it) })

        assertArrayEquals(arrayOf(null, "3", "4"), PandaStream.of(1, null, 2, "3", 3, "4")
                .isNot(Integer::class.java)
                .toArray { arrayOfNulls<String>(it) })
    }

    @Test
    fun `is primitive`() {
        assertArrayEquals(numbers, PandaStream.of(1, null, 2, "3", 3, "4")
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
            PandaStream.of(values)
                .flatMap { listOf("$it$it", "$it$it$it") }
                .map { it.toInt() }
                .sorted()
                .toArray { arrayOfNulls<Int>(it) }
        )
    }

    @Test
    fun filter() {
        assertTrue(PandaStream.of(values)
                .filter { it == "2" }
                .head()
                .isDefined)
    }

    @Test
    fun `shuffle has same elements`() {
        val shuffled = PandaStream.of(values).shuffle().toList()

        assertEquals(values.size, shuffled.size)
        assertTrue(shuffled.containsAll(values))
    }

    @Timeout(1)
    @Test
    fun `shuffle is shuffling`() {
        while (PandaStream.of(values).shuffle().toList() == values) {}
    }

    @Test
    fun skip() {
        assertEquals(2L, PandaStream.of(values)
                .skip(1L)
                .count())
    }

    @Test
    fun find() {
        assertTrue(PandaStream.of(values)
            .find { it == "2" }
            .isDefined)
    }

    @Test
    fun head() {
        assertEquals("1", PandaStream.of(values).head().get())
    }

    @Test
    fun count() {
        assertEquals(3, PandaStream.of(values).count())
        assertEquals(1, PandaStream.of(values).count { it == "2" })
    }

    @Test
    fun `take while`() {
        assertArrayEquals(arrayOf(1, 2), PandaStream.of(1, 2, 3, 4, 5)
            .takeWhile { it  < 3 }
            .toArray { arrayOfNulls<Int>(it) })
    }

    @Test
    fun `to shuffled list has same elements`() {
        val shuffled = PandaStream.of(values).toShuffledList()

        assertEquals(values.size, shuffled.size)
        assertTrue(shuffled.containsAll(values))
    }

    @Timeout(1)
    @Test
    fun `to shuffled list is shuffled`() {
        while (PandaStream.of(values).toShuffledList() == values) {}
    }

    @Test
    fun `to map`() {
        val map = PandaStream.of(values)
            .toMapByPair { panda.std.Pair(it, it.toInt()) }

        assertEquals(1, map["1"])
        assertEquals(2, map["2"])
        assertEquals(3, map["3"])
    }

    @Test
    fun `to stream`() {
        assertArrayEquals(numbers, PandaStream.of(values)
                .map { it.toInt() }
                .toStream()
                .sorted()
                .toArray { arrayOfNulls<Int>(it) })
    }

    @Test
    fun of() {
        val stream = values.stream()
        assertEquals(stream, PandaStream.of(stream).toStream())
    }

    @Test
    fun `flat of`() {
        val collectionOne = listOf(1, 2, 3)
        val collectionTwo = setOf(3, 4, 5)
        val resultList = listOf(1, 2, 3, 3, 4, 5)


        // collections
        val collectionOfIterables = PandaStream.flatOf(listOf(
            collectionOne,
            collectionTwo
        )).toList()
        assertEquals(resultList, collectionOfIterables)

        // iterables
        val iterableOfIterables = PandaStream.flatOf(listOf(
            collectionOne,
            collectionTwo
        ).asIterable()).toList()
        assertEquals(resultList, iterableOfIterables)

        // array
        val arrayOfIterables = PandaStream.flatOf(
            collectionOne,
            collectionTwo
        ).toList()
        assertEquals(resultList, arrayOfIterables)
    }

    @Test
    fun empty() {
        assertTrue(PandaStream.empty<Any>().toList().isEmpty())
    }

    @Test
    fun search() {
        val success = PandaStream.of("a", "b", "c").search { panda.std.Result.`when`(it == "b", it, it) }
        assertTrue(success.isOk)
        assertEquals("b", success.get())

        val error = PandaStream.of("a", "b", "c").search { panda.std.Result.`when`(it == "d", it, it) }
        assertTrue(error.isErr)
        assertEquals(listOf("a", "b", "c"), error.error)
    }

}