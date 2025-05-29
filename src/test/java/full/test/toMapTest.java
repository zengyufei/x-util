package full.test;

import com.zyf.util.X;
import full.test.entity.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class toMapTest {

    @Test
    @Order(1)
    public void toMap测试() {
        final List<User> jdkList = X.listOf(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 17, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", null, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        final List<User> myList = X.clone(jdkList);

        Map<String, User> jdk = jdkList.stream()
                .collect(Collectors.toMap(User::getName, e -> e, (a, b) -> a));

        Map<String, User> my = X.list(myList)
                .toMap(User::getName);

        for (Map.Entry<String, User> entry : my.entrySet()) {
            assert jdk.get(entry.getKey()).equals(entry.getValue());
        }
    }

    @Test
    @Order(2)
    public void toMap测试2() {
        final List<User> jdkList = X.listOf(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 17, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", null, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        final List<User> myList = X.clone(jdkList);

        Map<String, Integer> jdk = jdkList.stream()
                .filter(e -> e.getAge() != null)
                .collect(Collectors.toMap(User::getName, User::getAge, (a, b) -> b));

        Map<String, Integer> my = X.list(myList)
                .isNotNull(User::getAge)
                .toMap(User::getName, User::getAge);

        for (Map.Entry<String, Integer> entry : my.entrySet()) {
            assert jdk.get(entry.getKey()).equals(entry.getValue());
        }
    }


    @Test
    @Order(3)
    public void toMap测试3() {
        final List<User> jdkList = X.listOf(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 17, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", null, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        final List<User> myList = X.clone(jdkList);

        Map<String, Integer> jdk = jdkList.stream()
                .filter(e -> e.getAge() != null)
                .collect(Collectors.toMap(User::getName, User::getAge, (a, b) -> a));

        Map<String, Integer> my = X.list(myList)
                .isNotNull(User::getAge)
                .toMap(User::getName, User::getAge, (a, b) -> b);

        for (Map.Entry<String, Integer> entry : my.entrySet()) {
            assert jdk.get(entry.getKey()).equals(entry.getValue());
        }
    }


    @Test
    @Order(4)
    public void toMap测试4() {
        final List<User> jdkList = X.listOf(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 17, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", null, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        final List<User> myList = X.clone(jdkList);

        Map<String, Integer> jdk = jdkList.stream()
                .filter(e -> e.getAge() != null)
                .collect(Collectors.toMap(User::getName, User::getAge, (a, b) -> a, LinkedHashMap::new));

        Map<String, Integer> my = X.list(myList)
                .isNotNull(User::getAge)
                .toLinkedMap(User::getName, User::getAge);

        for (Map.Entry<String, Integer> entry : my.entrySet()) {
            assert jdk.get(entry.getKey()).equals(entry.getValue());
        }
    }


}
