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
class FiltersTest {

    /**
     * 测试目的：验证filters对普通集合的过滤。
     * 测试要求：输入为整数集合，谓词为偶数和大于2。
     * 测试功能：普通过滤。
     * 测试范围：普通整数集合。
     * 测试结果：只保留偶数且大于2的元素。
     */
    @Test
    @Order(1)
    void testFiltersNormal() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> result = X.list(list).filters(i -> i % 2 == 0, i -> i > 2).toList();
        assertEquals(Collections.singletonList(4), result);
    }

    /**
     * 测试目的：验证filters对空集合的过滤。
     * 测试要求：输入为空集合。
     * 测试功能：空集合过滤。
     * 测试范围：空集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(2)
    void testFiltersEmpty() {
        List<Integer> list = Collections.emptyList();
        List<Integer> result = X.list(list).filters(i -> i > 0).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证filters所有元素都满足谓词。
     * 测试要求：输入为全正数集合。
     * 测试功能：全满足。
     * 测试范围：全正数集合。
     * 测试结果：全部元素保留。
     */
    @Test
    @Order(3)
    void testFiltersAllMatch() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> result = X.list(list).filters(i -> i > 0).toList();
        assertEquals(list, result);
    }

    /**
     * 测试目的：验证filters所有元素都不满足谓词。
     * 测试要求：输入为全负数集合。
     * 测试功能：全不满足。
     * 测试范围：全负数集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(4)
    void testFiltersNoneMatch() {
        List<Integer> list = Arrays.asList(-1, -2, -3);
        List<Integer> result = X.list(list).filters(i -> i > 0).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证filters部分元素满足多个谓词。
     * 测试要求：输入为整数集合，谓词为偶数和大于3。
     * 测试功能：部分满足。
     * 测试范围：普通整数集合。
     * 测试结果：只保留偶数且大于3的元素。
     */
    @Test
    @Order(5)
    void testFiltersPartialMatch() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> result = X.list(list).filters(i -> i % 2 == 0, i -> i > 3).toList();
        assertEquals(Collections.singletonList(4), result);
    }

    /**
     * 测试目的：验证filters无谓词时的过滤。
     * 测试要求：无谓词。
     * 测试功能：无谓词过滤。
     * 测试范围：普通整数集合。
     * 测试结果：全部元素保留。
     */
    @Test
    @Order(6)
    void testFiltersNoPredicate() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> result = X.list(list).filters().toList();
        assertEquals(list, result);
    }

    /**
     * 测试目的：验证filters对含null元素集合的过滤。
     * 测试要求：集合中包含null。
     * 测试功能：含null过滤。
     * 测试范围：含null集合。
     * 测试结果：只保留null元素。
     */
    @Test
    @Order(7)
    void testFiltersWithNullElements() {
        List<Integer> list = Arrays.asList(1, null, 3, null, 5);
        List<Integer> result = X.list(list).filters(Objects::isNull).toList();
        assertEquals(Arrays.asList((Integer) null, null), result);
    }

    /**
     * 测试目的：验证filters对极大集合的过滤。
     * 测试要求：输入为极大集合，谓词为1。
     * 测试功能：极值过滤。
     * 测试范围：极大集合。
     * 测试结果：全部元素保留。
     */
    @Test
    @Order(8)
    void testFiltersWithExtremeValues() {
        int size = 10000;
        List<Integer> list = new ArrayList<>(Collections.nCopies(size, 1));
        List<Integer> result = X.list(list).filters(i -> i == 1).toList();
        assertEquals(size, result.size());
    }

    /**
     * 测试目的：验证filters对不同类型元素集合的过滤。
     * 测试要求：输入为不同类型元素集合，谓词为字符串类型。
     * 测试功能：多类型过滤。
     * 测试范围：混合类型集合。
     * 测试结果：只保留字符串。
     */
    @Test
    @Order(9)
    void testFiltersWithDifferentTypes() {
        List<Object> list = Arrays.asList(1, "a", 2.0, null);
        List<Object> result = X.list(list).filters(o -> o instanceof String).toList();
        assertEquals(Collections.singletonList("a"), result);
    }

    /**
     * 测试目的：验证filters对null集合的过滤。
     * 测试要求：输入为null。
     * 测试功能：null集合过滤。
     * 测试范围：null集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(10)
    void testFiltersOnNullList() {
        List<Integer> result = X.list((List<Integer>) null).filters(i -> i != null).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证filters谓词为null时抛出异常。
     * 测试要求：谓词为null。
     * 测试功能：异常参数。
     * 测试范围：异常参数。
     * 测试结果：抛出NullPointerException。
     */
    @Test
    @Order(11)
    void testFiltersNullPredicate() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertThrows(NullPointerException.class, () -> X.list(list).filters((Predicate<Integer>[]) null).toList());
    }
}