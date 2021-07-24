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
final class PairTest {

    private final Pair<String, String> pair = Pair.of('test', 'test')

    @Test
    void 'should keep order of associated values' () {
        assertEquals 'test', pair.getFirst()
        assertEquals 'test', pair.getSecond()
    }

    @Test
    void 'should create proper triple after adding a value'() {
        assertEquals Triple.of('test', 'test', 'test'), pair.add('test')
    }

    @Test
    void 'should display formatted values' () {
        assertEquals  "['test', 'test']", pair.toString()
    }

    @Test
    void 'should support equals & hashcode' () {
        assertEquals pair, pair

        def same = Pair.of('test', 'test')
        assertEquals same, pair
        assertEquals same.hashCode(), pair.hashCode()

        def different = Pair.of('other', 'other')
        assertNotEquals different, pair
        assertNotEquals different.hashCode(), pair.hashCode()
    }

}
