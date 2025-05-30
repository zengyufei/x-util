package single;

import com.zyf.util.X;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LastOrNullTest {

    /**
     * 验证lastOrNull()返回集合最后一个元素，空集合返回null。
     */
    @Test
    @Order(1)
    void testLastOrNullNormal() {
        List<Integer> list = Arrays.asList(10, 20, 30);
        Integer last = X.list(list).lastOrNull();
        assertEquals(30, last);
    }

    @Test
    @Order(2)
    void testLastOrNullEmptyList() {
        List<Integer> list = Collections.emptyList();
        Integer last = X.list(list).lastOrNull();
        assertNull(last);
    }

    @Test
    @Order(3)
    void testLastOrNullNullList() {
        Integer last = (Integer) X.list(null).lastOrNull();
        assertNull(last);
    }

    /**
     * 验证lastOrNull(Predicate)返回最后一个满足条件的元素，无匹配返回null。
     */
    @Test
    @Order(4)
    void testLastOrNullWithPredicate() {
        List<Integer> list = Arrays.asList(5, 15, 25, 12);
        Integer last = X.list(list).lastOrNull(i -> i > 10);
        assertEquals(12, last);
    }

    @Test
    @Order(5)
    void testLastOrNullWithMultiplePredicates() {
        List<Integer> list = Arrays.asList(5, 12, 15, 18, 22);
        Predicate<Integer> gt10 = i -> i > 10;
        Predicate<Integer> even = i -> i % 2 == 0;
        Integer last = X.list(list).lastOrNull(gt10, even);
        assertEquals(22, last);
    }

    @Test
    @Order(6)
    void testLastOrNullWithPredicateNoneMatch() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Integer last = X.list(list).lastOrNull(i -> i > 100);
        assertNull(last);
    }

    @Test
    @Order(7)
    void testLastOrNullWithPredicateEmptyList() {
        List<Integer> list = Collections.emptyList();
        Integer last = X.list(list).lastOrNull(i -> i > 0);
        assertNull(last);
    }

    @Test
    @Order(8)
    void testLastOrNullWithPredicateNullList() {
        Integer last = (Integer) X.list(null).lastOrNull(i -> true);
        assertNull(last);
    }

    /**
     * 验证lastOrNull()对对象集合的支持。
     */
    @Test
    @Order(9)
    void testLastOrNullWithObjects() {
        class Person {
            final String name;

            Person(String name) {
                this.name = name;
            }
        }
        List<Person> people = Arrays.asList(new Person("A"), new Person("B"));
        Person last = X.list(people).lastOrNull();
        assertEquals("B", last.name);
    }

    /**
     * 验证lastOrNull()对不同类型元素集合的支持。
     */
    @Test
    @Order(10)
    void testLastOrNullWithDifferentTypes() {
        List<Object> list = Arrays.asList("x", 1, 2.0);
        Object last = X.list(list).lastOrNull();
        assertEquals(2.0, last);
    }

    /**
     * 验证lastOrNull()对极值集合的支持。
     */
    @Test
    @Order(11)
    void testLastOrNullWithExtremeValues() {
        int size = 10000;
        List<Integer> list = Collections.nCopies(size, 7);
        Integer last = X.list(list).lastOrNull();
        assertEquals(7, last);
    }

    /**
     * 验证lastOrNull(Predicate...)参数为null时抛出异常。
     */
    @Test
    @Order(12)
    void testLastOrNullWithNullPredicates() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertThrows(NullPointerException.class, () -> X.list(list).lastOrNull((Predicate<Integer>[]) null));
    }
}