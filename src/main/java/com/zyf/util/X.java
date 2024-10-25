package com.zyf.util;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.*;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.stream.Collectors;

public class X {

    @SafeVarargs
    public static <T> List<T> asList(T... elements) {
        if (elements == null || elements.length == 0) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(elements));
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

    // 静态工厂方法
    @SafeVarargs
    public static <T> ListStream<T> list(T... elements) {
        return ListStream.of(asList(elements));
    }

    // 静态工厂方法
    public static <T> ListStream<T> list(List<T> list) {
        return ListStream.of(list == null ? new ArrayList<>() : list);
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

    // 排序顺序枚举
    public enum Sort {
        Asc, Desc,
        NullFirst, NullLast
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

    public static class ListStream<T> {
        private final Iterable<T> source;

        private ListStream(Iterable<T> source) {
            this.source = source;
        }

        public static <T> ListStream<T> of(Iterable<T> source) {
            return new ListStream<>(Objects.requireNonNull(source));
        }

        public ListStream<T> add(T t) {
            final List<T> list = toList();
            list.add(t);
            return this;
        }


        public ListStream<T> add(int index, T t) {
            final List<T> list = toList();
            list.add(index, t);
            return this;
        }


        public ListStream<T> addAll(List<T> ts) {
            final List<T> list = toList();
            list.addAll(ts);
            return this;
        }

        public ListStream<T> concat(List<T> ts) {
            final List<T> list = toList();
            list.addAll(ts);
            return of(list);
        }

        @SafeVarargs
        public final ListStream<T> add(T... ts) {
            final List<T> list = toList();
            list.addAll(asList(ts));
            return this;
        }

        public ListStream<T> skip(int skipIndex) {
            AtomicInteger i = new AtomicInteger(1);
            return of(createFilteredIterable(elem -> i.getAndIncrement() > skipIndex));
        }


        /**
         * 返回一个新的 ListStream，包含原始列表的子列表，从索引 0 开始，长度为 subLen。
         *
         * @param subLen 子列表的长度
         * @return 新的 ListStream 包含指定长度的子列表
         * @throws IllegalArgumentException 如果 subLen 为负数
         */
        public ListStream<T> sub(int subLen) {
            if (subLen < 0) {
                throw new IllegalArgumentException("subLen must be non-negative");
            }
            return sub(0, subLen);
        }

        /**
         * 返回一个新的 ListStream，包含原始列表的子列表，从索引 subBegin 开始，到索引 subEnd 结束（不包含）。
         *
         * @param subBegin 子列表的起始索引（包含）
         * @param subEnd   子列表的结束索引（不包含）
         * @return 新的 ListStream 包含指定范围的子列表
         * @throws IllegalArgumentException 如果 subBegin 为负数，或者 subEnd 小于 subBegin
         */
        public ListStream<T> sub(int subBegin, int subEnd) {
            if (subBegin < 0) {
                throw new IllegalArgumentException("subBegin must be non-negative");
            }
            if (subEnd < subBegin) {
                throw new IllegalArgumentException("subEnd must not be less than subBegin");
            }

            return of(() -> new Iterator<T>() {
                private final Iterator<T> iterator = source.iterator();
                private int currentIndex = 0;

                @Override
                public boolean hasNext() {
                    return currentIndex < subEnd && iterator.hasNext();
                }

                @Override
                public T next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    T next = iterator.next();
                    currentIndex++;
                    if (currentIndex <= subBegin) {
                        return this.next(); // 跳过 subBegin 之前的元素
                    }
                    return next;
                }
            });
        }

        public ListStream<T> reversed() {
            // 反转列表
            List<T> list = toList();
            Collections.reverse(list);
            return of(list);
        }

        // 过滤或的实现
        @SafeVarargs
        @SuppressWarnings("unused")
        public final boolean anyMatch(Predicate<T>... predicates) {
            Iterable<T> filteredIterable = createFilteredIterable(elem ->
                    Arrays.stream(predicates).anyMatch(predicate -> predicate.test(elem)));
            return of(filteredIterable).isNotEmpty();
        }

        // 过滤或的实现
        @SafeVarargs
        @SuppressWarnings("unused")
        public final boolean noneMatch(Predicate<T>... predicates) {
            Iterable<T> filteredIterable = createFilteredIterable(elem ->
                    Arrays.stream(predicates).anyMatch(predicate -> predicate.test(elem)));
            return of(filteredIterable).isEmpty();
        }

        // 获取第一个元素
        public T findFirst() {
            Iterator<T> iterator = source.iterator();
            if (iterator.hasNext()) {
                return iterator.next();
            }
            return null;
        }

        public ListStream<T> limit(int size) {
            return of(() -> new Iterator<T>() {
                private final Iterator<T> iterator = source.iterator();
                private int doneSize = 0;

                @Override
                public boolean hasNext() {
                    final boolean hasNext = iterator.hasNext();
                    if (hasNext && doneSize < size) {
                        return hasNext;
                    }
                    return false;
                }

                @Override
                public T next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    T next = iterator.next();
                    doneSize++;
                    return next;
                }
            });
        }


        public String joining(CharSequence symbol) {
            StringJoiner sb = new StringJoiner(symbol);
            for (T t : source) {
                if (t instanceof CharSequence) {
                    sb.add((CharSequence) t);
                } else {
                    if (t != null) {
                        sb.add(t.toString());
                    }
                }
            }
            return sb.toString();
        }


        // 过滤或的实现
        @SafeVarargs
        public final ListStream<T> filterOrs(Predicate<T>... predicates) {
            return ors(predicates);
        }

        // 过滤或的实现
        @SafeVarargs
        public final ListStream<T> ors(Predicate<T>... predicates) {
            return of(createFilteredIterable(elem ->
                    Arrays.stream(predicates).anyMatch(predicate -> predicate.test(elem))));
        }

        public ListStream<T> filter(Predicate<? super T> predicate) {
            return filters(predicate);
        }

        @SafeVarargs
        public final ListStream<T> ands(Predicate<T>... predicates) {
            return filters(predicates);
        }

        @SafeVarargs
        public final ListStream<T> filters(Predicate<? super T>... predicates) {
            Objects.requireNonNull(predicates);
            return of(createFilteredIterable(elem ->
                    Arrays.stream(predicates).allMatch(predicate -> predicate.test(elem))));
        }

        @SafeVarargs
        public final ListStream<T> filterNull(Function<T, ?>... functions) {
            return filterBlank(functions);
        }

        @SafeVarargs
        public final ListStream<T> filterNotNull(Function<T, ?>... functions) {
            return filterNotBlank(functions);
        }

        @SafeVarargs
        public final ListStream<T> isNull(Function<T, ?>... getters) {
            return filterBlank(getters);
        }

        @SafeVarargs
        public final ListStream<T> isNotNull(Function<T, ?>... getters) {
            return filterNotBlank(getters);
        }

        @SafeVarargs
        public final ListStream<T> isBlank(Function<T, ?>... getters) {
            return filterBlank(getters);
        }

        @SafeVarargs
        public final ListStream<T> isNotBlank(Function<T, ?>... getters) {
            return filterNotBlank(getters);
        }

        @SafeVarargs
        public final ListStream<T> filterNotBlank(Function<T, ?>... functions) {
            return of(createFilteredIterable(elem -> isNotBlankElement(elem, functions)));
        }

        @SafeVarargs
        public final ListStream<T> filterBlank(Function<T, ?>... functions) {
            return of(createFilteredIterable(elem -> isBlankElement(elem, functions)));
        }

        private boolean isBlankElement(T elem, Function<T, ?>[] functions) {
            if (functions == null || functions.length == 0) {
                if (elem == null) {
                    return true;
                }
                if (elem instanceof CharSequence str) {
                    return str.isEmpty() || "".contentEquals(str);
                }
                return false;
            }

            return Arrays.stream(functions).allMatch(fun -> {
                Object value = fun.apply(elem);
                if (value == null) {
                    return true;
                }
                if (value instanceof CharSequence str) {
                    return str.isEmpty() || "".contentEquals(str);
                }
                return false;
            });
        }


        private boolean isNotBlankElement(T elem, Function<T, ?>[] functions) {
            if (functions == null || functions.length == 0) {
                if (elem == null) {
                    return false;
                }
                if (elem instanceof CharSequence str) {
                    return !str.isEmpty() && !"".contentEquals(str);
                }
                return true;
            }

            return Arrays.stream(functions).allMatch(fun -> {
                Object value = fun.apply(elem);
                if (value == null) {
                    return false;
                }
                if (value instanceof CharSequence) {
                    return !value.toString().isEmpty();
                }
                return true;
            });
        }

        private Iterable<T> createFilteredIterable(Predicate<T> filterCondition) {
            return () -> new Iterator<>() {
                final Iterator<T> iterator = source.iterator();
                T nextElement;
                boolean hasNextComputed = false;
                boolean hasNextResult = false;

                @Override
                public boolean hasNext() {
                    if (!hasNextComputed) {
                        computeNext(filterCondition);
                    }
                    return hasNextResult;
                }

                @Override
                public T next() {
                    if (!hasNextComputed) {
                        computeNext(filterCondition);
                    }
                    if (!hasNextResult) {
                        throw new NoSuchElementException();
                    }
                    hasNextComputed = false;
                    return nextElement;
                }

                private void computeNext(Predicate<T> condition) {
                    while (iterator.hasNext()) {
                        T elem = iterator.next();
                        if (condition.test(elem)) {
                            nextElement = elem;
                            hasNextResult = true;
                            hasNextComputed = true;
                            return;
                        }
                    }
                    hasNextResult = false;
                    hasNextComputed = true;
                }
            };
        }

        /**
         * 将元素转换为Map，使用keyMapper生成key
         * 如果有重复的key，保留最后一个值
         */
        public <K> Map<K, T> toMap(Function<T, K> keyMapper) {
            return toMap(keyMapper, Function.identity());
        }

        /**
         * 将元素转换为Map，使用keyMapper生成key，valueMapper生成value
         * 如果有重复的key，保留最后一个值
         */
        public <K, V> Map<K, V> toMap(Function<T, K> keyMapper, Function<T, V> valueMapper) {
            return toMap(keyMapper, valueMapper, (v1, v2) -> v2);
        }

        /**
         * 将元素转换为Map，使用keyMapper生成key，valueMapper生成value
         * 如果有重复的key，使用mergeFunction合并值
         */
        public <K, V> Map<K, V> toMap(
                Function<T, K> keyMapper,
                Function<T, V> valueMapper,
                BinaryOperator<V> mergeFunction) {
            Objects.requireNonNull(keyMapper, "keyMapper cannot be null");
            Objects.requireNonNull(valueMapper, "valueMapper cannot be null");
            Objects.requireNonNull(mergeFunction, "mergeFunction cannot be null");

            if (isEmpty()) {
                return new HashMap<>();
            }

            Map<K, V> result = new HashMap<>();
            for (T element : source) {
                if (element != null) {
                    K key = keyMapper.apply(element);
                    if (key != null) {
                        V value = valueMapper.apply(element);
                        result.merge(key, value, mergeFunction);
                    }
                }
            }
            return result;
        }

        /**
         * 将元素转换为Map，使用keyMapper生成key
         * 如果有重复的key，将值收集到List中
         */
        public <K> MapListStream<K, T> groupBy(Function<T, K> keyMapper) {
            return groupBy(keyMapper, Function.identity());
        }

        /**
         * 将元素转换为Map，使用keyMapper生成key，valueMapper生成value
         * 如果有重复的key，将值收集到List中
         */
        public <K, V> MapListStream<K, V> groupBy(
                Function<T, K> keyMapper,
                Function<T, V> valueMapper) {
            Objects.requireNonNull(keyMapper, "keyMapper cannot be null");
            Objects.requireNonNull(valueMapper, "valueMapper cannot be null");

            Map<K, List<V>> result = new HashMap<>();
            if (isEmpty()) {
                return new MapListStream<>(result);
            }

            for (T element : source) {
                if (element != null) {
                    K key = keyMapper.apply(element);
                    V value = valueMapper.apply(element);
                    result.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
                }
            }
            return new MapListStream<>(result);
        }

        /**
         * 将元素转换为LinkedHashMap，保持插入顺序
         */
        public <K> Map<K, T> toLinkedMap(Function<T, K> keyMapper) {
            return toLinkedMap(keyMapper, Function.identity());
        }

        /**
         * 将元素转换为LinkedHashMap，保持插入顺序
         */
        public <K, V> Map<K, V> toLinkedMap(
                Function<T, K> keyMapper,
                Function<T, V> valueMapper) {
            Objects.requireNonNull(keyMapper, "keyMapper cannot be null");
            Objects.requireNonNull(valueMapper, "valueMapper cannot be null");

            if (isEmpty()) {
                return new LinkedHashMap<>();
            }

            Map<K, V> result = new LinkedHashMap<>();
            for (T element : source) {
                if (element != null) {
                    K key = keyMapper.apply(element);
                    if (key != null) {
                        V value = valueMapper.apply(element);
                        result.put(key, value);
                    }
                }
            }
            return result;
        }

        public <R> ListStream<R> map(Function<? super T, ? extends R> mapper) {
            Objects.requireNonNull(mapper);
            return of(() -> new Iterator<>() {
                final Iterator<T> iterator = source.iterator();

                public boolean hasNext() {
                    return iterator.hasNext();
                }

                public R next() {
                    return mapper.apply(iterator.next());
                }
            });
        }

        public double sumDouble(Function<T, Number> mapper) {
            return sumBigDecimal(mapper).doubleValue();
        }

        public int sumInt(Function<T, Number> mapper) {
            return sumBigDecimal(mapper).intValue();
        }

        public long sumLong(Function<T, Number> mapper) {
            return sumBigDecimal(mapper).longValue();
        }

        public BigDecimal sumBigDecimal(Function<T, Number> mapper) {
            BigDecimal sum = new BigDecimal("0.0");
            for (T t : source) {
                Number r = mapper.apply(t);
                sum = sum.add(new BigDecimal(String.valueOf(r)));
            }
            return sum;
        }

        public Double sumDouble() {
            return sumBigDecimal().doubleValue();
        }

        public Integer sumInt() {
            return sumBigDecimal().intValue();
        }

        public Long sumLong() {
            return sumBigDecimal().longValue();
        }

        public BigDecimal sumBigDecimal() {
            BigDecimal sum = new BigDecimal("0.0");
            for (T t : source) {
                if (t instanceof Number) {
                    sum = sum.add(new BigDecimal(String.valueOf(t)));
                } else {
                    throw new IllegalArgumentException("不是数字,不能计算");
                }
            }
            return sum;
        }

        // 排序方法
        @SuppressWarnings("unused")
        public ListStream<T> sort(Comparator<T> comparator) {
            // 转换为List以进行排序
            List<T> sortedList = toList();
            // 执行排序
            sortedList.sort(comparator);
            return of(sortedList);
        }

        /**
         * 根据提取的比较键对元素进行排序
         *
         * @param keyExtractor 键提取函数
         * @param order        排序顺序（升序/降序）
         * @return 排序后的ListStream
         */
        public <U extends Comparable<? super U>> ListStream<T> sort(
                Function<? super T, ? extends U> keyExtractor,
                Sort order) {
            return sort(keyExtractor, order, Sort.NullLast);
        }

        /**
         * 根据提取的比较键对元素进行排序
         *
         * @param keyExtractor 键提取函数
         * @param order        排序顺序（升序/降序）
         * @param nullPosition 空值位置（前/后）
         * @return 排序后的ListStream
         */
        public <U extends Comparable<? super U>> ListStream<T> sort(
                Function<? super T, ? extends U> keyExtractor,
                Sort order,
                Sort nullPosition) {
            Objects.requireNonNull(keyExtractor, "keyExtractor cannot be null");
            Objects.requireNonNull(order, "order cannot be null");
            Objects.requireNonNull(nullPosition, "nullPosition cannot be null");

            if (isEmpty()) {
                return this;
            }

            SortStream<T> sortStream = new SortStream<>();

            // 转换为List以进行排序
            List<T> sortedList = toList();

            // 创建比较器
            Comparator<T> comparator = sortStream.createComparator(keyExtractor, order, nullPosition);

            // 执行排序
            sortedList.sort(comparator);

            return of(sortedList);
        }

        /**
         * 根据提取的比较键对元素进行排序
         *
         * @return 排序后的ListStream
         */
        @SafeVarargs
        public final <U extends Comparable<? super U>> ListStream<T> sort(Function<SortStream<T>, Comparator<T>>... streamOperation) {

            if (isEmpty()) {
                return this;
            }

            // 转换为List以进行排序
            List<T> sortedList = toList();

            Comparator<T> comparator = null;
            for (Function<SortStream<T>, Comparator<T>> comparatorFunction : streamOperation) {
                if (comparator == null) {
                    comparator = comparatorFunction.apply(new SortStream<>());
                } else {
                    comparator = comparator.thenComparing(comparatorFunction.apply(new SortStream<>()));
                }
            }

            // 执行排序
            sortedList.sort(comparator);

            return of(sortedList);
        }


        /**
         * 简化版排序方法 - 升序，空值在最后
         */
        public <U extends Comparable<? super U>> ListStream<T> sortAsc(
                Function<? super T, ? extends U> keyExtractor) {
            return sort(keyExtractor, Sort.Asc, Sort.NullLast);
        }

        /**
         * 简化版排序方法 - 降序，空值在最后
         */
        public <U extends Comparable<? super U>> ListStream<T> sortDesc(
                Function<? super T, ? extends U> keyExtractor) {
            return sort(keyExtractor, Sort.Desc, Sort.NullLast);
        }

        /**
         * 将嵌套的集合展平成单层集合
         *
         * @param mapper 将元素转换为Iterable的函数
         * @return 新的ListStream
         */
        public <R> ListStream<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> mapper) {
            Objects.requireNonNull(mapper, "mapper cannot be null");

            if (isEmpty()) {
                return of(new ArrayList<>());
            }

            return of(new FlatteningIterable<>(source, mapper));
        }

        public final ListStream<T> peek(Consumer<T> consumer) {
            return of(() -> new Iterator<>() {
                final Iterator<T> iterator = source.iterator();

                public boolean hasNext() {
                    return iterator.hasNext();
                }

                public T next() {
                    final T item = iterator.next();
                    consumer.accept(item);
                    return item;
                }
            });
        }

        public ListStream<T> peekStream(Consumer<ListStream<T>> streamOperation) {
            streamOperation.accept(of(source));
            return this;
        }

        public <R> R reduce(Supplier<R> func, BiConsumer<R, T> consumer) {
            R r = func.get();
            for (T t : source) {
                consumer.accept(r, t);
            }
            return r;
        }

        public <S, E, R> R reduce(Supplier<R> supplier, Function<T, E> func, BiConsumer<R, E> consumer) {
            R r = supplier.get();
            for (T t : source) {
                E e = func.apply(t);
                consumer.accept(r, e);
            }
            return r;
        }

        public void forEach(Consumer<? super T> action) {
            Objects.requireNonNull(action);
            source.forEach(action);
        }

        public void split(int size, Consumer<List<T>> consumer) {
            List<List<T>> parts = new ArrayList<>();
            int i = 0;
            List<T> temp = null;
            for (T t : source) {
                if (temp == null || i % size == 0) {
                    temp = new ArrayList<>(size);
                    parts.add(temp);
                }
                temp.add(t);
                ++i;
            }

            for (List<T> part : parts) {
                consumer.accept(part);
            }
        }

        private boolean isNotEmpty() {
            return !isEmpty();
        }

        // 判断是否为空的方法
        public boolean isEmpty() {
            if (source == null) {
                return true;
            }

            // 如果是Collection类型，直接使用isEmpty()方法
            if (source instanceof Collection) {
                return ((Collection<?>) source).isEmpty();
            }

            // 如果是普通Iterable，检查是否有第一个元素
            return !source.iterator().hasNext();
        }

        // 去重方法，按指定属性去重
        public ListStream<T> distinct(Function<T, ?> keyExtractor) {
            Set<Object> seen = ConcurrentHashMap.newKeySet();
            return of(createFilteredIterable(elem -> {
                if (seen.contains(keyExtractor.apply(elem))) {
                    return false;
                }
                seen.add(keyExtractor.apply(elem));
                return true;
            }));
        }

        // 去重方法（重载）
        public ListStream<T> distinct() {
            Set<Object> seen = ConcurrentHashMap.newKeySet();
            return of(createFilteredIterable(elem -> {
                if (seen.contains(elem)) {
                    return false;
                }
                seen.add(elem);
                return true;
            }));
        }

        // 获取大小的方法
        public long size() {
            return count();
        }

        public long count() {
            if (isEmpty()) {
                return 0;
            }
            // 如果是Collection类型，直接返回size
            if (source instanceof Collection) {
                return ((Collection<?>) source).size();
            }
            // 否则遍历计数
            long count = 0;
            for (T ignored : source) {
                count++;
            }
            return count;
        }

        public List<T> toList() {
            // 如果是Collection类型，直接返回size
            if (source instanceof List<T>) {
                return (List<T>) source;
            }
            List<T> result = new ArrayList<>();
            source.forEach(result::add);
            return result;
        }

        public Set<T> toSet() {
            // 如果是Collection类型，直接返回size
            if (source instanceof Set<T>) {
                return (Set<T>) source;
            }
            Set<T> result = new HashSet<>();
            source.forEach(result::add);
            return result;
        }

    }

    // 内部类，封装流操作
    public final static class SortStream<T> {
        public <U extends Comparable<? super U>> Comparator<T> createComparator(
                Function<? super T, ? extends U> keyExtractor,
                Sort order,
                Sort nullPosition) {

            // 验证order参数
            if (order != Sort.Asc && order != Sort.Desc) {
                throw new IllegalArgumentException("order must be either Asc or Desc");
            }

            // 验证nullPosition参数
            if (nullPosition != Sort.NullFirst && nullPosition != Sort.NullLast) {
                throw new IllegalArgumentException("nullPosition must be either NullFirst or NullLast");
            }

            // 基础比较器

            return (o1, o2) -> {
                U key1 = keyExtractor.apply(o1);
                U key2 = keyExtractor.apply(o2);

                // 处理空值情况
                if (key1 == null && key2 == null) {
                    return 0;
                }
                if (key1 == null) {
                    return nullPosition == Sort.NullFirst ? -1 : 1;
                }
                if (key2 == null) {
                    return nullPosition == Sort.NullFirst ? 1 : -1;
                }

                // 正常比较
                int comparison = key1.compareTo(key2);
                return order == Sort.Asc ? comparison : -comparison;
            };
        }

        /**
         * 验证排序参数
         */
        private void validateSortParameters(Sort order, Sort nullPosition) {
            // 验证order参数
            if (order != Sort.Asc && order != Sort.Desc) {
                throw new IllegalArgumentException("order must be either Asc or Desc");
            }

            // 验证nullPosition参数
            if (nullPosition != Sort.NullFirst && nullPosition != Sort.NullLast) {
                throw new IllegalArgumentException("nullPosition must be either NullFirst or NullLast");
            }
        }
    }

    // 内部类，封装流操作
    public final static class MapListStream<K, V> {
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

        public <R> MapStream<K, List<R>> valueStream(Function<ListStream<V>, List<R>> func) {
            final Map<K, List<R>> newMap = new LinkedHashMap<>();
            for (Map.Entry<K, List<V>> entry : map.entrySet()) {
                final K key = entry.getKey();
                final List<V> values = entry.getValue();
                List<R> rList = new ArrayList<>(func.apply(new ListStream<>(values)));
                newMap.put(key, rList);
            }
            return new MapStream<>(newMap);
        }
    }

    // 内部类，封装流操作
    public final static class MapStream<K, V> {
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

    public static class Op<V> {

        private final V v;

        public Op(V v) {
            this.v = v;
        }

        public <R> Op<R> map(Function<V, R> func) {
            R r = func.apply(v);
            return new Op<>(r);
        }

        public boolean isNotBlank() {
            return !isBlank();
        }

        public void isBlank(Consumer<V> consumer) {
            if (isBlank()) {
                consumer.accept(v);
            }
        }

        public void isBlankOrElse(Consumer<V> consumer, Runnable runnable) {
            if (isBlank()) {
                consumer.accept(v);
            } else {
                runnable.run();
            }
        }


        public void isNotBlank(Consumer<V> consumer) {
            if (isNotBlank()) {
                consumer.accept(v);
            }
        }

        public void isNotBlankOrElse(Consumer<V> consumer, Runnable runnable) {
            if (isNotBlank()) {
                consumer.accept(v);
            } else {
                runnable.run();
            }
        }

        public boolean isBlank() {
            if (v == null) {
                return true;
            } else if (v instanceof Optional) {
                return ((Optional<?>) v).isEmpty();
            } else if (v instanceof CharSequence vStr) {
                return vStr.length() == 0;
            } else if (v instanceof Collection) {
                return ((Collection<?>) v).isEmpty();
            } else if (v.getClass().isArray()) {
                return Array.getLength(v) == 0;
            } else if (v instanceof Map) {
                return ((Map<?, ?>) v).isEmpty();
            }
            // else
            return false;
        }

        public V get() {
            if (v == null) {
                throw new NoSuchElementException("No value present");
            }
            return v;
        }

        public V get(V defaultValue) {
            if (v == null) {
                return defaultValue;
            }
            return v;
        }
    }

    @Data
    @Accessors(chain = true)
    public static class Diff<T> {

        private List<T> addList;
        private List<T> delList;
        // 已存在的对象集合 对应的新值
        private Map<T, T> updateMap;

        public List<T> getEffectList() {
            final List<T> list = new ArrayList<>(addList);
            list.addAll(getExistsList());
            return list;
        }

        public Diff<T> addConsumer(BiConsumer<Diff<T>, List<T>> biConsumer) {
            if (isNotEmpty(addList)) {
                biConsumer.accept(this, addList);
            }
            return this;
        }

        public Diff<T> delConsumer(BiConsumer<Diff<T>, List<T>> biConsumer) {
            if (isNotEmpty(delList)) {
                biConsumer.accept(this, delList);
            }
            return this;
        }

        private boolean isNotEmpty(List<T> list) {
            return list != null && !list.isEmpty();
        }

        public Diff<T> existsConsumer(BiConsumer<Diff<T>, List<T>> biConsumer) {
            final List<T> existsList = getExistsList();
            if (isNotEmpty(existsList)) {
                biConsumer.accept(this, existsList);
            }
            return this;
        }


        public Diff<T> updateConsumer(BiConsumer<Diff<T>, Map<T, T>> biConsumer) {
            if (updateMap != null && !updateMap.isEmpty()) {
                biConsumer.accept(this, updateMap);
            }
            return this;
        }

        public Diff<T> forEachUpdateMapConsumer(BiConsumer<T, T> biConsumer) {
            if (updateMap != null && !updateMap.isEmpty()) {
                for (Map.Entry<T, T> entry : updateMap.entrySet()) {
                    final T oldPo = entry.getKey();
                    final T newPo = entry.getValue();
                    biConsumer.accept(oldPo, newPo);
                }
            }
            return this;
        }

        public List<T> getExistsList() {
            return getUpdateMapKeys();
        }

        public List<T> getUpdateMapKeys() {
            return updateMap.keySet().stream().toList();
        }

        public List<T> getUpdateMapValues() {
            return updateMap.values().stream().toList();
        }
    }

    @Data
    @Accessors(chain = true)
    public static class Diff2<T, R> {

        private List<R> addList;
        private List<T> delList;
        // 已存在的对象集合 对应的新值
        private Map<T, R> updateMap;

        public List<Object> getEffectList() {
            final List<Object> list = new ArrayList<>(addList);
            list.addAll(getExistsList());
            return list;
        }

        public Diff2<T, R> addConsumer(BiConsumer<Diff2<T, R>, List<R>> biConsumer) {
            if (isNotEmpty(addList)) {
                biConsumer.accept(this, addList);
            }
            return this;
        }

        public Diff2<T, R> delConsumer(BiConsumer<Diff2<T, R>, List<T>> biConsumer) {
            if (isNotEmpty(delList)) {
                biConsumer.accept(this, delList);
            }
            return this;
        }

        public Diff2<T, R> existsConsumer(BiConsumer<Diff2<T, R>, List<T>> biConsumer) {
            final List<T> existsList = getExistsList();
            if (isNotEmpty(existsList)) {
                biConsumer.accept(this, existsList);
            }
            return this;
        }


        public Diff2<T, R> updateConsumer(BiConsumer<Diff2<T, R>, Map<T, R>> biConsumer) {
            if (updateMap != null && !updateMap.isEmpty()) {
                biConsumer.accept(this, updateMap);
            }
            return this;
        }

        public Diff2<T, R> forEachUpdateMapConsumer(BiConsumer<T, R> biConsumer) {
            if (updateMap != null && !updateMap.isEmpty()) {
                for (Map.Entry<T, R> entry : updateMap.entrySet()) {
                    final T oldPo = entry.getKey();
                    final R newPo = entry.getValue();
                    biConsumer.accept(oldPo, newPo);
                }
            }
            return this;
        }

        private <X> boolean isNotEmpty(List<X> list) {
            return list != null && !list.isEmpty();
        }

        public List<T> getExistsList() {
            return getUpdateMapKeys();
        }

        public List<T> getUpdateMapKeys() {
            return updateMap.keySet().stream().toList();
        }

        public List<R> getUpdateMapValues() {
            return updateMap.values().stream().toList();
        }
    }

    public static abstract class Try<T> {

        public abstract boolean isFailure();

        public abstract T get();

        public abstract Throwable getCause();

        public abstract boolean isEmpty();

        public abstract boolean isSuccess();

        public abstract String stringPrefix();

        public final Try<T> andThen(CheckedRunnable runnable) {
            Objects.requireNonNull(runnable, "runnable is null");
            if (isFailure()) {
                return this;
            } else {
                try {
                    runnable.run();
                    return this;
                } catch (Throwable t) {
                    return new Try.Failure<>(t);
                }
            }
        }

        public final Try<T> andThen(CheckedConsumer<? super T> consumer) {
            Objects.requireNonNull(consumer, "consumer is null");
            if (isFailure()) {
                return this;
            } else {
                try {
                    consumer.accept(get());
                    return this;
                } catch (Throwable t) {
                    return new Try.Failure<>(t);
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

        public final <E extends Throwable> Try<T> onFailure(Function<? super Throwable, E> action) {
            Objects.requireNonNull(action, "action is null");
            if (isFailure()) {
                final E apply = action.apply(getCause());
                throw new RuntimeException(apply);
            }
            return this;
        }

        @SuppressWarnings("unchecked")
        public final <X extends Throwable> Try<T> onFailure(Class<X> exceptionType, Consumer<? super X> action) {
            Objects.requireNonNull(exceptionType, "exceptionType is null");
            Objects.requireNonNull(action, "action is null");
            if (isFailure() && exceptionType.isAssignableFrom(getCause().getClass())) {
                action.accept((X) getCause());
            }
            return this;
        }


        public final Try<T> andFinally(CheckedRunnable runnable) {
            Objects.requireNonNull(runnable, "runnable is null");
            try {
                runnable.run();
                return this;
            } catch (Throwable t) {
                return new Try.Failure<>(t);
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
//                throw new UnsupportedOperationException("getCause on Success");
                return null;
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

    /**
     * 用于实现扁平化迭代的内部类
     */
    private static class FlatteningIterable<T, R> implements Iterable<R> {
        private final Iterable<? extends T> source;
        private final Function<? super T, ? extends Iterable<? extends R>> mapper;

        FlatteningIterable(Iterable<? extends T> source,
                           Function<? super T, ? extends Iterable<? extends R>> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public Iterator<R> iterator() {
            return new FlatteningIterator<>(source.iterator(), mapper);
        }
    }

    /**
     * 用于实现扁平化迭代的迭代器
     */
    private static class FlatteningIterator<T, R> implements Iterator<R> {
        private final Iterator<? extends T> sourceIterator;
        private final Function<? super T, ? extends Iterable<? extends R>> mapper;
        private Iterator<? extends R> currentIterator;
        private boolean hasNextComputed;
        private boolean hasNextResult;

        FlatteningIterator(Iterator<? extends T> sourceIterator,
                           Function<? super T, ? extends Iterable<? extends R>> mapper) {
            this.sourceIterator = sourceIterator;
            this.mapper = mapper;
            this.currentIterator = Collections.emptyIterator();
        }

        @Override
        public boolean hasNext() {
            if (!hasNextComputed) {
                computeNext();
            }
            return hasNextResult;
        }

        @Override
        public R next() {
            if (!hasNextComputed) {
                computeNext();
            }
            if (!hasNextResult) {
                throw new NoSuchElementException();
            }
            hasNextComputed = false;
            return currentIterator.next();
        }

        private void computeNext() {
            while (!currentIterator.hasNext() && sourceIterator.hasNext()) {
                T nextElement = sourceIterator.next();
                if (nextElement != null) {
                    Iterable<? extends R> nextIterable = mapper.apply(nextElement);
                    if (nextIterable != null) {
                        currentIterator = nextIterable.iterator();
                    }
                }
            }
            hasNextResult = currentIterator.hasNext();
            hasNextComputed = true;
        }
    }

}
