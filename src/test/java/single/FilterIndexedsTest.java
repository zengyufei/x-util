package single;

import com.zyf.util.X;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilterIndexedsTest {

    /**
     * 测试目的：验证filterIndexeds对普通集合的过滤。
     * 测试要求：输入为整数集合，谓词为索引为偶数且元素大于2。
     * 测试功能：普通过滤。
     * 测试范围：普通整数集合。
     * 测试结果：只保留索引为偶数且元素大于2的元素。
     */
    @Test
    @Order(1)
    void testFilterIndexedsNormal() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<Integer> result = X.list(list).filterIndexeds((idx, v) -> idx % 2 == 0, (idx, v) -> v > 2).toList();
        assertEquals(Arrays.asList(3, 5), result);
    }

    /**
     * 测试目的：验证filterIndexeds对空集合的过滤。
     * 测试要求：输入为空集合。
     * 测试功能：空集合过滤。
     * 测试范围：空集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(2)
    void testFilterIndexedsEmpty() {
        List<Integer> list = Collections.emptyList();
        List<Integer> result = X.list(list).filterIndexeds((idx, v) -> v != null).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证filterIndexeds所有元素都满足谓词。
     * 测试要求：输入为全正数集合。
     * 测试功能：全满足。
     * 测试范围：全正数集合。
     * 测试结果：全部元素保留。
     */
    @Test
    @Order(3)
    void testFilterIndexedsAllMatch() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> result = X.list(list).filterIndexeds((idx, v) -> v > 0).toList();
        assertEquals(list, result);
    }

    /**
     * 测试目的：验证filterIndexeds所有元素都不满足谓词。
     * 测试要求：输入为全负数集合。
     * 测试功能：全不满足。
     * 测试范围：全负数集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(4)
    void testFilterIndexedsNoneMatch() {
        List<Integer> list = Arrays.asList(-1, -2, -3);
        List<Integer> result = X.list(list).filterIndexeds((idx, v) -> v > 0).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证filterIndexeds部分元素满足多个谓词。
     * 测试要求：输入为整数集合，谓词为索引为偶数或元素大于3。
     * 测试功能：部分满足。
     * 测试范围：普通整数集合。
     * 测试结果：只保留索引为偶数且元素大于3的元素。
     */
    @Test
    @Order(5)
    void testFilterIndexedsPartialMatch() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> result = X.list(list).filterIndexeds((idx, v) -> idx % 2 == 0, (idx, v) -> v > 3).toList();
        assertEquals(Collections.singletonList(5), result);
    }

    /**
     * 测试目的：验证filterIndexeds无谓词时的过滤。
     * 测试要求：无谓词。
     * 测试功能：无谓词过滤。
     * 测试范围：普通整数集合。
     * 测试结果：全部元素保留。
     */
    @Test
    @Order(6)
    void testFilterIndexedsNoPredicate() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> result = X.list(list).filterIndexeds().toList();
        assertEquals(list, result);
    }

    /**
     * 测试目的：验证filterIndexeds对含null元素集合的过滤。
     * 测试要求：集合中包含null。
     * 测试功能：含null过滤。
     * 测试范围：含null集合。
     * 测试结果：只保留索引为奇数的null元素。
     */
    @Test
    @Order(7)
    void testFilterIndexedsWithNullElements() {
        List<Integer> list = Arrays.asList(1, null, 3, null, 5);
        List<Integer> result = X.list(list).filterIndexeds((idx, v) -> v == null && idx % 2 == 1).toList();
        assertEquals(Arrays.asList(null, null), result);
    }

    /**
     * 测试目的：验证filterIndexeds对极大集合的过滤。
     * 测试要求：输入为极大集合，谓词为索引小于size-1。
     * 测试功能：极值过滤。
     * 测试范围：极大集合。
     * 测试结果：只保留前size-1个元素。
     */
    @Test
    @Order(8)
    void testFilterIndexedsWithExtremeValues() {
        int size = 10000;
        List<Integer> list = new ArrayList<>(Collections.nCopies(size, 1));
        List<Integer> result = X.list(list).filterIndexeds((idx, v) -> idx < size - 1).toList();
        assertEquals(size - 1, result.size());
    }

    /**
     * 测试目的：验证filterIndexeds对不同类型元素集合的过滤。
     * 测试要求：输入为不同类型元素集合，谓词为索引为偶数且为字符串。
     * 测试功能：多类型过滤。
     * 测试范围：混合类型集合。
     * 测试结果：只保留索引为偶数且为字符串的元素。
     */
    @Test
    @Order(9)
    void testFilterIndexedsWithDifferentTypes() {
        List<Object> list = Arrays.asList(1, "a", 2.0, null, "b");
        List<Object> result = X.list(list).filterIndexeds((idx, v) -> idx % 2 == 0 && v instanceof String).toList();
        assertEquals(Collections.singletonList("b"), result);
    }

    /**
     * 测试目的：验证filterIndexeds对null集合的过滤。
     * 测试要求：输入为null。
     * 测试功能：null集合过滤。
     * 测试范围：null集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(10)
    void testFilterIndexedsOnNullList() {
        List<Integer> result = X.list((List<Integer>) null).filterIndexeds((idx, v) -> v != null).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证filterIndexeds谓词为null时抛出异常。
     * 测试要求：谓词为null。
     * 测试功能：异常参数。
     * 测试范围：异常参数。
     * 测试结果：抛出NullPointerException。
     */
    @Test
    @Order(11)
    void testFilterIndexedsNullPredicate() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertThrows(NullPointerException.class, () -> X.list(list).filterIndexeds((BiPredicate<Integer, Integer>[]) null).toList());
    }
}