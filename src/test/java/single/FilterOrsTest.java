package single;

import com.zyf.util.X;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.*;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilterOrsTest {

    /**
     * 测试目的：验证filterOrs对普通集合的过滤。
     * 测试要求：输入为整数集合，谓词为偶数或大于4。
     * 测试功能：普通过滤。
     * 测试范围：普通整数集合。
     * 测试结果：保留偶数或大于4的元素。
     */
    @Test
    @Order(1)
    void testFilterOrsNormal() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> result = X.list(list).filterOrs(i -> i % 2 == 0, i -> i > 4).toList();
        assertEquals(Arrays.asList(2, 4, 5), result);
    }

    /**
     * 测试目的：验证filterOrs对空集合的过滤。
     * 测试要求：输入为空集合。
     * 测试功能：空集合过滤。
     * 测试范围：空集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(2)
    void testFilterOrsEmpty() {
        List<Integer> list = Collections.emptyList();
        List<Integer> result = X.list(list).filterOrs(i -> i > 0).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证filterOrs所有元素都满足谓词。
     * 测试要求：输入为全正数集合。
     * 测试功能：全满足。
     * 测试范围：全正数集合。
     * 测试结果：全部元素保留。
     */
    @Test
    @Order(3)
    void testFilterOrsAllMatch() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> result = X.list(list).filterOrs(i -> i > 0).toList();
        assertEquals(list, result);
    }

    /**
     * 测试目的：验证filterOrs所有元素都不满足谓词。
     * 测试要求：输入为全负数集合。
     * 测试功能：全不满足。
     * 测试范围：全负数集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(4)
    void testFilterOrsNoneMatch() {
        List<Integer> list = Arrays.asList(-1, -2, -3);
        List<Integer> result = X.list(list).filterOrs(i -> i > 0).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证filterOrs部分元素满足多个谓词。
     * 测试要求：输入为整数集合，谓词为偶数或大于3。
     * 测试功能：部分满足。
     * 测试范围：普通整数集合。
     * 测试结果：保留偶数或大于3的元素。
     */
    @Test
    @Order(5)
    void testFilterOrsPartialMatch() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> result = X.list(list).filterOrs(i -> i % 2 == 0, i -> i > 3).toList();
        assertEquals(Arrays.asList(2, 4, 5), result);
    }

    /**
     * 测试目的：验证filterOrs无谓词时的过滤。
     * 测试要求：无谓词。
     * 测试功能：无谓词过滤。
     * 测试范围：普通整数集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(6)
    void testFilterOrsNoPredicate() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> result = X.list(list).filterOrs().toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证filterOrs对含null元素集合的过滤。
     * 测试要求：集合中包含null。
     * 测试功能：含null过滤。
     * 测试范围：含null集合。
     * 测试结果：保留null元素。
     */
    @Test
    @Order(7)
    void testFilterOrsWithNullElements() {
        List<Integer> list = Arrays.asList(1, null, 3, null, 5);
        List<Integer> result = X.list(list).filterOrs(Objects::isNull).toList();
        assertEquals(Arrays.asList(null, null), result);
    }

    /**
     * 测试目的：验证filterOrs对极大集合的过滤。
     * 测试要求：输入为极大集合，谓词为1。
     * 测试功能：极值过滤。
     * 测试范围：极大集合。
     * 测试结果：全部元素保留。
     */
    @Test
    @Order(8)
    void testFilterOrsWithExtremeValues() {
        int size = 10000;
        List<Integer> list = new ArrayList<>(Collections.nCopies(size, 1));
        List<Integer> result = X.list(list).filterOrs(i -> i == 1).toList();
        assertEquals(size, result.size());
    }

    /**
     * 测试目的：验证filterOrs对不同类型元素集合的过滤。
     * 测试要求：输入为不同类型元素集合，谓词为字符串类型。
     * 测试功能：多类型过滤。
     * 测试范围：混合类型集合。
     * 测试结果：保留字符串。
     */
    @Test
    @Order(9)
    void testFilterOrsWithDifferentTypes() {
        List<Object> list = Arrays.asList(1, "a", 2.0, null);
        List<Object> result = X.list(list).filterOrs(o -> o instanceof String).toList();
        assertEquals(Collections.singletonList("a"), result);
    }

    /**
     * 测试目的：验证filterOrs对null集合的过滤。
     * 测试要求：输入为null。
     * 测试功能：null集合过滤。
     * 测试范围：null集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(10)
    void testFilterOrsOnNullList() {
        List<Integer> result = X.list((List<Integer>) null).filterOrs(i -> i != null).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证filterOrs谓词为null时抛出异常。
     * 测试要求：谓词为null。
     * 测试功能：异常参数。
     * 测试范围：异常参数。
     * 测试结果：抛出NullPointerException。
     */
    @Test
    @Order(11)
    void testFilterOrsNullPredicate() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertThrows(NullPointerException.class, () -> X.list(list).filterOrs((Predicate<Integer>[]) null).toList());
    }
}