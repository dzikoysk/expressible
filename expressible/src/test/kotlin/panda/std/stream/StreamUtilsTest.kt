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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class StreamUtilsTest {

    private val collection = listOf(5, 2, 5)

    @Test
    fun sum() {
        assertEquals(12, StreamUtils.sum(collection) { it.toLong() })
    }

    @Test
    fun count() {
        assertEquals(3, StreamUtils.count(collection) { it < 10 })
    }

    @Test
    fun findFirst() {
        assertEquals(2, StreamUtils.findFirst(collection) { it == 2 }
            .orElse(-1))
    }

    @Test
    fun sumLongs() {
        assertEquals(10, StreamUtils.sumLongs(listOf(5L, 5L)) { it })
    }

    @Test
    fun map() {
        val mapped = StreamUtils.map<String, Any>(collection) { it.toString() }

        assertEquals(collection.size, mapped.size)
        assertTrue(mapped.contains("5"))
    }

}
