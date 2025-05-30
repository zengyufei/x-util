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
import static org.junit.jupiter.api.Assertions.assertNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ElementAtOrElseTest {

    /**
     * 测试目的：验证elementAtOrElse(index, defaultValue)对普通集合的取值。
     * 测试要求：输入为普通集合。
     * 测试功能：按索引取值。
     * 测试范围：普通集合。
     * 测试结果：返回对应索引的元素。
     */
    @Test
    @Order(1)
    void testElementAtOrElseNormal() {
        List<String> list = Arrays.asList("a", "b", "c");
        assertEquals("a", X.list(list).elementAtOrElse(0, "x"));
        assertEquals("b", X.list(list).elementAtOrElse(1, "x"));
        assertEquals("c", X.list(list).elementAtOrElse(2, "x"));
    }

    /**
     * 测试目的：验证elementAtOrElse(index, defaultValue)越界返回默认值。
     * 测试要求：索引超出范围。
     * 测试功能：越界返回默认值。
     * 测试范围：普通集合。
     * 测试结果：返回defaultValue。
     */
    @Test
    @Order(2)
    void testElementAtOrElseOutOfBounds() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertEquals(99, X.list(list).elementAtOrElse(3, 99));
        assertEquals(88, X.list(list).elementAtOrElse(-1, 88));
        assertEquals(77, X.list(list).elementAtOrElse(100, 77));
    }

    /**
     * 测试目的：验证elementAtOrElse(index, defaultValue)对空集合返回默认值。
     * 测试要求：输入为空集合。
     * 测试功能：空集合返回默认值。
     * 测试范围：空集合。
     * 测试结果：返回defaultValue。
     */
    @Test
    @Order(3)
    void testElementAtOrElseEmptyList() {
        List<Double> list = Collections.emptyList();
        assertEquals(6.6, X.list(list).elementAtOrElse(0, 6.6));
        assertEquals(7.7, X.list(list).elementAtOrElse(1, 7.7));
    }

    /**
     * 测试目的：验证elementAtOrElse(index, defaultValue)对null集合返回默认值。
     * 测试要求：输入为null。
     * 测试功能：null集合返回默认值。
     * 测试范围：null集合。
     * 测试结果：返回defaultValue。
     */
    @Test
    @Order(4)
    void testElementAtOrElseNullList() {
        assertEquals("default", X.list(null).elementAtOrElse(0, "default"));
        assertEquals(123, X.list(null).elementAtOrElse(10, 123));
    }

    /**
     * 测试目的：验证elementAtOrElse(index, defaultValue)对对象集合的支持。
     * 测试要求：对象集合。
     * 测试功能：对象索引取值。
     * 测试范围：对象集合。
     * 测试结果：返回对应对象或默认对象。
     */
    @Test
    @Order(5)
    void testElementAtOrElseWithObjects() {
        class Person {
            String name;
            int age;

            Person(String name, int age) {
                this.name = name;
                this.age = age;
            }
        }
        List<Person> people = Arrays.asList(
                new Person("Alice", 20),
                new Person("Bob", 30),
                new Person("Charlie", 40)
        );
        assertEquals("Alice", X.list(people).elementAtOrElse(0, new Person("X", 99)).name);
        assertEquals(99, X.list(people).elementAtOrElse(10, new Person("X", 99)).age);
    }

    /**
     * 测试目的：验证elementAtOrElse(index, defaultValue)对极值索引的处理。
     * 测试要求：索引为Integer.MAX_VALUE/Integer.MIN_VALUE。
     * 测试功能：极值索引。
     * 测试范围：极值。
     * 测试结果：返回默认值。
     */
    @Test
    @Order(6)
    void testElementAtOrElseExtremeIndex() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertEquals(-1, X.list(list).elementAtOrElse(Integer.MAX_VALUE, -1));
        assertEquals(-2, X.list(list).elementAtOrElse(Integer.MIN_VALUE, -2));
    }

    /**
     * 测试目的：验证elementAtOrElse(index, defaultValue)对不同类型集合的支持。
     * 测试要求：输入为混合类型集合。
     * 测试功能：多类型。
     * 测试范围：混合类型集合。
     * 测试结果：返回对应类型元素或默认值。
     */
    @Test
    @Order(7)
    void testElementAtOrElseWithDifferentTypes() {
        List<Object> list = Arrays.asList(1, "a", 2.0, null);
        assertEquals(1, X.list(list).elementAtOrElse(0, "x"));
        assertEquals("a", X.list(list).elementAtOrElse(1, "x"));
        assertEquals(2.0, X.list(list).elementAtOrElse(2, "x"));
        assertEquals("default", X.list(list).elementAtOrElse(4, "default"));
    }

    /**
     * 测试目的：验证elementAtOrElse(index, defaultValue)对单元素集合的处理。
     * 测试要求：输入为单元素集合。
     * 测试功能：单元素。
     * 测试范围：单元素集合。
     * 测试结果：返回该元素或默认值。
     */
    @Test
    @Order(8)
    void testElementAtOrElseSingleElement() {
        List<String> list = Collections.singletonList("only");
        assertEquals("only", X.list(list).elementAtOrElse(0, "x"));
        assertEquals("default", X.list(list).elementAtOrElse(1, "default"));
    }

    /**
     * 测试目的：验证elementAtOrElse(index, defaultValue)对包含null元素的集合。
     * 测试要求：集合中有null。
     * 测试功能：null元素。
     * 测试范围：含null集合。
     * 测试结果：返回null元素或默认值。
     */
    @Test
    @Order(9)
    void testElementAtOrElseWithNullElements() {
        List<String> list = Arrays.asList("a", null, "b");
        assertNull(X.list(list).elementAtOrElse(1, "x"));
        assertEquals("b", X.list(list).elementAtOrElse(2, "x"));
        assertEquals("default", X.list(list).elementAtOrElse(5, "default"));
    }
}