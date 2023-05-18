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

package expressible;

import java.util.Objects;

public class Mono<A> {

    protected final A first;

    public Mono(A first) {
        this.first = first;
    }

    public static <A> Mono<A> of(A first) {
        return new Mono<>(first);
    }

    public <B> Pair<A, B> add(B second) {
        return Pair.of(first, second);
    }

    public A getFirst() {
        return first;
    }

    @Override
    public boolean equals(Object to) {
        if (this == to) {
            return true;
        }

        if (to == null || getClass() != to.getClass()) {
            return false;
        }

        Mono<?> toMono = (Mono<?>) to;
        return Objects.equals(first, toMono.first);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first);
    }

    @Override
    public String toString() {
        return "['" + first + "']";
    }

}
