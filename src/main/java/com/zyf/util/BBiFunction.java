package com.zyf.util;

@FunctionalInterface
public interface BBiFunction<E, T, U, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     */
    R apply(E e, T t, U u);
}