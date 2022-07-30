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

package panda.std

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals

class PairTest {

    private val pair = Pair.of("test", "test")

    @Test
    fun shouldKeepOrderOfAssociatedValues() {
        assertEquals("test", pair.getFirst())
        assertEquals("test", pair.getSecond())
    }

    @Test
    fun shouldCreateProperTripleAfterAddingAValue() {
        assertEquals(Triple.of("test", "test", "test"), pair.add("test"))
    }

    @Test
    fun shouldDisplayFormattedValues() {
        assertEquals("['test', 'test']", pair.toString())
    }

    @Test
    @SuppressWarnings("ChangeToOperator")
    fun shouldSupportEqualsAndHashcode() {
        assertEquals(pair, pair)
        assertFalse(pair.equals(null))
        assertFalse(pair.equals(Object()))

        val same = Pair.of("test", "test")
        assertEquals(same, pair)
        assertEquals(same.hashCode(), pair.hashCode())

        val different = Pair.of("other", "other")
        assertNotEquals(different, pair)
        assertNotEquals(different.hashCode(), pair.hashCode())
    }

}
