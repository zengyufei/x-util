package com.zyf.util;

import java.util.Map;
import java.util.function.Consumer;

// 内部类，封装流操作
public final class MapStream<K, V> {
    private final Map<K, V> map;

    public MapStream(Map<K, V> map) {
        this.map = map;
    }

    public MapStream<K, V> hasKey(K key, Consumer<V> consumer) {
        if (map.containsKey(key)) {
            consumer.accept(map.get(key));
        }
        return this;
    }

    public MapStream<K, V> hasKey(K key, V defaultValue, Consumer<V> consumer) {
        if (map.containsKey(key)) {
            consumer.accept(map.getOrDefault(key, defaultValue));
        } else {
            consumer.accept(defaultValue);
        }
        return this;
    }

    public MapStream<K, V> put(K k, V v) {
        map.put(k, v);
        return this;
    }

    public Map<K, V> toMap() {
        return map;
    }
}
