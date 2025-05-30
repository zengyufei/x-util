package single;

import com.zyf.util.X;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IndexOfTest {

    /**
     * 测试目的：验证indexOf()对普通整数集合的索引查找。
     * 测试要求：输入为整数集合，查找存在元素。
     * 测试功能：查找元素索引。
     * 测试范围：普通整数集合。
     * 测试结果：返回元素的第一个索引。
     */
    @Test
    @Order(1)
    void testIndexOfNormal() {
        List<Integer> list = Arrays.asList(1, 2, 3, 2, 4);
        int idx = X.list(list).indexOf(2);
        assertEquals(1, idx);
    }

    /**
     * 测试目的：验证indexOf()查找不存在的元素。
     * 测试要求：输入为整数集合，查找不存在元素。
     * 测试功能：查找元素索引。
     * 测试范围：普通整数集合。
     * 测试结果：返回-1。
     */
    @Test
    @Order(2)
    void testIndexOfNotFound() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        int idx = X.list(list).indexOf(99);
        assertEquals(-1, idx);
    }

    /**
     * 测试目的：验证indexOf()对空集合的处理。
     * 测试要求：输入为空集合。
     * 测试功能：空集合查找。
     * 测试范围：空集合。
     * 测试结果：返回-1。
     */
    @Test
    @Order(3)
    void testIndexOfEmptyList() {
        List<Integer> list = Collections.emptyList();
        int idx = X.list(list).indexOf(1);
        assertEquals(-1, idx);
    }

    /**
     * 测试目的：验证indexOf()对null集合的处理。
     * 测试要求：输入为null。
     * 测试功能：null集合查找。
     * 测试范围：null集合。
     * 测试结果：返回-1。
     */
    @Test
    @Order(4)
    void testIndexOfNullList() {
        int idx = X.list(null).indexOf(1);
        assertEquals(-1, idx);
    }

    /**
     * 测试目的：验证indexOf()查找null元素。
     * 测试要求：集合中包含null。
     * 测试功能：查找null元素。
     * 测试范围：含null集合。
     * 测试结果：返回null元素的索引。
     */
    @Test
    @Order(5)
    void testIndexOfNullElement() {
        List<Integer> list = Arrays.asList(1, null, 3);
        int idx = X.list(list).indexOf(null);
        assertEquals(1, idx);
    }

    /**
     * 测试目的：验证indexOf()对对象集合的支持。
     * 测试要求：输入为对象集合，查找对象。
     * 测试功能：对象查找。
     * 测试范围：对象集合。
     * 测试结果：返回对象的索引。
     */
    @Test
    @Order(6)
    void testIndexOfObjects() {
        class Person {
            final String name;

            Person(String name) {
                this.name = name;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Person person = (Person) o;
                return name.equals(person.name);
            }
        }
        List<Person> people = Arrays.asList(new Person("A"), new Person("B"), new Person("C"));
        int idx = X.list(people).indexOf(new Person("B"));
        assertEquals(1, idx);
    }

    /**
     * 测试目的：验证indexOf()对不同类型元素集合的查找。
     * 测试要求：输入为不同类型元素集合。
     * 测试功能：多类型查找。
     * 测试范围：混合类型集合。
     * 测试结果：返回元素的索引。
     */
    @Test
    @Order(7)
    void testIndexOfWithDifferentTypes() {
        List<Object> list = Arrays.asList(1, "a", 2.0, null);
        assertEquals(1, X.list(list).indexOf("a"));
        assertEquals(2, X.list(list).indexOf(2.0));
        assertEquals(3, X.list(list).indexOf(null));
    }

    /**
     * 测试目的：验证indexOf()对极值集合的查找。
     * 测试要求：输入为极大集合。
     * 测试功能：极值查找。
     * 测试范围：极值集合。
     * 测试结果：返回元素的索引。
     */
    @Test
    @Order(8)
    void testIndexOfWithExtremeValues() {
        int size = 10000;
        List<Integer> list = Collections.nCopies(size, 7);
        assertEquals(0, X.list(list).indexOf(7));
        assertEquals(-1, X.list(list).indexOf(8));
    }

    /**
     * 测试目的：验证indexOf()对单元素集合的查找。
     * 测试要求：输入为单元素集合。
     * 测试功能：单元素查找。
     * 测试范围：单元素集合。
     * 测试结果：返回0或-1。
     */
    @Test
    @Order(9)
    void testIndexOfSingleElement() {
        List<Integer> list = Collections.singletonList(42);
        assertEquals(0, X.list(list).indexOf(42));
        assertEquals(-1, X.list(list).indexOf(1));
    }

    /**
     * 测试目的：验证indexOf()对重复元素的查找。
     * 测试要求：集合中有重复元素。
     * 测试功能：查找第一个出现的索引。
     * 测试范围：重复元素集合。
     * 测试结果：返回第一个出现的索引。
     */
    @Test
    @Order(10)
    void testIndexOfDuplicateElements() {
        List<String> list = Arrays.asList("a", "b", "a", "c");
        assertEquals(0, X.list(list).indexOf("a"));
        assertEquals(1, X.list(list).indexOf("b"));
        assertEquals(3, X.list(list).indexOf("c"));
    }
}