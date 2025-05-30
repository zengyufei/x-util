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
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DistinctTest {

    /**
     * 测试目的：验证distinct()对普通集合的去重。
     * 测试要求：输入为整数集合，含重复元素。
     * 测试功能：普通去重。
     * 测试范围：普通整数集合。
     * 测试结果：返回去重后的集合。
     */
    @Test
    @Order(1)
    void testDistinctNormal() {
        List<Integer> list = Arrays.asList(1, 2, 2, 3, 1, 4);
        List<Integer> result = X.list(list).distinct().toList();
        assertEquals(Arrays.asList(1, 2, 3, 4), result);
    }

    /**
     * 测试目的：验证distinct()对空集合的去重。
     * 测试要求：输入为空集合。
     * 测试功能：空集合去重。
     * 测试范围：空集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(2)
    void testDistinctEmpty() {
        List<Integer> list = Collections.emptyList();
        List<Integer> result = X.list(list).distinct().toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证distinct()所有元素唯一。
     * 测试要求：输入为全唯一集合。
     * 测试功能：全唯一。
     * 测试范围：全唯一集合。
     * 测试结果：全部元素保留。
     */
    @Test
    @Order(3)
    void testDistinctAllUnique() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> result = X.list(list).distinct().toList();
        assertEquals(list, result);
    }

    /**
     * 测试目的：验证distinct()所有元素重复。
     * 测试要求：输入为全重复集合。
     * 测试功能：全重复。
     * 测试范围：全重复集合。
     * 测试结果：只保留第一个元素。
     */
    @Test
    @Order(4)
    void testDistinctAllDuplicate() {
        List<Integer> list = Arrays.asList(1, 1, 1);
        List<Integer> result = X.list(list).distinct().toList();
        assertEquals(Collections.singletonList(1), result);
    }

    /**
     * 测试目的：验证distinct()部分元素重复。
     * 测试要求：输入为部分重复集合。
     * 测试功能：部分重复。
     * 测试范围：部分重复集合。
     * 测试结果：只保留第一个出现的元素。
     */
    @Test
    @Order(5)
    void testDistinctPartialDuplicate() {
        List<Integer> list = Arrays.asList(1, 2, 1, 3, 2);
        List<Integer> result = X.list(list).distinct().toList();
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    /**
     * 测试目的：验证distinct()含null元素。
     * 测试要求：集合中包含null。
     * 测试功能：含null去重。
     * 测试范围：含null集合。
     * 测试结果：只保留第一个null。
     */
    @Test
    @Order(6)
    void testDistinctWithNullElements() {
        List<Integer> list = Arrays.asList(null, 1, null, 2, null);
        List<Integer> result = X.list(list).distinct().toList();
        assertEquals(Arrays.asList(null, 1, 2), result);
    }

    /**
     * 测试目的：验证distinct()对极大集合的去重。
     * 测试要求：输入为极大集合，全部为1。
     * 测试功能：极值去重。
     * 测试范围：极大集合。
     * 测试结果：只保留一个元素。
     */
    @Test
    @Order(7)
    void testDistinctWithExtremeValues() {
        int size = 10000;
        List<Integer> list = new ArrayList<>(Collections.nCopies(size, 1));
        List<Integer> result = X.list(list).distinct().toList();
        assertEquals(Collections.singletonList(1), result);
    }

    /**
     * 测试目的：验证distinct()对不同类型元素集合的去重。
     * 测试要求：输入为不同类型元素集合。
     * 测试功能：多类型去重。
     * 测试范围：混合类型集合。
     * 测试结果：只保留第一个出现的不同类型元素。
     */
    @Test
    @Order(8)
    void testDistinctWithDifferentTypes() {
        List<Object> list = Arrays.asList(1, "a", 2.0, 1, "a", null);
        List<Object> result = X.list(list).distinct().toList();
        assertEquals(Arrays.asList(1, "a", 2.0, null), result);
    }

    /**
     * 测试目的：验证distinct(Function)按key去重。
     * 测试要求：输入为字符串集合，按长度去重。
     * 测试功能：keySelector。
     * 测试范围：字符串集合。
     * 测试结果：只保留每个长度的第一个字符串。
     */
    @Test
    @Order(9)
    void testDistinctByKeySelector() {
        List<String> list = Arrays.asList("a", "bb", "cc", "d", "eee", "f");
        List<String> result = X.list(list).distinct(String::length).toList();
        assertEquals(Arrays.asList("a", "bb", "eee"), result);
    }

    /**
     * 测试目的：验证distinct(Function)含null元素。
     * 测试要求：集合中包含null。
     * 测试功能：keySelector含null。
     * 测试范围：含null集合。
     * 测试结果：只保留第一个null。
     */
    @Test
    @Order(10)
    void testDistinctByKeySelectorWithNullElements() {
        List<String> list = Arrays.asList(null, "a", null, "bb", null);
        List<String> result = X.list(list).distinct(s -> s == null ? 0 : s.length()).toList();
        assertEquals(Arrays.asList(null, "a", "bb"), result);
    }

    /**
     * 测试目的：验证distinct(Function)参数为null时抛出异常。
     * 测试要求：keySelector为null。
     * 测试功能：异常参数。
     * 测试范围：异常参数。
     * 测试结果：抛出NullPointerException。
     */
    @Test
    @Order(11)
    void testDistinctByKeySelectorNull() {
        List<String> list = Arrays.asList("a", "b");
        assertThrows(NullPointerException.class, () -> X.list(list).distinct((Function<String, ?>) null).toList());
    }

    /**
     * 测试目的：验证distinct()对null集合的去重。
     * 测试要求：输入为null。
     * 测试功能：null集合去重。
     * 测试范围：null集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(12)
    void testDistinctOnNullList() {
        List<Integer> result = X.list((List<Integer>) null).distinct().toList();
        assertTrue(result.isEmpty());
    }
}