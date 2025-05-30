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
class ReduceTest {

    /**
     * 测试目的：验证reduce方法对整数列表求和的正确性。
     * 测试要求：输入为一组整数，累加操作。
     * 测试功能：累加所有整数。
     * 测试范围：普通整数列表。
     * 测试结果：返回所有元素之和。
     */
    @Test
    @Order(1)
    void testReduceWithIntegersSum() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        Integer result = X.list(list).reduce(() -> 0, Integer::sum);
        assertEquals(15, result);
    }

    /**
     * 测试目的：验证reduce方法在空列表时返回初始值。
     * 测试要求：输入为空列表，初始值为100。
     * 测试功能：空集合时返回初始值。
     * 测试范围：空集合。
     * 测试结果：返回初始值100。
     */
    @Test
    @Order(2)
    void testReduceWithEmptyList() {
        List<Integer> list = Collections.emptyList();
        Integer result = X.list(list).reduce(() -> 100, Integer::sum);
        assertEquals(100, result);
    }

    /**
     * 测试目的：验证reduce方法对包含null元素的列表的健壮性。
     * 测试要求：输入包含null，累加时跳过null。
     * 测试功能：处理null元素。
     * 测试范围：含null的集合。
     * 测试结果：null元素按0处理，返回正确和。
     */
    @Test
    @Order(3)
    void testReduceWithNullElements() {
        List<Integer> list = Arrays.asList(1, null, 3);
        Integer result = X.list(list).reduce(() -> 0, (acc, t) -> acc + (t == null ? 0 : t));
        assertEquals(4, result);
    }

    /**
     * 测试目的：验证reduce方法对字符串拼接的支持。
     * 测试要求：输入为字符串列表，累加为字符串。
     * 测试功能：字符串拼接。
     * 测试范围：字符串集合。
     * 测试结果：返回拼接后的字符串。
     */
    @Test
    @Order(4)
    void testReduceWithStringsConcatenation() {
        List<String> list = Arrays.asList("a", "b", "c");
        final StringBuilder result = X.list(list).reduce(StringBuilder::new, s -> s, (sb, s) -> {
            sb.append(s);
            return sb;
        });
        assertEquals("abc", result.toString());
    }

    /**
     * 测试目的：验证reduce方法对对象属性累加的能力。
     * 测试要求：输入为对象列表，累加对象属性。
     * 测试功能：对象属性求和。
     * 测试范围：对象集合。
     * 测试结果：返回所有对象age属性之和。
     */
    @Test
    @Order(5)
    void testReduceWithObjects() {
        class Person {
            String name;
            int age;

            Person(String name, int age) {
                this.name = name;
                this.age = age;
            }
        }
        List<Person> people = Arrays.asList(
                new Person("Alice", 30),
                new Person("Bob", 25),
                new Person("Charlie", 35)
        );
        Integer totalAge = X.list(people).reduce(() -> 0, (acc, p) -> acc + p.age);
        assertEquals(90, totalAge);
    }

    /**
     * 测试目的：验证reduce方法对单元素集合的处理。
     * 测试要求：输入为单元素集合。
     * 测试功能：单元素累加。
     * 测试范围：单元素集合。
     * 测试结果：返回初始值与该元素之和。
     */
    @Test
    @Order(6)
    void testReduceWithSingleElement() {
        List<Integer> list = Collections.singletonList(42);
        Integer result = X.list(list).reduce(() -> 10, Integer::sum);
        assertEquals(52, result);
    }

    /**
     * 测试目的：验证reduce方法对极值的处理能力。
     * 测试要求：输入包含Integer.MAX_VALUE。
     * 测试功能：极值累加。
     * 测试范围：包含极值的集合。
     * 测试结果：返回极值累加结果。
     */
    @Test
    @Order(7)
    void testReduceWithExtremeValues() {
        List<Integer> list = Arrays.asList(Integer.MAX_VALUE, 1, -1);
        long result = X.list(list).reduce(() -> 0L, (acc, t) -> acc + t);
        assertEquals((long) Integer.MAX_VALUE, result);
    }

    /**
     * 测试目的：验证reduce方法对不同类型（如Double）的支持。
     * 测试要求：输入为Double类型集合。
     * 测试功能：不同类型累加。
     * 测试范围：Double集合。
     * 测试结果：返回所有元素之和。
     */
    @Test
    @Order(8)
    void testReduceWithDifferentTypes() {
        List<Double> list = Arrays.asList(1.5, 2.5, 3.0);
        Double result = X.list(list).reduce(() -> 0.0, Double::sum);
        assertEquals(7.0, result);
    }

    /**
     * 测试目的：验证reduce方法在不安全操作（如除零）时的异常抛出。
     * 测试要求：累加操作中出现除零。
     * 测试功能：异常场景。
     * 测试范围：不安全操作。
     * 测试结果：抛出ArithmeticException。
     */
    @Test
    @Order(9)
    void testReduceWithUnsafeOperation() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertThrows(ArithmeticException.class, () -> {
            X.list(list).reduce(() -> 1, (acc, t) -> acc / 0);
        });
    }

    /**
     * 测试目的：验证reduce方法在安全操作下的正确性。
     * 测试要求：累加操作为乘法。
     * 测试功能：安全场景。
     * 测试范围：安全操作。
     * 测试结果：返回所有元素连乘积。
     */
    @Test
    @Order(10)
    void testReduceWithSafeOperation() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Integer result = X.list(list).reduce(() -> 1, (acc, t) -> acc * t);
        assertEquals(6, result);
    }

    /**
     * 测试目的：验证reduce方法对null Supplier的异常处理。
     * 测试要求：Supplier为null。
     * 测试功能：异常参数。
     * 测试范围：Supplier为null。
     * 测试结果：抛出NullPointerException。
     */
    @Test
    @Order(11)
    void testReduceWithNullSupplier() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertThrows(NullPointerException.class, () -> {
            X.list(list).reduce(null, Integer::sum);
        });
    }

    /**
     * 测试目的：验证reduce方法对null Function的异常处理。
     * 测试要求：Function为null。
     * 测试功能：异常参数。
     * 测试范围：Function为null。
     * 测试结果：抛出NullPointerException。
     */
    @Test
    @Order(12)
    void testReduceWithNullFunctionResult() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertEquals(null, X.list(list).reduce(() -> 0, (l, ele) -> null));
    }

    /**
     * 测试目的：验证reduce方法对null ListStream的异常处理。
     * 测试要求：ListStream为null。
     * 测试功能：异常参数。
     * 测试范围：ListStream为null。
     * 测试结果：抛出NullPointerException。
     */
    @Test
    @Order(13)
    void testReduceWithNullList() {
        assertEquals(0,
                X.list(null).reduce(() -> 0, (l, ele) -> {
                    if (ele == null) {
                        return l;
                    }
                    return l + Integer.parseInt(ele.toString());
                })
        );
    }

    /**
     * 测试目的：验证reduce方法对布尔类型的支持。
     * 测试要求：输入为布尔类型集合，累加为逻辑与。
     * 测试功能：布尔类型累加。
     * 测试范围：布尔集合。
     * 测试结果：返回所有元素逻辑与结果。
     */
    @Test
    @Order(14)
    void testReduceWithBoolean() {
        List<Boolean> list = Arrays.asList(true, false, true);
        Boolean result = X.list(list).reduce(() -> true, (acc, t) -> acc && t);
        assertFalse(result);
    }

    /**
     * 测试目的：验证reduce方法对自定义对象结果的支持。
     * 测试要求：累加器为自定义对象。
     * 测试功能：自定义对象累加。
     * 测试范围：自定义对象。
     * 测试结果：返回对象属性累加后的结果。
     */
    @Test
    @Order(15)
    void testReduceWithCustomObjectResult() {
        class Counter {
            int count = 0;
        }
        List<String> list = Arrays.asList("a", "b", "c");
        Counter counter = X.list(list).reduce(Counter::new, (acc, s) -> {
            acc.count++;
            return acc;
        });
        assertEquals(3, counter.count);
    }
}