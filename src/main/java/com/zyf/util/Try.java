package com.zyf.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Try<T> {

    public abstract boolean isFailure();

    public abstract T get();

    public abstract Throwable getCause();

    public abstract boolean isEmpty();

    public abstract boolean isSuccess();

    public abstract String stringPrefix();

    public final Try<T> andThen(CheckedRunnable runnable) {
        Objects.requireNonNull(runnable, "runnable is null");
        if (isFailure()) {
            return this;
        } else {
            try {
                runnable.run();
                return this;
            } catch (Throwable t) {
                return new Failure<>(t);
            }
        }
    }

    public final Try<T> andThen(CheckedConsumer<? super T> consumer) {
        Objects.requireNonNull(consumer, "consumer is null");
        if (isFailure()) {
            return this;
        } else {
            try {
                consumer.accept(get());
                return this;
            } catch (Throwable t) {
                return new Failure<>(t);
            }
        }
    }

    public final Try<T> onFailure(Consumer<? super Throwable> action) {
        Objects.requireNonNull(action, "action is null");
        if (isFailure()) {
            action.accept(getCause());
        }
        return this;
    }

    public final Try<T> onFailure(boolean isThrow) {
        if (isThrow && isFailure()) {
            throw new RuntimeException(getCause());
        }
        return this;
    }

    public final <E extends Throwable> Try<T> onFailure(Function<? super Throwable, E> action) {
        Objects.requireNonNull(action, "action is null");
        if (isFailure()) {
            final E apply = action.apply(getCause());
            throw new RuntimeException(apply);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public final <X extends Throwable> Try<T> onFailure(Class<X> exceptionType, Consumer<? super X> action) {
        Objects.requireNonNull(exceptionType, "exceptionType is null");
        Objects.requireNonNull(action, "action is null");
        if (isFailure() && exceptionType.isAssignableFrom(getCause().getClass())) {
            action.accept((X) getCause());
        }
        return this;
    }


    public final Try<T> andFinally(CheckedRunnable runnable) {
        Objects.requireNonNull(runnable, "runnable is null");
        try {
            runnable.run();
            return this;
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    @Deprecated
    public static final class Success<T> extends Try<T> implements Serializable {

        private static final long serialVersionUID = 1L;

        private final T value;

        /**
         * Constructs a Success.
         *
         * @param value The value of this Success.
         */
        Success(T value) {
            this.value = value;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public Throwable getCause() {
//                throw new UnsupportedOperationException("getCause on Success");
            return null;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean isFailure() {
            return false;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof Success && Objects.equals(value, ((Success<?>) obj).value));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }

        @Override
        public String stringPrefix() {
            return "Success";
        }

        @Override
        public String toString() {
            return stringPrefix() + "(" + value + ")";
        }
    }

    /**
     * A failed Try.
     *
     * @param <T> component type of this Failure
     * @deprecated will be removed from the public API
     */
    @Deprecated
    public static final class Failure<T> extends Try<T> implements Serializable {

        private static final long serialVersionUID = 1L;

        private final Throwable cause;

        /**
         * Constructs a Failure.
         *
         * @param cause A cause of type Throwable, may not be null.
         * @throws NullPointerException if {@code cause} is null
         * @throws Throwable            if the given {@code cause} is fatal, i.e. non-recoverable
         */
        Failure(Throwable cause) {
            Objects.requireNonNull(cause, "cause is null");
            if (TryModule.isFatal(cause)) {
                TryModule.sneakyThrow(cause);
            }
            this.cause = cause;
        }

        @Override
        public T get() {
            return TryModule.sneakyThrow(cause);
        }

        @Override
        public Throwable getCause() {
            return cause;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean isFailure() {
            return true;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof Failure && Arrays.deepEquals(cause.getStackTrace(), ((Failure<?>) obj).cause.getStackTrace()));
        }

        @Override
        public String stringPrefix() {
            return "Failure";
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(cause.getStackTrace());
        }

        @Override
        public String toString() {
            return stringPrefix() + "(" + cause + ")";
        }

    }

}
