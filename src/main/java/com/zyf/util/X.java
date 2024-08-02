package com.zyf.util;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;
import java.util.stream.Collectors;

public final class X {

    public static <T> Diff<T> diff(List<T> oldList,
                                   List<T> newList,
                                   BiFunction<T, T, Boolean> keyExtractor) {
        // 计算交集
        List<T> updateList = oldList.stream()
                .filter(s -> newList.stream().anyMatch(t -> keyExtractor.apply(t, s)))
                .collect(Collectors.toCollection(ArrayList::new));

        // 计算差集（新增的对象，即在新列表中，但是不在旧列表中的对象）
        List<T> addList = newList.stream()
                .filter(s -> updateList.stream().noneMatch(t -> keyExtractor.apply(t, s)))
                .collect(Collectors.toCollection(ArrayList::new));

        // 计算差集（删除的对象，即在旧列表中，但是不在新列表中的对象）
        List<T> delList = oldList.stream()
                .filter(s -> updateList.stream().noneMatch(t -> keyExtractor.apply(t, s)))
                .collect(Collectors.toCollection(ArrayList::new));
        return new Diff<T>().setAddList(addList).setUpdateList(updateList).setDelList(delList);
    }

    public static <T, R> Diff2<T, R> diff(List<T> oldList,
                                          List<R> newList,
                                          BiFunction<T, T, Boolean> keyExtractor1,
                                          BiFunction<T, R, Boolean> keyExtractor2) {
        // 计算交集
        List<T> updateList = oldList.stream()
                .filter(s -> newList.stream().anyMatch(t -> keyExtractor2.apply(s, t)))
                .collect(Collectors.toCollection(ArrayList::new));

        // 计算差集（新增的对象，即在新列表中，但是不在旧列表中的对象）
        List<R> addList = newList.stream()
                .filter(s -> updateList.stream().noneMatch(t -> keyExtractor2.apply(t, s)))
                .collect(Collectors.toCollection(ArrayList::new));

        // 计算差集（删除的对象，即在旧列表中，但是不在新列表中的对象）
        List<T> delList = oldList.stream()
                .filter(s -> updateList.stream().noneMatch(t -> keyExtractor1.apply(t, s)))
                .collect(Collectors.toCollection(ArrayList::new));
        return new Diff2<T, R>().setAddList(addList).setUpdateList(updateList).setDelList(delList);
    }

    // 静态方法，返回一个流的包装类
    public static <T> Try<T> Try(CheckedFunction0<T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        try {
            return new Try.Success<>(supplier.apply());
        } catch (Throwable t) {
            return new Try.Failure<>(t);
        }
    }

    public static <T> Try<T> TrySupplier(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return Try(supplier::get);
    }

    public static <T> Try<T> TryCallable(Callable<? extends T> callable) {
        Objects.requireNonNull(callable, "callable is null");
        return Try(callable::call);
    }

    public static Try<Void> TryRun(CheckedRunnable runnable) {
        Objects.requireNonNull(runnable, "runnable is null");
        try {
            runnable.run();
            return new Try.Success<>(null); // null represents the absence of an value, i.e. Void
        } catch (Throwable t) {
            return new Try.Failure<>(t);
        }
    }

    public static Try<Void> TryRunnable(Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable is null");
        return TryRun(runnable::run);
    }


    // 静态方法，返回一个流的包装类
    public static <T> ListWrapper<T> l(List<T> list) {
        if (list == null || list.isEmpty()) {
            list = new ArrayList<>();
        }
        return new ListWrapper<>(list);
    }

    public static <T> ListWrapper<T> of(T element) {
        Objects.requireNonNull(element, "elements is null");
        final List<T> list = new ArrayList<>();
        list.add(element);
        return new ListWrapper<>(list);
    }

    @SafeVarargs
    public static <T> List<T> of(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        final List<T> list = new ArrayList<>();
        for (int i = elements.length - 1; i >= 0; i--) {
            list.add(elements[i]);
        }
        return list;
    }


    // 静态方法，返回一个流的包装类
    public static <K, V> MapListWrapper<K, V> m(Map<K, List<V>> map) {
        return new MapListWrapper<>(map);
    }

    // 内部类，封装流操作
    public final static class MapListWrapper<K, V> {
        private final Map<K, List<V>> map;

        public MapListWrapper(Map<K, List<V>> map) {
            this.map = map;
        }

        // 扁平化处理 Map 的方法
//        @SuppressWarnings("unused")
//        public ListWrapper<V> flatMap() {
//            List<V> result = new ArrayList<>();
//            if (MapUtil.isNotEmpty(map)) {
//                result.addAll(map.values());
//            }
//            return new ListWrapper<>(result);
//        }

        //        public <E> MapListWrapper<K, E> mapValues(Function<List<V>, List<E>> func) {
//            final Map<K, List<E>> newMap = new LinkedHashMap<>();
//            for (Map.Entry<K, List<V>> entry : map.entrySet()) {
//                final K key = entry.getKey();
//                final List<V> values = entry.getValue();
//                List<E> rList = new ArrayList<>(func.apply(values));
//                newMap.put(key, rList);
//            }
//            return new MapListWrapper<>(newMap);
//        }
        public <E> MapListWrapper<K, E> mapValues(Function<XList<V>, List<E>> func) {
            final Map<K, List<E>> newMap = new LinkedHashMap<>();
            for (Map.Entry<K, List<V>> entry : map.entrySet()) {
                final K key = entry.getKey();
                final List<V> values = entry.getValue();
                List<E> rList = new ArrayList<>(func.apply(new XList<>(values)));
                newMap.put(key, rList);
            }
            return new MapListWrapper<>(newMap);
        }

        public Map<K, List<V>> map() {
            return map;
        }

    }

    // 内部类，封装流操作
    public final static class ListWrapper<T> {
        private final List<T> list;

        public ListWrapper(List<T> list) {
            this.list = list;
        }

        public ListWrapper<T> add(T t) {
            final List<T> newList = new ArrayList<>(list);
            newList.add(t);
            return new ListWrapper<>(newList);
        }

        public ListWrapper<T> addAll(List<T> ts) {
            final List<T> newList = new ArrayList<>(list);
            newList.addAll(ts);
            return new ListWrapper<>(newList);
        }

        public ListWrapper<T> add(int index, T t) {
            final List<T> newList = new ArrayList<>(list);
            newList.add(index, t);
            return new ListWrapper<>(newList);
        }

        @SafeVarargs
        public final ListWrapper<T> add(T... ts) {
            final List<T> newList = new ArrayList<>(list);
            newList.addAll(of(ts));
            return new ListWrapper<>(newList);
        }

        public ListWrapper<T> skip(int skipIndex) {
            return new ListWrapper<>(list.subList(skipIndex, list.size()));
        }

        public ListWrapper<T> sub(int subLen) {
            return new ListWrapper<>(list.subList(0, subLen));
        }

        public ListWrapper<T> sub(int subBegin, int subEnd) {
            return new ListWrapper<>(list.subList(subBegin, subEnd + 1));
        }

        public ListWrapper<T> reversed() {
            return new ListWrapper<>(new AbstractList<>() {
                @Override
                public T get(int index) {
                    return list.get(list.size() - 1 - index);
                }

                @Override
                public int size() {
                    return list.size();
                }
            });
        }

        // 过滤方法
        @SafeVarargs
        public final ListWrapper<T> filters(Predicate<T>... predicates) {
            List<T> result = new ArrayList<>();
            if (isNotEmpty()) {
                result = list.stream()
                        .filter(item -> Arrays.stream(predicates).allMatch(predicate -> predicate.test(item)))
                        .collect(Collectors.toCollection(ArrayList::new));
            }
            return new ListWrapper<>(result);
        }

        // 过滤或的实现
        @SafeVarargs
        public final ListWrapper<T> filterOrs(Predicate<T>... predicates) {
            List<T> result = new ArrayList<>();
            if (isNotEmpty()) {
                result = list.stream()
                        .filter(item -> Arrays.stream(predicates).anyMatch(predicate -> predicate.test(item)))
                        .collect(Collectors.toCollection(ArrayList::new));
            }
            return new ListWrapper<>(result);
        }

        // 过滤或的实现
        @SafeVarargs
        public final ListWrapper<T> ors(Predicate<T>... predicates) {
            List<T> result = new ArrayList<>();
            if (isNotEmpty()) {
                result = list.stream()
                        .filter(item -> Arrays.stream(predicates).anyMatch(predicate -> predicate.test(item)))
                        .collect(Collectors.toCollection(ArrayList::new));
            }
            return new ListWrapper<>(result);
        }

        // 过滤或的实现
        @SafeVarargs
        public final ListWrapper<T> or(Function<XItem<T>, Boolean>... predicates) {
            List<T> result = new ArrayList<>();
            if (isNotEmpty()) {
                result = list.stream()
                        .filter(item -> Arrays.stream(predicates).anyMatch(predicate -> predicate.apply(new XItem(item))))
                        .collect(Collectors.toCollection(ArrayList::new));
            }
            return new ListWrapper<>(result);
        }

        // 过滤或的实现
        @SuppressWarnings("unused")
        public boolean anyMatch(Predicate<T> predicate) {
            return list.stream().anyMatch(predicate);
        }

        public ListWrapper<T> gt(Function<T, Integer> func, Integer num) {
            List<T> result = new ArrayList<>();
            if (isNotEmpty()) {
                result = list.stream().filter(t -> func.apply(t) > num).collect(Collectors.toList());
            }
            return new ListWrapper<>(result);
        }

        public ListWrapper<T> gt(Function<T, Long> func, Long num) {
            List<T> result = new ArrayList<>();
            if (isNotEmpty()) {
                result = list.stream().filter(t -> func.apply(t) > num).collect(Collectors.toList());
            }
            return new ListWrapper<>(result);
        }

        // 过滤或的实现
        @SuppressWarnings("unused")
        public boolean noneMatch(Predicate<T> predicate) {
            return list.stream().noneMatch(predicate);
        }

        public ListWrapper<T> peek(Consumer<T> consumer) {
            List<T> result = new ArrayList<>();
            if (isNotEmpty()) {
                result = list.stream()
                        .map(X::clone)
                        .peek(consumer)
                        .collect(Collectors.toCollection(ArrayList::new));
            }
            return new ListWrapper<>(result);
        }

        public long count() {
            return list.size();
        }

        // 过滤非空的方法
        @SafeVarargs
        public final ListWrapper<T> filterNotNull(Function<T, ?>... getters) {
            return filterNotBlank(getters);
        }


        // 过滤非空的方法
        @SafeVarargs
        public final ListWrapper<T> filterIsNull(Function<T, ?>... getters) {
            return filterBlank(getters);
        }

        // 过滤非空的方法
        @SafeVarargs
        public final ListWrapper<T> isNotNull(Function<T, ?>... getters) {
            return filterNotBlank(getters);
        }

        // 过滤非空的方法
        @SafeVarargs
        public final ListWrapper<T> isNull(Function<T, ?>... getters) {
            return filterBlank(getters);
        }

        // 过滤非空的方法
        @SafeVarargs
        public final ListWrapper<T> isNotBlank(Function<T, ?>... getters) {
            return filterNotBlank(getters);
        }

        // 过滤非空的方法
        @SafeVarargs
        public final ListWrapper<T> isBlank(Function<T, ?>... getters) {
            return filterBlank(getters);
        }

        // 过滤非空的方法
        @SafeVarargs
        public final ListWrapper<T> filterNotBlank(Function<T, ?>... getters) {
            List<T> result = new ArrayList<>();
            if (isNotEmpty()) {
                result = list.stream()
                        .filter(item -> Arrays.stream(getters).allMatch(getter -> {
                            Object value = getter.apply(item);
                            if (value != null) {
                                if (value instanceof CharSequence) {
                                    return !value.toString().isEmpty();
                                }
                                return true;
                            }
                            return false;
                        }))
                        .collect(Collectors.toCollection(ArrayList::new));
            }
            return new ListWrapper<>(result);
        }

        // 过滤非空的方法
        @SafeVarargs
        public final ListWrapper<T> filterBlank(Function<T, ?>... getters) {
            List<T> result = new ArrayList<>();
            if (isNotEmpty()) {
                result = list.stream()
                        .filter(item -> Arrays.stream(getters).allMatch(getter -> {
                            Object value = getter.apply(item);
                            if (value == null) {
                                return true;
                            }
                            return (value instanceof CharSequence) && value.toString().isEmpty();
                        }))
                        .collect(Collectors.toCollection(ArrayList::new));
            }
            return new ListWrapper<>(result);
        }

        // 映射方法
        public <R> ListWrapper<R> map(Function<T, R> mapper) {
            List<R> result = new ArrayList<>();
            if (isNotEmpty()) {
                result = list.stream()
                        .map(mapper)
                        .collect(Collectors.toCollection(ArrayList::new));
            }
            return new ListWrapper<>(result);
        }

        // 转换为 List
        public List<T> list() {
            return new ArrayList<>(list);
        }

        // 去重方法，按指定属性去重
        public ListWrapper<T> distinct(Function<T, ?> keyExtractor) {
            Set<Object> seen = ConcurrentHashMap.newKeySet();
            List<T> result = new ArrayList<>();
            if (isNotEmpty()) {
                result = list.stream()
                        .filter(t -> seen.add(keyExtractor.apply(t)))
                        .collect(Collectors.toCollection(ArrayList::new));
            }
            return new ListWrapper<>(result);
        }

        // 去重方法（重载）
        public ListWrapper<T> distinct() {
            List<T> result = new ArrayList<>();
            if (isNotEmpty()) {
                result = list.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
            }
            return new ListWrapper<>(result);
        }

        // 按照指定的键进行分组
        public <K> MapListWrapper<K, T> groupBy(Function<T, K> classifier) {
            final Map<K, List<T>> map = list.stream().collect(Collectors.groupingBy(classifier));
            return new MapListWrapper<>(map);
        }


        // 获取第一个元素
        public T findFirst() {
            return list.get(0);
        }

        // 转换为 Map
        public <K> Map<K, T> toMap(Function<T, K> keyMapper) {
            return list.stream().collect(Collectors.toMap(keyMapper, e -> e, (a, b) -> a));
        }

        // 转换为 Map
        public <K, V> Map<K, V> toMap(Function<T, K> keyMapper, Function<T, V> valueMapper) {
            return list.stream().collect(Collectors.toMap(keyMapper, valueMapper));
        }

        // 转换为 Map
        @SuppressWarnings("unused")
        public <K, V> Map<K, V> toMap(Function<T, K> keyMapper, Function<T, V> valueMapper, BinaryOperator<V> mergeExtractor) {
            return list.stream().collect(Collectors.toMap(keyMapper, valueMapper, mergeExtractor));
        }

        // 排序方法
        @SuppressWarnings("unused")
        public ListWrapper<T> sort(Comparator<T> comparator) {
            List<T> result = list.stream()
                    .sorted(comparator)
                    .collect(Collectors.toCollection(ArrayList::new));
            return new ListWrapper<>(result);
        }

        // 排序方法（带枚举）
        public <U extends Comparable<? super U>> ListWrapper<T> sort(Function<? super T, ? extends U> keyExtractor, Sort order, Sort nullPosition) {
            Comparator<T> comparator;
            if (nullPosition == Sort.NullLast) {
                comparator = Comparator.comparing(keyExtractor, Comparator.nullsLast(U::compareTo));
            } else {
                comparator = Comparator.comparing(keyExtractor, Comparator.nullsFirst(U::compareTo));
            }

            if (order == Sort.Desc) {
                comparator = comparator.reversed();
            }

            Comparator<T> finalComparator = comparator;
            List<T> result = list.stream()
                    .collect(
                            Collectors.collectingAndThen(Collectors.toCollection(() ->
                                    new TreeSet<>(finalComparator)
                            ), ArrayList::new));

            return new ListWrapper<>(result);
        }

        // 扁平化方法
        @SuppressWarnings("unused")
        public <R> List<R> flatMap(Function<T, List<R>> function) {
            if (isEmpty()) {
                return new ArrayList<>();
            }

            // 去掉 null 值并进行扁平化处理
            return list.stream()
                    .flatMap(item -> function.apply(item).stream()) // 扁平化处理
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        private boolean isEmpty() {
            return list == null || list.isEmpty();
        }

        private boolean isNotEmpty() {
            return !isEmpty();
        }

    }

    public static class XItem<V> {

        private final V item;

        public XItem(V item) {
            this.item = item;
        }

        public boolean gt(Function<V, Long> func, Long num) {
            return func.apply(item) > num;
        }

        public boolean gt(Function<V, Integer> func, Integer num) {
            return func.apply(item) > num;
        }

        public boolean gt(Function<V, Double> func, Double num) {
            return func.apply(item) > num;
        }

        public boolean gt(Function<V, BigDecimal> func, BigDecimal num) {
            return func.apply(item).compareTo(num) > 0;
        }

        public boolean ge(Function<V, Long> func, Long num) {
            return func.apply(item) >= num;
        }

        public boolean ge(Function<V, Integer> func, Integer num) {
            return func.apply(item) >= num;
        }

        public boolean ge(Function<V, Double> func, Double num) {
            return func.apply(item) >= num;
        }

        public boolean ge(Function<V, BigDecimal> func, BigDecimal num) {
            return func.apply(item).compareTo(num) >= 0;
        }

        public boolean lt(Function<V, Long> func, Long num) {
            return func.apply(item) < num;
        }

        public boolean lt(Function<V, Integer> func, Integer num) {
            return func.apply(item) < num;
        }

        public boolean lt(Function<V, Double> func, Double num) {
            return func.apply(item) < num;
        }

        public boolean lt(Function<V, BigDecimal> func, BigDecimal num) {
            return func.apply(item).compareTo(num) < 0;
        }

        public boolean le(Function<V, Long> func, Long num) {
            return func.apply(item) <= num;
        }

        public boolean le(Function<V, Integer> func, Integer num) {
            return func.apply(item) <= num;
        }

        public boolean le(Function<V, Double> func, Double num) {
            return func.apply(item) <= num;
        }

        public boolean le(Function<V, BigDecimal> func, BigDecimal num) {
            return func.apply(item).compareTo(num) <= 0;
        }
    }

    public static class XList<V> {

        private final List<V> list;

        public XList(List<V> values) {
            this.list = values;
        }

        public <R> List<R> map(Function<V, R> func) {
            return list.stream()
                    .map(func)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

    }

    public static abstract class Try<T> {

        public abstract boolean isFailure();

        public abstract T get();

        public abstract Throwable getCause();

        public abstract boolean isEmpty();

        public abstract boolean isSuccess();

        public abstract String stringPrefix();


        public final Try<T> andThen(Consumer<? super T> consumer) {
            Objects.requireNonNull(consumer, "consumer is null");
            return andThenTry(consumer::accept);
        }

        public final Try<T> andThen(Runnable runnable) {
            Objects.requireNonNull(runnable, "runnable is null");
            return andThenTry(runnable::run);
        }

        public final Try<T> andThenTry(CheckedRunnable runnable) {
            Objects.requireNonNull(runnable, "runnable is null");
            if (isFailure()) {
                return this;
            } else {
                try {
                    runnable.run();
                    return this;
                } catch (Throwable t) {
                    return new Failure<>(t);
                }
            }
        }

        public final Try<T> andThenTry(CheckedConsumer<? super T> consumer) {
            Objects.requireNonNull(consumer, "consumer is null");
            if (isFailure()) {
                return this;
            } else {
                try {
                    consumer.accept(get());
                    return this;
                } catch (Throwable t) {
                    return new Failure<>(t);
                }
            }
        }

        public final Try<T> onFailure(Consumer<? super Throwable> action) {
            Objects.requireNonNull(action, "action is null");
            if (isFailure()) {
                action.accept(getCause());
            }
            return this;
        }

        public final Try<T> onFailure(boolean isThrow) {
            if (isThrow && isFailure()) {
                throw new RuntimeException(getCause());
            }
            return this;
        }

//        public final <E extends Throwable> Try<T> onFailure(Function<? super Throwable, E> action) {
//            Objects.requireNonNull(action, "action is null");
//            if (isFailure()) {
//                final E apply = action.apply(getCause());
//                throw new RuntimeException(apply);
//            }
//            return this;
//        }

        @SuppressWarnings("unchecked")
        public final <X extends Throwable> Try<T> onFailure(Class<X> exceptionType, Consumer<? super X> action) {
            Objects.requireNonNull(exceptionType, "exceptionType is null");
            Objects.requireNonNull(action, "action is null");
            if (isFailure() && exceptionType.isAssignableFrom(getCause().getClass())) {
                action.accept((X) getCause());
            }
            return this;
        }

        public final Try<T> andFinally(Runnable runnable) {
            Objects.requireNonNull(runnable, "runnable is null");
            return andFinallyTry(runnable::run);
        }

        public final Try<T> andFinallyTry(CheckedRunnable runnable) {
            Objects.requireNonNull(runnable, "runnable is null");
            try {
                runnable.run();
                return this;
            } catch (Throwable t) {
                return new Failure<>(t);
            }
        }

        @Deprecated
        public static final class Success<T> extends Try<T> implements Serializable {

            private static final long serialVersionUID = 1L;

            private final T value;

            /**
             * Constructs a Success.
             *
             * @param value The value of this Success.
             */
            private Success(T value) {
                this.value = value;
            }

            @Override
            public T get() {
                return value;
            }

            @Override
            public Throwable getCause() {
                throw new UnsupportedOperationException("getCause on Success");
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean isFailure() {
                return false;
            }

            @Override
            public boolean isSuccess() {
                return true;
            }

            @Override
            public boolean equals(Object obj) {
                return (obj == this) || (obj instanceof Success && Objects.equals(value, ((Success<?>) obj).value));
            }

            @Override
            public int hashCode() {
                return Objects.hashCode(value);
            }

            @Override
            public String stringPrefix() {
                return "Success";
            }

            @Override
            public String toString() {
                return stringPrefix() + "(" + value + ")";
            }
        }

        /**
         * A failed Try.
         *
         * @param <T> component type of this Failure
         * @deprecated will be removed from the public API
         */
        @Deprecated
        public static final class Failure<T> extends Try<T> implements Serializable {

            private static final long serialVersionUID = 1L;

            private final Throwable cause;

            /**
             * Constructs a Failure.
             *
             * @param cause A cause of type Throwable, may not be null.
             * @throws NullPointerException if {@code cause} is null
             * @throws Throwable            if the given {@code cause} is fatal, i.e. non-recoverable
             */
            private Failure(Throwable cause) {
                Objects.requireNonNull(cause, "cause is null");
                if (TryModule.isFatal(cause)) {
                    TryModule.sneakyThrow(cause);
                }
                this.cause = cause;
            }

            @Override
            public T get() {
                return TryModule.sneakyThrow(cause);
            }

            @Override
            public Throwable getCause() {
                return cause;
            }

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public boolean isFailure() {
                return true;
            }

            @Override
            public boolean isSuccess() {
                return false;
            }

            @Override
            public boolean equals(Object obj) {
                return (obj == this) || (obj instanceof Failure && Arrays.deepEquals(cause.getStackTrace(), ((Failure<?>) obj).cause.getStackTrace()));
            }

            @Override
            public String stringPrefix() {
                return "Failure";
            }

            @Override
            public int hashCode() {
                return Arrays.hashCode(cause.getStackTrace());
            }

            @Override
            public String toString() {
                return stringPrefix() + "(" + cause + ")";
            }

        }

    }

    // 排序顺序枚举
    public enum Sort {
        Asc, Desc,
        NullFirst, NullLast
    }

    @Data
    @Accessors(chain = true)
    public static class Diff<T> {
        private List<T> addList;
        private List<T> updateList;
        private List<T> delList;

        public List<T> getEffectList() {
            return X.l(updateList).addAll(addList).list();
        }

        public Diff<T> addList(Consumer<List<T>> consumer) {
            consumer.accept(addList);
            return this;
        }

        public Diff<T> updateList(Consumer<List<T>> consumer) {
            consumer.accept(updateList);
            return this;
        }

        public Diff<T> delList(Consumer<List<T>> consumer) {
            consumer.accept(delList);
            return this;
        }
    }

    @Data
    @Accessors(chain = true)
    public static class Diff2<T, R> {
        private List<R> addList;
        private List<T> updateList;
        private List<T> delList;

        public Diff2<T, R> addList(Consumer<List<R>> consumer) {
            consumer.accept(addList);
            return this;
        }

        public Diff2<T, R> updateList(Consumer<List<T>> consumer) {
            consumer.accept(updateList);
            return this;
        }

        public Diff2<T, R> delList(Consumer<List<T>> consumer) {
            consumer.accept(delList);
            return this;
        }
    }

    @FunctionalInterface
    public interface CheckedFunction0<R> {
        R apply() throws Throwable;
    }

    @FunctionalInterface
    public interface CheckedConsumer<T> {
        void accept(T t) throws Throwable;
    }

    @FunctionalInterface
    public interface CheckedRunnable {
        void run() throws Throwable;
    }

    interface TryModule {

        static boolean isFatal(Throwable throwable) {
            return throwable instanceof InterruptedException
                    || throwable instanceof LinkageError
                    || throwable instanceof ThreadDeath
                    || throwable instanceof VirtualMachineError;
        }

        // DEV-NOTE: we do not plan to expose this as public API
        @SuppressWarnings("unchecked")
        static <T extends Throwable, R> R sneakyThrow(Throwable t) throws T {
            throw (T) t;
        }

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
}
