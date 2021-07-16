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

package panda

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

import panda.function.ThrowingFunction

import java.util.function.Function
import java.util.stream.Collectors

import static org.junit.jupiter.api.Assertions.assertEquals

@CompileStatic
final class ThrowingFunctionTest {

    @Test
    void 'should represent throwing function with exception handler as a standard function' () {
        List<String> data = Arrays.asList("1", "b", "2")
        ThrowingFunction<String, Integer, NumberFormatException> parseFunction = element -> Integer.parseInt(element.toString())
        Function<NumberFormatException, Integer> errorHandler = exception -> -1

        List<Integer> numbers = data.stream()
                .map(ThrowingFunction.asFunction(parseFunction, errorHandler))
                .collect(Collectors.toList())

        assertEquals(Arrays.asList(1, -1, 2), numbers)
    }

}