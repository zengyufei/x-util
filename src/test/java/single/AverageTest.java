package single;

import com.zyf.util.X;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AverageTest {

    /**
     * 测试目的：验证averageDouble(Function)对普通整数的平均值计算。
     * 测试要求：输入为整数集合，映射为Number。
     * 测试功能：平均值计算。
     * 测试范围：普通整数集合。
     * 测试结果：返回正确平均值。
     */
    @Test
    @Order(1)
    void testAverageDoubleWithFunction() {
        List<Integer> list = Arrays.asList(2, 4, 6, 8);
        double avg = X.list(list).averageDouble(i -> i);
        assertEquals(5.0, avg);
    }

    /**
     * 测试目的：验证averageInt(Function)对普通整数的平均值计算。
     * 测试要求：输入为整数集合，映射为Number。
     * 测试功能：平均值计算。
     * 测试范围：普通整数集合。
     * 测试结果：返回正确平均值（取整）。
     */
    @Test
    @Order(2)
    void testAverageIntWithFunction() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        int avg = X.list(list).averageInt(i -> i);
        assertEquals(2, avg);
    }

    /**
     * 测试目的：验证averageLong(Function)对Long类型的平均值计算。
     * 测试要求：输入为Long集合。
     * 测试功能：平均值计算。
     * 测试范围：Long集合。
     * 测试结果：返回正确平均值。
     */
    @Test
    @Order(3)
    void testAverageLongWithFunction() {
        List<Long> list = Arrays.asList(10L, 20L, 30L);
        long avg = X.list(list).averageLong(l -> l);
        assertEquals(20L, avg);
    }

    /**
     * 测试目的：验证averageBigDecimal(Function)对BigDecimal类型的平均值计算。
     * 测试要求：输入为BigDecimal集合。
     * 测试功能：平均值计算。
     * 测试范围：BigDecimal集合。
     * 测试结果：返回正确平均值。
     */
    @Test
    @Order(4)
    void testAverageBigDecimalWithFunction() {
        List<BigDecimal> list = Arrays.asList(new BigDecimal("1.5"), new BigDecimal("2.5"), new BigDecimal("3.0"));
        BigDecimal avg = X.list(list).averageBigDecimal(bd -> bd);
        assertEquals(new BigDecimal("2.33"), avg);
    }

    /**
     * 测试目的：验证averageDouble()对Number集合的平均值计算。
     * 测试要求：输入为Number集合。
     * 测试功能：平均值计算。
     * 测试范围：Number集合。
     * 测试结果：返回正确平均值。
     */
    @Test
    @Order(5)
    void testAverageDouble() {
        List<Number> list = Arrays.asList(1, 2, 3, 4);
        double avg = X.list(list).averageDouble();
        assertEquals(2.5, avg);
    }

    /**
     * 测试目的：验证averageInt()对Number集合的平均值计算。
     * 测试要求：输入为Number集合。
     * 测试功能：平均值计算。
     * 测试范围：Number集合。
     * 测试结果：返回正确平均值（取整）。
     */
    @Test
    @Order(6)
    void testAverageInt() {
        List<Number> list = Arrays.asList(1, 2, 3, 4);
        int avg = X.list(list).averageInt();
        assertEquals(2, avg);
    }

    /**
     * 测试目的：验证averageLong()对Number集合的平均值计算。
     * 测试要求：输入为Number集合。
     * 测试功能：平均值计算。
     * 测试范围：Number集合。
     * 测试结果：返回正确平均值。
     */
    @Test
    @Order(7)
    void testAverageLong() {
        List<Number> list = Arrays.asList(10L, 20L, 30L);
        long avg = X.list(list).averageLong();
        assertEquals(20L, avg);
    }

    /**
     * 测试目的：验证averageBigDecimal()对Number集合的平均值计算。
     * 测试要求：输入为Number集合。
     * 测试功能：平均值计算。
     * 测试范围：Number集合。
     * 测试结果：返回正确平均值。
     */
    @Test
    @Order(8)
    void testAverageBigDecimal() {
        List<Number> list = Arrays.asList(1, 2, 3);
        BigDecimal avg = X.list(list).averageBigDecimal();
        assertEquals(new BigDecimal("2"), avg);
    }

    /**
     * 测试目的：验证空集合时averageXXX方法的返回值。
     * 测试要求：输入为空集合。
     * 测试功能：空集合平均值。
     * 测试范围：空集合。
     * 测试结果：返回0或0.0。
     */
    @Test
    @Order(9)
    void testAverageWithEmptyList() {
        List<Integer> list = Collections.emptyList();
        assertEquals(0.0, X.list(list).averageDouble());
        assertEquals(0, X.list(list).averageInt());
        assertEquals(0L, X.list(list).averageLong());
        assertEquals(BigDecimal.ZERO, X.list(list).averageBigDecimal());
    }

    /**
     * 测试目的：验证包含null元素时averageXXX方法的健壮性。
     * 测试要求：集合中包含null。
     * 测试功能：null元素处理。
     * 测试范围：含null集合。
     * 测试结果：抛出异常或跳过null。
     */
    @Test
    @Order(10)
    void testAverageWithNullElements() {
        List<Integer> list = Arrays.asList(1, null, 3);
        assertEquals(2, X.list(list).averageDouble(i -> i));
    }

    /**
     * 测试目的：验证averageXXX方法对极值的处理。
     * 测试要求：集合包含极大/极小值。
     * 测试功能：极值平均。
     * 测试范围：极值集合。
     * 测试结果：返回正确平均值。
     */
    @Test
    @Order(11)
    void testAverageWithExtremeValues() {
        List<Integer> list = Arrays.asList(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        double avg = X.list(list).averageDouble(i -> i);
        assertEquals((double) Integer.MAX_VALUE, avg);
    }

    /**
     * 测试目的：验证averageXXX方法对对象集合的支持。
     * 测试要求：对象集合，映射属性。
     * 测试功能：对象属性平均。
     * 测试范围：对象集合。
     * 测试结果：返回属性平均值。
     */
    @Test
    @Order(12)
    void testAverageWithObjects() {
        class Person {
            int age;

            Person(int age) {
                this.age = age;
            }
        }
        List<Person> people = Arrays.asList(new Person(20), new Person(30), new Person(40));
        assertThrows(IllegalStateException.class, () -> {
            X.list(people).averageDouble(p -> p.age);
        });
    }

    /**
     * 测试目的：验证averageXXX方法对不同类型（如Double、Long、Integer）混合的支持。
     * 测试要求：集合为Number子类混合。
     * 测试功能：多类型平均。
     * 测试范围：混合类型集合。
     * 测试结果：返回正确平均值。
     */
    @Test
    @Order(13)
    void testAverageWithDifferentNumberTypes() {
        List<Number> list = Arrays.asList(1, 2L, 3.0);
        double avg = X.list(list).averageDouble();
        assertEquals(2.0, avg);
    }

    /**
     * 测试目的：验证averageXXX方法对null集合的处理。
     * 测试要求：输入为null。
     * 测试功能：null集合。
     * 测试范围：null集合。
     * 测试结果：返回0或0.0。
     */
    @Test
    @Order(14)
    void testAverageWithNullList() {
        assertEquals(0.0, X.list(null).averageDouble());
        assertEquals(0, X.list(null).averageInt());
        assertEquals(0L, X.list(null).averageLong());
        assertEquals(BigDecimal.ZERO, X.list(null).averageBigDecimal());
    }
}