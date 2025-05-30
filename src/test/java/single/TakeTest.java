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
class TakeTest {

    /**
     * 测试目的：验证take(int)对普通集合的获取。
     * 测试要求：输入为整数集合，获取前2个。
     * 测试功能：普通获取。
     * 测试范围：普通整数集合。
     * 测试结果：返回前2个元素。
     */
    @Test
    @Order(1)
    void testTakeNormal() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> result = X.list(list).take(2).toList();
        assertEquals(Arrays.asList(1, 2), result);
    }

    /**
     * 测试目的：验证take(int)对空集合的获取。
     * 测试要求：输入为空集合。
     * 测试功能：空集合获取。
     * 测试范围：空集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(2)
    void testTakeEmpty() {
        List<Integer> list = Collections.emptyList();
        List<Integer> result = X.list(list).take(2).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证take(int)获取数大于集合长度。
     * 测试要求：n大于集合长度。
     * 测试功能：全部获取。
     * 测试范围：普通整数集合。
     * 测试结果：全部元素保留。
     */
    @Test
    @Order(3)
    void testTakeMoreThanSize() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> result = X.list(list).take(5).toList();
        assertEquals(list, result);
    }

    /**
     * 测试目的：验证take(int)获取数等于集合长度。
     * 测试要求：n等于集合长度。
     * 测试功能：全部获取。
     * 测试范围：普通整数集合。
     * 测试结果：全部元素保留。
     */
    @Test
    @Order(4)
    void testTakeEqualToSize() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> result = X.list(list).take(3).toList();
        assertEquals(list, result);
    }

    /**
     * 测试目的：验证take(int)获取数为0。
     * 测试要求：n为0。
     * 测试功能：不获取。
     * 测试范围：普通整数集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(5)
    void testTakeZero() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> result = X.list(list).take(0).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证take(int)获取数为负数。
     * 测试要求：n为负数。
     * 测试功能：异常参数。
     * 测试范围：异常参数。
     * 测试结果：抛出IllegalArgumentException。
     */
    @Test
    @Order(6)
    void testTakeNegative() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertEquals(list, X.list(list).take(-1).toList());
    }

    /**
     * 测试目的：验证take(int)只剩一个元素。
     * 测试要求：获取n后只剩一个元素。
     * 测试功能：边界。
     * 测试范围：普通整数集合。
     * 测试结果：只剩第一个元素。
     */
    @Test
    @Order(7)
    void testTakeLeaveOne() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> result = X.list(list).take(1).toList();
        assertEquals(Collections.singletonList(1), result);
    }

    /**
     * 测试目的：验证take(int)含null元素。
     * 测试要求：集合中包含null。
     * 测试功能：含null获取。
     * 测试范围：含null集合。
     * 测试结果：获取前3个元素。
     */
    @Test
    @Order(8)
    void testTakeWithNullElements() {
        List<Integer> list = Arrays.asList(null, 2, null, 4, 5);
        List<Integer> result = X.list(list).take(3).toList();
        assertEquals(Arrays.asList(null, 2, null), result);
    }

    /**
     * 测试目的：验证take(int)对极大集合的获取。
     * 测试要求：输入为极大集合，获取前size-1个。
     * 测试功能：极值获取。
     * 测试范围：极大集合。
     * 测试结果：只剩最后一个未获取。
     */
    @Test
    @Order(9)
    void testTakeWithExtremeValues() {
        int size = 10000;
        List<Integer> list = new ArrayList<>(Collections.nCopies(size, 1));
        list.set(size - 1, 2);
        List<Integer> result = X.list(list).take(size - 1).toList();
        assertEquals(size - 1, result.size());
    }

    /**
     * 测试目的：验证take(int)对不同类型元素集合的获取。
     * 测试要求：输入为不同类型元素集合，获取前2个。
     * 测试功能：多类型获取。
     * 测试范围：混合类型集合。
     * 测试结果：获取前2个元素。
     */
    @Test
    @Order(10)
    void testTakeWithDifferentTypes() {
        List<Object> list = Arrays.asList(1, "a", 2.0, null, "b");
        List<Object> result = X.list(list).take(2).toList();
        assertEquals(Arrays.asList(1, "a"), result);
    }

    /**
     * 测试目的：验证take(int)对null集合的获取。
     * 测试要求：输入为null。
     * 测试功能：null集合获取。
     * 测试范围：null集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(11)
    void testTakeOnNullList() {
        List<Integer> result = X.list((List<Integer>) null).take(2).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证limit(int)与take(int)一致。
     * 测试要求：输入为整数集合，limit与take。
     * 测试功能：limit一致性。
     * 测试范围：普通整数集合。
     * 测试结果：limit与take结果一致。
     */
    @Test
    @Order(12)
    void testLimitEqualsTake() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> takeResult = X.list(list).take(3).toList();
        List<Integer> limitResult = X.list(list).limit(3).toList();
        assertEquals(takeResult, limitResult);
    }
}