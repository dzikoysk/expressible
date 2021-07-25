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

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNotEquals

@CompileStatic
final class QuadTest {

    private final Quad<String, String, String, String> quad = Quad.of('test', 'test', 'test', 'test')

    @Test
    void 'should keep order of associated values' () {
        assertEquals 'test', quad.getFirst()
        assertEquals 'test', quad.getSecond()
        assertEquals 'test', quad.getThird()
        assertEquals 'test', quad.getFourth()
    }

    @Test
    void 'should display formatted values' () {
        assertEquals  "['test', 'test', 'test', 'test']", quad.toString()
    }

    @Test
    void 'should support equals & hashcode' () {
        assertEquals quad, quad
        assertNotEquals null, quad
        assertNotEquals new Object(), quad

        def same = Quad.of('test', 'test', 'test', 'test')
        assertEquals same, quad
        assertEquals same.hashCode(), quad.hashCode()

        def different = Quad.of('other', 'other', 'other', 'other')
        assertNotEquals different, quad
        assertNotEquals different.hashCode(), quad.hashCode()
    }

}
