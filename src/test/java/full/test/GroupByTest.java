package full.test;

import com.zyf.util.ListStream;
import com.zyf.util.X;
import full.test.entity.User;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GroupByTest {

    @Test
    @Order(1)
    public void groupBy测试() {
        final List<User> jdkList = X.listOf(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 31, 201),
                        new User("Charlie", 17, 155), // 有变动
                        new User("David", 31, 158),
                        new User("Eve", 20, 158)
                )
                .toList();

        final List<User> myList = X.clone(jdkList);

        Map<Integer, List<User>> jdk = jdkList.stream()
                .collect(Collectors.groupingBy(User::getAge));

        Map<Integer, List<User>> my = X.list(myList)
                .groupBy(User::getAge)
                .toMap();

        Assertions.assertEquals(jdk, my);
    }

    @Test
    @Order(2)
    public void groupBy测试2() {
        final List<User> jdkList = X.listOf(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 31, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", 42, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        final List<User> myList = X.clone(jdkList);

        Map<Integer, List<String>> jdk = jdkList.stream()
                .collect(Collectors.groupingBy(User::getAge, Collectors.mapping(User::getName, Collectors.toList())));

        Map<Integer, List<String>> my = X.list(myList)
                .groupBy(User::getAge, User::getName)
                .toMap();

        Assertions.assertEquals(jdk, my);
    }


    @Test
    @Order(3)
    public void groupBy测试3() {
        final List<User> jdkList = X.listOf(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 31, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", 42, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        final List<User> myList = X.clone(jdkList);

        Map<Integer, List<String>> jdk = jdkList.stream()
                .collect(Collectors.groupingBy(User::getAge, Collectors.mapping(User::getName, Collectors.toList())));

        Map<Integer, List<String>> my = X.list(myList)
                .groupBy(User::getAge)
                .valueStream(e -> e.map(User::getName).toList())
                .toMap();

        Assertions.assertEquals(jdk, my);
    }


    @Test
    @Order(4)
    public void groupBy测试4() {
        final List<User> jdkList = X.listOf(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 31, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", 42, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        final List<User> myList = X.clone(jdkList);

        Map<Integer, Map<String, Long>> jdk = jdkList.stream()
                .collect(Collectors.groupingBy(User::getAge, Collectors.groupingBy(User::getName, Collectors.counting())));

        Map<Integer, Map<String, Long>> my = X.list(myList)
                .groupBy(User::getAge)
                .valueStream(e -> e.groupBy(User::getName).valueStream(ListStream::count).toMap())
                .toMap();

        Assertions.assertEquals(jdk, my);
    }


    @Test
    @Order(5)
    public void groupBy测试5() {
        final List<User> jdkList = X.listOf(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 31, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", 42, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        final List<User> myList = X.clone(jdkList);

        Map<Integer, Map<String, Long>> jdk = jdkList.stream()
                .collect(Collectors.groupingBy(User::getAge, Collectors.groupingBy(User::getName, Collectors.counting())));

        Map<Integer, Map<String, Long>> my = X.list(myList)
                .groupingBy(
                        User::getAge,
                        Collectors.groupingBy(User::getName, Collectors.counting())
                )
                .toMap();
        Assertions.assertEquals(jdk, my);
    }


}
