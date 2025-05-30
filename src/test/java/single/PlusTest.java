package single;

import com.zyf.util.X;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PlusTest {

    /**
     * 测试目的：验证plus(T element)能正确添加单个元素。
     * 测试要求：输入为整数集合，添加一个元素。
     * 测试功能：单元素添加。
     * 测试范围：普通整数集合。
     * 测试结果：返回包含新元素的集合。
     */
    @Test
    @Order(1)
    void testPlusSingleElement() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> result = X.list(list).plus(4).toList();
        assertEquals(Arrays.asList(1, 2, 3, 4), result);
    }

    /**
     * 测试目的：验证plus(T element)在空集合时的行为。
     * 测试要求：输入为空集合，添加一个元素。
     * 测试功能：空集合添加。
     * 测试范围：空集合。
     * 测试结果：返回仅包含新元素的集合。
     */
    @Test
    @Order(2)
    void testPlusSingleElementToEmpty() {
        List<Integer> list = Collections.emptyList();
        List<Integer> result = X.list(list).plus(42).toList();
        assertEquals(Collections.singletonList(42), result);
    }

    /**
     * 测试目的：验证plus(Iterable...)能正确添加多个集合。
     * 测试要求：输入为整数集合，添加多个集合。
     * 测试功能：多集合合并。
     * 测试范围：普通整数集合。
     * 测试结果：返回合并后的集合。
     */
    @Test
    @Order(3)
    void testPlusMultipleIterables() {
        List<Integer> list = Arrays.asList(1, 2);
        List<Integer> other1 = Arrays.asList(3, 4);
        List<Integer> other2 = Arrays.asList(5, 6);
        List<Integer> result = X.list(list).plus(other1, other2).toList();
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6), result);
    }

    /**
     * 测试目的：验证plus(Iterable...)在空集合时的行为。
     * 测试要求：输入为空集合，添加多个集合。
     * 测试功能：空集合合并。
     * 测试范围：空集合。
     * 测试结果：返回合并后的集合。
     */
    @Test
    @Order(4)
    void testPlusMultipleIterablesToEmpty() {
        List<Integer> list = Collections.emptyList();
        List<Integer> other1 = Arrays.asList(1, 2);
        List<Integer> other2 = Arrays.asList(3, 4);
        List<Integer> result = X.list(list).plus(other1, other2).toList();
        assertEquals(Arrays.asList(1, 2, 3, 4), result);
    }

    /**
     * 测试目的：验证plus(T element)和plus(Iterable...)的链式调用。
     * 测试要求：输入为整数集合，先添加单个元素再添加集合。
     * 测试功能：链式添加。
     * 测试范围：普通整数集合。
     * 测试结果：返回正确合并后的集合。
     */
    @Test
    @Order(5)
    void testPlusChained() {
        List<Integer> list = Arrays.asList(1, 2);
        List<Integer> other = Arrays.asList(3, 4);
        List<Integer> result = X.list(list).plus(5).plus(other).toList();
        assertEquals(Arrays.asList(1, 2, 5, 3, 4), result);
    }

    /**
     * 测试目的：验证plus(Iterable...)参数为null时抛出异常。
     * 测试要求：plus(null)应抛出NullPointerException。
     * 测试功能：异常场景。
     * 测试范围：异常参数。
     * 测试结果：抛出NullPointerException。
     */
    @Test
    @Order(6)
    void testPlusNullIterable() {
        List<Integer> list = Arrays.asList(1, 2);
        assertThrows(NullPointerException.class, () -> X.list(list).plus((Iterable<Integer>[]) null));
    }

    /**
     * 测试目的：验证plus(T element)添加null元素。
     * 测试要求：添加null。
     * 测试功能：添加null。
     * 测试范围：普通集合。
     * 测试结果：集合包含null。
     */
    @Test
    @Order(7)
    void testPlusNullElement() {
        List<String> list = Arrays.asList("a", "b");
        List<String> result = X.list(list).plus((String) null).toList();
        assertEquals(Arrays.asList("a", "b", null), result);
    }

    /**
     * 测试目的：验证plus(Iterable...)添加空集合。
     * 测试要求：添加空集合。
     * 测试功能：添加空集合。
     * 测试范围：普通集合。
     * 测试结果：集合不变。
     */
    @Test
    @Order(8)
    void testPlusEmptyIterable() {
        List<Integer> list = Arrays.asList(1, 2);
        List<Integer> result = X.list(list).plus(Collections.emptyList()).toList();
        assertEquals(Arrays.asList(1, 2), result);
    }
}