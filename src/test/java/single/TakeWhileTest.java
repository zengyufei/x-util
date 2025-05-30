package single;

import com.zyf.util.X;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TakeWhileTest {

    /**
     * 测试目的：验证takeWhile对普通集合的获取。
     * 测试要求：输入为整数集合，谓词为小于3。
     * 测试功能：普通获取。
     * 测试范围：普通整数集合。
     * 测试结果：获取前面小于3的元素。
     */
    @Test
    @Order(1)
    void testTakeWhileNormal() {
        List<Integer> list = Arrays.asList(1, 2, 3, 2, 1);
        List<Integer> result = X.list(list).takeWhile(i -> i < 3).toList();
        assertEquals(Arrays.asList(1, 2), result);
    }

    /**
     * 测试目的：验证takeWhile对空集合的获取。
     * 测试要求：输入为空集合。
     * 测试功能：空集合获取。
     * 测试范围：空集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(2)
    void testTakeWhileEmpty() {
        List<Integer> list = Collections.emptyList();
        List<Integer> result = X.list(list).takeWhile(i -> i < 3).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证takeWhile所有元素都满足谓词。
     * 测试要求：输入为全小于10集合。
     * 测试功能：全获取。
     * 测试范围：全小于10集合。
     * 测试结果：全部元素保留。
     */
    @Test
    @Order(3)
    void testTakeWhileAllMatch() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> result = X.list(list).takeWhile(i -> i < 10).toList();
        assertEquals(list, result);
    }

    /**
     * 测试目的：验证takeWhile所有元素都不满足谓词。
     * 测试要求：输入为全大于0集合。
     * 测试功能：无获取。
     * 测试范围：全大于0集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(4)
    void testTakeWhileNoneMatch() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> result = X.list(list).takeWhile(i -> i < 0).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证takeWhile部分元素满足谓词。
     * 测试要求：输入为整数集合，谓词为小于3。
     * 测试功能：部分获取。
     * 测试范围：普通整数集合。
     * 测试结果：获取前面小于3的元素。
     */
    @Test
    @Order(5)
    void testTakeWhilePartialMatch() {
        List<Integer> list = Arrays.asList(1, 2, 3, 2, 1);
        List<Integer> result = X.list(list).takeWhile(i -> i < 3).toList();
        assertEquals(Arrays.asList(1, 2), result);
    }

    /**
     * 测试目的：验证takeWhile含null元素。
     * 测试要求：集合中包含null。
     * 测试功能：含null获取。
     * 测试范围：含null集合。
     * 测试结果：获取前面为null的元素。
     */
    @Test
    @Order(6)
    void testTakeWhileWithNullElements() {
        List<Integer> list = Arrays.asList(null, null, 3, null, 5);
        List<Integer> result = X.list(list).takeWhile(Objects::isNull).toList();
        assertEquals(Arrays.asList(null, null), result);
    }

    /**
     * 测试目的：验证takeWhile对极大集合的获取。
     * 测试要求：输入为极大集合，前size-1个为1，最后一个为2。
     * 测试功能：极值获取。
     * 测试范围：极大集合。
     * 测试结果：获取前size-1个元素。
     */
    @Test
    @Order(7)
    void testTakeWhileWithExtremeValues() {
        int size = 10000;
        List<Integer> list = new ArrayList<>(Collections.nCopies(size, 1));
        list.set(size - 1, 2);
        List<Integer> result = X.list(list).takeWhile(i -> i == 1).toList();
        assertEquals(size - 1, result.size());
    }

    /**
     * 测试目的：验证takeWhile对不同类型元素集合的获取。
     * 测试要求：输入为不同类型元素集合，谓词为null。
     * 测试功能：多类型获取。
     * 测试范围：混合类型集合。
     * 测试结果：获取前面为null的元素。
     */
    @Test
    @Order(8)
    void testTakeWhileWithDifferentTypes() {
        List<Object> list = Arrays.asList(null, null, 2.0, "a", "b");
        List<Object> result = X.list(list).takeWhile(Objects::isNull).toList();
        assertEquals(Arrays.asList(null, null), result);
    }

    /**
     * 测试目的：验证takeWhile对null集合的获取。
     * 测试要求：输入为null。
     * 测试功能：null集合获取。
     * 测试范围：null集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(9)
    void testTakeWhileOnNullList() {
        List<Integer> result = X.list((List<Integer>) null).takeWhile(i -> i < 3).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证takeWhile谓词为null时抛出异常。
     * 测试要求：谓词为null。
     * 测试功能：异常参数。
     * 测试范围：异常参数。
     * 测试结果：抛出NullPointerException。
     */
    @Test
    @Order(10)
    void testTakeWhileNullPredicate() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertThrows(NullPointerException.class, () -> X.list(list).takeWhile(null).toList());
    }
}