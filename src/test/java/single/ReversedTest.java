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
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReversedTest {

    /**
     * 测试目的：验证reversed()对普通整数集合的反转。
     */
    @Test
    @Order(1)
    void testReversedNormal() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> reversed = X.list(list).reversed().toList();
        assertEquals(Arrays.asList(5, 4, 3, 2, 1), reversed);
    }

    /**
     * 测试目的：验证reversed()对空集合的反转。
     */
    @Test
    @Order(2)
    void testReversedEmptyList() {
        List<Integer> list = Collections.emptyList();
        List<Integer> reversed = X.list(list).reversed().toList();
        assertTrue(reversed.isEmpty());
    }

    /**
     * 测试目的：验证reversed()对null集合的反转。
     */
    @Test
    @Order(3)
    void testReversedNullList() {
        List<Object> reversed = X.list(null).reversed().toList();
        assertTrue(reversed.isEmpty());
    }

    /**
     * 测试目的：验证reversed()对单元素集合的反转。
     */
    @Test
    @Order(4)
    void testReversedSingleElement() {
        List<String> list = Collections.singletonList("a");
        List<String> reversed = X.list(list).reversed().toList();
        assertEquals(Collections.singletonList("a"), reversed);
    }

    /**
     * 测试目的：验证reversed()对包含null元素的集合的反转。
     */
    @Test
    @Order(5)
    void testReversedWithNullElements() {
        List<String> list = Arrays.asList("a", null, "b");
        List<String> reversed = X.list(list).reversed().toList();
        assertEquals(Arrays.asList("b", null, "a"), reversed);
    }

    /**
     * 测试目的：验证reversed()对对象集合的反转。
     */
    @Test
    @Order(6)
    void testReversedWithObjects() {
        class Person {
            String name;

            Person(String name) {
                this.name = name;
            }
        }
        List<Person> people = Arrays.asList(new Person("A"), new Person("B"), new Person("C"));
        List<Person> reversed = X.list(people).reversed().toList();
        assertEquals("C", reversed.get(0).name);
        assertEquals("B", reversed.get(1).name);
        assertEquals("A", reversed.get(2).name);
    }

    /**
     * 测试目的：验证reversed()多次调用的幂等性。
     */
    @Test
    @Order(7)
    void testReversedTwiceIsOriginal() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        List<Integer> reversedTwice = X.list(list).reversed().reversed().toList();
        assertEquals(list, reversedTwice);
    }

    /**
     * 测试目的：验证reversed()对已反转集合的幂等性。
     */
    @Test
    @Order(8)
    void testReversedAlreadyReversed() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> reversed = X.list(list).reversed().toList();
        List<Integer> reversedAgain = X.list(reversed).reversed().toList();
        assertEquals(list, reversedAgain);
    }
}