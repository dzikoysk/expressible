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

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class LazyTest {

    private val lazy = Lazy<Any> { Object() }

    @Test
    fun `should be initialized after first get`() {
        assertFalse(lazy.isInitialized)
        lazy.get()
        assertTrue(lazy.isInitialized)
    }

    @Test
    fun `should return the same value every time`() {
        val firstGet = lazy.get()
        val secondGet = lazy.get()
        assertSame(firstGet, secondGet)
    }

    @Test
    fun `should execute runnable only once`() {
        var status = false
        val lazy = Lazy.ofRunnable { status = !status }
        lazy.get()
        lazy.get()
        assertTrue(status)
    }

    @Test
    fun `should be initialized instantly if value is given`() {
        assertTrue(Lazy("value").isInitialized)
    }

}