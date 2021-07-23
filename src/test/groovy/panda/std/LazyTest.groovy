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

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

import java.util.function.Supplier

import static org.junit.jupiter.api.Assertions.*

@CompileStatic
final class LazyTest {

    private final Lazy<Object> lazy = new Lazy<>({ new Object() } as Supplier)

    @Test
    void 'should be initialized after first get' () {
        assertFalse lazy.isInitialized()
        lazy.get()
        assertTrue lazy.isInitialized()
    }

    @Test
    void 'should return the same value every time' () {
        def firstGet = lazy.get()
        def secondGet = lazy.get()
        assertSame firstGet, secondGet
    }

}