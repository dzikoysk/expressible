/*
 * Copyright (c) 2021 dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

package panda.std.collection

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ReversedIterableTest {

    @Test
    fun `next should move to the next element in order from the back & return it`() {
        val reversedIterable = ReversedIterable(listOf("a", "b", "c"))
        val iterator = reversedIterable.iterator()

        assertTrue(iterator.hasNext())
        assertEquals("c", iterator.next())
        assertEquals("b", iterator.next())
        assertEquals("a", iterator.next())
        assertFalse(iterator.hasNext())
    }

}
