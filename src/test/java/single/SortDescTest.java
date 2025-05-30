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
class SortDescTest {

    /**
     * 测试目的：验证sortDesc对整数集合的降序排序。
     */
    @Test
    @Order(1)
    void testSortDescWithIntegers() {
        List<Integer> list = Arrays.asList(5, 3, 1, 4, 2);
        List<Integer> sorted = X.list(list).sortDesc(i -> i).toList();
        assertEquals(Arrays.asList(5, 4, 3, 2, 1), sorted);
    }

    /**
     * 测试目的：验证sortDesc对字符串集合按长度降序排序。
     */
    @Test
    @Order(2)
    void testSortDescWithStringsByLength() {
        List<String> list = Arrays.asList("a", "bbb", "cc");
        List<String> sorted = X.list(list).sortDesc(String::length).toList();
        assertEquals(Arrays.asList("bbb", "cc", "a"), sorted);
    }

    /**
     * 测试目的：验证sortDesc对对象集合按属性降序排序。
     */
    @Test
    @Order(3)
    void testSortDescWithObjectsByField() {
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
        List<Person> sorted = X.list(people).sortDesc(p -> p.age).toList();
        assertEquals(Arrays.asList("C", "A", "B"),
                Arrays.asList(sorted.get(0).name, sorted.get(1).name, sorted.get(2).name));
    }

    /**
     * 测试目的：验证sortDesc对包含null元素的集合排序（null应排在最后）。
     */
    @Test
    @Order(4)
    void testSortDescWithNullElements() {
        List<String> list = Arrays.asList("a", null, "bb");
        List<String> sorted = X.list(list).sortDesc(s -> s).toList();
        assertEquals(Arrays.asList("bb", "a", null), sorted);
    }

    /**
     * 测试目的：验证sortDesc对空集合的处理。
     */
    @Test
    @Order(5)
    void testSortDescWithEmptyList() {
        List<Integer> list = Collections.emptyList();
        List<Integer> sorted = X.list(list).sortDesc(i -> i).toList();
        assertTrue(sorted.isEmpty());
    }

    /**
     * 测试目的：验证sortDesc对null集合的处理。
     */
    @Test
    @Order(6)
    void testSortDescWithNullList() {
        // 不存在这种用法
        // List<Integer> sorted = X.list(null).sortDesc(i -> i).toList();
        // assertTrue(sorted.isEmpty());
    }

    /**
     * 测试目的：验证sortDesc对已降序集合的幂等性。
     */
    @Test
    @Order(7)
    void testSortDescAlreadySorted() {
        List<Integer> list = Arrays.asList(5, 4, 3, 2, 1);
        List<Integer> sorted = X.list(list).sortDesc(i -> i).toList();
        assertEquals(list, sorted);
    }

    /**
     * 测试目的：验证sortDesc对包含重复元素的集合排序。
     */
    @Test
    @Order(8)
    void testSortDescWithDuplicates() {
        List<Integer> list = Arrays.asList(3, 1, 2, 3, 2);
        List<Integer> sorted = X.list(list).sortDesc(i -> i).toList();
        assertEquals(Arrays.asList(3, 3, 2, 2, 1), sorted);
    }

    /**
     * 测试目的：验证sortDesc对极值集合的排序。
     */
    @Test
    @Order(9)
    void testSortDescWithExtremeValues() {
        int size = 10000;
        List<Integer> list = new ArrayList<>(size);
        for (int i = 1; i <= size; i++) list.add(i);
        List<Integer> sorted = X.list(list).sortDesc(i -> i).toList();
        for (int i = 0; i < size; i++) {
            assertEquals(size - i, sorted.get(i));
        }
    }

    /**
     * 测试目的：验证sortDesc对不同类型元素集合的异常处理。
     */
    @Test
    @Order(10)
    void testSortDescWithDifferentTypes() {
        List<Object> list = Arrays.asList("x", 1, 2.0);
        assertThrows(ClassCastException.class, () -> X.list(list).sortDesc(o -> (Comparable) o).toList());
    }

    /**
     * 测试目的：验证sortDesc对单元素集合的排序。
     */
    @Test
    @Order(11)
    void testSortDescWithSingleElement() {
        List<Integer> list = Collections.singletonList(42);
        List<Integer> sorted = X.list(list).sortDesc(i -> i).toList();
        assertEquals(list, sorted);
    }

    /**
     * 测试目的：验证sortDesc对null key的排序（keyExtractor返回null）。
     */
    @Test
    @Order(12)
    void testSortDescWithNullKey() {
        List<String> list = Arrays.asList("a", null, "bb");
        List<String> sorted = X.list(list).sortDesc(s -> s == null ? null : s).toList();
        assertEquals(Arrays.asList("bb", "a", null), sorted);
    }

    /**
     * 测试目的：验证sortDesc多重排序（降序+次级降序）。
     */
    @Test
    @Order(13)
    void testSortDescWithMultipleFields() {
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
                new Person("B", 30),
                new Person("C", 40),
                new Person("D", 20)
        );
        List<Person> sorted = X.list(people).sort(
                p -> p.createComparator(e -> e.age, com.zyf.util.Sort.Desc),
                p -> p.createComparator(e -> e.name, com.zyf.util.Sort.Desc)
        ).toList();
        assertEquals(Arrays.asList("C", "B", "A", "D"),
                Arrays.asList(sorted.get(0).name, sorted.get(1).name, sorted.get(2).name, sorted.get(3).name));
    }
}