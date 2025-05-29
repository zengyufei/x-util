package com.zyf.util;

@FunctionalInterface
public interface CheckedConsumer<T> {
    void accept(T t) throws Throwable;
}