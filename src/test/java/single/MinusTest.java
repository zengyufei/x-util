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
class MinusTest {

    /**
     * 测试目的：验证minus(T element)移除单个元素。
     * 测试要求：输入为整数集合，移除指定元素。
     * 测试功能：移除单个元素。
     * 测试范围：普通整数集合。
     * 测试结果：返回移除后的集合。
     */
    @Test
    @Order(1)
    void testMinusSingleElement() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> result = X.list(list).minus(3).toList();
        assertEquals(Arrays.asList(1, 2, 4, 5), result);
    }

    /**
     * 测试目的：验证minus(T element)移除不存在的元素。
     * 测试要求：输入为整数集合，移除不存在的元素。
     * 测试功能：无移除。
     * 测试范围：普通整数集合。
     * 测试结果：集合不变。
     */
    @Test
    @Order(2)
    void testMinusNonExistentElement() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> result = X.list(list).minus(99).toList();
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    /**
     * 测试目的：验证minus(T element)对空集合的处理。
     * 测试要求：输入为空集合。
     * 测试功能：空集合移除。
     * 测试范围：空集合。
     * 测试结果：返回空集合。
     */
    @Test
    @Order(3)
    void testMinusOnEmptyList() {
        List<Integer> list = Collections.emptyList();
        List<Integer> result = X.list(list).minus(1).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证minus(Iterable...)移除多个元素。
     * 测试要求：输入为整数集合，移除多个元素。
     * 测试功能：批量移除。
     * 测试范围：普通整数集合。
     * 测试结果：返回移除后的集合。
     */
    @Test
    @Order(4)
    void testMinusMultipleElements() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> toRemove = Arrays.asList(2, 4);
        List<Integer> result = X.list(list).minus(toRemove).toList();
        assertEquals(Arrays.asList(1, 3, 5), result);
    }

    /**
     * 测试目的：验证minus(Iterable...)移除所有元素。
     * 测试要求：输入为整数集合，移除所有元素。
     * 测试功能：全移除。
     * 测试范围：普通整数集合。
     * 测试结果：返回空集合。
     */
    @Test
    @Order(5)
    void testMinusAllElements() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> toRemove = Arrays.asList(1, 2, 3);
        List<Integer> result = X.list(list).minus(toRemove).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证minus(Iterable...)参数为多个集合时的行为。
     * 测试要求：输入为整数集合，移除多个集合的元素。
     * 测试功能：多集合批量移除。
     * 测试范围：普通整数集合。
     * 测试结果：返回正确集合。
     */
    @Test
    @Order(6)
    void testMinusMultipleIterables() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<Integer> remove1 = Arrays.asList(2, 4);
        List<Integer> remove2 = Arrays.asList(5, 6);
        List<Integer> result = X.list(list).minus(remove1, remove2).toList();
        assertEquals(Arrays.asList(1, 3), result);
    }

    /**
     * 测试目的：验证minus(T element)和minus(Iterable...)的链式调用。
     * 测试要求：输入为整数集合，先移除单个元素再移除集合。
     * 测试功能：链式移除。
     * 测试范围：普通整数集合。
     * 测试结果：返回正确集合。
     */
    @Test
    @Order(7)
    void testMinusChained() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> result = X.list(list).minus(1).minus(Arrays.asList(3, 5)).toList();
        assertEquals(Arrays.asList(2, 4), result);
    }

    /**
     * 测试目的：验证minus(Iterable...)参数为null时抛出异常。
     * 测试要求：minus(null)应抛出NullPointerException。
     * 测试功能：异常场景。
     * 测试范围：异常参数。
     * 测试结果：抛出NullPointerException。
     */
    @Test
    @Order(8)
    void testMinusNullArgument() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertEquals(list, X.list(list).minus((Iterable<Integer>[]) null).toList());
    }

    /**
     * 测试目的：验证minus(T element)参数为null时的行为。
     * 测试要求：minus(null)应移除所有null元素。
     * 测试功能：移除null元素。
     * 测试范围：含null集合。
     * 测试结果：返回移除null后的集合。
     */
    @Test
    @Order(9)
    void testMinusNullElement() {
        List<Integer> list = Arrays.asList(1, null, 2, null, 3);
        List<Integer> result = X.list(list).minus((Integer) null).toList();
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    /**
     * 测试目的：验证minus(Iterable...)移除不同类型元素。
     * 测试要求：输入为Object集合，移除字符串类型元素。
     * 测试功能：不同类型移除。
     * 测试范围：混合类型集合。
     * 测试结果：返回移除后的集合。
     */
    @Test
    @Order(10)
    void testMinusWithDifferentTypes() {
        List<Object> list = Arrays.asList(1, "a", 2.0, null);
        List<Object> result = X.list(list)
                .minus("a")
                .minus(null)
                .toList();
        assertEquals(Arrays.asList(1, 2.0, null), result);
    }

    /**
     * 测试目的：验证minus(Iterable...)对极值集合的处理。
     * 测试要求：输入为极大集合，移除部分元素。
     * 测试功能：极值移除。
     * 测试范围：极值集合。
     * 测试结果：返回正确集合。
     */
    @Test
    @Order(11)
    void testMinusWithExtremeValues() {
        int size = 10000;
        List<Integer> list = new ArrayList<>(Collections.nCopies(size, 1));
        list.set(0, 999);
        List<Integer> toRemove = Arrays.asList(1);
        List<Integer> result = X.list(list).minus(toRemove).toList();
        assertEquals(Collections.singletonList(999), result);
    }

    /**
     * 测试目的：验证minus(T element)对null集合的处理。
     * 测试要求：输入为null。
     * 测试功能：null集合移除。
     * 测试范围：null集合。
     * 测试结果：返回空集合。
     */
    @Test
    @Order(12)
    void testMinusOnNullList() {
        List<Object> result = X.list(null).minus(1).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证minus(Iterable...)对null集合的处理。
     * 测试要求：输入为null。
     * 测试功能：null集合移除。
     * 测试范围：null集合。
     * 测试结果：返回空集合。
     */
    @Test
    @Order(13)
    void testMinusIterableOnNullList() {
        List<Integer> toRemove = Arrays.asList(1, 2);
        List<Object> result = X.list(null).minus(toRemove).toList();
        assertTrue(result.isEmpty());
    }
}