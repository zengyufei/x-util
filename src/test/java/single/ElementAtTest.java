package single;

import com.zyf.util.X;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ElementAtTest {

    /**
     * 测试目的：验证elementAt(index)返回指定索引元素。
     * 测试要求：输入为普通整数集合。
     * 测试功能：按索引取值。
     * 测试范围：普通集合。
     * 测试结果：返回对应索引的元素。
     */
    @Test
    @Order(1)
    void testElementAtNormal() {
        List<Integer> list = Arrays.asList(10, 20, 30, 40, 50);
        assertEquals(10, X.list(list).elementAt(0));
        assertEquals(30, X.list(list).elementAt(2));
        assertEquals(50, X.list(list).elementAt(4));
    }

    /**
     * 测试目的：验证elementAt(index)越界抛出异常。
     * 测试要求：索引超出范围。
     * 测试功能：越界异常。
     * 测试范围：普通集合。
     * 测试结果：抛出IndexOutOfBoundsException。
     */
    @Test
    @Order(2)
    void testElementAtOutOfBounds() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertThrows(IndexOutOfBoundsException.class, () -> X.list(list).elementAt(3));
        assertThrows(IndexOutOfBoundsException.class, () -> X.list(list).elementAt(-1));
    }

    /**
     * 测试目的：验证elementAt(index)对空集合抛出异常。
     * 测试要求：输入为空集合。
     * 测试功能：空集合异常。
     * 测试范围：空集合。
     * 测试结果：抛出IndexOutOfBoundsException。
     */
    @Test
    @Order(3)
    void testElementAtEmptyList() {
        List<Integer> list = Collections.emptyList();
        assertThrows(IndexOutOfBoundsException.class, () -> X.list(list).elementAt(0));
    }

    /**
     * 测试目的：验证elementAt(index)对null集合抛出异常。
     * 测试要求：输入为null。
     * 测试功能：null集合异常。
     * 测试范围：null集合。
     * 测试结果：抛出IndexOutOfBoundsException。
     */
    @Test
    @Order(4)
    void testElementAtNullList() {
        assertThrows(IndexOutOfBoundsException.class, () -> X.list(null).elementAt(0));
    }

    /**
     * 测试目的：验证elementAtOrNull(index)正常返回。
     * 测试要求：输入为普通集合。
     * 测试功能：按索引取值。
     * 测试范围：普通集合。
     * 测试结果：返回对应索引的元素。
     */
    @Test
    @Order(5)
    void testElementAtOrNullNormal() {
        List<String> list = Arrays.asList("a", "b", "c");
        assertEquals("a", X.list(list).elementAtOrNull(0));
        assertEquals("c", X.list(list).elementAtOrNull(2));
    }

    /**
     * 测试目的：验证elementAtOrNull(index)越界返回null。
     * 测试要求：索引超出范围。
     * 测试功能：越界返回null。
     * 测试范围：普通集合。
     * 测试结果：返回null。
     */
    @Test
    @Order(6)
    void testElementAtOrNullOutOfBounds() {
        List<String> list = Arrays.asList("x", "y");
        assertNull(X.list(list).elementAtOrNull(2));
        assertNull(X.list(list).elementAtOrNull(-1));
    }

    /**
     * 测试目的：验证elementAtOrNull(index)对空集合返回null。
     * 测试要求：输入为空集合。
     * 测试功能：空集合返回null。
     * 测试范围：空集合。
     * 测试结果：返回null。
     */
    @Test
    @Order(7)
    void testElementAtOrNullEmptyList() {
        List<String> list = Collections.emptyList();
        assertNull(X.list(list).elementAtOrNull(0));
    }

    /**
     * 测试目的：验证elementAtOrNull(index)对null集合返回null。
     * 测试要求：输入为null。
     * 测试功能：null集合返回null。
     * 测试范围：null集合。
     * 测试结果：返回null。
     */
    @Test
    @Order(8)
    void testElementAtOrNullNullList() {
        assertNull(X.list(null).elementAtOrNull(0));
    }

    /**
     * 测试目的：验证elementAtOrElse(index, defaultValue)正常返回。
     * 测试要求：输入为普通集合。
     * 测试功能：按索引取值。
     * 测试范围：普通集合。
     * 测试结果：返回对应索引的元素。
     */
    @Test
    @Order(9)
    void testElementAtOrElseNormal() {
        List<Double> list = Arrays.asList(1.1, 2.2, 3.3);
        assertEquals(2.2, X.list(list).elementAtOrElse(1, 9.9));
    }

    /**
     * 测试目的：验证elementAtOrElse(index, defaultValue)越界返回默认值。
     * 测试要求：索引超出范围。
     * 测试功能：越界返回默认值。
     * 测试范围：普通集合。
     * 测试结果：返回defaultValue。
     */
    @Test
    @Order(10)
    void testElementAtOrElseOutOfBounds() {
        List<Double> list = Arrays.asList(1.1, 2.2);
        assertEquals(8.8, X.list(list).elementAtOrElse(5, 8.8));
        assertEquals(7.7, X.list(list).elementAtOrElse(-1, 7.7));
    }

    /**
     * 测试目的：验证elementAtOrElse(index, defaultValue)对空集合返回默认值。
     * 测试要求：输入为空集合。
     * 测试功能：空集合返回默认值。
     * 测试范围：空集合。
     * 测试结果：返回defaultValue。
     */
    @Test
    @Order(11)
    void testElementAtOrElseEmptyList() {
        List<Double> list = Collections.emptyList();
        assertEquals(6.6, X.list(list).elementAtOrElse(0, 6.6));
    }

    /**
     * 测试目的：验证elementAtOrElse(index, defaultValue)对null集合返回默认值。
     * 测试要求：输入为null。
     * 测试功能：null集合返回默认值。
     * 测试范围：null集合。
     * 测试结果：返回defaultValue。
     */
    @Test
    @Order(12)
    void testElementAtOrElseNullList() {
        assertEquals("default", X.list(null).elementAtOrElse(0, "default"));
    }

    /**
     * 测试目的：验证elementAt系列对对象集合的支持。
     * 测试要求：对象集合。
     * 测试功能：对象索引取值。
     * 测试范围：对象集合。
     * 测试结果：返回对应对象。
     */
    @Test
    @Order(13)
    void testElementAtWithObjects() {
        class Person {
            String name;
            int age;

            Person(String name, int age) {
                this.name = name;
                this.age = age;
            }
        }
        List<Person> people = Arrays.asList(
                new Person("A", 10),
                new Person("B", 20),
                new Person("C", 30)
        );
        assertEquals("B", X.list(people).elementAt(1).name);
        assertNull(X.list(people).elementAtOrNull(5));
        assertEquals(99, X.list(people).elementAtOrElse(9, new Person("X", 99)).age);
    }
}