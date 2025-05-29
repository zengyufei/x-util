package com.zyf.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

// 内部类，封装流操作
public final class MapListStream<K, V> {
    private final Map<K, List<V>> map;

    public MapListStream(Map<K, List<V>> map) {
        this.map = map;
    }

    public Map<K, List<V>> toMap() {
        return map;
    }

    public List<V> getValues(K key) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return new ArrayList<>();
    }

//        public <R> MapStream<K, R> apply(Function<ListStream<V>, R> func) {
//            final Map<K, R> newMap = new LinkedHashMap<>();
//            for (Map.Entry<K, List<V>> entry : map.entrySet()) {
//                final K key = entry.getKey();
//                final List<V> values = entry.getValue();
//                R apply = func.apply(new ListStream<>(values));
//                newMap.put(key, apply);
//            }
//            return new MapStream<>(newMap);
//        }

//        public <R> MapStream<K, List<R>> valueStream(Function<ListStream<V>, List<R>> func) {
//            final Map<K, List<R>> newMap = new LinkedHashMap<>();
//            for (Map.Entry<K, List<V>> entry : map.entrySet()) {
//                final K key = entry.getKey();
//                final List<V> values = entry.getValue();
//                List<R> rList = new ArrayList<>(func.apply(new ListStream<>(values)));
//                newMap.put(key, rList);
//            }
//            return new MapStream<>(newMap);
//        }

//        public <R> MapStream<K, Map<K, List<V>>> valueStreamGroupByMap(Function<ListStream<V>, Map<K, List<V>>> func) {
//            final Map<K, Map<K, List<V>>> newMap = new LinkedHashMap<>();
//            for (Map.Entry<K, List<V>> entry : map.entrySet()) {
//                final K key = entry.getKey();
//                final List<V> values = entry.getValue();
//                newMap.put(key, func.apply(new ListStream<>(values)));
//            }
//            return new MapStream<>(newMap);
//        }

    public <R> MapStream<K, R> valueStream(Function<ListStream<V>, R> func) {
        final Map<K, R> newMap = new LinkedHashMap<>();
        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
            final K key = entry.getKey();
            final List<V> values = entry.getValue();
            newMap.put(key, func.apply(new ListStream<>(values)));
        }
        return new MapStream<>(newMap);
    }
}
