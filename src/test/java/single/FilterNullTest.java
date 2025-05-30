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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilterNullTest {

    /**
     * 测试目的：验证filterNulls对普通集合的过滤。
     * 测试要求：输入为含null和非null的集合。
     * 测试功能：过滤null。
     * 测试范围：普通集合。
     * 测试结果：只保留非null元素。
     */
    @Test
    @Order(1)
    void testFilterNullsNormal() {
        List<Integer> list = Arrays.asList(1, null, 3, null, 5);
        List<Integer> result = X.list(list).filterNull().toList();
        assertEquals(Arrays.asList(null, null), result);
    }

    /**
     * 测试目的：验证filterNulls对空集合的过滤。
     * 测试要求：输入为空集合。
     * 测试功能：空集合过滤。
     * 测试范围：空集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(2)
    void testFilterNullsEmpty() {
        List<Integer> list = Collections.emptyList();
        List<Integer> result = X.list(list).filterNull().toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证filterNulls所有元素都为null。
     * 测试要求：输入为全null集合。
     * 测试功能：全为null。
     * 测试范围：全null集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(3)
    void testFilterNullsAllNull() {
        List<Integer> list = Arrays.asList(null, null, null);
        List<Integer> result = X.list(list).filterNull().toList();
        assertTrue(!result.isEmpty());
    }

    /**
     * 测试目的：验证filterNulls所有元素都非null。
     * 测试要求：输入为全非null集合。
     * 测试功能：全非null。
     * 测试范围：全非null集合。
     * 测试结果：全部元素保留。
     */
    @Test
    @Order(4)
    void testFilterNullsAllNonNull() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> result = X.list(list).filterNull().toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证filterNulls对不同类型元素集合的过滤。
     * 测试要求：输入为不同类型元素集合，含null。
     * 测试功能：多类型过滤。
     * 测试范围：混合类型集合。
     * 测试结果：只保留非null元素。
     */
    @Test
    @Order(5)
    void testFilterNullsWithDifferentTypes() {
        List<Object> list = Arrays.asList(1, "a", null, 2.0, null);
        List<Object> result = X.list(list).filterNull().toList();
        assertEquals(Arrays.asList(null, null), result);
    }

    /**
     * 测试目的：验证filterNulls对极大集合的过滤。
     * 测试要求：输入为极大集合，部分为null。
     * 测试功能：极值过滤。
     * 测试范围：极大集合。
     * 测试结果：只保留非null元素。
     */
    @Test
    @Order(6)
    void testFilterNullsWithExtremeValues() {
        int size = 10000;
        List<Integer> list = new ArrayList<>(Collections.nCopies(size, 1));
        list.set(0, null);
        list.set(size - 1, null);
        List<Integer> result = X.list(list).filterNull().toList();
        assertEquals(2, result.size());
    }

    /**
     * 测试目的：验证filterNulls对null集合的过滤。
     * 测试要求：输入为null。
     * 测试功能：null集合过滤。
     * 测试范围：null集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(7)
    void testFilterNullsOnNullList() {
        List<Integer> result = X.list((List<Integer>) null).filterNull().toList();
        assertTrue(result.isEmpty());
    }
}