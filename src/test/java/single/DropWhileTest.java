package single;

import com.zyf.util.X;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DropWhileTest {

    /**
     * 测试目的：验证dropWhile对普通集合的丢弃。
     * 测试要求：输入为整数集合，谓词为小于3。
     * 测试功能：普通丢弃。
     * 测试范围：普通整数集合。
     * 测试结果：丢弃前面小于3的元素。
     */
    @Test
    @Order(1)
    void testDropWhileNormal() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 1);
        List<Integer> result = X.list(list).dropWhile(i -> i < 3).toList();
        assertEquals(Arrays.asList(3, 4, 1), result);
    }

    /**
     * 测试目的：验证dropWhile对空集合的丢弃。
     * 测试要求：输入为空集合。
     * 测试功能：空集合丢弃。
     * 测试范围：空集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(2)
    void testDropWhileEmpty() {
        List<Integer> list = Collections.emptyList();
        List<Integer> result = X.list(list).dropWhile(i -> i < 3).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证dropWhile所有元素都满足谓词。
     * 测试要求：输入为全小于10集合。
     * 测试功能：全丢弃。
     * 测试范围：全小于10集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(3)
    void testDropWhileAllMatch() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> result = X.list(list).dropWhile(i -> i < 10).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证dropWhile所有元素都不满足谓词。
     * 测试要求：输入为全大于0集合。
     * 测试功能：无丢弃。
     * 测试范围：全大于0集合。
     * 测试结果：全部元素保留。
     */
    @Test
    @Order(4)
    void testDropWhileNoneMatch() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> result = X.list(list).dropWhile(i -> i < 0).toList();
        assertEquals(list, result);
    }

    /**
     * 测试目的：验证dropWhile部分元素满足谓词。
     * 测试要求：输入为整数集合，谓词为小于3。
     * 测试功能：部分丢弃。
     * 测试范围：普通整数集合。
     * 测试结果：丢弃前面小于3的元素。
     */
    @Test
    @Order(5)
    void testDropWhilePartialMatch() {
        List<Integer> list = Arrays.asList(1, 2, 3, 2, 1);
        List<Integer> result = X.list(list).dropWhile(i -> i < 3).toList();
        assertEquals(Arrays.asList(3, 2, 1), result);
    }

    /**
     * 测试目的：验证dropWhile含null元素。
     * 测试要求：集合中包含null。
     * 测试功能：含null丢弃。
     * 测试范围：含null集合。
     * 测试结果：丢弃前面为null的元素。
     */
    @Test
    @Order(6)
    void testDropWhileWithNullElements() {
        List<Integer> list = Arrays.asList(null, null, 3, null, 5);
        List<Integer> result = X.list(list).dropWhile(Objects::isNull).toList();
        assertEquals(Arrays.asList(3, null, 5), result);
    }

    /**
     * 测试目的：验证dropWhile对极大集合的丢弃。
     * 测试要求：输入为极大集合，前size-1个为1，最后一个为2。
     * 测试功能：极值丢弃。
     * 测试范围：极大集合。
     * 测试结果：只剩最后一个元素。
     */
    @Test
    @Order(7)
    void testDropWhileWithExtremeValues() {
        int size = 10000;
        List<Integer> list = new ArrayList<>(Collections.nCopies(size, 1));
        list.set(size - 1, 2);
        List<Integer> result = X.list(list).dropWhile(i -> i == 1).toList();
        assertEquals(Collections.singletonList(2), result);
    }

    /**
     * 测试目的：验证dropWhile对不同类型元素集合的丢弃。
     * 测试要求：输入为不同类型元素集合，谓词为null。
     * 测试功能：多类型丢弃。
     * 测试范围：混合类型集合。
     * 测试结果：全部元素保留。
     */
    @Test
    @Order(8)
    void testDropWhileWithDifferentTypes() {
        List<Object> list = Arrays.asList(1, "a", 2.0, null, "b");
        List<Object> result = X.list(list).dropWhile(Objects::isNull).toList();
        assertEquals(list, result);
    }

    /**
     * 测试目的：验证dropWhile对null集合的丢弃。
     * 测试要求：输入为null。
     * 测试功能：null集合丢弃。
     * 测试范围：null集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(9)
    void testDropWhileOnNullList() {
        List<Integer> result = X.list((List<Integer>) null).dropWhile(i -> i < 3).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证dropWhile谓词为null时抛出异常。
     * 测试要求：谓词为null。
     * 测试功能：异常参数。
     * 测试范围：异常参数。
     * 测试结果：抛出NullPointerException。
     */
    @Test
    @Order(10)
    void testDropWhileNullPredicate() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertThrows(NullPointerException.class, () -> X.list(list).dropWhile(null).toList());
    }
}