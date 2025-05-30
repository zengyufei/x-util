package single;

import com.zyf.util.Sort;
import com.zyf.util.X;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SortedTest {

    /**
     * 测试目的：验证sorted()对整数集合的自然升序排序。
     */
    @Test
    @Order(1)
    void testSortedNaturalOrder() {
        List<Integer> list = Arrays.asList(5, 3, 1, 4, 2);
        List<Integer> sorted = X.list(list).sorted().toList();
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), sorted);
    }

    /**
     * 测试目的：验证sorted()对空集合的处理。
     */
    @Test
    @Order(2)
    void testSortedEmptyList() {
        List<Integer> list = Collections.emptyList();
        List<Integer> sorted = X.list(list).sorted().toList();
        assertTrue(sorted.isEmpty());
    }

    /**
     * 测试目的：验证sorted()对null集合的处理。
     */
    @Test
    @Order(3)
    void testSortedNullList() {
        List<Object> sorted = X.list(null).sorted().toList();
        assertTrue(sorted.isEmpty());
    }

    /**
     * 测试目的：验证sorted()对已排序集合的幂等性。
     */
    @Test
    @Order(4)
    void testSortedAlreadySorted() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sorted = X.list(list).sorted().toList();
        assertEquals(list, sorted);
    }

    /**
     * 测试目的：验证sorted()对包含重复元素的集合排序。
     */
    @Test
    @Order(5)
    void testSortedWithDuplicates() {
        List<Integer> list = Arrays.asList(3, 1, 2, 3, 2);
        List<Integer> sorted = X.list(list).sorted().toList();
        assertEquals(Arrays.asList(1, 2, 2, 3, 3), sorted);
    }

    /**
     * 测试目的：验证sort(Comparator)自定义比较器排序。
     */
    @Test
    @Order(6)
    void testSortedWithComparator() {
        List<String> list = Arrays.asList("apple", "banana", "pear", "grape");
        List<String> sorted = X.list(list).sort(Comparator.comparingInt(String::length)).toList();
        assertEquals(Arrays.asList("pear", "apple", "grape", "banana"), sorted);
    }

    /**
     * 测试目的：验证sort(Comparator)降序排序。
     */
    @Test
    @Order(7)
    void testSortedWithComparatorDescending() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sorted = X.list(list).sort(Comparator.reverseOrder()).toList();
        assertEquals(Arrays.asList(5, 4, 3, 2, 1), sorted);
    }

    /**
     * 测试目的：验证sort(Function, Sort)按key升序/降序排序。
     */
    @Test
    @Order(8)
    void testSortedByKeyAscDesc() {
        List<String> list = Arrays.asList("a", "bbb", "cc");
        List<String> asc = X.list(list).sort(String::length, Sort.Asc).toList();
        List<String> desc = X.list(list).sort(String::length, Sort.Desc).toList();
        assertEquals(Arrays.asList("a", "cc", "bbb"), asc);
        assertEquals(Arrays.asList("bbb", "cc", "a"), desc);
    }

    /**
     * 测试目的：验证sort(Function, Sort, Sort)空值处理。
     */
    @Test
    @Order(9)
    void testSortedByKeyWithNulls() {
        List<String> list = Arrays.asList("a", null, "bb");
        List<String> nullLast = X.list(list).sort(s -> s, Sort.Asc, Sort.NullLast).toList();
        List<String> nullFirst = X.list(list).sort(s -> s, Sort.Asc, Sort.NullFirst).toList();
        assertEquals(Arrays.asList("a", "bb", null), nullLast);
        assertEquals(Arrays.asList(null, "a", "bb"), nullFirst);
    }

    /**
     * 测试目的：验证sortAsc/sortDesc按key排序。
     */
    @Test
    @Order(10)
    void testSortAscSortDesc() {
        List<String> list = Arrays.asList("a", "bbb", "cc");
        List<String> asc = X.list(list).sortAsc(String::length).toList();
        List<String> desc = X.list(list).sortDesc(String::length).toList();
        assertEquals(Arrays.asList("a", "cc", "bbb"), asc);
        assertEquals(Arrays.asList("bbb", "cc", "a"), desc);
    }

    /**
     * 测试目的：验证sort(Function<SortStream, Comparator>...)多重排序。
     */
    @Test
    @Order(11)
    void testSortedWithMultipleComparators() {
        List<String> list = Arrays.asList("ab", "a", "abc", "b");
        List<String> sorted = X.list(list).sort(
                s -> s.createComparator(String::length, Sort.Asc),
                s -> s.createComparator(e -> {
                    // 转换为 char
                    char ch = e.charAt(0);
                    // 获取数字值
                    int num = ch - '0';
                    System.out.println(num);
                    return num;
                }, Sort.Asc)
        ).toList();
        assertEquals(Arrays.asList("a", "b", "ab", "abc"), sorted);
    }

    /**
     * 测试目的：验证sorted()对对象集合的排序（实现Comparable）。
     */
    @Test
    @Order(12)
    void testSortedWithObjectsComparable() {
        class Person implements Comparable<Person> {
            String name;
            int age;

            Person(String name, int age) {
                this.name = name;
                this.age = age;
            }

            @Override
            public int compareTo(Person o) {
                return Integer.compare(this.age, o.age);
            }
        }
        List<Person> people = Arrays.asList(
                new Person("A", 30),
                new Person("B", 20),
                new Person("C", 40)
        );
        List<Person> sorted = X.list(people).sorted().toList();
        assertEquals(Arrays.asList("B", "A", "C"),
                Arrays.asList(sorted.get(0).name, sorted.get(1).name, sorted.get(2).name));
    }

    /**
     * 测试目的：验证sort(Comparator)对对象集合的排序（不实现Comparable）。
     */
    @Test
    @Order(13)
    void testSortedWithObjectsComparator() {
        class Person {
            String name;
            int age;

            Person(String name, int age) {
                this.name = name;
                this.age = age;
            }
        }
        List<Person> people = Arrays.asList(
                new Person("A", 30),
                new Person("B", 20),
                new Person("C", 40)
        );
        List<Person> sorted = X.list(people).sort(Comparator.comparingInt(p -> p.age)).toList();
        assertEquals(Arrays.asList("B", "A", "C"),
                Arrays.asList(sorted.get(0).name, sorted.get(1).name, sorted.get(2).name));
    }

    /**
     * 测试目的：验证sorted()对极值集合的排序。
     */
    @Test
    @Order(14)
    void testSortedWithExtremeValues() {
        int size = 10000;
        List<Integer> list = new ArrayList<>(size);
        for (int i = size; i > 0; i--) list.add(i);
        List<Integer> sorted = X.list(list).sorted().toList();
        for (int i = 0; i < size; i++) {
            assertEquals(i + 1, sorted.get(i));
        }
    }

    /**
     * 测试目的：验证sorted()对不同类型元素集合的异常处理。
     */
    @Test
    @Order(15)
    void testSortedWithDifferentTypes() {
        List<Object> list = Arrays.asList("x", 1, 2.0);
        assertThrows(ClassCastException.class, () -> X.list(list).sorted().toList());
    }
}