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
class FirstOrNullTest {

    /**
     * 测试目的：验证firstOrNull()返回集合第一个元素或null。
     * 测试要求：输入为普通集合。
     * 测试功能：获取第一个元素。
     * 测试范围：普通集合。
     * 测试结果：返回第一个元素。
     */
    @Test
    @Order(1)
    void testFirstOrNullNormal() {
        List<Integer> list = Arrays.asList(10, 20, 30);
        Integer first = X.list(list).firstOrNull();
        assertEquals(10, first);
    }

    /**
     * 测试目的：验证firstOrNull()对空集合返回null。
     * 测试要求：输入为空集合。
     * 测试功能：空集合。
     * 测试范围：空集合。
     * 测试结果：返回null。
     */
    @Test
    @Order(2)
    void testFirstOrNullEmptyList() {
        List<Integer> list = Collections.emptyList();
        Integer first = X.list(list).firstOrNull();
        assertNull(first);
    }

    /**
     * 测试目的：验证firstOrNull()对null集合返回null。
     * 测试要求：输入为null。
     * 测试功能：null集合。
     * 测试范围：null集合。
     * 测试结果：返回null。
     */
    @Test
    @Order(3)
    void testFirstOrNullNullList() {
        Integer first = (Integer) X.list(null).firstOrNull();
        assertNull(first);
    }

    /**
     * 测试目的：验证firstOrNull(Predicate)返回第一个满足条件的元素或null。
     * 测试要求：输入为普通集合，谓词为大于10。
     * 测试功能：条件获取。
     * 测试范围：普通集合。
     * 测试结果：返回第一个满足条件的元素。
     */
    @Test
    @Order(4)
    void testFirstOrNullWithPredicate() {
        List<Integer> list = Arrays.asList(5, 15, 25);
        Integer first = X.list(list).firstOrNull(i -> i > 10);
        assertEquals(15, first);
    }

    /**
     * 测试目的：验证firstOrNull(Predicate)在无元素满足条件时返回null。
     * 测试要求：输入为普通集合，谓词为大于100。
     * 测试功能：无匹配。
     * 测试范围：普通集合。
     * 测试结果：返回null。
     */
    @Test
    @Order(5)
    void testFirstOrNullWithPredicateNoneMatch() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Integer first = X.list(list).firstOrNull(i -> i > 100);
        assertNull(first);
    }

    /**
     * 测试目的：验证firstOrNull(Predicate)对空集合返回null。
     * 测试要求：输入为空集合，谓词任意。
     * 测试功能：空集合。
     * 测试范围：空集合。
     * 测试结果：返回null。
     */
    @Test
    @Order(6)
    void testFirstOrNullWithPredicateEmptyList() {
        List<Integer> list = Collections.emptyList();
        Integer first = X.list(list).firstOrNull(i -> i > 0);
        assertNull(first);
    }

    /**
     * 测试目的：验证firstOrNull(Predicate)对null集合返回null。
     * 测试要求：输入为null，谓词任意。
     * 测试功能：null集合。
     * 测试范围：null集合。
     * 测试结果：返回null。
     */
    @Test
    @Order(7)
    void testFirstOrNullWithPredicateNullList() {
        Integer first = (Integer) X.list(null).firstOrNull(i -> true);
        assertNull(first);
    }

    /**
     * 测试目的：验证firstOrNull()对对象集合的支持。
     * 测试要求：输入为对象集合。
     * 测试功能：对象获取。
     * 测试范围：对象集合。
     * 测试结果：返回第一个对象。
     */
    @Test
    @Order(8)
    void testFirstOrNullWithObjects() {
        class Person {
            final String name;

            Person(String name) {
                this.name = name;
            }
        }
        List<Person> people = Arrays.asList(new Person("A"), new Person("B"));
        Person first = X.list(people).firstOrNull();
        assertEquals("A", first.name);
    }

    /**
     * 测试目的：验证firstOrNull()对不同类型元素集合的支持。
     * 测试要求：输入为不同类型元素集合。
     * 测试功能：多类型获取。
     * 测试范围：混合类型集合。
     * 测试结果：返回第一个元素。
     */
    @Test
    @Order(9)
    void testFirstOrNullWithDifferentTypes() {
        List<Object> list = Arrays.asList("x", 1, 2.0);
        Object first = X.list(list).firstOrNull();
        assertEquals("x", first);
    }

    /**
     * 测试目的：验证firstOrNull()对极值集合的支持。
     * 测试要求：输入为极大集合。
     * 测试功能：极值获取。
     * 测试范围：极值集合。
     * 测试结果：返回第一个元素。
     */
    @Test
    @Order(10)
    void testFirstOrNullWithExtremeValues() {
        int size = 10000;
        List<Integer> list = Collections.nCopies(size, 7);
        Integer first = X.list(list).firstOrNull();
        assertEquals(7, first);
    }
}