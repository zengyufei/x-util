package com.zyf.util;

import java.io.*;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class X {

    @SafeVarargs
    public static <T> List<T> asList(T... elements) {
        if (elements == null || elements.length == 0) {
            return new ArrayList<>();
        }
        final List<T> list = new ArrayList<>(elements.length);
        Collections.addAll(list, elements);
        return list;
    }

    @SafeVarargs
    public static <T> ListStream<T> listOf(T... elements) {
        return ListStream.of(asList(elements));
    }

    public static <T> ListStream<T> list(List<T> list) {
        return ListStream.of(list == null ? new ArrayList<>() : list);
    }


    public static <K, V> void hasKey(Map<K, V> map, K key, Consumer<V> has, CheckedRunnable noHas) {
        if (map.containsKey(key)) {
            final V v = map.get(key);
            has.accept(v);
        } else {
            try {
                noHas.run();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static <K, V> Map<K, V> asMap(K k, V v) {
        final Map<K, V> map = new HashMap<>();
        map.put(k, v);
        return map;
    }

    public static <K, V> MapStream<K, V> map(K k, V v) {
        final Map<K, V> map = new HashMap<>();
        map.put(k, v);
        return new MapStream<>(map);
    }


    public static <V> Op<V> op(V v) {
        return new Op<>(v);
    }

    /**
     * 根据某个比较逻辑，计算出两个列表之前的差别，返回一个数组，数组中依次为 符合条件的新增条目列表，符合条件的删除条目列表，符合条件的未变更条目列表
     *
     * @param <T>          待处理列表中元素的类型
     * @param oldList      旧的列表数据，不能为 null
     * @param newList      新的列表数据，不能为 null
     * @param keyExtractor 用来判断元素是否为同一个元素的比较逻辑
     * @return 包含三个 List 的数组，分别为 新增元素列表，删除元素列表，未变更元素列表
     */
    public static <T> Diff<T> getDiff(List<T> oldList,
                                      List<T> newList,
                                      BiFunction<T, T, Boolean> keyExtractor) {
        // 计算交集
        List<T> existsList = oldList.stream()
                .filter(s -> newList.stream().anyMatch(t -> keyExtractor.apply(t, s)))
                .collect(Collectors.toCollection(ArrayList::new));

        Map<T, T> map = new LinkedHashMap<>();
        for (T t1 : existsList) {
            for (T t2 : newList) {
                if (keyExtractor.apply(t1, t2)) {
                    map.put(t1, t2);
                    break;
                }
            }
        }

        // 计算差集（新增的对象，即在新列表中，但是不在旧列表中的对象）
        List<T> addList = newList.stream()
                .filter(s -> existsList.stream().noneMatch(t -> keyExtractor.apply(t, s)))
                .collect(Collectors.toCollection(ArrayList::new));

        // 计算差集（删除的对象，即在旧列表中，但是不在新列表中的对象）
        List<T> delList = oldList.stream()
                .filter(s -> existsList.stream().noneMatch(t -> keyExtractor.apply(t, s)))
                .collect(Collectors.toCollection(ArrayList::new));
        return new Diff<T>().setAddList(addList).setDelList(delList).setUpdateMap(map);
    }

    /**
     * 根据某个比较逻辑，计算出两个列表之前的差别，返回一个数组，数组中依次为 符合条件的新增条目列表，符合条件的删除条目列表，符合条件的未变更条目列表
     *
     * @param <T>          待处理列表中元素的类型
     * @param oldList      旧的列表数据，不能为 null
     * @param newList      新的列表数据，不能为 null
     * @param keyExtractor 用来判断元素是否为同一个元素的比较逻辑
     * @return 包含三个 List 的数组，分别为 新增元素列表，删除元素列表，未变更元素列表
     */
    public static <T, R> Diff2<T, R> getDiff2(List<T> oldList,
                                              List<R> newList,
                                              BiFunction<T, R, Boolean> keyExtractor) {
        // 计算交集
        List<T> existsList = oldList.stream()
                .filter(s -> newList.stream().anyMatch(t -> keyExtractor.apply(s, t)))
                .collect(Collectors.toCollection(ArrayList::new));

        Map<T, R> map = new LinkedHashMap<>();
        for (T t : existsList) {
            for (R r : newList) {
                if (keyExtractor.apply(t, r)) {
                    map.put(t, r);
                    break;
                }
            }
        }

        // 计算差集（新增的对象，即在新列表中，但是不在旧列表中的对象）
        List<R> addList = newList.stream()
                .filter(s -> existsList.stream().noneMatch(t -> keyExtractor.apply(t, s)))
                .collect(Collectors.toCollection(ArrayList::new));

        // 计算差集（删除的对象，即在旧列表中，但是不在新列表中的对象）
        List<T> delList = oldList.stream()
                .filter(s -> newList.stream().noneMatch(t -> keyExtractor.apply(s, t)))
                .collect(Collectors.toCollection(ArrayList::new));

        return new Diff2<T, R>().setAddList(addList).setDelList(delList).setUpdateMap(map);
    }

    public static <T> T clone(T obj) {
        if (!(obj instanceof Serializable)) {
            throw new RuntimeException("对象没有实现 Serializable 接口");
        }
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.close();
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            Object newObj = ois.readObject();
            ois.close();
            return (T) newObj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 静态方法，返回一个流的包装类
    public static <T> Try<T> tryBegin(CheckedFunction0<T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        try {
            return new Try.Success<>(supplier.apply());
        } catch (Throwable t) {
            return new Try.Failure<>(t);
        }
    }

    public static <T> Try<T> trySupplier(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return tryBegin(supplier::get);
    }

    public static Try<Void> tryRun(CheckedRunnable runnable) {
        Objects.requireNonNull(runnable, "runnable is null");
        try {
            runnable.run();
            return new Try.Success<>(null); // null represents the absence of an value, i.e. Void
        } catch (Throwable t) {
            return new Try.Failure<>(t);
        }
    }














}
