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

class MonoTest {

    private val mono = Mono.of("test")

    @Test
    fun shouldKeepOrderOfAssociatedValues() {
        assertEquals("test", mono.getFirst())
    }

    @Test
    fun shouldCreateProperPairAfterAddingAValue() {
        assertEquals(Pair.of("test", "test"), mono.add("test"))
    }

    @Test
    fun shouldDisplayFormattedValues() {
        assertEquals("['test']", mono.toString())
    }

    @Test
    @SuppressWarnings("ChangeToOperator")
    fun shouldImplementEqualsAndHashcode() {
        assertEquals(mono, mono)
        assertFalse(mono.equals(null))
        assertFalse(mono.equals(Object()))

        val same = Mono.of("test")
        assertEquals(same, mono)
        assertEquals(same.hashCode(), mono.hashCode())

        val different = Mono.of("other")
        assertNotEquals(different, mono)
        assertNotEquals(different.hashCode(), mono.hashCode())
    }

}
