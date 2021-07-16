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

package panda.std.collection;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public final class ReversedIterable<T> implements Iterable<T> {

    private final List<T> list;

    public ReversedIterable(List<T> list) {
        this.list = list;
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return new ReversedIterator();
    }

    final class ReversedIterator implements Iterator<T> {

        private int index = list.size();

        @Override
        public boolean hasNext() {
            return index > 0;
        }

        @Override
        public T next() {
            return list.get(--index);
        }

    }

}