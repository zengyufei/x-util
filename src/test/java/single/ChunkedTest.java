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
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChunkedTest {

    /**
     * 测试目的：验证chunkedToList(int)对普通集合的分块。
     * 测试要求：输入为整数集合，分块大小为2。
     * 测试功能：普通分块。
     * 测试范围：普通整数集合。
     * 测试结果：返回正确分块集合。
     */
    @Test
    @Order(1)
    void testChunkedToListNormal() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<List<Integer>> chunks = X.list(list).chunkedToList(2);
        assertEquals(Arrays.asList(
                Arrays.asList(1, 2),
                Arrays.asList(3, 4),
                Arrays.asList(5)
        ), chunks);
    }

    /**
     * 测试目的：验证chunkedToList(int)对空集合的分块。
     * 测试要求：输入为空集合。
     * 测试功能：空集合分块。
     * 测试范围：空集合。
     * 测试结果：返回空分块集合。
     */
    @Test
    @Order(2)
    void testChunkedToListEmpty() {
        List<Integer> list = Collections.emptyList();
        List<List<Integer>> chunks = X.list(list).chunkedToList(3);
        assertTrue(chunks.isEmpty());
    }

    /**
     * 测试目的：验证chunkedToList(int)分块大小大于集合长度。
     * 测试要求：集合长度小于分块大小。
     * 测试功能：大块分组。
     * 测试范围：普通整数集合。
     * 测试结果：返回单一分块。
     */
    @Test
    @Order(3)
    void testChunkedToListChunkSizeGreaterThanList() {
        List<Integer> list = Arrays.asList(1, 2);
        List<List<Integer>> chunks = X.list(list).chunkedToList(5);
        assertEquals(Collections.singletonList(Arrays.asList(1, 2)), chunks);
    }

    /**
     * 测试目的：验证chunkedToList(int)分块大小等于集合长度。
     * 测试要求：集合长度等于分块大小。
     * 测试功能：整块分组。
     * 测试范围：普通整数集合。
     * 测试结果：返回单一分块。
     */
    @Test
    @Order(4)
    void testChunkedToListChunkSizeEqualsList() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<List<Integer>> chunks = X.list(list).chunkedToList(3);
        assertEquals(Collections.singletonList(Arrays.asList(1, 2, 3)), chunks);
    }

    /**
     * 测试目的：验证chunkedToList(int)分块不能整除集合长度。
     * 测试要求：集合长度不能被分块整除。
     * 测试功能：非整除分组。
     * 测试范围：普通整数集合。
     * 测试结果：最后一块元素数小于分块大小。
     */
    @Test
    @Order(5)
    void testChunkedToListNotDivisible() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        List<List<Integer>> chunks = X.list(list).chunkedToList(3);
        assertEquals(Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7)
        ), chunks);
    }

    /**
     * 测试目的：验证chunkedToList(int)只剩一个元素时的分块。
     * 测试要求：集合只剩一个元素。
     * 测试功能：单元素分组。
     * 测试范围：单元素集合。
     * 测试结果：返回单一分块。
     */
    @Test
    @Order(6)
    void testChunkedToListSingleElement() {
        List<Integer> list = Collections.singletonList(42);
        List<List<Integer>> chunks = X.list(list).chunkedToList(2);
        assertEquals(Collections.singletonList(Collections.singletonList(42)), chunks);
    }

    /**
     * 测试目的：验证chunkedToList(int)对含null元素集合的分块。
     * 测试要求：集合中包含null。
     * 测试功能：含null分组。
     * 测试范围：含null集合。
     * 测试结果：分块结果包含null。
     */
    @Test
    @Order(7)
    void testChunkedToListWithNullElements() {
        List<Integer> list = Arrays.asList(1, null, 3, null, 5);
        List<List<Integer>> chunks = X.list(list).chunkedToList(2);
        assertEquals(Arrays.asList(
                Arrays.asList(1, null),
                Arrays.asList(3, null),
                Arrays.asList(5)
        ), chunks);
    }

    /**
     * 测试目的：验证chunkedToList(int)对极大集合的分块。
     * 测试要求：输入为极大集合。
     * 测试功能：极值分组。
     * 测试范围：极大集合。
     * 测试结果：分块数量正确。
     */
    @Test
    @Order(8)
    void testChunkedToListWithExtremeValues() {
        int size = 10000;
        List<Integer> list = new ArrayList<>(Collections.nCopies(size, 1));
        List<List<Integer>> chunks = X.list(list).chunkedToList(1000);
        assertEquals(10, chunks.size());
        assertEquals(1000, chunks.get(0).size());
    }

    /**
     * 测试目的：验证chunkedToList(int)对不同类型元素集合的分块。
     * 测试要求：输入为不同类型元素集合。
     * 测试功能：多类型分组。
     * 测试范围：混合类型集合。
     * 测试结果：分块结果正确。
     */
    @Test
    @Order(9)
    void testChunkedToListWithDifferentTypes() {
        List<Object> list = Arrays.asList(1, "a", 2.0, null);
        List<List<Object>> chunks = X.list(list).chunkedToList(2);
        assertEquals(Arrays.asList(
                Arrays.asList(1, "a"),
                Arrays.asList(2.0, null)
        ), chunks);
    }

    /**
     * 测试目的：验证chunkedToList(int)对null集合的分块。
     * 测试要求：输入为null。
     * 测试功能：null集合分组。
     * 测试范围：null集合。
     * 测试结果：返回空分块集合。
     */
    @Test
    @Order(10)
    void testChunkedToListOnNullList() {
        List<List<Integer>> chunks = X.list((List<Integer>) null).chunkedToList(2);
        assertTrue(chunks.isEmpty());
    }

    /**
     * 测试目的：验证chunkedToList(int)分块大小为0或负数时抛出异常。
     * 测试要求：分块大小为0或负数。
     * 测试功能：异常参数。
     * 测试范围：异常参数。
     * 测试结果：抛出IllegalArgumentException。
     */
    @Test
    @Order(11)
    void testChunkedToListWithInvalidSize() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertThrows(ArithmeticException.class, () -> X.list(list).chunkedToList(0));
        assertThrows(IllegalArgumentException.class, () -> X.list(list).chunkedToList(-1));
    }

    /**
     * 测试目的：验证chunked(int, Consumer)的正常分块回调。
     * 测试要求：输入为整数集合，分块大小为2。
     * 测试功能：分块回调。
     * 测试范围：普通整数集合。
     * 测试结果：回调被正确调用。
     */
    @Test
    @Order(12)
    void testChunkedConsumerNormal() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<List<Integer>> result = new ArrayList<>();
        X.list(list).chunked(2, result::add);
        assertEquals(Arrays.asList(
                Arrays.asList(1, 2),
                Arrays.asList(3, 4),
                Arrays.asList(5)
        ), result);
    }

    /**
     * 测试目的：验证chunked(int, Consumer)对空集合的分块回调。
     * 测试要求：输入为空集合。
     * 测试功能：空集合分块回调。
     * 测试范围：空集合。
     * 测试结果：回调未被调用。
     */
    @Test
    @Order(13)
    void testChunkedConsumerEmpty() {
        List<Integer> list = Collections.emptyList();
        AtomicInteger counter = new AtomicInteger(0);
        X.list(list).chunked(3, chunk -> counter.incrementAndGet());
        assertEquals(0, counter.get());
    }
}