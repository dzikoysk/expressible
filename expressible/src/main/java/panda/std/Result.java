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
import panda.std.function.ThrowingFunction;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Result<VALUE, ERROR>  {

    private final VALUE value;
    private final ERROR error;

    private Result(@Nullable VALUE value, @Nullable ERROR error) {
        this.value = value;
        this.error = error;
    }

    public static <VALUE, ERROR> Result<VALUE, ERROR> ok(VALUE value) {
        return new Result<>(value, null);
    }

    public static <VALUE, ERROR> Result<VALUE, ERROR> error(ERROR err) {
        return new Result<>(null, err);
    }

    public static <VALUE, ERROR> Result<VALUE, ERROR> when(boolean condition, Supplier<VALUE> value, Supplier<ERROR> err) {
        return condition ? ok(value.get()) : error(err.get());
    }

    public static <VALUE, ERROR> Result<VALUE, ERROR> when(boolean condition, VALUE value, ERROR err) {
        return condition ? ok(value) : error(err);
    }

    public <MAPPED_VALUE> Result<MAPPED_VALUE, ERROR> map(Function<VALUE, MAPPED_VALUE> function) {
        return isOk() ? ok(function.apply(value)) : error(error);
    }

    public <MAPPED_ERROR> Result<VALUE, MAPPED_ERROR> mapErr(Function<ERROR, MAPPED_ERROR> function) {
        return isOk() ? ok(value) : error(function.apply(error));
    }

    public <MAPPED_VALUE> Result<MAPPED_VALUE, ERROR> flatMap(Function<VALUE, Result<MAPPED_VALUE, ERROR>> function) {
        return isOk() ? function.apply(value) : error(error);
    }

    public Result<VALUE, ERROR> filter(Predicate<VALUE> filter, Supplier<ERROR> errorSupplier) {
        return isOk() && !filter.test(value) ? error(errorSupplier.get()) : this;
    }

    public <COMMON> COMMON merge(Function<VALUE, COMMON> valueMerge, Function<ERROR, COMMON> errorMerge) {
        return isOk() ? valueMerge.apply(get()) : errorMerge.apply(getError());
    }

    public <REQUIRED_ERROR>  Result<VALUE, REQUIRED_ERROR> projectToValue() {
        if (isErr()) {
            throw new IllegalStateException("Cannot project result with error to value");
        }

        return ok(this.get());
    }

    public <REQUIRED_VALUE> Result<REQUIRED_VALUE, ERROR> projectToError() {
        if (isOk()) {
            throw new IllegalStateException("Cannot project result with value to error");
        }

        return error(error);
    }

    public Result<VALUE, ERROR> orElse(Function<ERROR, Result<VALUE, ERROR>> orElse) {
        return isOk() ? this : orElse.apply(error);
    }

    public VALUE orElseGet(Function<ERROR, VALUE> orElse) {
        return isOk() ? value : orElse.apply(error);
    }

    public <T extends Exception> VALUE orElseThrow(ThrowingFunction<ERROR, T, T> consumer) throws T {
        if (isOk()) {
            return get();
        }

        throw consumer.apply(getError());
    }

    public Result<VALUE, ERROR> peek(Consumer<VALUE> consumer) {
        if (isOk()) {
            consumer.accept(value);
        }

        return this;
    }

    public Result<VALUE, ERROR> onError(Consumer<ERROR> consumer) {
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

    public VALUE get() {
        if (isErr()) {
            throw new NoSuchElementException("No value present");
        }

        return value;
    }

    public ERROR getError() {
        if (isOk()) {
            throw new NoSuchElementException("No error present");
        }

        return error;
    }

    public Object getAny() {
        return isOk() ? value : error;
    }

    @SuppressWarnings("unchecked")
    public <AS> AS getAnyAs() {
        return (AS) getAny();
    }

    public Option<VALUE> toOption() {
        return Option.of(value);
    }

    public @Nullable VALUE orNull() {
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
        return Objects.hash(value, error);
    }

    @Override
    public String toString() {
        return "Result{" + (isOk() ? "VALUE=" + value : "ERR=" + error) + "}";
    }

}
