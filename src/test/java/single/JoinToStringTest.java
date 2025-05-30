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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JoinToStringTest {

    /**
     * 测试目的：验证joinToString(CharSequence)对普通字符串集合的连接。
     * 测试要求：输入为字符串集合，分隔符为","。
     * 测试功能：字符串连接。
     * 测试范围：普通字符串集合。
     * 测试结果：返回用逗号分隔的字符串。
     */
    @Test
    @Order(1)
    void testJoinToStringWithComma() {
        List<String> list = Arrays.asList("a", "b", "c");
        String result = X.list(list).joinToString(",");
        assertEquals("a,b,c", result);
    }

    /**
     * 测试目的：验证joinToString(CharSequence)对空集合的处理。
     * 测试要求：输入为空集合。
     * 测试功能：空集合连接。
     * 测试范围：空集合。
     * 测试结果：返回空字符串。
     */
    @Test
    @Order(2)
    void testJoinToStringWithEmptyList() {
        List<String> list = Collections.emptyList();
        String result = X.list(list).joinToString(",");
        assertEquals("", result);
    }

    /**
     * 测试目的：验证joinToString(CharSequence)对单元素集合的处理。
     * 测试要求：输入为单元素集合。
     * 测试功能：单元素连接。
     * 测试范围：单元素集合。
     * 测试结果：返回该元素字符串。
     */
    @Test
    @Order(3)
    void testJoinToStringWithSingleElement() {
        List<String> list = Collections.singletonList("x");
        String result = X.list(list).joinToString("-");
        assertEquals("x", result);
    }

    /**
     * 测试目的：验证joinToString(CharSequence)对包含null元素的集合的健壮性。
     * 测试要求：输入包含null。
     * 测试功能：处理null元素。
     * 测试范围：含null的集合。
     * 测试结果：null元素被跳过或toString为"null"。
     */
    @Test
    @Order(4)
    void testJoinToStringWithNullElements() {
        List<String> list = Arrays.asList("a", null, "c");
        String result = X.list(list).joinToString("|");
        assertEquals("a|c", result);
    }

    /**
     * 测试目的：验证joinToString(CharSequence)对数字集合的连接。
     * 测试要求：输入为整数集合。
     * 测试功能：数字连接。
     * 测试范围：整数集合。
     * 测试结果：返回用分号分隔的字符串。
     */
    @Test
    @Order(5)
    void testJoinToStringWithNumbers() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        String result = X.list(list).joinToString(";");
        assertEquals("1;2;3", result);
    }

    /**
     * 测试目的：验证joinToString(CharSequence)对极值的处理。
     * 测试要求：输入包含Integer.MAX_VALUE。
     * 测试功能：极值连接。
     * 测试范围：极值集合。
     * 测试结果：返回极值字符串。
     */
    @Test
    @Order(6)
    void testJoinToStringWithExtremeValues() {
        List<Integer> list = Arrays.asList(Integer.MAX_VALUE, Integer.MIN_VALUE);
        String result = X.list(list).joinToString(",");
        assertEquals(Integer.MAX_VALUE + "," + Integer.MIN_VALUE, result);
    }

    /**
     * 测试目的：验证joinToString(CharSequence)对对象集合的连接。
     * 测试要求：输入为对象集合。
     * 测试功能：对象toString连接。
     * 测试范围：对象集合。
     * 测试结果：返回对象toString拼接。
     */
    @Test
    @Order(7)
    void testJoinToStringWithObjects() {
        class Person {
            String name;

            Person(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return name;
            }
        }
        List<Person> people = Arrays.asList(new Person("A"), new Person("B"));
        String result = X.list(people).joinToString("/");
        assertEquals("A/B", result);
    }

    /**
     * 测试目的：验证joinToString(CharSequence)对混合类型集合的连接。
     * 测试要求：输入为不同类型元素集合。
     * 测试功能：多类型连接。
     * 测试范围：混合类型集合。
     * 测试结果：返回元素toString拼接。
     */
    @Test
    @Order(8)
    void testJoinToStringWithDifferentTypes() {
        List<Object> list = Arrays.asList(1, "a", 2.0, null);
        String result = X.list(list).joinToString("-");
        assertEquals("1-a-2.0", result);
    }
}