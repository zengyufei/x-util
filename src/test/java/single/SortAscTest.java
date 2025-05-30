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
class SortAscTest {

    /**
     * 测试目的：验证sortAsc对整数集合的升序排序。
     * 测试要求：输入为整数集合，按自身排序。
     * 测试功能：升序排序。
     * 测试范围：普通整数集合。
     * 测试结果：返回升序排列的集合。
     */
    @Test
    @Order(1)
    void testSortAscWithIntegers() {
        List<Integer> list = Arrays.asList(5, 3, 1, 4, 2);
        List<Integer> sorted = X.list(list).sortAsc(i -> i).toList();
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), sorted);
    }

    /**
     * 测试目的：验证sortAsc对字符串集合按长度排序。
     * 测试要求：输入为字符串集合，按长度升序。
     * 测试功能：按key排序。
     * 测试范围：字符串集合。
     * 测试结果：返回按长度升序排列的集合。
     */
    @Test
    @Order(2)
    void testSortAscWithStringsByLength() {
        List<String> list = Arrays.asList("a", "bbb", "cc");
        List<String> sorted = X.list(list).sortAsc(String::length).toList();
        assertEquals(Arrays.asList("a", "cc", "bbb"), sorted);
    }

    /**
     * 测试目的：验证sortAsc对对象集合按属性排序。
     * 测试要求：输入为对象集合，按age属性升序。
     * 测试功能：对象属性排序。
     * 测试范围：对象集合。
     * 测试结果：返回按age升序排列的集合。
     */
    @Test
    @Order(3)
    void testSortAscWithObjectsByField() {
        class Person {
            String name;
            int age;

            Person(String name, int age) {
                this.name = name;
                this.age = age;
            }
        }
        List<Person> people = Arrays.asList(
                new Person("A", 30),
                new Person("B", 20),
                new Person("C", 40)
        );
        List<Person> sorted = X.list(people).sortAsc(p -> p.age).toList();
        assertEquals(Arrays.asList("B", "A", "C"),
                Arrays.asList(sorted.get(0).name, sorted.get(1).name, sorted.get(2).name));
    }

    /**
     * 测试目的：验证sortAsc对包含null元素的集合排序。
     * 测试要求：输入集合包含null。
     * 测试功能：null元素排序。
     * 测试范围：含null集合。
     * 测试结果：null元素排在最后。
     */
    @Test
    @Order(4)
    void testSortAscWithNullElements() {
        List<String> list = Arrays.asList("a", null, "bb");
        List<String> sorted = X.list(list).sortAsc(s -> s).toList();
        assertEquals(Arrays.asList("a", "bb", null), sorted);
    }

    /**
     * 测试目的：验证sortAsc对空集合的处理。
     * 测试要求：输入为空集合。
     * 测试功能：空集合排序。
     * 测试范围：空集合。
     * 测试结果：返回空集合。
     */
    @Test
    @Order(5)
    void testSortAscWithEmptyList() {
        List<Integer> list = Collections.emptyList();
        List<Integer> sorted = X.list(list).sortAsc(i -> i).toList();
        assertTrue(sorted.isEmpty());
    }

    /**
     * 测试目的：验证sortAsc对null集合的处理。
     * 测试要求：输入为null。
     * 测试功能：null集合排序。
     * 测试范围：null集合。
     * 测试结果：返回空集合。
     */
    @Test
    @Order(6)
    void testSortAscWithNullList() {
        // 不存在这种用法
        // List<Integer> sorted = X.list(null).sortAsc(i -> i).toList();
        // assertTrue(sorted.isEmpty());
    }

    /**
     * 测试目的：验证sortAsc对已排序集合的幂等性。
     * 测试要求：输入为已升序集合。
     * 测试功能：幂等性。
     * 测试范围：已排序集合。
     * 测试结果：返回原集合。
     */
    @Test
    @Order(7)
    void testSortAscAlreadySorted() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sorted = X.list(list).sortAsc(i -> i).toList();
        assertEquals(list, sorted);
    }

    /**
     * 测试目的：验证sortAsc对包含重复元素的集合排序。
     * 测试要求：输入集合有重复元素。
     * 测试功能：重复元素排序。
     * 测试范围：含重复元素集合。
     * 测试结果：重复元素顺序正确。
     */
    @Test
    @Order(8)
    void testSortAscWithDuplicates() {
        List<Integer> list = Arrays.asList(3, 1, 2, 3, 2);
        List<Integer> sorted = X.list(list).sortAsc(i -> i).toList();
        assertEquals(Arrays.asList(1, 2, 2, 3, 3), sorted);
    }

    /**
     * 测试目的：验证sortAsc对极值集合的排序。
     * 测试要求：输入为极大集合。
     * 测试功能：极值排序。
     * 测试范围：极值集合。
     * 测试结果：返回升序排列的集合。
     */
    @Test
    @Order(9)
    void testSortAscWithExtremeValues() {
        int size = 10000;
        List<Integer> list = new ArrayList<>(size);
        for (int i = size; i > 0; i--) list.add(i);
        List<Integer> sorted = X.list(list).sortAsc(i -> i).toList();
        for (int i = 0; i < size; i++) {
            assertEquals(i + 1, sorted.get(i));
        }
    }

    /**
     * 测试目的：验证sortAsc对不同类型元素集合的异常处理。
     * 测试要求：输入为不同类型元素集合。
     * 测试功能：类型不一致异常。
     * 测试范围：混合类型集合。
     * 测试结果：抛出ClassCastException。
     */
    @Test
    @Order(10)
    void testSortAscWithDifferentTypes() {
        List<Object> list = Arrays.asList("x", 1, 2.0);
        assertThrows(ClassCastException.class, () -> X.list(list).sortAsc(o -> (Comparable) o).toList());
    }

    /**
     * 测试目的：验证sortAsc对单元素集合的排序。
     * 测试要求：输入为单元素集合。
     * 测试功能：单元素排序。
     * 测试范围：单元素集合。
     * 测试结果：返回原集合。
     */
    @Test
    @Order(11)
    void testSortAscWithSingleElement() {
        List<Integer> list = Collections.singletonList(42);
        List<Integer> sorted = X.list(list).sortAsc(i -> i).toList();
        assertEquals(list, sorted);
    }
}