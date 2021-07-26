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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Completable<VALUE> implements Publisher<Completable<VALUE>, VALUE> {

    private VALUE value;
    private boolean ready;
    private List<Subscriber<? super VALUE>> subscribers = new ArrayList<>(3);

    public Completable() {
        this.value = null;
    }

    public static <VALUE> Completable<VALUE> completed(VALUE value) {
        return new Completable<VALUE>().complete(value);
    }

    public static <VALUE> Completable<VALUE> create() {
        return new Completable<>();
    }

    public boolean isReady() {
        return ready;
    }

    public boolean isUnprepared() {
        return !isReady();
    }

    public VALUE get() {
        if (isReady()) {
            return value;
        }

        throw new IllegalStateException("Option has not been completed");
    }

    public <ERROR extends Exception> VALUE orThrow(Supplier<ERROR> exception) throws ERROR {
        if (isReady()) {
            return value;
        }

        throw exception.get();
    }

    @Override
    public Completable<VALUE> subscribe(Subscriber<? super VALUE> subscriber) {
        if (isReady()) {
            subscriber.onComplete(get());
        }
        else {
            subscribers.add(subscriber);
        }

        return this;
    }

    public Completable<VALUE> complete(VALUE value) {
        if (isReady()) {
            return this;
        }

        this.ready = true;
        this.value = Objects.requireNonNull(value);

        for (Subscriber<? super VALUE> subscriber : subscribers) {
            subscriber.onComplete(value);
        }

        subscribers = null;
        return this;
    }

    public Completable<VALUE> then(Consumer<? super VALUE> consumer) {
        subscribe(consumer::accept);
        return this;
    }

    public <R> Completable<R> thenApply(Function<? super VALUE, R> map) {
        Completable<R> mappedOption = new Completable<>();
        subscribe(completedValue -> mappedOption.complete(map.apply(completedValue)));
        return mappedOption;
    }

    public <R> Completable<R> thenCompose(Function<? super VALUE, ? extends Completable<R>> map) {
        Completable<R> mappedOption = new Completable<>();
        subscribe(completedValue -> map.apply(completedValue).then(mappedOption::complete));
        return mappedOption;
    }

    public CompletableFuture<VALUE> toFuture() {
        CompletableFuture<VALUE> future = new CompletableFuture<>();
        then(future::complete);
        return future;
    }

}
