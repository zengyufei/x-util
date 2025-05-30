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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CountTest {

    /**
     * 测试目的：验证count()对普通整数集合的计数。
     * 测试要求：输入为整数集合。
     * 测试功能：计数。
     * 测试范围：普通整数集合。
     * 测试结果：返回集合元素个数。
     */
    @Test
    @Order(1)
    void testCountNormal() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        long count = X.list(list).count();
        assertEquals(5, count);
    }

    /**
     * 测试目的：验证count()对空集合的计数。
     * 测试要求：输入为空集合。
     * 测试功能：空集合计数。
     * 测试范围：空集合。
     * 测试结果：返回0。
     */
    @Test
    @Order(2)
    void testCountEmptyList() {
        List<Integer> list = Collections.emptyList();
        long count = X.list(list).count();
        assertEquals(0, count);
    }

    /**
     * 测试目的：验证count()对null集合的计数。
     * 测试要求：输入为null。
     * 测试功能：null集合计数。
     * 测试范围：null集合。
     * 测试结果：返回0。
     */
    @Test
    @Order(3)
    void testCountNullList() {
        long count = X.list(null).count();
        assertEquals(0, count);
    }

    /**
     * 测试目的：验证count(Predicate)对满足条件元素的计数。
     * 测试要求：输入为整数集合，谓词为偶数。
     * 测试功能：条件计数。
     * 测试范围：普通整数集合。
     * 测试结果：返回满足条件的元素个数。
     */
    @Test
    @Order(4)
    void testCountWithPredicate() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
        long count = X.list(list).count(i -> i % 2 == 0);
        assertEquals(3, count);
    }

    /**
     * 测试目的：验证count(Predicate...)对多个谓词的计数（任意一个满足）。
     * 测试要求：输入为整数集合，谓词为偶数或大于4。
     * 测试功能：多条件计数。
     * 测试范围：普通整数集合。
     * 测试结果：返回满足任一条件的元素个数。
     */
    @Test
    @Order(5)
    void testCountWithMultiplePredicates() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
        Predicate<Integer> even = i -> i % 2 == 0;
        Predicate<Integer> gt4 = i -> i > 4;
        long count = X.list(list).count(even, gt4);
        assertEquals(4, count); // 2, 4, 5, 6
    }

    /**
     * 测试目的：验证count()对对象集合的计数。
     * 测试要求：输入为对象集合。
     * 测试功能：对象计数。
     * 测试范围：对象集合。
     * 测试结果：返回对象个数。
     */
    @Test
    @Order(6)
    void testCountWithObjects() {
        class Person {
            final String name;

            Person(String name) {
                this.name = name;
            }
        }
        List<Person> people = Arrays.asList(new Person("A"), new Person("B"));
        long count = X.list(people).count();
        assertEquals(2, count);
    }

    /**
     * 测试目的：验证count()对不同类型元素集合的计数。
     * 测试要求：输入为不同类型元素集合。
     * 测试功能：多类型计数。
     * 测试范围：混合类型集合。
     * 测试结果：返回元素个数。
     */
    @Test
    @Order(7)
    void testCountWithDifferentTypes() {
        List<Object> list = Arrays.asList(1, "a", 2.0, null);
        long count = X.list(list).count();
        assertEquals(4, count);
    }

    /**
     * 测试目的：验证count()对极值集合的计数。
     * 测试要求：输入为极大集合。
     * 测试功能：极值计数。
     * 测试范围：极值集合。
     * 测试结果：返回元素个数。
     */
    @Test
    @Order(8)
    void testCountWithExtremeValues() {
        int size = 10000;
        List<Integer> list = Collections.nCopies(size, 1);
        long count = X.list(list).count();
        assertEquals(size, count);
    }

    /**
     * 测试目的：验证count(Predicate)对所有元素不满足条件时的计数。
     * 测试要求：输入为整数集合，谓词为大于100。
     * 测试功能：条件计数。
     * 测试范围：普通整数集合。
     * 测试结果：返回0。
     */
    @Test
    @Order(9)
    void testCountWithPredicateNoneMatch() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        long count = X.list(list).count(i -> i > 100);
        assertEquals(0, count);
    }

    /**
     * 测试目的：验证count(Predicate)对所有元素都满足条件时的计数。
     * 测试要求：输入为整数集合，谓词为大于0。
     * 测试功能：条件计数。
     * 测试范围：普通整数集合。
     * 测试结果：返回集合元素个数。
     */
    @Test
    @Order(10)
    void testCountWithPredicateAllMatch() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        long count = X.list(list).count(i -> i > 0);
        assertEquals(3, count);
    }
}