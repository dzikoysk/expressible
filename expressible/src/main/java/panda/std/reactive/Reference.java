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

import panda.std.Option;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Reference<V> implements Publisher<Reference<V>, V> {

    protected final Collection<DetailedSubscriber<? super V>> subscribers = new ArrayList<>();
    protected V value;

    public Reference(V value) {
        set(value);
    }

    void set(V newValue) {
        if (value == null) {
            throw new IllegalArgumentException("Reference does not support null values");
        }

        V oldValue = value;
        this.value = newValue;
        notifySubscribers(oldValue, newValue);
    }


    protected void notifySubscribers(V oldValue, V newValue) {
        subscribers.forEach(subscriber -> subscriber.onComplete(oldValue, newValue));
    }

    @Override
    public Reference<V> subscribe(Subscriber<? super V> subscriber) {
        return subscribe((oldValue, newValue) -> subscriber.onComplete(newValue), false);
    }

    public Reference<V> subscribe(Subscriber<? super V> subscriber, boolean immediately) {
        return subscribe((oldValue, newValue) -> subscriber.onComplete(newValue), immediately);
    }

    public Reference<V> subscribe(DetailedSubscriber<? super V> subscriber) {
        return subscribe(subscriber, false);
    }

    public Reference<V> subscribe(DetailedSubscriber<? super V> subscriber, boolean immediately) {
        if (immediately) {
            subscriber.onComplete(get(), get());
        }
        subscribers.add(subscriber);
        return this;
    }

    public V get() {
        return value;
    }

    public <R> R map(Function<V, R> function) {
        return function.apply(get());
    }

    public Reference<V> peek(Consumer<V> consumer) {
        consumer.accept(get());
        return this;
    }

    public Option<V> toOption() {
        return Option.of(get());
    }

    @SuppressWarnings("unchecked")
    public Class<V> getType() {
        return (Class<V>) value.getClass();
    }

    public static <T> Reference<T> reference(T value) {
        return new Reference<>(value);
    }

    public static class Dependencies {

        final List<Reference<?>> references;

        Dependencies(List<Reference<?>> references) {
            this.references = references;
        }

        public static Dependencies dependencies(List<Reference<?>> references) {
            return new Dependencies(references);
        }

        public static Dependencies dependencies(Reference<?>... references) {
             return new Dependencies(Arrays.asList(references));
        }

    }

    public static <T> Reference<T> computed(Dependencies dependencies, Supplier<T> recalculateFunction) {
        Reference<T> computed = reference(recalculateFunction.get());
        dependencies.references.forEach(reference -> reference.subscribe(newValue -> computed.set(recalculateFunction.get())));
        return computed;
    }

}
