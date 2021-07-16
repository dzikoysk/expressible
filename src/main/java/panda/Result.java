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

package panda;

import org.jetbrains.annotations.Nullable;
import panda.function.ThrowingFunction;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Result<V, E>  {

    private final V value;
    private final E error;

    private Result(@Nullable V value, @Nullable E error) {
        this.value = value;
        this.error = error;
    }

    public static <V, E> Result<V, E> ok(V value) {
        return new Result<>(value, null);
    }

    public static <V, E> Result<V, E> error(E err) {
        return new Result<>(null, err);
    }

    public static <V, E> Result<V, E> when(boolean condition, Supplier<V> value, Supplier<E> err) {
        return condition ? Result.ok(value.get()) : Result.error(err.get());
    }

    public static <V, E> Result<V, E> when(boolean condition, V value, E err) {
        return condition ? Result.ok(value) : Result.error(err);
    }

    public <R> R merge(Function<V, R> valueMerge, Function<E, R> errorMerge) {
        return isOk() ? valueMerge.apply(get()) : errorMerge.apply(getError());
    }

    public <R> Result<R, E> map(Function<V, R> function) {
        return isOk() ? Result.ok(function.apply(value)) : Result.error(error);
    }

    public <R> Result<V, R> mapErr(Function<E, R> function) {
        return isOk() ? Result.ok(value) : Result.error(function.apply(error));
    }

    public <R> Result<R, E> flatMap(Function<V, Result<R, E>> function) {
        return isOk() ? function.apply(value) : Result.error(error);
    }

    public Result<V, E> orElse(Function<E, Result<V, E>> orElse) {
        return isOk() ? this : orElse.apply(error);
    }

    public V orElseGet(Function<E, V> orElse) {
        return isOk() ? value : orElse.apply(error);
    }

    public <T extends Exception> V orElseThrow(ThrowingFunction<E, T, T> consumer) throws T {
        if (isOk()) {
            return get();
        }

        throw consumer.apply(getError());
    }

    public Result<V, E> peek(Consumer<V> consumer) {
        if (isOk()) {
            consumer.accept(value);
        }

        return this;
    }

    public Result<V, E> onError(Consumer<E> consumer) {
        if (isErr()) {
            consumer.accept(error);
        }

        return this;
    }

    public boolean isOk() {
        return value != null;
    }

    public boolean isErr() {
        return error != null;
    }

    public V get() {
        if (isErr()) {
            throw new NoSuchElementException("No value present");
        }

        return value;
    }

    public E getError() {
        if (isOk()) {
            throw new NoSuchElementException("No error present");
        }

        return error;
    }

    public Object getAny() {
        return isOk() ? value : error;
    }

    @SuppressWarnings("unchecked")
    public <T> T getAnyAs() {
        return (T) getAny();
    }

    public Option<V> toOption() {
        return Option.of(value);
    }

    public @Nullable V orNull() {
        return value;
    }

    @Override
    public boolean equals(Object to) {
        if (this == to) {
            return true;
        }

        if (to == null || getClass() != to.getClass()) {
            return false;
        }

        Result<?, ?> other = (Result<?, ?>) to;
        return Objects.equals(value, other.value) && Objects.equals(error, other.error);

    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (error != null ? error.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Result{" + (isOk() ? "VALUE=" + value : "ERR=" + error);
    }

}
