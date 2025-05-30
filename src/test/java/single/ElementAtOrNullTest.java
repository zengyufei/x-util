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
class ElementAtOrNullTest {

    /**
     * 测试目的：验证elementAtOrNull(index)对普通集合的取值。
     * 测试要求：输入为普通集合。
     * 测试功能：按索引取值。
     * 测试范围：普通集合。
     * 测试结果：返回对应索引的元素。
     */
    @Test
    @Order(1)
    void testElementAtOrNullNormal() {
        List<String> list = Arrays.asList("a", "b", "c");
        assertEquals("a", X.list(list).elementAtOrNull(0));
        assertEquals("b", X.list(list).elementAtOrNull(1));
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
    @Order(2)
    void testElementAtOrNullOutOfBounds() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertNull(X.list(list).elementAtOrNull(3));
        assertNull(X.list(list).elementAtOrNull(100));
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
    @Order(3)
    void testElementAtOrNullEmptyList() {
        List<Double> list = Collections.emptyList();
        assertNull(X.list(list).elementAtOrNull(0));
        assertNull(X.list(list).elementAtOrNull(1));
    }

    /**
     * 测试目的：验证elementAtOrNull(index)对null集合返回null。
     * 测试要求：输入为null。
     * 测试功能：null集合返回null。
     * 测试范围：null集合。
     * 测试结果：返回null。
     */
    @Test
    @Order(4)
    void testElementAtOrNullNullList() {
        assertNull(X.list(null).elementAtOrNull(0));
        assertNull(X.list(null).elementAtOrNull(10));
    }

    /**
     * 测试目的：验证elementAtOrNull(index)对对象集合的支持。
     * 测试要求：对象集合。
     * 测试功能：对象索引取值。
     * 测试范围：对象集合。
     * 测试结果：返回对应对象。
     */
    @Test
    @Order(5)
    void testElementAtOrNullWithObjects() {
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
        assertEquals("Alice", X.list(people).elementAtOrNull(0).name);
        assertEquals("Charlie", X.list(people).elementAtOrNull(2).name);
        assertNull(X.list(people).elementAtOrNull(5));
    }

    /**
     * 测试目的：验证elementAtOrNull(index)对极值索引的处理。
     * 测试要求：索引为Integer.MAX_VALUE/Integer.MIN_VALUE。
     * 测试功能：极值索引。
     * 测试范围：极值。
     * 测试结果：返回null。
     */
    @Test
    @Order(6)
    void testElementAtOrNullExtremeIndex() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertNull(X.list(list).elementAtOrNull(Integer.MAX_VALUE));
        assertNull(X.list(list).elementAtOrNull(Integer.MIN_VALUE));
    }

    /**
     * 测试目的：验证elementAtOrNull(index)对不同类型集合的支持。
     * 测试要求：输入为混合类型集合。
     * 测试功能：多类型。
     * 测试范围：混合类型集合。
     * 测试结果：返回对应类型元素。
     */
    @Test
    @Order(7)
    void testElementAtOrNullWithDifferentTypes() {
        List<Object> list = Arrays.asList(1, "a", 2.0, null);
        assertEquals(1, X.list(list).elementAtOrNull(0));
        assertEquals("a", X.list(list).elementAtOrNull(1));
        assertEquals(2.0, X.list(list).elementAtOrNull(2));
        assertNull(X.list(list).elementAtOrNull(3));
    }

    /**
     * 测试目的：验证elementAtOrNull(index)对单元素集合的处理。
     * 测试要求：输入为单元素集合。
     * 测试功能：单元素。
     * 测试范围：单元素集合。
     * 测试结果：返回该元素。
     */
    @Test
    @Order(8)
    void testElementAtOrNullSingleElement() {
        List<String> list = Collections.singletonList("only");
        assertEquals("only", X.list(list).elementAtOrNull(0));
        assertNull(X.list(list).elementAtOrNull(1));
    }

    /**
     * 测试目的：验证elementAtOrNull(index)对包含null元素的集合。
     * 测试要求：集合中有null。
     * 测试功能：null元素。
     * 测试范围：含null集合。
     * 测试结果：返回null元素。
     */
    @Test
    @Order(9)
    void testElementAtOrNullWithNullElements() {
        List<String> list = Arrays.asList("a", null, "b");
        assertNull(X.list(list).elementAtOrNull(1));
        assertEquals("b", X.list(list).elementAtOrNull(2));
    }
}