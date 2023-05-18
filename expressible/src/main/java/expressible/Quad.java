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

public class Quad<A, B, C, D> {

    private final A first;
    private final B second;
    private final C third;
    private final D fourth;

    public Quad(A first, B second, C third, D fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    public static <A, C, B, D> Quad<A, B, C, D> of(A first, B second, C third, D fourth) {
        return new Quad<>(first, second, third, fourth);
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    public C getThird() {
        return third;
    }

    public D getFourth() {
        return fourth;
    }

    @Override
    public boolean equals(Object to) {
        if (this == to) {
            return true;
        }

        if (to == null || getClass() != to.getClass()) {
            return false;
        }

        Quad<?, ?, ?, ?> quad = (Quad<?, ?, ?, ?>) to;

        return Objects.equals(first, quad.first)
                && Objects.equals(second, quad.second)
                && Objects.equals(third, quad.third)
                && Objects.equals(fourth, quad.fourth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third, fourth);
    }

    @Override
    public String toString() {
        return "['" + first + "', '" + second + "', '" + third + "', '" + fourth + "']";
    }

}
