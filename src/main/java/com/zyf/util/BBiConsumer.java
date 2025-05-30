package com.zyf.util;

@FunctionalInterface
public interface BBiConsumer<E, T, U> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     */
    void accept(E e, T t, U u);

}