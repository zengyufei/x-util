package com.zyf.util;

import java.math.BigDecimal;
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

    //  drop(n): 返回一个新的列表，移除了前n个元素。

    public ListStream<T> drop(int n) {
        return of(createFilteredIterable((index, elem) -> index + 1 > n));
    }

    public ListStream<T> skip(int n) {
        return of(createFilteredIterable((index, elem) -> index + 1 > n));
    }

    //  dropWhile { predicate }: 从第一个不满足条件的元素开始，返回剩余的元素。

    //  take(n): 返回前n个元素的新列表。

    public ListStream<T> take(int n) {
        return of(createFilteredIterable((index, elem) -> index + 1 <= n));
    }

    public ListStream<T> limit(int n) {
        return of(createFilteredIterable((index, elem) -> index + 1 <= n));
    }

    //  takeWhile { predicate }: 返回从开头开始，连续满足条件的元素。
    //  slice(indices): 返回一个新列表，包含指定索引处的元素。
    //  slice(range): 返回一个新列表，包含指定范围内的元素。
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
        Objects.requireNonNull(other, "other cannot be null");
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
    // ====================================================================================
    // ====================================================================================


    // ================================ 聚合 (Aggregating / Reducing)  ==================================
    // ====================================================================================
    //  reduce { acc, value -> operation }: 从第一个元素开始，将元素累计起来，每次使用上一次的累计值和当前元素进行操作。
    //  reduceIndexed { index, acc, value -> operation }: 类似reduce，但操作函数同时接收元素的索引。
    //  fold(initial) { acc, value -> operation }: 类似reduce，但可以提供一个初始值。
    //  foldIndexed(initial) { index, acc, value -> operation }: 类似fold，但操作函数同时接收元素的索引。
    //  sum(): 计算数字集合中所有元素的和。
    //  average(): 计算数字集合中所有元素的平均值。
    //  count(): 返回集合中的元素数量。
    //  count { predicate }: 返回满足给定条件的元素数量。
    //  maxOrNull(): 返回集合中的最大元素，如果为空则返回null。
    //  maxByOrNull { selector }: 返回通过选择器函数得到最大值的元素，如果为空则返回null。
    //  maxOfOrNull { selector }: 返回通过选择器函数得到的最大值，如果为空则返回null。
    //  minOrNull(): 返回集合中的最小元素，如果为空则返回null。
    //  minByOrNull { selector }: 返回通过选择器函数得到最小值的元素，如果为空则返回null。
    //  minOfOrNull { selector }: 返回通过选择器函数得到的最小值，如果为空则返回null。
    //  sumOf { selector }: 对通过选择器函数得到的数字值求和。
    //  associate { transform }: 将集合中的每个元素映射为一个Pair，然后将这些Pair放入一个Map中。
    //  associateBy { keySelector }: 根据键选择器函数创建一个Map，键是选择器返回的值，值是原始元素。
    //  associateWith { valueTransform }: 根据值转换函数创建一个Map，键是原始元素，值是转换函数返回的值。
    //  groupBy { keySelector }: 根据键选择器函数对元素进行分组，返回一个Map，其中键是选择器返回的值，值是一个包含所有具有该键的元素的列表。
    //  groupingBy { keySelector }: 返回一个Grouping对象，用于更复杂的聚合操作，如eachCount()、fold()等。
    // ====================================================================================
    // ====================================================================================


    // ================================ 检查 (Checking)  ==================================
    // ====================================================================================
    //  any(): 检查集合是否包含任何元素。
    //  any { predicate }: 检查集合中是否有任何元素满足给定条件。
    //  all { predicate }: 检查集合中是否所有元素都满足给定条件。
    //  none(): 检查集合是否不包含任何元素。
    //  none { predicate }: 检查集合中是否没有元素满足给定条件。
    //  contains(element): 检查集合是否包含指定元素。
    //  containsAll(elements): 检查集合是否包含指定集合中的所有元素。
    //  isEmpty(): 检查集合是否为空。
    //  isNotEmpty(): 检查集合是否不为空。
    //  single(): 返回集合中唯一的元素，如果集合为空或包含多个元素则抛出异常。
    //  singleOrNull(): 返回集合中唯一的元素，如果集合为空或包含多个元素则返回null。
    //  first(): 返回集合中的第一个元素，如果为空则抛出异常。
    //  first { predicate }: 返回第一个满足条件的元素，如果不存在则抛出异常。
    //  firstOrNull(): 返回集合中的第一个元素，如果为空则返回null。
    //  firstOrNull { predicate }: 返回第一个满足条件的元素，如果不存在则返回null。
    //  last(): 返回集合中的最后一个元素，如果为空则抛出异常。
    //  last { predicate }: 返回最后一个满足条件的元素，如果不存在则抛出异常。
    //  lastOrNull(): 返回集合中的最后一个元素，如果为空则返回null。
    //  lastOrNull { predicate }: 返回最后一个满足条件的元素，如果不存在则返回null。
    //  indexOf(element): 返回指定元素的第一个索引，如果不存在则返回-1。
    // ====================================================================================
    // ====================================================================================


    // ================================ 排序 (Ordering)  ==================================
    // ====================================================================================
    //  sorted(): 返回一个新列表，按元素的自然顺序升序排序。
    //  sortedDescending(): 返回一个新列表，按元素的自然顺序降序排序。
    //  sortedBy { selector }: 返回一个新列表，通过选择器函数返回的键进行升序排序。
    //  sortedByDescending { selector }: 返回一个新列表，通过选择器函数返回的键进行降序排序。
    //  sortedWith(comparator): 返回一个新列表，使用提供的比较器进行排序。
    //  shuffled(): 返回一个随机排列的新列表。
    //  reversed(): 返回一个元素顺序颠倒的新列表。
    // ====================================================================================
    // ====================================================================================


    // ================================ 转换 (Transforming)  ==================================
    // ====================================================================================
    //  toSet(): 将集合转换为一个Set。
    //  toList(): 将集合转换为一个List。
    //  toMutableList(): 将集合转换为一个可变的MutableList。
    //  toMutableSet(): 将集合转换为一个可变的MutableSet。
    //  toCollection(destination): 将集合中的所有元素添加到给定的可变集合中。
    //  joinToString(separator = ", ", prefix = "", postfix = "", limit = -1, truncated = "...", transform = null): 将集合中的元素连接成一个字符串。
    // ====================================================================================
    // ====================================================================================


    // ================================ 元素操作 (Element Operations)  ==================================
    // ====================================================================================
    //  elementAt(index): 返回指定索引处的元素。
    //  elementAtOrNull(index): 返回指定索引处的元素，如果索引越界则返回null。
    //  elementAtOrElse(index, defaultValue): 返回指定索引处的元素，如果索引越界则返回defaultValue。
    //  plus(element) / plus(elements): 返回一个包含原集合元素和新元素的集合。
    //  minus(element) / minus(elements): 返回一个移除指定元素的集合。
    // ====================================================================================
    // ====================================================================================

    // ================================ 其他 (Miscellaneous)  ==================================
    // ====================================================================================
    //  chunked(size): 将集合分解成固定大小的块。
    //  windowed(size, step = 1, partialWindows = false): 创建滑动窗口。
    //  onEach { action }: 对每个元素执行给定的操作，并返回原始集合。
    //  partition { predicate }: 将集合分成两个列表：一个包含满足条件的元素，另一个包含不满足条件的元素。
    // ====================================================================================
    // ====================================================================================


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