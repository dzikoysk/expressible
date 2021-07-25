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
final class MonoTest {

    private final Mono<String> mono = Mono.of('test')

    @Test
    void 'should keep order of associated values' () {
        assertEquals 'test', mono.getFirst()
    }

    @Test
    void 'should create proper pair after adding a value'() {
        assertEquals Pair.of('test', 'test'), mono.add('test')
    }

    @Test
    void 'should display formatted values' () {
        assertEquals  "['test']", mono.toString()
    }

    @Test
    void 'should implement equals & hashcode' () {
        assertEquals mono, mono
        assertNotEquals null, mono
        assertNotEquals new Object(), mono

        def same = Mono.of('test')
        assertEquals same, mono
        assertEquals same.hashCode(), mono.hashCode()

        def different = Mono.of('other')
        assertNotEquals different, mono
        assertNotEquals different.hashCode(), mono.hashCode()
    }

}
