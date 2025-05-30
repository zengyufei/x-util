package single;

import com.zyf.util.X;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ShuffledTest {

    /**
     * 测试目的：验证shuffled()对普通整数集合的随机排列。
     * 测试要求：输入为整数集合。
     * 测试功能：随机排列。
     * 测试范围：普通整数集合。
     * 测试结果：返回的集合包含原有元素，顺序被打乱。
     */
    @Test
    @Order(1)
    void testShuffledNormal() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> shuffled = X.list(list).shuffled().toList();
        assertEquals(new HashSet<>(list), new HashSet<>(shuffled));
        // 允许偶尔顺序未变，但大概率会变
        // assertNotEquals(list, shuffled); // 不强制顺序变
    }

    /**
     * 测试目的：验证shuffled()对空集合的处理。
     * 测试要求：输入为空集合。
     * 测试功能：空集合。
     * 测试范围：空集合。
     * 测试结果：返回空集合。
     */
    @Test
    @Order(2)
    void testShuffledEmptyList() {
        List<Integer> list = Collections.emptyList();
        List<Integer> shuffled = X.list(list).shuffled().toList();
        assertTrue(shuffled.isEmpty());
    }

    /**
     * 测试目的：验证shuffled()对单元素集合的处理。
     * 测试要求：输入为单元素集合。
     * 测试功能：单元素。
     * 测试范围：单元素集合。
     * 测试结果：返回同样的单元素集合。
     */
    @Test
    @Order(3)
    void testShuffledSingleElement() {
        List<String> list = Collections.singletonList("a");
        List<String> shuffled = X.list(list).shuffled().toList();
        assertEquals(list, shuffled);
    }

    /**
     * 测试目的：验证shuffled()对包含null元素集合的处理。
     * 测试要求：输入包含null。
     * 测试功能：包含null。
     * 测试范围：含null集合。
     * 测试结果：返回的集合包含null。
     */
    @Test
    @Order(4)
    void testShuffledWithNullElements() {
        List<String> list = Arrays.asList("a", null, "b");
        List<String> shuffled = X.list(list).shuffled().toList();
        assertEquals(new HashSet<>(list), new HashSet<>(shuffled));
        assertTrue(shuffled.contains(null));
    }

    /**
     * 测试目的：验证shuffled()对不同类型元素集合的处理。
     * 测试要求：输入为不同类型元素集合。
     * 测试功能：多类型。
     * 测试范围：混合类型集合。
     * 测试结果：返回的集合包含所有原有元素。
     */
    @Test
    @Order(5)
    void testShuffledWithDifferentTypes() {
        List<Object> list = Arrays.asList(1, "a", 2.0, null);
        List<Object> shuffled = X.list(list).shuffled().toList();
        assertEquals(new HashSet<>(list), new HashSet<>(shuffled));
    }

    /**
     * 测试目的：验证shuffled()对极大集合的处理。
     * 测试要求：输入为极大集合。
     * 测试功能：极值。
     * 测试范围：极大集合。
     * 测试结果：返回的集合元素个数一致。
     */
    @Test
    @Order(6)
    void testShuffledWithExtremeValues() {
        int size = 10000;
        List<Integer> list = new ArrayList<>(Collections.nCopies(size, 1));
        List<Integer> shuffled = X.list(list).shuffled().toList();
        assertEquals(list.size(), shuffled.size());
    }

    /**
     * 测试目的：验证shuffled()对null集合的处理。
     * 测试要求：输入为null。
     * 测试功能：null集合。
     * 测试范围：null集合。
     * 测试结果：返回空集合或抛出异常。
     */
    @Test
    @Order(7)
    void testShuffledNullList() {
        List<Object> shuffled = X.list(null).shuffled().toList();
        assertTrue(shuffled.isEmpty());
    }
}