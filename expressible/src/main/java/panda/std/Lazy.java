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

package panda.std;

import java.util.function.Supplier;

public class Lazy<T> implements Supplier<T> {

    private Supplier<T> supplier;
    private boolean initialized;
    private T value;

    public Lazy(T value) {
        this.initialized = true;
        this.value = value;
    }

    public Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static Lazy<Void> ofRunnable(Runnable runnable) {
        return new Lazy<>(() -> {
            runnable.run();
            return null;
        });
    }

    @Override
    public synchronized T get() {
        if (initialized) {
            return value;
        }

        this.value = supplier.get();
        this.initialized = true;

        return value;
    }

    public boolean isInitialized() {
        return initialized;
    }

}
