package single;

import com.zyf.util.X;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SliceTest {

    /**
     * 测试目的：验证slice(Collection<Integer>)对普通集合的切片。
     * 测试要求：输入为整数集合，索引为[1,3]。
     * 测试功能：普通切片。
     * 测试范围：普通整数集合。
     * 测试结果：返回索引1和3的元素。
     */
    @Test
    @Order(1)
    void testSliceNormal() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> result = X.list(list).slice(Arrays.asList(1, 3)).toList();
        assertEquals(Arrays.asList(2, 4), result);
    }

    /**
     * 测试目的：验证slice(Collection<Integer>)对空集合的切片。
     * 测试要求：输入为空集合。
     * 测试功能：空集合切片。
     * 测试范围：空集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(2)
    void testSliceEmpty() {
        List<Integer> list = Collections.emptyList();
        List<Integer> result = X.list(list).slice(Arrays.asList(1, 3)).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证slice(Collection<Integer>)索引越界。
     * 测试要求：索引超出集合长度。
     * 测试功能：索引越界。
     * 测试范围：普通整数集合。
     * 测试结果：只返回有效索引元素。
     */
    @Test
    @Order(3)
    void testSliceIndexOutOfBounds() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> result = X.list(list).slice(Arrays.asList(1, 5)).toList();
        assertEquals(Collections.singletonList(2), result);
    }

    /**
     * 测试目的：验证slice(Collection<Integer>)重复索引。
     * 测试要求：索引有重复。
     * 测试功能：重复索引。
     * 测试范围：普通整数集合。
     * 测试结果：返回重复元素。
     */
    @Test
    @Order(4)
    void testSliceDuplicateIndices() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> result = X.list(list).slice(Arrays.asList(1, 1, 2)).toList();
        assertEquals(Arrays.asList(2, 2, 3), result);
    }

    /**
     * 测试目的：验证slice(Collection<Integer>)乱序索引。
     * 测试要求：索引乱序。
     * 测试功能：乱序索引。
     * 测试范围：普通整数集合。
     * 测试结果：返回乱序元素。
     */
    @Test
    @Order(5)
    void testSliceUnorderedIndices() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        List<Integer> result = X.list(list).slice(Arrays.asList(3, 0, 2)).toList();
        assertEquals(Arrays.asList(4, 1, 3), result);
    }

    /**
     * 测试目的：验证slice(Collection<Integer>)负数索引。
     * 测试要求：索引为负数。
     * 测试功能：负数索引。
     * 测试范围：普通整数集合。
     * 测试结果：忽略负数索引。
     */
    @Test
    @Order(6)
    void testSliceNegativeIndices() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> result = X.list(list).slice(Arrays.asList(-1, 1)).toList();
        assertEquals(Collections.singletonList(2), result);
    }

    /**
     * 测试目的：验证slice(Collection<Integer>)含null元素。
     * 测试要求：集合中包含null。
     * 测试功能：含null切片。
     * 测试范围：含null集合。
     * 测试结果：返回指定索引元素。
     */
    @Test
    @Order(7)
    void testSliceWithNullElements() {
        List<Integer> list = Arrays.asList(null, 2, null, 4, 5);
        List<Integer> result = X.list(list).slice(Arrays.asList(0, 2, 4)).toList();
        assertEquals(Arrays.asList(null, null, 5), result);
    }

    /**
     * 测试目的：验证slice(Collection<Integer>)对极大集合的切片。
     * 测试要求：输入为极大集合，索引为0和size-1。
     * 测试功能：极值切片。
     * 测试范围：极大集合。
     * 测试结果：返回首尾元素。
     */
    @Test
    @Order(8)
    void testSliceWithExtremeValues() {
        int size = 10000;
        List<Integer> list = new ArrayList<>(Collections.nCopies(size, 1));
        list.set(0, 2);
        list.set(size - 1, 3);
        List<Integer> result = X.list(list).slice(Arrays.asList(0, size - 1)).toList();
        assertEquals(Arrays.asList(2, 3), result);
    }

    /**
     * 测试目的：验证slice(Collection<Integer>)对不同类型元素集合的切片。
     * 测试要求：输入为不同类型元素集合，索引为1和3。
     * 测试功能：多类型切片。
     * 测试范围：混合类型集合。
     * 测试结果：返回指定索引元素。
     */
    @Test
    @Order(9)
    void testSliceWithDifferentTypes() {
        List<Object> list = Arrays.asList(1, "a", 2.0, null, "b");
        List<Object> result = X.list(list).slice(Arrays.asList(1, 3)).toList();
        assertEquals(Arrays.asList("a", null), result);
    }

    /**
     * 测试目的：验证slice(Collection<Integer>)对null集合的切片。
     * 测试要求：输入为null。
     * 测试功能：null集合切片。
     * 测试范围：null集合。
     * 测试结果：结果为空。
     */
    @Test
    @Order(10)
    void testSliceOnNullList() {
        List<Integer> result = X.list((List<Integer>) null).slice(Arrays.asList(1, 2)).toList();
        assertTrue(result.isEmpty());
    }

    /**
     * 测试目的：验证slice(Collection<Integer>)参数为null时抛出异常。
     * 测试要求：参数为null。
     * 测试功能：异常参数。
     * 测试范围：异常参数。
     * 测试结果：抛出NullPointerException。
     */
    @Test
    @Order(11)
    void testSliceNullIndices() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertThrows(NullPointerException.class, () -> X.list(list).slice((Collection<Integer>) null).toList());
    }
}