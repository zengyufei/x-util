package com.zyf.util;

@FunctionalInterface
public interface CheckedFunction0<R> {
    R apply() throws Throwable;
}