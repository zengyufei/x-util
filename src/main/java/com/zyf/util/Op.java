package com.zyf.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class Op<V> {

    private final V v;

    public Op(V v) {
        this.v = v;
    }

    public <R> Op<R> map(Function<V, R> func) {
        R r = func.apply(v);
        return new Op<>(r);
    }

    public boolean isNotBlank() {
        return !isBlank();
    }

    public void isBlank(Consumer<V> consumer) {
        if (isBlank()) {
            consumer.accept(v);
        }
    }

    public void isBlankOrElse(Consumer<V> consumer, Runnable runnable) {
        if (isBlank()) {
            consumer.accept(v);
        } else {
            runnable.run();
        }
    }


    public void isNotBlank(Consumer<V> consumer) {
        if (isNotBlank()) {
            consumer.accept(v);
        }
    }

    public void isNotBlankOrElse(Consumer<V> consumer, Runnable runnable) {
        if (isNotBlank()) {
            consumer.accept(v);
        } else {
            runnable.run();
        }
    }

    public boolean isBlank() {
        if (v == null) {
            return true;
        } else if (v instanceof Optional) {
            return ((Optional<?>) v).isEmpty();
        } else if (v instanceof CharSequence vStr) {
            return vStr.length() == 0;
        } else if (v instanceof Collection) {
            return ((Collection<?>) v).isEmpty();
        } else if (v.getClass().isArray()) {
            return Array.getLength(v) == 0;
        } else if (v instanceof Map) {
            return ((Map<?, ?>) v).isEmpty();
        }
        // else
        return false;
    }

    public V get() {
        if (v == null) {
            throw new NoSuchElementException("No value present");
        }
        return v;
    }

    public V get(V defaultValue) {
        if (v == null) {
            return defaultValue;
        }
        return v;
    }
}
