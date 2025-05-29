package com.zyf.util;

import java.util.Comparator;
import java.util.function.Function;

// 内部类，封装流操作
public final class SortStream<T> {
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
