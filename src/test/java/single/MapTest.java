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
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MapTest {

    /**
     * 测试目的：验证map(Function)对普通集合的映射。
     * 测试要求：输入为整数集合，映射为字符串。
     * 测试功能：普通映射。
     * 测试范围：普通整数集合。
     * 测试结果：返回字符串集合。
     */
    @Test
    @Order(1)
    void testMapNormal() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<String> result = X.list(list).map(String::valueOf).toList();
        assertEquals(Arrays.asList("1", "2", "3"), result);
    }

    /**
     * 测试目的：验证map(Function)对空集合的映射。
     * 测试要求：输入为空集合。
     * 测试功能：空集合映射。
     * 测试范围：空集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(2)
    void testMapEmpty() {
        List<Integer> list = Collections.emptyList();
        List<String> result = X.list(list).map(String::valueOf).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证map(Function)含null元素。
     * 测试要求：集合中包含null。
     * 测试功能：含null映射。
     * 测试范围：含null集合。
     * 测试结果：null元素映射为"null"。
     */
    @Test
    @Order(3)
    void testMapWithNullElements() {
        List<Integer> list = Arrays.asList(1, null, 3);
        List<String> result = X.list(list).map(String::valueOf).toList();
        assertEquals(Arrays.asList("1", "null", "3"), result);
    }

    /**
     * 测试目的：验证map(Function)对极大集合的映射。
     * 测试要求：输入为极大集合。
     * 测试功能：极值映射。
     * 测试范围：极大集合。
     * 测试结果：全部元素映射。
     */
    @Test
    @Order(4)
    void testMapWithExtremeValues() {
        int size = 10000;
        List<Integer> list = new ArrayList<>(Collections.nCopies(size, 1));
        List<String> result = X.list(list).map(String::valueOf).toList();
        assertEquals(size, result.size());
        assertEquals("1", result.get(0));
    }

    /**
     * 测试目的：验证map(Function)对不同类型元素集合的映射。
     * 测试要求：输入为不同类型元素集合，映射为字符串。
     * 测试功能：多类型映射。
     * 测试范围：混合类型集合。
     * 测试结果：全部元素映射为字符串。
     */
    @Test
    @Order(5)
    void testMapWithDifferentTypes() {
        List<Object> list = Arrays.asList(1, "a", 2.0, null);
        List<String> result = X.list(list).map(String::valueOf).toList();
        assertEquals(Arrays.asList("1", "a", "2.0", "null"), result);
    }

    /**
     * 测试目的：验证map(Function)参数为null时抛出异常。
     * 测试要求：函数为null。
     * 测试功能：异常参数。
     * 测试范围：异常参数。
     * 测试结果：抛出NullPointerException。
     */
    @Test
    @Order(6)
    void testMapNullFunction() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertThrows(NullPointerException.class, () -> X.list(list).map((Function<Integer, String>) null).toList());
    }

    /**
     * 测试目的：验证map(Function)对null集合的映射。
     * 测试要求：输入为null。
     * 测试功能：null集合映射。
     * 测试范围：null集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(7)
    void testMapOnNullList() {
        List<String> result = X.list((List<Integer>) null).map(String::valueOf).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证mapIndexed(BiFunction)对普通集合的映射。
     * 测试要求：输入为整数集合，映射为索引+值。
     * 测试功能：索引相关映射。
     * 测试范围：普通整数集合。
     * 测试结果：返回索引+值的字符串集合。
     */
    @Test
    @Order(8)
    void testMapIndexedNormal() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<String> result = X.list(list).mapIndexed((idx, v) -> idx + ":" + v).toList();
        assertEquals(Arrays.asList("0:1", "1:2", "2:3"), result);
    }

    /**
     * 测试目的：验证mapIndexed(BiFunction)对空集合的映射。
     * 测试要求：输入为空集合。
     * 测试功能：空集合映射。
     * 测试范围：空集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(9)
    void testMapIndexedEmpty() {
        List<Integer> list = Collections.emptyList();
        List<String> result = X.list(list).mapIndexed((idx, v) -> idx + ":" + v).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证mapIndexed(BiFunction)含null元素。
     * 测试要求：集合中包含null。
     * 测试功能：含null映射。
     * 测试范围：含null集合。
     * 测试结果：索引+null字符串。
     */
    @Test
    @Order(10)
    void testMapIndexedWithNullElements() {
        List<Integer> list = Arrays.asList(1, null, 3);
        List<String> result = X.list(list).mapIndexed((idx, v) -> idx + ":" + v).toList();
        assertEquals(Arrays.asList("0:1", "1:null", "2:3"), result);
    }

    /**
     * 测试目的：验证mapIndexed(BiFunction)对极大集合的映射。
     * 测试要求：输入为极大集合。
     * 测试功能：极值映射。
     * 测试范围：极大集合。
     * 测试结果：全部元素映射。
     */
    @Test
    @Order(11)
    void testMapIndexedWithExtremeValues() {
        int size = 10000;
        List<Integer> list = new ArrayList<>(Collections.nCopies(size, 1));
        List<String> result = X.list(list).mapIndexed((idx, v) -> idx + ":" + v).toList();
        assertEquals(size, result.size());
        assertEquals("0:1", result.get(0));
    }

    /**
     * 测试目的：验证mapIndexed(BiFunction)对不同类型元素集合的映射。
     * 测试要求：输入为不同类型元素集合，映射为索引+值。
     * 测试功能：多类型映射。
     * 测试范围：混合类型集合。
     * 测试结果：全部元素映射为索引+值。
     */
    @Test
    @Order(12)
    void testMapIndexedWithDifferentTypes() {
        List<Object> list = Arrays.asList(1, "a", 2.0, null);
        List<String> result = X.list(list).mapIndexed((idx, v) -> idx + ":" + v).toList();
        assertEquals(Arrays.asList("0:1", "1:a", "2:2.0", "3:null"), result);
    }

    /**
     * 测试目的：验证mapIndexed(BiFunction)参数为null时抛出异常。
     * 测试要求：函数为null。
     * 测试功能：异常参数。
     * 测试范围：异常参数。
     * 测试结果：抛出NullPointerException。
     */
    @Test
    @Order(13)
    void testMapIndexedNullFunction() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertThrows(NullPointerException.class, () -> X.list(list).mapIndexed((BiFunction<Integer, Integer, String>) null).toList());
    }

    /**
     * 测试目的：验证mapIndexed(BiFunction)对null集合的映射。
     * 测试要求：输入为null。
     * 测试功能：null集合映射。
     * 测试范围：null集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(14)
    void testMapIndexedOnNullList() {
        List<String> result = X.list((List<Integer>) null).mapIndexed((idx, v) -> idx + ":" + v).toList();
        assertTrue(result.isEmpty());
    }
}