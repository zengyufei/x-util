package com.zyf.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;
import java.util.stream.Collector;

public class ListStream<T> {

    private final Iterable<T> source;

    ListStream(Iterable<T> source) {
        this.source = source;
    }

    public static <T> ListStream<T> of(Iterable<T> source) {
        return new ListStream<>(Objects.requireNonNull(source));
    }

    // ================================ 过滤 (Filtering)  ==================================
    // ====================================================================================
    //  filter { predicate }: 返回一个新的列表，包含所有满足给定条件的元素。

    @SafeVarargs
    public final ListStream<T> ands(Predicate<T>... predicates) {
        return filters(predicates);
    }

    public ListStream<T> filter(Predicate<? super T> predicate) {
        return filters(predicate);
    }

    @SafeVarargs
    public final ListStream<T> filters(Predicate<? super T>... predicates) {
        Objects.requireNonNull(predicates);
        return of(createFilteredIterable(elem ->
                Arrays.stream(predicates).allMatch(predicate -> predicate.test(elem))));
    }

    //  filterOrs { predicate }: 返回一个新的列表，包含任意满足给定条件的元素。

    // 过滤或的实现
    @SafeVarargs
    public final ListStream<T> ors(Predicate<T>... predicates) {
        return filterOrs(predicates);
    }

    // 过滤或的实现
    @SafeVarargs
    public final ListStream<T> filterOrs(Predicate<T>... predicates) {
        return of(createFilteredIterable(elem ->
                Arrays.stream(predicates).anyMatch(predicate -> predicate.test(elem))));
    }

    //  filterNot { predicate }: 返回一个新列表，包含所有不满足给定条件的元素。

    public ListStream<T> filterNot(Predicate<? super T> predicate) {
        return filterNots(predicate);
    }

    @SafeVarargs
    public final ListStream<T> filterNots(Predicate<? super T>... predicates) {
        Objects.requireNonNull(predicates);
        return of(createFilteredIterable(elem ->
                Arrays.stream(predicates).noneMatch(predicate -> predicate.test(elem))));
    }


    //  filterNull(): 返回一个新列表，其中包含null元素。

    public final ListStream<T> filterNull() {
        return of(createFilteredIterable(Objects::isNull));
    }

    public final ListStream<T> filterNull(Function<T, ?> function) {
        return filterNulls(function);
    }

    @SafeVarargs
    public final ListStream<T> filterNulls(Function<T, ?>... functions) {
        Objects.requireNonNull(functions);
        return of(createFilteredIterable(elem ->
                Arrays.stream(functions).allMatch(fun -> {
                    Object value = fun.apply(elem);
                    return value == null;
                })));
    }

    //  filterNotNull(): 返回一个新列表，其中不包含null元素。

    public final ListStream<T> filterNotNull() {
        return of(createFilteredIterable(Objects::nonNull));
    }

    public final ListStream<T> filterNotNull(Function<T, ?> function) {
        return filterNotNulls(function);
    }

    @SafeVarargs
    public final ListStream<T> filterNotNulls(Function<T, ?>... functions) {
        Objects.requireNonNull(functions);
        return of(createFilteredIterable(elem ->
                Arrays.stream(functions).allMatch(fun -> {
                    Object value = fun.apply(elem);
                    return value != null;
                })));
    }


    //  filterIndexed { index, value -> predicate }: 类似filter，但谓词同时接收元素的索引。

    @SafeVarargs
    public final ListStream<T> filterIndexeds(BiPredicate<Integer, ? super T>... predicates) {
        Objects.requireNonNull(predicates);
        return of(createFilteredIterable((index, elem) ->
                Arrays.stream(predicates).allMatch(predicate -> predicate.test(index, elem))));
    }

    //  filterIsInstance<R>(): 返回一个新列表，包含所有指定类型R的元素。
    public final <R> ListStream<R> filterIsInstance(Class<R> classOfR) {
        Objects.requireNonNull(classOfR, "classOfR cannot be null");

        return ListStream.of(() -> new Iterator<>() {
            final Iterator<T> sourceIterator = source.iterator();
            R nextElement;
            boolean hasNextComputed = false;
            boolean hasNextResult = false;

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
                return nextElement;
            }

            private void computeNext() {
                while (sourceIterator.hasNext()) {
                    T current = sourceIterator.next();
                    // 使用 Class.isInstance() 进行类型检查
                    if (classOfR.isInstance(current)) {
                        // 如果是指定类型的实例，进行强制类型转换并存储
                        nextElement = classOfR.cast(current); // 或者 (R) current; 但 cast() 更安全
                        hasNextResult = true;
                        hasNextComputed = true;
                        return;
                    }
                }
                // 如果没有找到更多匹配的元素
                hasNextResult = false;
                hasNextComputed = true;
            }
        });
    }

    //  drop(n): 返回一个新的列表，移除了前n个元素。

    public ListStream<T> drop(int n) {
        return skip(n);
    }

    public ListStream<T> skip(int n) {
        return of(createFilteredIterable((index, elem) -> index + 1 > n));
    }

    //  dropWhile { predicate }: 从第一个不满足条件的元素开始，返回剩余的元素。
    public final ListStream<T> dropWhile(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate cannot be null");

        return ListStream.of(() -> new Iterator<>() {
            final Iterator<T> sourceIterator = source.iterator();
            boolean dropping = true; // 初始状态为“正在丢弃”
            T nextElement;
            boolean hasNextComputed = false;
            boolean hasNextResult = false;

            @Override
            public boolean hasNext() {
                if (!hasNextComputed) {
                    computeNext();
                }
                return hasNextResult;
            }

            @Override
            public T next() {
                if (!hasNextComputed) {
                    computeNext();
                }
                if (!hasNextResult) {
                    throw new NoSuchElementException();
                }
                hasNextComputed = false;
                return nextElement;
            }

            private void computeNext() {
                while (sourceIterator.hasNext()) {
                    T current = sourceIterator.next();
                    if (dropping) {
                        // 如果还在丢弃阶段，检查当前元素是否满足条件
                        if (predicate.test(current)) {
                            // 满足条件，继续丢弃，不返回此元素
                            continue;
                        } else {
                            // 第一个不满足条件的元素，从这里开始“保留”
                            dropping = false; // 停止丢弃
                            nextElement = current;
                            hasNextResult = true;
                            hasNextComputed = true;
                            return;
                        }
                    } else {
                        // 一旦停止丢弃，之后的所有元素都直接返回
                        nextElement = current;
                        hasNextResult = true;
                        hasNextComputed = true;
                        return;
                    }
                }
                // 如果迭代器没有更多元素了
                hasNextResult = false;
                hasNextComputed = true;
            }
        });
    }

    //  take(n): 返回前n个元素的新列表。

    public ListStream<T> take(int n) {
        return limit(n);
    }

    public ListStream<T> limit(int n) {
        return of(createFilteredIterable((index, elem) -> index + 1 <= n));
    }

    //  takeWhile { predicate }: 返回从开头开始，连续满足条件的元素。
    public final ListStream<T> takeWhile(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate cannot be null");

        return ListStream.of(() -> new Iterator<>() {
            final Iterator<T> sourceIterator = source.iterator();
            boolean taking = true; // 初始状态为“正在获取”
            T nextElement;
            boolean hasNextComputed = false;
            boolean hasNextResult = false;

            @Override
            public boolean hasNext() {
                if (!hasNextComputed) {
                    computeNext();
                }
                return hasNextResult;
            }

            @Override
            public T next() {
                if (!hasNextComputed) {
                    computeNext();
                }
                if (!hasNextResult) {
                    throw new NoSuchElementException();
                }
                hasNextComputed = false;
                return nextElement;
            }

            private void computeNext() {
                if (!taking) { // 如果已经停止获取，直接返回没有更多元素
                    hasNextResult = false;
                    hasNextComputed = true;
                    return;
                }

                while (sourceIterator.hasNext()) {
                    T current = sourceIterator.next();
                    if (predicate.test(current)) {
                        // 如果满足条件，则获取此元素
                        nextElement = current;
                        hasNextResult = true;
                        hasNextComputed = true;
                        return;
                    } else {
                        // 遇到第一个不满足条件的元素，停止获取
                        taking = false; // 停止获取
                        break; // 退出循环，表示没有更多元素可返回
                    }
                }
                // 如果循环结束，或者因为不满足条件而中断，表示没有更多元素
                hasNextResult = false;
                hasNextComputed = true;
            }
        });
    }

    //  slice(indices): 返回一个新列表，包含指定索引处的元素。

    public final ListStream<T> slice(Collection<Integer> indices) {
        Objects.requireNonNull(indices, "indices collection cannot be null");

        // 将索引集合转换为HashSet以便O(1)查找
        final Set<Integer> targetIndices = new HashSet<>(indices);

        return ListStream.of(() -> new Iterator<>() {
            final Iterator<T> sourceIterator = source.iterator();
            int currentIndex = 0; // 当前遍历的元素索引
            T nextElement;
            boolean hasNextComputed = false;
            boolean hasNextResult = false;

            @Override
            public boolean hasNext() {
                if (!hasNextComputed) {
                    computeNext();
                }
                return hasNextResult;
            }

            @Override
            public T next() {
                if (!hasNextComputed) {
                    computeNext();
                }
                if (!hasNextResult) {
                    throw new NoSuchElementException();
                }
                hasNextComputed = false;
                return nextElement;
            }

            private void computeNext() {
                while (sourceIterator.hasNext()) {
                    T current = sourceIterator.next();
                    if (targetIndices.contains(currentIndex)) {
                        nextElement = current;
                        hasNextResult = true;
                        hasNextComputed = true;
                        // 注意：这里不增加 currentIndex，因为一个索引可能被多次请求，
                        // 但我们的目标是按原始流顺序提取元素。
                        // 如果 indices 是一个排好序的 Set，并且只希望每个索引提取一次，
                        // 那么可以在找到后从 targetIndices 中移除。
                        // 当前实现允许重复索引返回重复元素。
                        currentIndex++; // 继续检查下一个原始元素
                        return;
                    }
                    currentIndex++; // 即使不匹配，也增加索引
                }
                hasNextResult = false;
                hasNextComputed = true;
            }
        });
    }

    //  slice(range): 返回一个新列表，包含指定范围内的元素。
    public final ListStream<T> slice(int startIndex, int endIndex) {
        if (startIndex < 0) {
            throw new IllegalArgumentException("startIndex cannot be negative: " + startIndex);
        }
        if (endIndex < 0) {
            throw new IllegalArgumentException("endIndex cannot be negative: " + endIndex);
        }
        if (startIndex > endIndex) {
            throw new IllegalArgumentException("startIndex (" + startIndex + ") cannot be greater than endIndex (" + endIndex + ")");
        }

        return ListStream.of(() -> new Iterator<>() {
            final Iterator<T> sourceIterator = source.iterator();
            int currentIndex = 0; // 当前遍历的元素索引
            T nextElement;
            boolean hasNextComputed = false;
            boolean hasNextResult = false;

            @Override
            public boolean hasNext() {
                if (!hasNextComputed) {
                    computeNext();
                }
                return hasNextResult;
            }

            @Override
            public T next() {
                if (!hasNextComputed) {
                    computeNext();
                }
                if (!hasNextResult) {
                    throw new NoSuchElementException();
                }
                hasNextComputed = false;
                return nextElement;
            }

            private void computeNext() {
                // 跳过 startIndex 之前的元素
                while (currentIndex < startIndex && sourceIterator.hasNext()) {
                    sourceIterator.next(); // 消耗元素
                    currentIndex++;
                }

                // 如果当前索引在有效范围内且有下一个元素
                if (currentIndex >= startIndex && currentIndex < endIndex && sourceIterator.hasNext()) {
                    nextElement = sourceIterator.next();
                    currentIndex++;
                    hasNextResult = true;
                    hasNextComputed = true;
                    return;
                }
                // 否则，没有更多元素
                hasNextResult = false;
                hasNextComputed = true;
            }
        });
    }

    //  distinct(): 返回一个新列表，包含所有唯一的元素（基于equals()）。

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

    //  distinctBy { selector }: 返回一个新列表，通过给定选择器函数返回的键来判断唯一性。

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

    // ====================================================================================
    // ====================================================================================


    // ================================ 映射 (Mapping)  ==================================
    // ====================================================================================
    //  map { transform }: 返回一个新的列表，其中每个元素都是通过给定转换函数转换而来。

    public <R> ListStream<R> map(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        return of(() -> new Iterator<>() {
            final Iterator<T> iterator = source.iterator();

            public boolean hasNext() {
                return iterator.hasNext();
            }

            public R next() {
                final T next = iterator.next();
                if (next == null) {
                    return null;
                }
                return mapper.apply(next);
            }
        });
    }

    //  mapIndexed { index, value -> transform }: 类似map，但转换函数同时接收元素的索引。接收元素的索引。

    public <R> ListStream<R> mapIndexed(BiFunction<Integer, ? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        return of(() -> new Iterator<>() {
            final Iterator<T> iterator = source.iterator();
            int index = 0;

            public boolean hasNext() {
                return iterator.hasNext();
            }

            public R next() {
                final R r = mapper.apply(index, iterator.next());
                index += 1;
                return r;
            }
        });
    }

    //  mapNotNull { transform }: 类似map，但会过滤掉转换后为null的元素。

    public <R> ListStream<R> mapNotNull(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        return of(() -> new Iterator<>() {
            final Iterator<T> iterator = source.iterator();
            R nextElement;
            boolean hasNextComputed = false;
            boolean hasNextResult = false;

            @Override
            public boolean hasNext() {
                if (!hasNextComputed) {
                    computeNext(Objects::nonNull);
                }
                return hasNextResult;
            }

            @Override
            public R next() {
                if (!hasNextComputed) {
                    computeNext(Objects::nonNull);
                }
                if (!hasNextResult) {
                    throw new NoSuchElementException();
                }
                hasNextComputed = false;
                return nextElement;
            }

            private void computeNext(Predicate<R> condition) {
                while (iterator.hasNext()) {
                    final T next = iterator.next();
                    R elem = mapper.apply(next);
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
        });
    }

    //  flatMap { transform }: 将每个元素映射为一个Iterable，然后将所有结果展平到一个列表中。

    public <R> ListStream<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> mapper) {
        Objects.requireNonNull(mapper, "mapper cannot be null");

        if (isEmpty()) {
            return of(new ArrayList<>());
        }

        return of(() -> new Iterator<>() {
            final Iterator<T> iterator = source.iterator();
            private Iterator<? extends R> currentIterator = Collections.emptyIterator();
            private boolean hasNextComputed;
            private boolean hasNextResult;

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
                while (!currentIterator.hasNext() && iterator.hasNext()) {
                    T nextElement = iterator.next();
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
        });
    }

    //  zip(other): 将两个列表的元素按位置配对，生成一个Pair的列表。

    public <U> ListPair<T, U, Pair<T, U>> zip(Iterable<U> other) {
        Objects.requireNonNull(other, "list cannot be null");
        return ListPair.of(() -> new Iterator<>() {
            final Iterator<T> it1 = source.iterator();
            final Iterator<U> it2 = other.iterator();

            @Override
            public boolean hasNext() {
                return it1.hasNext() && it2.hasNext();
            }

            @Override
            public Pair<T, U> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return Pair.of(it1.next(), it2.next());
            }
        });
    }

    //  unzip(): 将一个Pair列表解构为两个列表（第一个元素一个列表，第二个元素一个列表）。
    public final <A, B> Pair<List<A>, List<B>> unzip() {
        final List<A> firstElements = new ArrayList<>();
        final List<B> secondElements = new ArrayList<>();

        // 遍历原始 ListStream，并解构每个 Pair
        for (final T element : source) {
            try {
                @SuppressWarnings("unchecked") // 运行时类型擦除，此处需要抑制警告
                Pair<A, B> pair = (Pair<A, B>) element;
                firstElements.add(pair.first);
                secondElements.add(pair.second);
            } catch (ClassCastException e) {
                throw new IllegalStateException("ListStream elements must be of type Pair to call unzip(). " +
                        "Encountered non-Pair element: " + element, e);
            }
        }

        // 直接返回包含两个 List 的 Pair
        return Pair.of(firstElements, secondElements);
    }

    // ====================================================================================
    // ====================================================================================


    // ================================ 聚合 (Aggregating / Reducing)  ==================================
    // ====================================================================================
    //  reduce { acc, value -> operation }: 从第一个元素开始，将元素累计起来，每次使用上一次的累计值和当前元素进行操作。


    public <S, E, R> R reduce(Supplier<R> supplier, Function<T, E> func, BiFunction<R, E, R> function) {
        R r = supplier.get();
        for (T t : source) {
            E e = func.apply(t);
            r = function.apply(r, e);
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

    public <R> R reduce(Supplier<R> func, BiFunction<R, T, R> function) {
        R r = func.get();
        for (T t : source) {
            r = function.apply(r, t);
        }
        return r;
    }

    public <R> R reduce(Supplier<R> func, BiConsumer<R, T> consumer) {
        R r = func.get();
        for (T t : source) {
            consumer.accept(r, t);
        }
        return r;
    }

    public <E, R> List<R> reduceList(Function<T, E> func, BiConsumer<List<R>, E> consumer) {
        List<R> rs = new ArrayList<>();
        for (T t : source) {
            E e = func.apply(t);
            consumer.accept(rs, e);
        }
        return rs;
    }

    public <R> List<R> reduceList(Function<T, R> func) {
        List<R> rs = new ArrayList<>();
        for (T t : source) {
            R e = func.apply(t);
            rs.add(e);
        }
        return rs;
    }


    public <E, R> Set<R> reduceSet(Function<T, E> func, BiConsumer<Set<R>, E> consumer) {
        Set<R> rs = new HashSet<>();
        for (T t : source) {
            E e = func.apply(t);
            consumer.accept(rs, e);
        }
        return rs;
    }

    public <R> Set<R> reduceSet(Function<T, R> func) {
        Set<R> rs = new HashSet<>();
        for (T t : source) {
            R e = func.apply(t);
            rs.add(e);
        }
        return rs;
    }

    //  reduceIndexed { index, acc, value -> operation }: 类似reduce，但操作函数同时接收元素的索引。


    public <E, R> R reduceIndexed(Supplier<R> supplier, Function<T, E> func, BBiFunction<Integer, R, E, R> function) {
        R r = supplier.get();
        int index = 0;
        for (T t : source) {
            E e = func.apply(t);
            r = function.apply(index++, r, e);
        }
        return r;
    }

    public <S, E, R> R reduceIndexed(Supplier<R> supplier, Function<T, E> func, BBiConsumer<Integer, R, E> consumer) {
        R r = supplier.get();
        int index = 0;
        for (T t : source) {
            E e = func.apply(t);
            consumer.accept(index++, r, e);
        }
        return r;
    }

    public <R> R reduceIndexed(Supplier<R> func, BBiFunction<Integer, R, T, R> function) {
        R r = func.get();
        int index = 0;
        for (T t : source) {
            r = function.apply(index++, r, t);
        }
        return r;
    }

    public <R> R reduceIndexed(Supplier<R> func, BBiConsumer<Integer, R, T> consumer) {
        R r = func.get();
        int index = 0;
        for (T t : source) {
            consumer.accept(index++, r, t);
        }
        return r;
    }

    public <E, R> List<R> reduceIndexedList(Function<T, E> func, BBiConsumer<Integer, List<R>, E> consumer) {
        List<R> rs = new ArrayList<>();
        int index = 0;
        for (T t : source) {
            E e = func.apply(t);
            consumer.accept(index++, rs, e);
        }
        return rs;
    }

    public <E, R> Set<R> reduceIndexedSet(Function<T, E> func, BBiConsumer<Integer, Set<R>, E> consumer) {
        Set<R> rs = new HashSet<>();
        int index = 0;
        for (T t : source) {
            E e = func.apply(t);
            consumer.accept(index++, rs, e);
        }
        return rs;
    }

    //  fold(initial) { acc, value -> operation }: 类似reduce，但可以提供一个初始值。
    public final <R> R fold(R initial, BiFunction<R, T, R> operation) {
        Objects.requireNonNull(operation, "operation cannot be null");

        R accumulator = initial; // 初始化累加器

        for (final T element : source) {
            accumulator = operation.apply(accumulator, element); // 应用操作，更新累加器
        }
        return accumulator; // 返回最终的累加值
    }
    //  foldIndexed(initial) { index, acc, value -> operation }: 类似fold，但操作函数同时接收元素的索引。

    public final <R> R foldIndexed(R initial, BBiFunction<Integer, R, T, R> operation) {
        Objects.requireNonNull(operation, "operation cannot be null");

        R accumulator = initial; // 初始化累加器
        Iterator<T> iterator = source.iterator();
        int index = 0; // 初始化索引

        while (iterator.hasNext()) {
            T element = iterator.next();
            accumulator = operation.apply(index, accumulator, element); // 应用操作，更新累加器
            index++; // 索引递增
        }
        return accumulator; // 返回最终的累加值
    }
    //  sum(): 计算数字集合中所有元素的和。


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

    //  average(): 计算数字集合中所有元素的平均值。
    public final Double averageDouble(Function<T, Number> mapper) {
        return averageBigDecimal(mapper).doubleValue();
    }

    public final Integer averageInt(Function<T, Number> mapper) {
        return averageBigDecimal(mapper).intValue();
    }

    public final Long averageLong(Function<T, Number> mapper) {
        return averageBigDecimal(mapper).longValue();
    }

    public final BigDecimal averageBigDecimal(Function<T, Number> mapper) {
        BigDecimal total = new BigDecimal("0.0");
        BigDecimal count = BigDecimal.ZERO;

        for (final T element : source) {
            final Number number = mapper.apply(element);
            if (number == null) {
                continue;
            } else if (element instanceof Number) {
                total = total.add(new BigDecimal(String.valueOf(number)));
                count = count.add(BigDecimal.ONE); // 计数
            } else if (element != null) {
                // 如果遇到非 Number 类型的元素，抛出异常
                throw new IllegalStateException("Element is not a Number type when calculating average: " + element.getClass().getName() + " -> " + element);
            }
        }

        if (count.equals(BigDecimal.ZERO)) {
            return total; // 如果没有数字元素（空集合或只包含非数字元素），返回 NaN
        } else {
            return total.divide(count, 2, RoundingMode.HALF_UP); // 计算并返回平均值
        }
    }

    //  average(): 计算数字集合中所有元素的平均值。
    public final Double averageDouble() {
        return averageBigDecimal().doubleValue();
    }

    public final Integer averageInt() {
        return averageBigDecimal().intValue();
    }

    public final Long averageLong() {
        return averageBigDecimal().longValue();
    }

    public final BigDecimal averageBigDecimal() {
        BigDecimal total = new BigDecimal("0");
        BigDecimal count = BigDecimal.ZERO;

        for (final T element : source) {
            if (element instanceof Number) {
                total = total.add(new BigDecimal(String.valueOf(element)));
                count = count.add(BigDecimal.ONE); // 计数
            } else if (element != null) {
                // 如果遇到非 Number 类型的元素，抛出异常
                throw new IllegalStateException("Element is not a Number type when calculating average: " + element.getClass().getName() + " -> " + element);
            }
        }

        if (count.equals(BigDecimal.ZERO)) {
            return total; // 如果没有数字元素（空集合或只包含非数字元素），返回 NaN
        } else {
            return total.divide(count, 2, RoundingMode.HALF_UP); // 计算并返回平均值
        }
    }
    //  count(): 返回集合中的元素数量。


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

    //  count { predicate }: 返回满足给定条件的元素数量。

    @SafeVarargs
    public final long count(Predicate<T>... predicates) {
        if (isEmpty()) {
            return 0;
        }
        // 否则遍历计数
        long count = 0;
        for (T e : source) {
            if (Arrays.stream(predicates).anyMatch(predicate -> predicate.test(e))) {
                count++;
            }
        }
        return count;
    }

    //  maxOrNull(): 返回集合中的最大元素，如果为空则返回null。

    public final T maxOrNull() {
        Iterator<T> iterator = source.iterator();
        if (!iterator.hasNext()) {
            return null; // 集合为空，返回 null
        }

        T maxElement = iterator.next(); // 假定第一个元素是最大值

        while (iterator.hasNext()) {
            T current = iterator.next();
            // 元素必须是 Comparable 的
            if (!(current instanceof Comparable)) {
                throw new IllegalStateException("Elements must be Comparable to use maxOrNull()");
            }
            @SuppressWarnings("unchecked") // 安全转换，因为我们已经检查了 instanceof Comparable
            Comparable<T> comparableCurrent = (Comparable<T>) current;

            if (comparableCurrent.compareTo(maxElement) > 0) {
                maxElement = current; // 找到更大的元素，更新最大值
            }
        }
        return maxElement;
    }


    //  maxByOrNull { selector }: 返回通过选择器函数得到最大值的元素，如果为空则返回null。

    public final <R extends Comparable<R>> T maxByOrNull(Function<T, R> selector) {
        Objects.requireNonNull(selector, "selector cannot be null");
        Iterator<T> iterator = source.iterator();
        if (!iterator.hasNext()) {
            return null;
        }

        T maxElement = iterator.next();
        R maxValue = selector.apply(maxElement);

        while (iterator.hasNext()) {
            T current = iterator.next();
            R currentValue = selector.apply(current);
            if (currentValue.compareTo(maxValue) > 0) {
                maxValue = currentValue;
                maxElement = current;
            }
        }
        return maxElement;
    }

    //  maxOfOrNull { selector }: 返回通过选择器函数得到的最大值，如果为空则返回null。

    public final <R extends Comparable<R>> R maxOfOrNull(Function<T, R> selector) {
        Objects.requireNonNull(selector, "selector cannot be null");
        Iterator<T> iterator = source.iterator();
        if (!iterator.hasNext()) {
            return null;
        }

        R maxValue = selector.apply(iterator.next()); // 获取第一个元素的 selector 结果作为初始最大值

        while (iterator.hasNext()) {
            T current = iterator.next();
            R currentValue = selector.apply(current);
            if (currentValue.compareTo(maxValue) > 0) {
                maxValue = currentValue;
            }
        }
        return maxValue;
    }

    //  minOrNull(): 返回集合中的最小元素，如果为空则返回null。

    public final T minOrNull() {
        Iterator<T> iterator = source.iterator();
        if (!iterator.hasNext()) {
            return null; // 集合为空，返回 null
        }

        T minElement = iterator.next(); // 假定第一个元素是最小值

        while (iterator.hasNext()) {
            T current = iterator.next();
            if (!(current instanceof Comparable)) {
                throw new IllegalStateException("Elements must be Comparable to use minOrNull()");
            }
            @SuppressWarnings("unchecked")
            Comparable<T> comparableCurrent = (Comparable<T>) current;

            if (comparableCurrent.compareTo(minElement) < 0) { // 比较方向相反
                minElement = current; // 找到更小的元素，更新最小值
            }
        }
        return minElement;
    }

    //  minByOrNull { selector }: 返回通过选择器函数得到最小值的元素，如果为空则返回null。
    public final <R extends Comparable<R>> T minByOrNull(Function<T, R> selector) {
        Objects.requireNonNull(selector, "selector cannot be null");
        Iterator<T> iterator = source.iterator();
        if (!iterator.hasNext()) {
            return null;
        }

        T minElement = iterator.next();
        R minValue = selector.apply(minElement);

        while (iterator.hasNext()) {
            T current = iterator.next();
            R currentValue = selector.apply(current);
            if (currentValue.compareTo(minValue) < 0) { // 比较方向相反
                minValue = currentValue;
                minElement = current;
            }
        }
        return minElement;
    }

    //  minOfOrNull { selector }: 返回通过选择器函数得到的最小值，如果为空则返回null。

    public final <R extends Comparable<R>> R minOfOrNull(Function<T, R> selector) {
        Objects.requireNonNull(selector, "selector cannot be null");
        Iterator<T> iterator = source.iterator();
        if (!iterator.hasNext()) {
            return null;
        }

        R minValue = selector.apply(iterator.next()); // 获取第一个元素的 selector 结果作为初始最小值

        while (iterator.hasNext()) {
            T current = iterator.next();
            R currentValue = selector.apply(current);
            if (currentValue.compareTo(minValue) < 0) { // 比较方向相反
                minValue = currentValue;
            }
        }
        return minValue;
    }

    //  sumOf { selector }: 对通过选择器函数得到的数字值求和。

    public final double sumOf(Function<T, ? extends Number> selector) {
        Objects.requireNonNull(selector, "selector cannot be null");
        double total = 0.0;
        Iterator<T> iterator = source.iterator();

        while (iterator.hasNext()) {
            T element = iterator.next();
            Number value = selector.apply(element);
            if (value != null) {
                total += value.doubleValue(); // 将选择器返回的 Number 值转换为 double 累加
            }
            // 如果 selector 返回 null，则跳过该值
        }
        return total;
    }

    //  associate { transform }: 将集合中的每个元素映射为一个Pair，然后将这些Pair放入一个Map中。

    public final <K, V> Map<K, V> associate(Function<T, Pair<K, V>> transform) {
        Objects.requireNonNull(transform, "transform function cannot be null");
        Map<K, V> resultMap = new HashMap<>();
        Iterator<T> iterator = source.iterator();

        while (iterator.hasNext()) {
            T element = iterator.next();
            Pair<K, V> pair = transform.apply(element);
            if (pair != null) {
                resultMap.put(pair.first, pair.second);
            }
        }
        return resultMap;
    }

    //  associateBy { keySelector }: 根据键选择器函数创建一个Map，键是选择器返回的值，值是原始元素。

    public final <K> Map<K, T> associateBy(Function<T, K> keySelector) {
        Objects.requireNonNull(keySelector, "keySelector cannot be null");
        Map<K, T> resultMap = new HashMap<>();
        Iterator<T> iterator = source.iterator();

        while (iterator.hasNext()) {
            T element = iterator.next();
            K key = keySelector.apply(element);
            resultMap.put(key, element); // 键是选择器结果，值是原始元素
        }
        return resultMap;
    }

    //  associateWith { valueTransform }: 根据值转换函数创建一个Map，键是原始元素，值是转换函数返回的值。

    public final <V> Map<T, V> associateWith(Function<T, V> valueTransform) {
        Objects.requireNonNull(valueTransform, "valueTransform cannot be null");
        Map<T, V> resultMap = new HashMap<>();
        Iterator<T> iterator = source.iterator();

        while (iterator.hasNext()) {
            T element = iterator.next();
            V value = valueTransform.apply(element);
            resultMap.put(element, value); // 键是原始元素，值是转换函数结果
        }
        return resultMap;
    }

    //  groupBy { keySelector }: 根据键选择器函数对元素进行分组，返回一个Map，其中键是选择器返回的值，值是一个包含所有具有该键的元素的列表。


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

    //  groupingBy { keySelector }: 返回一个Grouping对象，用于更复杂的聚合操作，如eachCount()、fold()等。

    public <S, A, V> MapStream<S, V> groupingBy(
            Function<T, S> keyMapper,
            Collector<T, A, V> collector
    ) {
        // 获取collector的组件
        Supplier<A> supplier = collector.supplier();
        BiConsumer<A, T> accumulator = collector.accumulator();
        Function<A, V> finisher = collector.finisher();

        // 同时进行分组和累加，避免两次遍历
        Map<S, A> accumulatorMap = new HashMap<>();
        for (T element : source) {
            S key = keyMapper.apply(element);
            // 获取或创建累加器
            A acc = accumulatorMap.computeIfAbsent(key, k -> supplier.get());
            // 直接累加元素
            accumulator.accept(acc, element);
        }

        // 对每个分组应用finisher得到最终结果
        Map<S, V> finalResult = new HashMap<>(accumulatorMap.size());
        for (Map.Entry<S, A> entry : accumulatorMap.entrySet()) {
            finalResult.put(entry.getKey(), finisher.apply(entry.getValue()));
        }

        return new MapStream<>(finalResult);
    }


    // ====================================================================================
    // ====================================================================================


    // ================================ 检查 (Checking)  ==================================
    // ====================================================================================
    //  any(): 检查集合是否包含任何元素。

    public final boolean any() {
        // 这是最直接的方式，判断迭代器是否有下一个元素
        return source.iterator().hasNext();
    }

    //  any { predicate }: 检查集合中是否有任何元素满足给定条件。

    @SafeVarargs
    public final boolean any(Predicate<T>... predicates) {
        return anyMatch(predicates);
    }

    @SafeVarargs
    public final boolean anyMatch(Predicate<T>... predicates) {
        Iterable<T> filteredIterable = createFilteredIterable(elem ->
                Arrays.stream(predicates).anyMatch(predicate -> predicate.test(elem)));
        return of(filteredIterable).isNotEmpty();
    }

    //  all { predicate }: 检查集合中是否所有元素都满足给定条件。

    @SafeVarargs
    public final boolean all(Predicate<T>... predicates) {
        return allMatch(predicates);
    }

    @SafeVarargs
    public final boolean allMatch(Predicate<T>... predicates) {
        Iterable<T> filteredIterable = createFilteredIterable(elem ->
                Arrays.stream(predicates).allMatch(predicate -> predicate.test(elem)));
        return of(filteredIterable).isNotEmpty();
    }

    //  none(): 检查集合是否不包含任何元素。

    public final boolean none() {
        // 最直接的方式就是检查迭代器是否还有下一个元素
        return !source.iterator().hasNext();
    }

    //  none { predicate }: 检查集合中是否没有元素满足给定条件。

    @SafeVarargs
    public final boolean none(Predicate<T>... predicates) {
        return noneMatch(predicates);
    }

    @SafeVarargs
    public final boolean noneMatch(Predicate<T>... predicates) {
        Iterable<T> filteredIterable = createFilteredIterable(elem ->
                Arrays.stream(predicates).anyMatch(predicate -> predicate.test(elem)));
        return of(filteredIterable).isEmpty();
    }

    //  contains(element): 检查集合是否包含指定元素。
    public final boolean contains(T element) {
        Iterator<T> iterator = source.iterator();
        while (iterator.hasNext()) {
            T current = iterator.next();
            if (Objects.equals(current, element)) { // 使用 Objects.equals 安全比较
                return true; // 找到匹配元素，返回 true
            }
        }
        return false; // 遍历完所有元素，没有找到，返回 false
    }

    //  containsAll(elements): 检查集合是否包含指定集合中的所有元素。

    public final boolean containsAll(Collection<T> elements) {
        Objects.requireNonNull(elements, "elements collection cannot be null");

        if (elements.isEmpty()) {
            return true; // 如果要检查的集合是空的，则认为当前集合包含所有其元素
        }

        for (T elementToFind : elements) {
            // 对 elements 中的每个元素，检查它是否在当前的 ListStream 中
            if (!this.contains(elementToFind)) { // 内部会调用上面实现的 contains(element)
                return false; // 只要有一个元素不存在，就返回 false
            }
        }
        return true; // 所有元素都存在，返回 true
    }

    //  isEmpty(): 检查集合是否为空。

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

    private boolean isNotEmpty() {
        return !isEmpty();
    }

    //  single(): 返回集合中唯一的元素，如果集合为空或包含多个元素则抛出异常。
    public final T single() {
        Iterator<T> iterator = source.iterator();

        // 检查是否为空
        if (!iterator.hasNext()) {
            throw new NoSuchElementException("Collection is empty.");
        }

        // 获取第一个元素
        T singleElement = iterator.next();

        // 检查是否还有其他元素
        if (iterator.hasNext()) {
            throw new IllegalStateException("Collection contains more than one element.");
        }

        return singleElement;
    }

    //  singleOrNull(): 返回集合中唯一的元素，如果集合为空或包含多个元素则返回null。
    public final T singleOrNull() {
        Iterator<T> iterator = source.iterator();

        // 检查是否为空
        if (!iterator.hasNext()) {
            return null; // 集合为空，返回 null
        }

        // 获取第一个元素
        T singleElement = iterator.next();

        // 检查是否还有其他元素
        if (iterator.hasNext()) {
            return null; // 集合包含多个元素，返回 null
        }

        return singleElement; // 正好有一个元素，返回该元素
    }
    //  first(): 返回集合中的第一个元素，如果为空则抛出异常。

    public T first() {
        Iterator<T> iterator = source.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            throw new IllegalCallerException("无法找到参数");
        }
    }

    //  first { predicate }: 返回第一个满足条件的元素，如果不存在则抛出异常。

    @SafeVarargs
    public final T first(Predicate<T>... predicates) {
        Objects.requireNonNull(predicates);
        for (final T t : source) {
            if (Arrays.stream(predicates).allMatch(predicate -> predicate.test(t))) {
                return t;
            }
        }
        throw new IllegalCallerException("无法找到参数");
    }


    //  firstOrNull(): 返回集合中的第一个元素，如果为空则返回null。

    public T firstOrNull() {
        Iterator<T> iterator = source.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            return null;
        }
    }

    //  firstOrNull { predicate }: 返回第一个满足条件的元素，如果不存在则返回null。

    public T firstOrNull(Predicate<T> predicate) {
        for (final T t : source) {
            if (predicate.test(t)) {
                return t;
            }
        }
        return null;
    }

    //  last(): 返回集合中的最后一个元素，如果为空则抛出异常。

    public T last() {
        Iterator<T> iterator = source.iterator();

        T next = null;
        while (iterator.hasNext()) {
            next = iterator.next();
        }

        if (next == null) {
            throw new IllegalCallerException("无法找到参数");
        }

        return next;
    }

    //  last { predicate }: 返回最后一个满足条件的元素，如果不存在则抛出异常。

    @SafeVarargs
    public final T last(Predicate<T>... predicates) {
        Objects.requireNonNull(predicates);
        T next = null;
        for (final T temp : source) {
            if (Arrays.stream(predicates).allMatch(predicate -> predicate.test(temp))) {
                next = temp;
            }
        }

        if (next == null) {
            throw new IllegalCallerException("无法找到参数");
        }

        return next;
    }

    //  lastOrNull(): 返回集合中的最后一个元素，如果为空则返回null。

    public T lastOrNull() {
        Iterator<T> iterator = source.iterator();

        T next = null;
        while (iterator.hasNext()) {
            next = iterator.next();
        }

        return next;
    }

    //  lastOrNull { predicate }: 返回最后一个满足条件的元素，如果不存在则返回null。

    @SafeVarargs
    public final T lastOrNull(Predicate<T>... predicates) {
        Objects.requireNonNull(predicates);
        T next = null;
        for (final T temp : source) {
            if (Arrays.stream(predicates).allMatch(predicate -> predicate.test(temp))) {
                next = temp;
            }
        }

        return next;
    }

    //  indexOf(element): 返回指定元素的第一个索引，如果不存在则返回-1。

    public final int indexOf(T element) {
        Iterator<T> iterator = source.iterator();
        int index = 0; // 初始化索引为0

        while (iterator.hasNext()) {
            T current = iterator.next();
            // 使用 Objects.equals 进行安全比较，处理 null 值
            if (Objects.equals(current, element)) {
                return index; // 找到元素，返回当前索引
            }
            index++; // 移动到下一个元素，索引递增
        }

        return -1; // 遍历完所有元素，没有找到，返回 -1
    }

    // ====================================================================================
    // ====================================================================================


    // ================================ 排序 (Ordering)  ==================================
    // ====================================================================================
    //  sorted(): 返回一个新列表，按元素的自然顺序升序排序。
    public final ListStream<T> sorted() {
        // 1. 将所有元素收集到一个临时列表中
        List<T> tempList = toList();

        // 2. 对列表进行自然顺序升序排序
        // 这里会要求 T 是 Comparable 类型，否则编译会报错。
        // 如果你的 T 肯定都是 Comparable 类型，可以这样写。
        // 如果不确定，或者想更通用，请看下面的 "补充说明"。
        Collections.sort((List<Comparable>) tempList); // 强制转换为List<Comparable>，以便调用sort方法

        // 3. 返回一个新的ListStream，包含排序后的元素
        return ListStream.of(tempList);
    }

    //  sortedDescending(): 返回一个新列表，按元素的自然顺序降序排序。

    public <U extends Comparable<? super U>> ListStream<T> sortDesc(
            Function<? super T, ? extends U> keyExtractor) {
        return sort(keyExtractor, Sort.Desc, Sort.NullLast);
    }

    //  sortedBy { selector }: 返回一个新列表，通过选择器函数返回的键进行升序排序。
    //  sortedByDescending { selector }: 返回一个新列表，通过选择器函数返回的键进行降序排序。


    //  sortedWith(comparator): 返回一个新列表，使用提供的比较器进行排序。

    @SuppressWarnings("unused")
    public ListStream<T> sort(Comparator<T> comparator) {
        // 转换为List以进行排序
        List<T> sortedList = toList();
        // 执行排序
        sortedList.sort(comparator);
        return of(sortedList);
    }

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

    //  shuffled(): 返回一个随机排列的新列表。

    public final ListStream<T> shuffled() {
        // 1. 将所有元素收集到一个临时列表中
        List<T> tempList = toList();

        // 2. 随机洗牌这个列表
        Collections.shuffle(tempList);

        // 3. 返回一个新的ListStream，包含洗牌后的元素
        return ListStream.of(tempList);
    }

    //  reversed(): 返回一个元素顺序颠倒的新列表。

    public ListStream<T> reversed() {
        // 反转列表
        List<T> list = toList();
        Collections.reverse(list);
        return of(list);
    }

    // ====================================================================================
    // ====================================================================================


    // ================================ 转换 (Transforming)  ==================================
    // ====================================================================================
    //  toSet(): 将集合转换为一个Set。

    public Set<T> toSet() {
        // 如果是Collection类型，直接返回size
        if (source instanceof Set<T>) {
            return (Set<T>) source;
        }
        Set<T> result = new HashSet<>();
        source.forEach(result::add);
        return result;
    }

    //  toList(): 将集合转换为一个List。

    public List<T> toList() {
        // 如果是Collection类型，直接返回size
        if (source instanceof List<T>) {
            return (List<T>) source;
        }
        List<T> result = new ArrayList<>();
        source.forEach(result::add);
        return result;
    }

    //  toMutableList(): 将集合转换为一个可变的MutableList。
    //  toMutableSet(): 将集合转换为一个可变的MutableSet。
    //  toCollection(destination): 将集合中的所有元素添加到给定的可变集合中。
    //  joinToString(separator = ", ", prefix = "", postfix = "", limit = -1, truncated = "...", transform = null): 将集合中的元素连接成一个字符串。

    public String joining(CharSequence symbol) {
        return joinToString(symbol);
    }

    public String joinToString(CharSequence symbol) {
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


    // ====================================================================================
    // ====================================================================================


    // ================================ 元素操作 (Element Operations)  ==================================
    // ====================================================================================
    //  elementAt(index): 返回指定索引处的元素。

    public T elementAt(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        if (source instanceof final Collection<T> ts) {
            final int endIndex = ts.size() - 1;
            if (endIndex > index) {
                if (ts instanceof List<T> list) {
                    return list.get(index);
                }
                return ts.stream().skip(index).toList().get(0);
            } else if (endIndex == index) {
                return last();
            } else {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + ts.size());
            }
        }
        int i = 0;
        for (final T t : source) {
            if (i == index) {
                return t;
            }
            i++;
        }
        if (index > i) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + (i + 1));
        } else {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + i);
        }
    }

    //  elementAtOrNull(index): 返回指定索引处的元素，如果索引越界则返回null。

    public T elementAtOrNull(int index) {
        if (index < 0) {
            return null;
        }
        if (source instanceof final Collection<T> ts) {
            final int endIndex = ts.size() - 1;
            if (endIndex > index) {
                if (ts instanceof List<T> list) {
                    return list.get(index);
                }
                return ts.stream().skip(index).toList().get(0);
            } else if (endIndex == index) {
                return last();
            } else {
                return null;
            }
        }
        int i = 0;
        for (final T t : source) {
            if (i == index) {
                return t;
            }
            i++;
        }
        return null;
    }

    //  elementAtOrElse(index, defaultValue): 返回指定索引处的元素，如果索引越界则返回defaultValue。
    public T elementAtOrElse(int index, T defaultValue) {
        if (index < 0) {
            return defaultValue;
        }
        if (source instanceof final Collection<T> ts) {
            final int endIndex = ts.size() - 1;
            if (endIndex > index) {
                if (ts instanceof List<T> list) {
                    return list.get(index);
                }
                return ts.stream().skip(index).toList().get(0);
            } else if (endIndex == index) {
                return last();
            } else {
                return defaultValue;
            }
        }
        int i = 0;
        for (final T t : source) {
            if (i == index) {
                return t;
            }
            i++;
        }
        return defaultValue;
    }

    //  plus(element) / plus(elements): 返回一个包含原集合元素和新元素的集合。

    public final ListStream<T> plus(T element) {
        return ListStream.of(() -> new Iterator<>() {
            final Iterator<T> sourceIterator = source.iterator(); // 原始集合的迭代器
            boolean elementAdded = false; // 标记新元素是否已经返回
            T nextElement;
            boolean hasNextComputed = false;
            boolean hasNextResult = false;

            @Override
            public boolean hasNext() {
                if (!hasNextComputed) {
                    computeNext();
                }
                return hasNextResult;
            }

            @Override
            public T next() {
                if (!hasNextComputed) {
                    computeNext();
                }
                if (!hasNextResult) {
                    throw new NoSuchElementException();
                }
                hasNextComputed = false;
                return nextElement;
            }

            private void computeNext() {
                // 首先遍历原始集合的元素
                if (sourceIterator.hasNext()) {
                    nextElement = sourceIterator.next();
                    hasNextResult = true;
                    hasNextComputed = true;
                    return;
                }

                // 如果原始集合遍历完毕，且新元素还没添加
                if (!elementAdded) {
                    nextElement = element; // 返回要添加的单个元素
                    elementAdded = true;    // 标记为已添加
                    hasNextResult = true;
                    hasNextComputed = true;
                    return;
                }

                // 所有元素都已返回
                hasNextResult = false;
                hasNextComputed = true;
            }
        });
    }

    @SafeVarargs
    public final ListStream<T> plus(Iterable<T>... others) {
        Objects.requireNonNull(others, "list cannot be null");
        return ListStream.of(() -> new Iterator<>() {
            final int endIndex = others.length - 1;
            final Iterator<T> iterator = source.iterator();
            int index = 0;
            Iterator<T> otherIterator = null;
            T nextElement;
            boolean hasNextComputed = false;
            boolean hasNextResult = false;

            @Override
            public boolean hasNext() {
                if (!hasNextComputed) {
                    computeNext();
                }
                return hasNextResult;
            }

            @Override
            public T next() {
                if (!hasNextComputed) {
                    computeNext();
                }
                if (!hasNextResult) {
                    throw new NoSuchElementException();
                }
                hasNextComputed = false;
                return nextElement;
            }

            private void computeNext() {
                while (iterator.hasNext()) {
                    nextElement = iterator.next();
                    hasNextResult = true;
                    hasNextComputed = true;
                    return;
                }

                if (otherIterator == null) {
                    otherIterator = others[index].iterator();
                }

                while (index <= endIndex) {
                    while (otherIterator.hasNext()) {
                        nextElement = otherIterator.next();
                        hasNextResult = true;
                        hasNextComputed = true;
                        return;
                    }
                    index += 1;
                    if (index <= endIndex) {
                        otherIterator = others[index].iterator();
                    }
                }

                hasNextResult = false;
                hasNextComputed = true;
            }
        });
    }


    //  minus(element) / minus(elements): 返回一个移除指定元素的集合。

    public final ListStream<T> minus(T element) {
        return ListStream.of(() -> new Iterator<>() {
            final Iterator<T> iterator = source.iterator();
            T nextElement;
            boolean hasNextComputed = false;
            boolean hasNextResult = false;

            @Override
            public boolean hasNext() {
                if (!hasNextComputed) {
                    computeNextSingle();
                }
                return hasNextResult;
            }

            @Override
            public T next() {
                if (!hasNextComputed) {
                    computeNextSingle();
                }
                if (!hasNextResult) {
                    throw new NoSuchElementException();
                }
                hasNextComputed = false;
                return nextElement;
            }

            private void computeNextSingle() {
                while (iterator.hasNext()) {
                    T current = iterator.next();
                    if (!Objects.equals(current, element)) { // 检查是否是要移除的元素
                        nextElement = current;
                        hasNextResult = true;
                        hasNextComputed = true;
                        return;
                    }
                }
                hasNextResult = false;
                hasNextComputed = true;
            }
        });
    }

    @SafeVarargs
    public final ListStream<T> minus(Iterable<T>... elementsToRemove) {
        Objects.requireNonNull(elementsToRemove, "elementsToRemove cannot be null");

        // 将所有要移除的元素收集到一个Set中，以便高效查找
        Set<T> removalSet = new HashSet<>();
        for (Iterable<T> iterable : elementsToRemove) {
            for (T item : iterable) {
                removalSet.add(item);
            }
        }

        return ListStream.of(() -> new Iterator<>() {
            final Iterator<T> iterator = source.iterator();
            T nextElement;
            boolean hasNextComputed = false;
            boolean hasNextResult = false;

            @Override
            public boolean hasNext() {
                if (!hasNextComputed) {
                    computeNextMultiple();
                }
                return hasNextResult;
            }

            @Override
            public T next() {
                if (!hasNextComputed) {
                    computeNextMultiple();
                }
                if (!hasNextResult) {
                    throw new NoSuchElementException();
                }
                hasNextComputed = false;
                return nextElement;
            }

            private void computeNextMultiple() {
                while (iterator.hasNext()) {
                    T current = iterator.next();
                    if (!removalSet.contains(current)) { // 检查当前元素是否在移除集合中
                        nextElement = current;
                        hasNextResult = true;
                        hasNextComputed = true;
                        return;
                    }
                }
                hasNextResult = false;
                hasNextComputed = true;
            }
        });
    }

    // ====================================================================================
    // ====================================================================================

    // ================================ 其他 (Miscellaneous)  ==================================
    // ====================================================================================
    //  chunked(size): 将集合分解成固定大小的块。

    public void chunked(int size, Consumer<List<T>> consumer) {
        split(size, consumer);
    }

    public List<List<T>> chunkedToList(int size) {
        return splitToList(size);
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

    public List<List<T>> splitToList(int size) {
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

        return parts;
    }


    //  windowed(size, step = 1, partialWindows = false): 创建滑动窗口。
    public final ListStream<List<T>> windowed(int size, int step, boolean partialWindows) {
        if (size <= 0) {
            throw new IllegalArgumentException("Window size must be greater than 0: " + size);
        }
        if (step <= 0) {
            throw new IllegalArgumentException("Step must be greater than 0: " + step);
        }

        return ListStream.of(() -> new Iterator<>() {
            final Iterator<T> sourceIterator = source.iterator();
            // 使用 Deque 作为滑动窗口的内部存储，以便高效地添加和移除元素
            final ArrayDeque<T> window = new ArrayDeque<>();
            int currentWindowIndex = 0; // 记录当前窗口的起始索引（对于完整的窗口）
            boolean hasNextComputed = false;
            boolean hasNextResult = false;
            List<T> nextWindow;

            @Override
            public boolean hasNext() {
                if (!hasNextComputed) {
                    computeNext();
                }
                return hasNextResult;
            }

            @Override
            public List<T> next() {
                if (!hasNextComputed) {
                    computeNext();
                }
                if (!hasNextResult) {
                    throw new NoSuchElementException();
                }
                hasNextComputed = false;
                return nextWindow;
            }

            private void computeNext() {
                // 填充或滑动窗口直到找到下一个合格的窗口或耗尽源
                while (sourceIterator.hasNext() || (partialWindows && !window.isEmpty())) {
                    // 如果窗口大小不足，先填充窗口
                    while (sourceIterator.hasNext() && window.size() < size) {
                        window.addLast(sourceIterator.next());
                    }

                    // 检查是否能形成完整窗口
                    if (window.size() == size) {
                        nextWindow = new ArrayList<>(window); // 复制窗口内容
                        hasNextResult = true;
                        hasNextComputed = true;

                        // 根据 step 移除窗口开头的元素
                        for (int i = 0; i < step; i++) {
                            if (!window.isEmpty()) {
                                window.removeFirst();
                            } else {
                                break; // 如果窗口已经空了，就停止移除
                            }
                        }
                        currentWindowIndex += step; // 更新逻辑索引
                        return;
                    }
                    // 如果不能形成完整窗口且允许 partialWindows，并且窗口不为空
                    else if (partialWindows && !window.isEmpty()) {
                        nextWindow = new ArrayList<>(window); // 复制当前（不完整）窗口内容
                        hasNextResult = true;
                        hasNextComputed = true;

                        // 对于 partialWindows，我们通常在返回后清空窗口，
                        // 或者根据 step 移除部分，但通常 partialWindows 表示流的末尾，
                        // 一旦返回了一个不完整的窗口，就意味着结束了。
                        // 如果 step > window.size()，就需要清空窗口并结束。
                        // 这里我们移除所有，因为是最后一次机会返回。
                        window.clear(); // 返回最后一个部分窗口后，清空并结束
                        return;
                    }
                    // 如果 sourceIterator 已经没有更多元素，且窗口为空或不能形成完整窗口
                    else {
                        break; // 退出循环，没有更多窗口
                    }
                }
                // 没有更多窗口可生成
                hasNextResult = false;
                hasNextComputed = true;
            }
        });
    }

    // 提供带默认 step = 1 的重载方法
    public final ListStream<List<T>> windowed(int size) {
        return windowed(size, 1, false);
    }

    // 提供带默认 step = 1 和 partialWindows = false 的重载方法
    public final ListStream<List<T>> windowed(int size, int step) {
        return windowed(size, step, false);
    }
    //  onEach { action }: 对每个元素执行给定的操作，并返回原始集合。
    //  partition { predicate }: 将集合分成两个列表：一个包含满足条件的元素，另一个包含不满足条件的元素。

    public Tuple2<List<T>, List<T>> partitionTuple2(Predicate<T>... predicates) {
        Tuple2<List<T>, List<T>> parts = new Tuple2<>();
        for (T t : source) {
            if (Arrays.stream(predicates).allMatch(predicate -> predicate.test(t))) {
                parts.t1.add(t);
            } else {
                parts.t2.add(t);
            }
        }

        return parts;
    }

    public List<List<T>> partition(Predicate<T>... predicates) {
        List<List<T>> parts = new ArrayList<>();
        parts.add(new ArrayList<>());
        parts.add(new ArrayList<>());
        for (T t : source) {
            if (Arrays.stream(predicates).allMatch(predicate -> predicate.test(t))) {
                parts.get(0).add(t);
            } else {
                parts.get(1).add(t);
            }
        }

        return parts;
    }

    @SafeVarargs
    public final List<List<T>> partitionEveryOne(Predicate<T>... predicates) {
        final int length = predicates.length;
        List<List<T>> parts = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            parts.add(new ArrayList<>());
        }
        for (T t : source) {
            for (int i = 0; i < predicates.length; i++) {
                final Predicate<T> predicate = predicates[i];
                if (predicate.test(t)) {
                    parts.get(i).add(t);
                }
            }
        }

        return parts;
    }

    // ====================================================================================
    // ====================================================================================

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
        list.addAll(X.asList(ts));
        return this;
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

    private Iterable<T> createFilteredIterable(BiPredicate<Integer, T> filterCondition) {
        return () -> new Iterator<>() {
            final Iterator<T> iterator = source.iterator();
            int index = 0;
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

            private void computeNext(BiPredicate<Integer, T> condition) {
                while (iterator.hasNext()) {
                    T elem = iterator.next();
                    if (condition.test(index, elem)) {
                        nextElement = elem;
                        hasNextResult = true;
                        hasNextComputed = true;
                        index += 1;
                        return;
                    }
                    index += 1;
                }
                hasNextResult = false;
                hasNextComputed = true;
            }
        };
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


    public void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        source.forEach(action);
    }


}