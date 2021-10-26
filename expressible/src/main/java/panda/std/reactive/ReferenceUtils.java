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

package panda.std.reactive;

public final class ReferenceUtils {

    private ReferenceUtils() {}

    /**
     * Bypass immutability of Reference to e.g. initialize value.
     * This method should be considered as unsafe, and it shouldn't be exposed to final user of your api
     *
     * @param reference the reference to update
     * @param value the value to set
     * @param <T> type of value
     */
    public static <T> void setValue(Reference<T> reference, T value) {
        reference.set(value);
    }

}
