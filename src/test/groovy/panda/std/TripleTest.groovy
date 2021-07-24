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
final class TripleTest {

    private final Triple<String, String, String> triple = Triple.of('test', 'test', 'test')

    @Test
    void 'should keep order of associated values' () {
        assertEquals 'test', triple.getFirst()
        assertEquals 'test', triple.getSecond()
        assertEquals 'test', triple.getThird()
    }

    @Test
    void 'should create proper quad after adding a value'() {
        assertEquals Quad.of('test', 'test', 'test', 'test'), triple.add('test')
    }

    @Test
    void 'should display formatted values' () {
        assertEquals  "['test', 'test', 'test']", triple.toString()
    }

    @Test
    void 'should support equals & hashcode' () {
        assertEquals triple, triple

        def same = Triple.of('test', 'test', 'test')
        assertEquals same, triple
        assertEquals same.hashCode(), triple.hashCode()

        def different = Triple.of('other', 'other', 'other')
        assertNotEquals different, triple
        assertNotEquals different.hashCode(), triple.hashCode()
    }

}
