package com.zyf.util;

import java.util.*;

public class ListPair<T, U, S extends Pair<T, U>> {

    private final Iterable<S> source;

    ListPair(Iterable<S> source) {
        this.source = source;
    }

    public static <T, U, S extends Pair<T, U>> ListPair<T, U, S> of(Iterable<S> source) {
        final Iterable<S> pairs = Objects.requireNonNull(source);
        return new ListPair<>(pairs);
    }

    public List<Pair<T, U>> toList() {
        // 如果是Collection类型，直接返回size
        if (source instanceof List<S>) {
            return (List<Pair<T, U>>) source;
        }
        List<Pair<T, U>> result = new ArrayList<>();
        source.forEach(result::add);
        return result;
    }

    public Map<T, U> toMap() {
        final Map<T, U> map = new HashMap<>();
        source.forEach(pair -> map.put(pair.getFirst(), pair.getSecond()));
        return map;
    }
}
