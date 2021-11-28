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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import panda.std.function.ThrowingFunction;
import panda.std.function.ThrowingSupplier;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * {@link panda.std.Result} represents value or associated error that caused the absence of the expected value.
 * By definition, Result has to contain one non-null value - the value or the error.
 * If you want to use nullable value or nullable error, you have to use wrapper like {@link panda.std.Option} to explicitly declare it.
 *
 * @param <VALUE> type of value
 * @param <ERROR> type of error
 */
public class Result<VALUE, ERROR>  {

    private final VALUE value;
    private final ERROR error;

    private Result(@Nullable VALUE value, @Nullable ERROR error) {
        if (value == null && error == null) {
            throw new IllegalStateException("Value and error are null - Cannot determine state of Result");
        }

        if (value != null && error != null) {
            throw new IllegalStateException("Value and error are not null - Cannot determine state of Result");
        }

        this.value = value;
        this.error = error;
    }

    public static <VALUE, ERROR> @NotNull Result<VALUE, ERROR> ok(@NotNull VALUE value) {
        return new Result<>(value, null);
    }

    public static <ERROR> @NotNull Result<Unit, ERROR> ok() {
        return new Result<>(Unit.UNIT, null);
    }

    public static <VALUE, ERROR> @NotNull Result<VALUE, ERROR> error(@NotNull ERROR err) {
        return new Result<>(null, err);
    }

    public static <VALUE> @NotNull Result<VALUE, Unit> error() {
        return new Result<>(null, Unit.UNIT);
    }

    public static <VALUE, ERROR> @NotNull Result<VALUE, ERROR> when(boolean condition, @NotNull Supplier<@NotNull VALUE> value, @NotNull Supplier<@NotNull ERROR> err) {
        return condition ? ok(value.get()) : error(err.get());
    }

    public static <VALUE, ERROR> @NotNull Result<VALUE, ERROR> when(boolean condition, @NotNull VALUE value, @NotNull ERROR err) {
        return condition ? ok(value) : error(err);
    }

    @SuppressWarnings("unchecked")
    public static <VALUE, ERROR extends Throwable> @NotNull Result<VALUE, ERROR> attempt(
            @NotNull Class<? extends ERROR> exceptionType,
            @NotNull ThrowingSupplier<@NotNull VALUE, @NotNull ERROR> supplier
    ) throws AttemptFailedException {
        try {
            return Result.ok(supplier.get());
        } catch (Throwable throwable) {
            if (exceptionType.isAssignableFrom(throwable.getClass())) {
                return Result.error((ERROR) throwable);
            }

            throw new AttemptFailedException(throwable);
        }
    }

    public <MAPPED_VALUE> @NotNull Result<MAPPED_VALUE, ERROR> map(@NotNull Function<@NotNull VALUE, @NotNull MAPPED_VALUE> function) {
        return isOk() ? ok(function.apply(get())) : projectToError();
    }

    public @NotNull Result<Unit, ERROR> mapToUnit() {
        return isOk() ? ok() : projectToError();
    }

    public <MAPPED_ERROR> @NotNull Result<VALUE, MAPPED_ERROR> mapErr(@NotNull Function<@NotNull ERROR, @NotNull MAPPED_ERROR> function) {
        return isOk() ? projectToValue() : error(function.apply(getError()));
    }

    public <MAPPED_VALUE> @NotNull Result<MAPPED_VALUE, ERROR> flatMap(@NotNull Function<@NotNull VALUE, @NotNull Result<MAPPED_VALUE, ? extends ERROR>> function) {
        //noinspection unchecked
        return isOk()
                ? (Result<MAPPED_VALUE, ERROR>) function.apply(get())
                : projectToError();
    }

    public <MAPPED_ERROR> @NotNull Result<VALUE, MAPPED_ERROR> flatMapErr(@NotNull Function<@NotNull ERROR, @NotNull Result<? extends VALUE, MAPPED_ERROR>> function) {
        //noinspection unchecked
        return isErr()
                ? (Result<VALUE, MAPPED_ERROR>) function.apply(getError())
                : projectToValue();
    }

    public @NotNull Result<VALUE, ERROR> filter(@NotNull Predicate<@NotNull VALUE> predicate, @NotNull Function<@NotNull VALUE, @NotNull ERROR> errorSupplier) {
        return isOk() && !predicate.test(get()) ? error(errorSupplier.apply(get())) : this;
    }

    public @NotNull Result<VALUE, ERROR> filterNot(@NotNull Predicate<VALUE> predicate, @NotNull Function<VALUE, ERROR> errorSupplier) {
        return filter(value -> !predicate.test(value), errorSupplier);
    }

    public <COMMON> COMMON fold(@NotNull Function<@NotNull VALUE, COMMON> valueMerge, @NotNull Function<@NotNull ERROR, COMMON> errorMerge) {
        return isOk() ? valueMerge.apply(get()) : errorMerge.apply(getError());
    }

    public <MAPPED_VALUE> @NotNull Result<MAPPED_VALUE, ERROR> is(@NotNull Class<MAPPED_VALUE> type, @NotNull Function<@NotNull VALUE, @NotNull ERROR> errorSupplier) {
        return this
                .filter(type::isInstance, errorSupplier)
                .map(type::cast);
    }

    public @NotNull Result<ERROR, VALUE> swap() {
        return isOk() ? error(get()) : ok(getError());
    }

    public Result<VALUE, ERROR> consume(@NotNull Consumer<@NotNull VALUE> valueConsumer, @NotNull Consumer<@NotNull ERROR> errorConsumer) {
        return this.peek(valueConsumer).onError(errorConsumer);
    }

    public <REQUIRED_ERROR> @NotNull Result<VALUE, REQUIRED_ERROR> projectToValue() {
        if (isErr()) {
            throw new IllegalStateException("Cannot project result with error to value");
        }

        return ok(get());
    }

    public <REQUIRED_VALUE> @NotNull Result<REQUIRED_VALUE, ERROR> projectToError() {
        if (isOk()) {
            throw new IllegalStateException("Cannot project result with value to error");
        }

        return error(getError());
    }

    public @NotNull Result<VALUE, ERROR> orElse(@NotNull Function<@NotNull ERROR, @NotNull Result<VALUE, ERROR>> orElse) {
        return isOk() ? this : orElse.apply(getError());
    }

    public @NotNull VALUE orElseGet(@NotNull Function<@NotNull ERROR, @NotNull VALUE> orElse) {
        return isOk() ? get() : orElse.apply(getError());
    }

    public <T extends Exception> @NotNull VALUE orElseThrow(@NotNull ThrowingFunction<@NotNull ERROR, @NotNull T, @NotNull T> consumer) throws T {
        if (isOk()) {
            return get();
        }

        throw consumer.apply(getError());
    }

    public @NotNull Result<VALUE, ERROR> peek(@NotNull Consumer<@NotNull VALUE> consumer) {
        if (isOk()) {
            consumer.accept(get());
        }

        return this;
    }

    public @NotNull Result<VALUE, ERROR> onError(@NotNull Consumer<@NotNull ERROR> consumer) {
        if (isErr()) {
            consumer.accept(getError());
        }

        return this;
    }

    public boolean isOk() {
        return value != null;
    }

    public boolean isErr() {
        return error != null;
    }

    public @NotNull VALUE get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }

        return value;
    }

    public @NotNull ERROR getError() {
        if (error == null) {
            throw new NoSuchElementException("No error present");
        }

        return error;
    }

    public @NotNull Object getAny() {
        //noinspection ConstantConditions
        return isOk() ? value : error;
    }

    @SuppressWarnings("unchecked")
    public <AS> @NotNull AS getAnyAs() {
        return (AS) getAny();
    }

    public @NotNull Option<VALUE> toOption() {
        return Option.of(value);
    }

    public @NotNull Option<ERROR> errorToOption() {
        return Option.of(error);
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
