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

import org.jetbrains.annotations.Nullable;
import panda.std.function.ThrowingSupplier;
import panda.std.stream.PandaStream;
import panda.std.collection.SingletonIterator;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Option<T> implements Iterable<T>, Serializable {

    private static final Option<?> NONE = new Option<>(null);

    protected @Nullable T value;

    protected Option(@Nullable T value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public static <T> Option<T> none() {
        return (Option<T>) NONE;
    }

    public static <T> Option<T> of(@Nullable T value) {
        return value != null? new Option<>(value) : none();
    }

    @SuppressWarnings({ "OptionalUsedAsFieldOrParameterType" })
    public static <T> Option<T> ofOptional(Optional<T> optional) {
        return of(optional.orElse(null));
    }

    public static <T> Option<Completable<T>> withCompleted(T value) {
        return Option.of(Completable.completed(value));
    }

    public static <T> Option<T> when(boolean condition, @Nullable T value) {
        return when(condition, () -> value);
    }

    public static <T> Option<T> when(boolean condition, Supplier<@Nullable T> valueSupplier) {
        return condition ? of(valueSupplier.get()) : Option.none();
    }

    public static <T> Option<T> flatWhen(boolean condition, Option<T> value) {
        return condition ? value : none();
    }

    public static <T> Option<T> flatWhen(boolean condition, Supplier<Option<T>> supplier) {
        return condition ? supplier.get() : none();
    }

    public static <T, E extends Throwable> Option<T> attempt(Class<E> throwableType, ThrowingSupplier<T, E> supplier) throws AttemptFailedException {
        try {
            return of(supplier.get());
        } catch (Throwable throwable) {
            if (throwableType.isAssignableFrom(throwable.getClass())) {
                return Option.none();
            }

            throw new AttemptFailedException(throwable);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(@Nullable Object to) {
        if (!(to instanceof Option)) {
            return false;
        }

        return Objects.equals(value, ((Option<?>) to).value);
    }

    @Override
    public String toString() {
        return isEmpty() ? "Option{EMPTY}" : "Option{'" + value + "'}";
    }

    @Override
    public Iterator<T> iterator() {
        return isDefined() ? new SingletonIterator<>(value) : Collections.emptyIterator();
    }

    public Option<T> filter(Predicate<T> predicate) {
        return (isDefined() && predicate.test(value)) ? this : Option.none();
    }

    public Option<T> filterNot(Predicate<T> predicate) {
        return filter(value -> !predicate.test(value));
    }

    public <R> Option<R> map(Function<T, R> function) {
        return isDefined() ? Option.of(function.apply(value)) : Option.none();
    }

    public <R> Option<R> flatMap(Function<T, Option<R>> function) {
        return isDefined() ? function.apply(value) : Option.none();
    }

    @SafeVarargs
    public final <R> Option<R> match(Case<T, R>... cases) {
        return match(Arrays.asList(cases));
    }

    public <R> Option<R> match(List<? extends Case<T, R>> cases) {
        for (Case<T, R> currentCase : cases) {
            if (currentCase.getCondition().test(value)) {
                return Option.of(currentCase.getValue().apply(value));
            }
        }

        return Option.none();
    }

    public <T> Option<T> is(Class<T> type) {
        return this
                .filter(type::isInstance)
                .map(type::cast);
    }

    public Option<T> peek(Consumer<T> consumer) {
        if (isDefined()) {
            consumer.accept(value);
        }

        return this;
    }

    public Option<T> onEmpty(Runnable runnable) {
        if (isEmpty()) {
            runnable.run();
        }

        return this;
    }

    public Option<T> orElse(T value) {
        return isDefined() ? this : of(value);
    }

    public Option<T> orElse(Option<T> value) {
        return isDefined() ? this : value;
    }

    public Option<T> orElse(Supplier<Option<T>> supplier) {
        return isDefined() ? this : supplier.get();
    }

    public <E extends Throwable> T orThrow(Supplier<E> exceptionSupplier) throws E {
        if (isEmpty()) {
            throw exceptionSupplier.get();
        }

        return value;
    }

    public T orElseGet(T elseValue) {
        return isDefined() ? value : elseValue;
    }

    public T orElseGet(Supplier<T> supplier) {
        return isDefined() ? value : supplier.get();
    }

    public @Nullable T getOrNull() {
        return value;
    }

    public T get() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("Value is not defined");
        }

        return value;
    }

    public boolean isPresent() {
        return isDefined();
    }

    public boolean isDefined() {
        return value != null;
    }

    public boolean isEmpty() {
        return value == null;
    }

    public <R> PandaStream<R> toStream(Function<T, Stream<R>> function) {
        return isDefined() ? PandaStream.of(function.apply(value)) : PandaStream.empty();
    }

    public PandaStream<T> toStream() {
        return PandaStream.of(toJavaStream());
    }

    public Stream<T> toJavaStream() {
        return isDefined() ? Stream.of(value) : Stream.empty();
    }

    public <E> Result<T, E> toResult(E orElse) {
        return toResult(() -> orElse);
    }

    public <E> Result<T, E> toResult(Supplier<E> orElse) {
        return isDefined() ? Result.ok(get()) : Result.error(orElse.get());
    }

    public Optional<T> toOptional() {
        return Optional.ofNullable(value);
    }

}
