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

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilterIsInstanceTest {

    /**
     * 测试目的：验证filterIsInstance对普通集合的过滤。
     * 测试要求：输入为混合类型集合，过滤String类型。
     * 测试功能：类型过滤。
     * 测试范围：混合类型集合。
     * 测试结果：只保留String类型元素。
     */
    @Test
    @Order(1)
    void testFilterIsInstanceNormal() {
        List<Object> list = Arrays.asList(1, "a", 2.0, null, "b", 300L);
        assertEquals(Arrays.asList("a", "b"), X.list(list).filterIsInstance(String.class).toList());
        assertEquals(Arrays.asList(1), X.list(list).filterIsInstance(Integer.class).toList());
        assertEquals(Arrays.asList(2.0), X.list(list).filterIsInstance(Double.class).toList());
        assertEquals(Arrays.asList(300L), X.list(list).filterIsInstance(Long.class).toList());
    }

    /**
     * 测试目的：验证filterIsInstance对空集合的过滤。
     * 测试要求：输入为空集合。
     * 测试功能：空集合过滤。
     * 测试范围：空集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(2)
    void testFilterIsInstanceEmpty() {
        List<Object> list = Collections.emptyList();
        List<String> result = X.list(list).filterIsInstance(String.class).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证filterIsInstance所有元素都为目标类型。
     * 测试要求：输入为全String集合。
     * 测试功能：全为目标类型。
     * 测试范围：全String集合。
     * 测试结果：全部元素保留。
     */
    @Test
    @Order(3)
    void testFilterIsInstanceAllMatch() {
        List<Object> list = Arrays.asList("a", "b", "c");
        List<String> result = X.list(list).filterIsInstance(String.class).toList();
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }

    /**
     * 测试目的：验证filterIsInstance所有元素都不为目标类型。
     * 测试要求：输入为全Integer集合。
     * 测试功能：全不为目标类型。
     * 测试范围：全Integer集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(4)
    void testFilterIsInstanceNoneMatch() {
        List<Object> list = Arrays.asList(1, 2, 3);
        List<String> result = X.list(list).filterIsInstance(String.class).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证filterIsInstance部分元素为目标类型。
     * 测试要求：输入为混合类型集合。
     * 测试功能：部分为目标类型。
     * 测试范围：混合类型集合。
     * 测试结果：只保留目标类型元素。
     */
    @Test
    @Order(5)
    void testFilterIsInstancePartialMatch() {
        List<Object> list = Arrays.asList(1, "a", 2, "b", 3);
        List<String> result = X.list(list).filterIsInstance(String.class).toList();
        assertEquals(Arrays.asList("a", "b"), result);
    }

    /**
     * 测试目的：验证filterIsInstance对含null元素集合的过滤。
     * 测试要求：集合中包含null。
     * 测试功能：含null过滤。
     * 测试范围：含null集合。
     * 测试结果：只保留目标类型元素。
     */
    @Test
    @Order(6)
    void testFilterIsInstanceWithNullElements() {
        List<Object> list = Arrays.asList(null, "a", null, "b", null);
        List<String> result = X.list(list).filterIsInstance(String.class).toList();
        assertEquals(Arrays.asList("a", "b"), result);
    }

    /**
     * 测试目的：验证filterIsInstance对极大集合的过滤。
     * 测试要求：输入为极大集合，部分为目标类型。
     * 测试功能：极值过滤。
     * 测试范围：极大集合。
     * 测试结果：只保留目标类型元素。
     */
    @Test
    @Order(7)
    void testFilterIsInstanceWithExtremeValues() {
        int size = 10000;
        List<Object> list = new ArrayList<>(Collections.nCopies(size, 1));
        list.set(0, "a");
        list.set(size - 1, "b");
        List<String> result = X.list(list).filterIsInstance(String.class).toList();
        assertEquals(Arrays.asList("a", "b"), result);
    }

    /**
     * 测试目的：验证filterIsInstance对null集合的过滤。
     * 测试要求：输入为null。
     * 测试功能：null集合过滤。
     * 测试范围：null集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(8)
    void testFilterIsInstanceOnNullList() {
        List<String> result = X.list(null).filterIsInstance(String.class).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证filterIsInstance类型参数为null时抛出异常。
     * 测试要求：类型参数为null。
     * 测试功能：异常参数。
     * 测试范围：异常参数。
     * 测试结果：抛出NullPointerException。
     */
    @Test
    @Order(9)
    void testFilterIsInstanceNullType() {
        List<Object> list = Arrays.asList(1, "a", 2.0);
        assertThrows(NullPointerException.class, () -> X.list(list).filterIsInstance(null).toList());
    }
}