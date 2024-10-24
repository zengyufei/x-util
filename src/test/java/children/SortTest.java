package children;

import children.entity.User;
import com.zyf.util.X;
import org.junit.jupiter.api.*;

import java.util.Comparator;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SortTest {

    @Test
    @Order(1)
    public void sort测试() {
        final List<User> jdkList = X.list(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 31, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", 31, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        final List<User> myList = X.clone(jdkList);

        List<Integer> jdk = jdkList.stream()
                .map(User::getAge)
                .sorted(Integer::compareTo)
                .toList();

        List<Integer> my = X.list(myList)
                .map(User::getAge)
                .sort(Integer::compareTo)
                .toList();

        Assertions.assertEquals(jdk, my);
    }

    @Test
    @Order(2)
    public void sort测试2() {
        final List<User> jdkList = X.list(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 31, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", 42, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        final List<User> myList = X.clone(jdkList);

        List<User> jdk = jdkList.stream()
                .sorted(Comparator.comparing(User::getAge))
                .toList();

        List<User> my = X.list(myList)
                .sort(Comparator.comparing(User::getAge))
                .toList();

        Assertions.assertEquals(jdk, my);
    }


    @Test
    @Order(2)
    public void sort测试3() {
        final List<User> jdkList = X.list(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 31, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", null, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        final List<User> myList = X.clone(jdkList);

        List<User> jdk = jdkList.stream()
                .sorted(Comparator.nullsFirst((a, b) -> {
                    if (b.getAge() == null) {
                        return -1;
                    } else if (a.getAge() == null) {
                        return -1;
                    }
                    return b.getAge().compareTo(a.getAge());
                }))
                .toList();

        List<User> my = X.list(myList)
                .sort(User::getAge, X.Sort.Desc, X.Sort.NullFirst)
                .toList();

        Assertions.assertEquals(jdk, my);
    }


    @Test
    @Order(2)
    public void sort测试4() {
        final List<User> jdkList = X.list(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 17, 201),
                        new User("Charlie", 17, 155), // 有变动
                        new User("David", 17, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        final List<User> myList = X.clone(jdkList);

        Comparator<User> comparator = Comparator.nullsFirst((a, b) -> {
            if (b.getAge() == null) {
                return -1;
            } else if (a.getAge() == null) {
                return -1;
            }
            return b.getAge().compareTo(a.getAge());
        });
        Comparator<User> comparator2 = Comparator.nullsFirst((a, b) -> {
            if (b.getHeight() == null) {
                return -1;
            } else if (a.getHeight() == null) {
                return -1;
            }
            return b.getHeight().compareTo(a.getHeight());
        });

        List<User> jdk = jdkList.stream()
                .sorted(comparator.thenComparing(comparator2))
                .toList();

        List<User> my = X.list(myList)
                .sort(
                        s -> s.createComparator(User::getAge, X.Sort.Desc, X.Sort.NullLast),
                        s -> s.createComparator(User::getHeight, X.Sort.Desc, X.Sort.NullLast)
                )
                .toList();

        Assertions.assertEquals(jdk, my);

    }


}
