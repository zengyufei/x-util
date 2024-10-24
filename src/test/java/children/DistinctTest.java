package children;

import children.entity.User;
import com.zyf.util.X;
import org.junit.jupiter.api.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DistinctTest {

    @Test
    @Order(1)
    public void distinct测试() {
        final List<User> jdkList = X.list(
                        new User("Alice", 20),
                        new User("Bob", 17),
                        new User("Yama", 17),
                        new User("Charlie", 19),
                        new User("David", null),
                        new User("Eve", 5)
                )
                .toList();

        final List<User> myList = X.clone(jdkList);

        List<String> jdk = jdkList.stream()
                .map(User::getName)
                .distinct()
                .toList();

        List<String> my = X.list(myList)
                .map(User::getName)
                .distinct()
                .toList();

        Assertions.assertEquals(jdk, my);
    }

    @Test
    @Order(2)
    public void distinct测试2() {
        final List<User> jdkList = X.list(
                        new User("Alice", 20),
                        new User("Bob", 17),
                        new User("Yama", 17),
                        new User("Alice", 19),
                        new User("David", null),
                        new User("Eve", 5)
                )
                .toList();

        final List<User> myList = X.clone(jdkList);

        final Set<String> set = new HashSet<>();
        List<User> jdk = jdkList.stream()
                .filter(e -> !set.contains(e.getName()))
                .peek(e -> set.add(e.getName()))
                .toList();

        List<User> my = X.list(myList)
                .distinct(User::getName)
                .toList();

        Assertions.assertEquals(jdk, my);
    }


}
