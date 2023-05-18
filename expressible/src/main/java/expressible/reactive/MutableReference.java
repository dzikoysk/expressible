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

package expressible.reactive;

import org.jetbrains.annotations.NotNull;
import java.util.function.Function;

public class MutableReference<V> extends Reference<V> {

    public MutableReference(V value) {
        super(value);
    }

    public MutableReference<V> update(V value) {
        set(value);
        return this;
    }

    public MutableReference<V> update(Function<V, V> function) {
        set(function.apply(get()));
        return this;
    }

    public static <T> @NotNull MutableReference<T> mutableReference(T value) {
        return new MutableReference<>(value);
    }

}
