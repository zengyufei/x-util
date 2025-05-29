package com.zyf.util;

@FunctionalInterface
public interface CheckedRunnable {
    void run() throws Throwable;
}