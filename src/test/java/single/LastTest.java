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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LastTest {

    /**
     * 测试目的：验证last()返回集合最后一个元素。
     * 测试要求：输入为普通集合。
     * 测试功能：获取最后一个元素。
     * 测试范围：普通集合。
     * 测试结果：返回最后一个元素。
     */
    @Test
    @Order(1)
    void testLastNormal() {
        List<Integer> list = Arrays.asList(10, 20, 30);
        int last = X.list(list).last();
        assertEquals(30, last);
    }

    /**
     * 测试目的：验证last()对空集合抛出异常。
     * 测试要求：输入为空集合。
     * 测试功能：异常场景。
     * 测试范围：空集合。
     * 测试结果：抛出IllegalCallerException。
     */
    @Test
    @Order(2)
    void testLastEmptyList() {
        List<Integer> list = Collections.emptyList();
        assertThrows(IllegalCallerException.class, () -> X.list(list).last());
    }

    /**
     * 测试目的：验证last()对null集合抛出异常。
     * 测试要求：输入为null。
     * 测试功能：异常场景。
     * 测试范围：null集合。
     * 测试结果：抛出IllegalCallerException。
     */
    @Test
    @Order(3)
    void testLastNullList() {
        assertThrows(IllegalCallerException.class, () -> X.list(null).last());
    }

    /**
     * 测试目的：验证last(Predicate)返回最后一个满足条件的元素。
     * 测试要求：输入为普通集合，谓词为大于10。
     * 测试功能：条件获取。
     * 测试范围：普通集合。
     * 测试结果：返回最后一个满足条件的元素。
     */
    @Test
    @Order(4)
    void testLastWithPredicate() {
        List<Integer> list = Arrays.asList(5, 15, 25, 12);
        int last = X.list(list).last(i -> i > 10);
        assertEquals(12, last);
    }

    /**
     * 测试目的：验证last(Predicate...)返回最后一个同时满足所有条件的元素。
     * 测试要求：输入为普通集合，谓词为大于10且为偶数。
     * 测试功能：多条件获取。
     * 测试范围：普通集合。
     * 测试结果：返回最后一个满足所有条件的元素。
     */
    @Test
    @Order(5)
    void testLastWithMultiplePredicates() {
        List<Integer> list = Arrays.asList(5, 12, 15, 18, 22);
        Predicate<Integer> gt10 = i -> i > 10;
        Predicate<Integer> even = i -> i % 2 == 0;
        int last = X.list(list).last(gt10, even);
        assertEquals(22, last);
    }

    /**
     * 测试目的：验证last(Predicate)在无元素满足条件时抛出异常。
     * 测试要求：输入为普通集合，谓词为大于100。
     * 测试功能：异常场景。
     * 测试范围：普通集合。
     * 测试结果：抛出IllegalCallerException。
     */
    @Test
    @Order(6)
    void testLastWithPredicateNoneMatch() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertThrows(IllegalCallerException.class, () -> X.list(list).last(i -> i > 100));
    }

    /**
     * 测试目的：验证last(Predicate)对空集合抛出异常。
     * 测试要求：输入为空集合，谓词任意。
     * 测试功能：空集合。
     * 测试范围：空集合。
     * 测试结果：抛出IllegalCallerException。
     */
    @Test
    @Order(7)
    void testLastWithPredicateEmptyList() {
        List<Integer> list = Collections.emptyList();
        assertThrows(IllegalCallerException.class, () -> X.list(list).last(i -> i > 0));
    }

    /**
     * 测试目的：验证last(Predicate)对null集合抛出异常。
     * 测试要求：输入为null，谓词任意。
     * 测试功能：null集合。
     * 测试范围：null集合。
     * 测试结果：抛出IllegalCallerException。
     */
    @Test
    @Order(8)
    void testLastWithPredicateNullList() {
        assertThrows(IllegalCallerException.class, () -> X.list(null).last(i -> true));
    }

    /**
     * 测试目的：验证last()对对象集合的支持。
     * 测试要求：输入为对象集合。
     * 测试功能：对象获取。
     * 测试范围：对象集合。
     * 测试结果：返回最后一个对象。
     */
    @Test
    @Order(9)
    void testLastWithObjects() {
        class Person {
            final String name;

            Person(String name) {
                this.name = name;
            }
        }
        List<Person> people = Arrays.asList(new Person("A"), new Person("B"));
        Person last = X.list(people).last();
        assertEquals("B", last.name);
    }

    /**
     * 测试目的：验证last()对不同类型元素集合的支持。
     * 测试要求：输入为不同类型元素集合。
     * 测试功能：多类型获取。
     * 测试范围：混合类型集合。
     * 测试结果：返回最后一个元素。
     */
    @Test
    @Order(10)
    void testLastWithDifferentTypes() {
        List<Object> list = Arrays.asList("x", 1, 2.0);
        Object last = X.list(list).last();
        assertEquals(2.0, last);
    }

    /**
     * 测试目的：验证last()对极值集合的支持。
     * 测试要求：输入为极大集合。
     * 测试功能：极值获取。
     * 测试范围：极值集合。
     * 测试结果：返回最后一个元素。
     */
    @Test
    @Order(11)
    void testLastWithExtremeValues() {
        int size = 10000;
        List<Integer> list = Collections.nCopies(size, 7);
        int last = X.list(list).last();
        assertEquals(7, last);
    }
}