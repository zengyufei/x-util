package single;

import com.zyf.util.Tuple2;
import com.zyf.util.X;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.*;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PartitionTuple2Test {

    /**
     * 测试目的：验证partitionTuple2对普通集合的分组。
     * 测试要求：输入为整数集合，谓词为偶数。
     * 测试功能：普通分组。
     * 测试范围：普通整数集合。
     * 测试结果：t1为偶数，t2为奇数。
     */
    @Test
    @Order(1)
    void testPartitionTuple2Normal() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        Tuple2<List<Integer>, List<Integer>> result = X.list(list).partitionTuple2(i -> i % 2 == 0);
        assertEquals(Arrays.asList(2, 4), result.getT1());
        assertEquals(Arrays.asList(1, 3, 5), result.getT2());
    }

    /**
     * 测试目的：验证partitionTuple2对空集合的分组。
     * 测试要求：输入为空集合。
     * 测试功能：空集合分组。
     * 测试范围：空集合。
     * 测试结果：t1和t2均为空。
     */
    @Test
    @Order(2)
    void testPartitionTuple2Empty() {
        List<Integer> list = Collections.emptyList();
        Tuple2<List<Integer>, List<Integer>> result = X.list(list).partitionTuple2(i -> i > 0);
        assertTrue(result.getT1().isEmpty());
        assertTrue(result.getT2().isEmpty());
    }

    /**
     * 测试目的：验证partitionTuple2所有元素都满足谓词。
     * 测试要求：输入为全偶数集合。
     * 测试功能：全满足。
     * 测试范围：全偶数集合。
     * 测试结果：t1为全部元素，t2为空。
     */
    @Test
    @Order(3)
    void testPartitionTuple2AllMatch() {
        List<Integer> list = Arrays.asList(2, 4, 6);
        Tuple2<List<Integer>, List<Integer>> result = X.list(list).partitionTuple2(i -> i % 2 == 0);
        assertEquals(list, result.getT1());
        assertTrue(result.getT2().isEmpty());
    }

    /**
     * 测试目的：验证partitionTuple2所有元素都不满足谓词。
     * 测试要求：输入为全奇数集合。
     * 测试功能：全不满足。
     * 测试范围：全奇数集合。
     * 测试结果：t1为空，t2为全部元素。
     */
    @Test
    @Order(4)
    void testPartitionTuple2NoneMatch() {
        List<Integer> list = Arrays.asList(1, 3, 5);
        Tuple2<List<Integer>, List<Integer>> result = X.list(list).partitionTuple2(i -> i % 2 == 0);
        assertTrue(result.getT1().isEmpty());
        assertEquals(list, result.getT2());
    }

    /**
     * 测试目的：验证partitionTuple2多个谓词的分组。
     * 测试要求：输入为整数集合，谓词为偶数且大于2。
     * 测试功能：多谓词分组。
     * 测试范围：普通整数集合。
     * 测试结果：t1为偶数且大于2，t2为其他。
     */
    @Test
    @Order(5)
    void testPartitionTuple2MultiplePredicates() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
        Tuple2<List<Integer>, List<Integer>> result = X.list(list).partitionTuple2(i -> i % 2 == 0, i -> i > 2);
        assertEquals(Arrays.asList(4, 6), result.getT1());
        assertEquals(Arrays.asList(1, 2, 3, 5), result.getT2());
    }

    /**
     * 测试目的：验证partitionTuple2无谓词时的分组。
     * 测试要求：无谓词。
     * 测试功能：无谓词分组。
     * 测试范围：普通整数集合。
     * 测试结果：t1为全部元素，t2为空。
     */
    @Test
    @Order(6)
    void testPartitionTuple2NoPredicate() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Tuple2<List<Integer>, List<Integer>> result = X.list(list).partitionTuple2();
        assertEquals(list, result.getT1());
        assertTrue(result.getT2().isEmpty());
    }

    /**
     * 测试目的：验证partitionTuple2对含null元素集合的分组。
     * 测试要求：集合中包含null。
     * 测试功能：含null分组。
     * 测试范围：含null集合。
     * 测试结果：t1为null，t2为非null。
     */
    @Test
    @Order(7)
    void testPartitionTuple2WithNullElements() {
        List<Integer> list = Arrays.asList(1, null, 3, null, 5);
        Tuple2<List<Integer>, List<Integer>> result = X.list(list).partitionTuple2(Objects::isNull);
        assertEquals(Arrays.asList((Integer) null, null), result.getT1());
        assertEquals(Arrays.asList(1, 3, 5), result.getT2());
    }

    /**
     * 测试目的：验证partitionTuple2对极大集合的分组。
     * 测试要求：输入为极大集合，谓词为1。
     * 测试功能：极值分组。
     * 测试范围：极大集合。
     * 测试结果：t1为全部元素，t2为空。
     */
    @Test
    @Order(8)
    void testPartitionTuple2WithExtremeValues() {
        int size = 10000;
        List<Integer> list = new ArrayList<>(Collections.nCopies(size, 1));
        Tuple2<List<Integer>, List<Integer>> result = X.list(list).partitionTuple2(i -> i == 1);
        assertEquals(size, result.getT1().size());
        assertTrue(result.getT2().isEmpty());
    }

    /**
     * 测试目的：验证partitionTuple2对不同类型元素集合的分组。
     * 测试要求：输入为不同类型元素集合，谓词为字符串类型。
     * 测试功能：多类型分组。
     * 测试范围：混合类型集合。
     * 测试结果：t1为字符串，t2为其他。
     */
    @Test
    @Order(9)
    void testPartitionTuple2WithDifferentTypes() {
        List<Object> list = Arrays.asList(1, "a", 2.0, null);
        Tuple2<List<Object>, List<Object>> result = X.list(list).partitionTuple2(o -> o instanceof String);
        assertEquals(Collections.singletonList("a"), result.getT1());
        assertEquals(Arrays.asList(1, 2.0, null), result.getT2());
    }

    /**
     * 测试目的：验证partitionTuple2对null集合的分组。
     * 测试要求：输入为null。
     * 测试功能：null集合分组。
     * 测试范围：null集合。
     * 测试结果：t1和t2均为空。
     */
    @Test
    @Order(10)
    void testPartitionTuple2OnNullList() {
        Tuple2<List<Integer>, List<Integer>> result = X.list((List<Integer>) null).partitionTuple2(i -> i != null);
        assertTrue(result.getT1().isEmpty());
        assertTrue(result.getT2().isEmpty());
    }

    /**
     * 测试目的：验证partitionTuple2谓词为null时抛出异常。
     * 测试要求：谓词为null。
     * 测试功能：异常参数。
     * 测试范围：异常参数。
     * 测试结果：抛出NullPointerException。
     */
    @Test
    @Order(11)
    void testPartitionTuple2NullPredicate() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertThrows(NullPointerException.class, () -> X.list(list).partitionTuple2((Predicate<Integer>[]) null));
    }
}