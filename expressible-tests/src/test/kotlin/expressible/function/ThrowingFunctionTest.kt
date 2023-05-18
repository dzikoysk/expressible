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

package expressible.function

import org.junit.jupiter.api.Test
import java.util.stream.Collectors
import org.junit.jupiter.api.Assertions.assertEquals

class ThrowingFunctionTest {

    @Test
    fun `should represent throwing function with exception handler as a standard function`() {
        val data = listOf("1", "b", "2")
        val parseFunction: (String) -> Int = { it.toInt() }
        val errorHandler: (Exception) -> Int = { -1 }

        val numbers = data.stream()
                .map<Any>(expressible.function.ThrowingFunction.asFunction(parseFunction, errorHandler))
                .collect(Collectors.toList())

        assertEquals(listOf(1, -1, 2), numbers)
    }

    @Test
    fun `should return the same value through the identify function`() {
        val value = Object()
        assertEquals(value, expressible.function.ThrowingFunction.identity<Any, Exception>().apply(value))
    }

}