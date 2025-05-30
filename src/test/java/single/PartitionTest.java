package single;

import com.zyf.util.X;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.*;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PartitionTest {

    /**
     * 测试目的：验证partition对普通集合的分组。
     * 测试要求：输入为整数集合，谓词为偶数。
     * 测试功能：普通分组。
     * 测试范围：普通整数集合。
     * 测试结果：第一个列表为偶数，第二个为奇数。
     */
    @Test
    @Order(1)
    void testPartitionNormal() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<List<Integer>> result = X.list(list).partition(i -> i % 2 == 0);
        assertEquals(Arrays.asList(2, 4), result.get(0));
        assertEquals(Arrays.asList(1, 3, 5), result.get(1));
    }

    /**
     * 测试目的：验证partition对空集合的分组。
     * 测试要求：输入为空集合。
     * 测试功能：空集合分组。
     * 测试范围：空集合。
     * 测试结果：两个列表均为空。
     */
    @Test
    @Order(2)
    void testPartitionEmpty() {
        List<Integer> list = Collections.emptyList();
        List<List<Integer>> result = X.list(list).partition(i -> i > 0);
        assertTrue(result.get(0).isEmpty());
        assertTrue(result.get(1).isEmpty());
    }

    /**
     * 测试目的：验证partition所有元素都满足谓词。
     * 测试要求：输入为全偶数集合。
     * 测试功能：全满足。
     * 测试范围：全偶数集合。
     * 测试结果：第一个列表为全部元素，第二个为空。
     */
    @Test
    @Order(3)
    void testPartitionAllMatch() {
        List<Integer> list = Arrays.asList(2, 4, 6);
        List<List<Integer>> result = X.list(list).partition(i -> i % 2 == 0);
        assertEquals(list, result.get(0));
        assertTrue(result.get(1).isEmpty());
    }

    /**
     * 测试目的：验证partition所有元素都不满足谓词。
     * 测试要求：输入为全奇数集合。
     * 测试功能：全不满足。
     * 测试范围：全奇数集合。
     * 测试结果：第一个列表为空，第二个为全部元素。
     */
    @Test
    @Order(4)
    void testPartitionNoneMatch() {
        List<Integer> list = Arrays.asList(1, 3, 5);
        List<List<Integer>> result = X.list(list).partition(i -> i % 2 == 0);
        assertTrue(result.get(0).isEmpty());
        assertEquals(list, result.get(1));
    }

    /**
     * 测试目的：验证partition多个谓词的分组。
     * 测试要求：输入为整数集合，谓词为偶数且大于2。
     * 测试功能：多谓词分组。
     * 测试范围：普通整数集合。
     * 测试结果：第一个列表为偶数且大于2，第二个为其他。
     */
    @Test
    @Order(5)
    void testPartitionMultiplePredicates() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<List<Integer>> result = X.list(list).partition(i -> i % 2 == 0, i -> i > 2);
        assertEquals(Arrays.asList(4, 6), result.get(0));
        assertEquals(Arrays.asList(1, 2, 3, 5), result.get(1));
    }

    /**
     * 测试目的：验证partition无谓词时的分组。
     * 测试要求：无谓词。
     * 测试功能：无谓词分组。
     * 测试范围：普通整数集合。
     * 测试结果：第一个列表为全部元素，第二个为空。
     */
    @Test
    @Order(6)
    void testPartitionNoPredicate() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<List<Integer>> result = X.list(list).partition();
        assertEquals(list, result.get(0));
        assertTrue(result.get(1).isEmpty());
    }

    /**
     * 测试目的：验证partition对含null元素集合的分组。
     * 测试要求：集合中包含null。
     * 测试功能：含null分组。
     * 测试范围：含null集合。
     * 测试结果：第一个列表为null，第二个为非null。
     */
    @Test
    @Order(7)
    void testPartitionWithNullElements() {
        List<Integer> list = Arrays.asList(1, null, 3, null, 5);
        List<List<Integer>> result = X.list(list).partition(Objects::isNull);
        assertEquals(Arrays.asList((Integer) null, null), result.get(0));
        assertEquals(Arrays.asList(1, 3, 5), result.get(1));
    }

    /**
     * 测试目的：验证partition对极大集合的分组。
     * 测试要求：输入为极大集合，谓词为1。
     * 测试功能：极值分组。
     * 测试范围：极大集合。
     * 测试结果：第一个列表为全部元素，第二个为空。
     */
    @Test
    @Order(8)
    void testPartitionWithExtremeValues() {
        int size = 10000;
        List<Integer> list = new ArrayList<>(Collections.nCopies(size, 1));
        List<List<Integer>> result = X.list(list).partition(i -> i == 1);
        assertEquals(size, result.get(0).size());
        assertTrue(result.get(1).isEmpty());
    }

    /**
     * 测试目的：验证partition对不同类型元素集合的分组。
     * 测试要求：输入为不同类型元素集合，谓词为字符串类型。
     * 测试功能：多类型分组。
     * 测试范围：混合类型集合。
     * 测试结果：第一个列表为字符串，第二个为其他。
     */
    @Test
    @Order(9)
    void testPartitionWithDifferentTypes() {
        List<Object> list = Arrays.asList(1, "a", 2.0, null);
        List<List<Object>> result = X.list(list).partition(o -> o instanceof String);
        assertEquals(Collections.singletonList("a"), result.get(0));
        assertEquals(Arrays.asList(1, 2.0, null), result.get(1));
    }

    /**
     * 测试目的：验证partition对null集合的分组。
     * 测试要求：输入为null。
     * 测试功能：null集合分组。
     * 测试范围：null集合。
     * 测试结果：两个列表均为空。
     */
    @Test
    @Order(10)
    void testPartitionOnNullList() {
        List<List<Integer>> result = X.list((List<Integer>) null).partition(i -> i != null);
        assertTrue(result.get(0).isEmpty());
        assertTrue(result.get(1).isEmpty());
    }

    /**
     * 测试目的：验证partition谓词为null时抛出异常。
     * 测试要求：谓词为null。
     * 测试功能：异常参数。
     * 测试范围：异常参数。
     * 测试结果：抛出NullPointerException。
     */
    @Test
    @Order(11)
    void testPartitionNullPredicate() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertThrows(NullPointerException.class, () -> X.list(list).partition((Predicate<Integer>[]) null));
    }
}