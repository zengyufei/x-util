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
class WindowedTest {

    /**
     * 测试目的：验证windowed(int)对普通集合的滑动窗口。
     * 测试要求：输入为整数集合，窗口大小为3。
     * 测试功能：普通滑动窗口。
     * 测试范围：普通整数集合。
     * 测试结果：返回正确窗口集合。
     */
    @Test
    @Order(1)
    void testWindowedNormal() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<List<Integer>> windows = X.list(list).windowed(3).toList();
        assertEquals(Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(2, 3, 4),
                Arrays.asList(3, 4, 5)
        ), windows);
    }

    /**
     * 测试目的：验证windowed(int)对空集合的滑动窗口。
     * 测试要求：输入为空集合。
     * 测试功能：空集合滑动窗口。
     * 测试范围：空集合。
     * 测试结果：返回空窗口集合。
     */
    @Test
    @Order(2)
    void testWindowedEmpty() {
        List<Integer> list = Collections.emptyList();
        List<List<Integer>> windows = X.list(list).windowed(2).toList();
        assertTrue(windows.isEmpty());
    }

    /**
     * 测试目的：验证windowed(int)窗口大于集合长度。
     * 测试要求：集合长度小于窗口大小。
     * 测试功能：大窗口。
     * 测试范围：普通整数集合。
     * 测试结果：返回空集合。
     */
    @Test
    @Order(3)
    void testWindowedWindowGreaterThanList() {
        List<Integer> list = Arrays.asList(1, 2);
        List<List<Integer>> windows = X.list(list).windowed(5).toList();
        assertTrue(windows.isEmpty());
    }

    /**
     * 测试目的：验证windowed(int)窗口等于集合长度。
     * 测试要求：集合长度等于窗口大小。
     * 测试功能：整块窗口。
     * 测试范围：普通整数集合。
     * 测试结果：返回单一窗口。
     */
    @Test
    @Order(4)
    void testWindowedWindowEqualsList() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<List<Integer>> windows = X.list(list).windowed(3).toList();
        assertEquals(Collections.singletonList(Arrays.asList(1, 2, 3)), windows);
    }

    /**
     * 测试目的：验证windowed(int)窗口不能整除集合长度。
     * 测试要求：集合长度不能被窗口整除。
     * 测试功能：非整除窗口。
     * 测试范围：普通整数集合。
     * 测试结果：最后一块元素数小于窗口大小。
     */
    @Test
    @Order(5)
    void testWindowedNotDivisible() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        List<List<Integer>> windows = X.list(list).windowed(4).toList();
        assertEquals(Arrays.asList(
                Arrays.asList(1, 2, 3, 4),
                Arrays.asList(2, 3, 4, 5),
                Arrays.asList(3, 4, 5, 6),
                Arrays.asList(4, 5, 6, 7)
        ), windows);
    }

    /**
     * 测试目的：验证windowed(int)只剩一个元素时的窗口。
     * 测试要求：集合只剩一个元素。
     * 测试功能：单元素窗口。
     * 测试范围：单元素集合。
     * 测试结果：返回空集合。
     */
    @Test
    @Order(6)
    void testWindowedSingleElement() {
        List<Integer> list = Collections.singletonList(42);
        List<List<Integer>> windows = X.list(list).windowed(2).toList();
        assertTrue(windows.isEmpty());
    }

    /**
     * 测试目的：验证windowed(int)对含null元素集合的窗口。
     * 测试要求：集合中包含null。
     * 测试功能：含null窗口。
     * 测试范围：含null集合。
     * 测试结果：窗口结果包含null。
     */
    @Test
    @Order(7)
    void testWindowedWithNullElements() {
        List<Integer> list = Arrays.asList(1, null, 3, null, 5);
        List<List<Integer>> windows = X.list(list).windowed(2).toList();
        assertEquals(Arrays.asList(
                Arrays.asList(1, null),
                Arrays.asList(null, 3),
                Arrays.asList(3, null),
                Arrays.asList(null, 5)
        ), windows);
    }

    /**
     * 测试目的：验证windowed(int)对极大集合的窗口。
     * 测试要求：输入为极大集合。
     * 测试功能：极值窗口。
     * 测试范围：极大集合。
     * 测试结果：窗口数量正确。
     */
    @Test
    @Order(8)
    void testWindowedWithExtremeValues() {
        int size = 10000;
        List<Integer> list = new ArrayList<>(Collections.nCopies(size, 1));
        List<List<Integer>> windows = X.list(list).windowed(1000).toList();
        assertEquals(9001, windows.size());
        assertEquals(1000, windows.get(0).size());
    }

    /**
     * 测试目的：验证windowed(int)对不同类型元素集合的窗口。
     * 测试要求：输入为不同类型元素集合。
     * 测试功能：多类型窗口。
     * 测试范围：混合类型集合。
     * 测试结果：窗口结果正确。
     */
    @Test
    @Order(9)
    void testWindowedWithDifferentTypes() {
        List<Object> list = Arrays.asList(1, "a", 2.0, null);
        List<List<Object>> windows = X.list(list).windowed(2).toList();
        assertEquals(Arrays.asList(
                Arrays.asList(1, "a"),
                Arrays.asList("a", 2.0),
                Arrays.asList(2.0, null)
        ), windows);
    }

    /**
     * 测试目的：验证windowed(int)对null集合的窗口。
     * 测试要求：输入为null。
     * 测试功能：null集合窗口。
     * 测试范围：null集合。
     * 测试结果：返回空窗口集合。
     */
    @Test
    @Order(10)
    void testWindowedOnNullList() {
        List<List<Integer>> windows = X.list((List<Integer>) null).windowed(2).toList();
        assertTrue(windows.isEmpty());
    }

    /**
     * 测试目的：验证windowed(int)窗口大小为0或负数时抛出异常。
     * 测试要求：窗口大小为0或负数。
     * 测试功能：异常参数。
     * 测试范围：异常参数。
     * 测试结果：抛出IllegalArgumentException。
     */
    @Test
    @Order(11)
    void testWindowedWithInvalidSize() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertThrows(IllegalArgumentException.class, () -> X.list(list).windowed(0).toList());
        assertThrows(IllegalArgumentException.class, () -> X.list(list).windowed(-1).toList());
    }

    /**
     * 测试目的：验证windowed(int, int)步长大于1的滑动窗口。
     * 测试要求：输入为整数集合，窗口大小为2，步长为2。
     * 测试功能：步长窗口。
     * 测试范围：普通整数集合。
     * 测试结果：窗口结果正确。
     */
    @Test
    @Order(12)
    void testWindowedWithStep() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<List<Integer>> windows = X.list(list).windowed(2, 2).toList();
        assertEquals(Arrays.asList(
                Arrays.asList(1, 2),
                Arrays.asList(3, 4)
        ), windows);
    }

    /**
     * 测试目的：验证windowed(int, int, boolean) partialWindows=true 时的行为。
     * 测试要求：输入为整数集合，窗口大小为3，步长为2，partialWindows=true。
     * 测试功能：partialWindows。
     * 测试范围：普通整数集合。
     * 测试结果：窗口结果包含最后不足窗口大小的部分窗口。
     */
    @Test
    @Order(13)
    void testWindowedPartialWindowsTrue() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<List<Integer>> windows = X.list(list).windowed(3, 2, true).toList();
        assertEquals(Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(3, 4, 5),
                Arrays.asList(5)
        ), windows);
    }

    /**
     * 测试目的：验证windowed(int, int, boolean) partialWindows=false 时的行为。
     * 测试要求：输入为整数集合，窗口大小为3，步长为2，partialWindows=false。
     * 测试功能：partialWindows。
     * 测试范围：普通整数集合。
     * 测试结果：窗口结果不包含最后不足窗口大小的部分窗口。
     */
    @Test
    @Order(14)
    void testWindowedPartialWindowsFalse() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<List<Integer>> windows = X.list(list).windowed(3, 2, false).toList();
        assertEquals(Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(3, 4, 5)
        ), windows);
    }
}