package children;

import children.entity.User;
import com.zyf.util.X;
import org.junit.jupiter.api.*;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PeekTest {

    @Test
    @Order(1)
    public void peek测试() {
        final List<User> jdkList = X.list(
                        new User("Alice", 20),  // 有变动
                        new User("Bob", 17),
                        new User("Yama", 17),
                        new User("Charlie", 19), // 有变动
                        new User("David", null),
                        new User("Eve", 5)
                )
                .toList();

        final List<User> myList = X.clone(jdkList);

        List<String> jdk = jdkList.stream()
                .peek(e -> e.setName(e.getName() + "123"))
                .map(User::getName)
                .toList();

        List<String> my = X.list(myList)
                .peek(e -> e.setName(e.getName() + "123"))
                .map(User::getName)
                .toList();

        Assertions.assertEquals(jdk, my);
    }

    @Test
    @Order(2)
    public void peek测试2() {
        final List<User> jdkList = X.list(
                        new User("Alice", 20),  // 有变动
                        new User("Bob", 17),
                        new User("Yama", 17),
                        new User("Charlie", 19), // 有变动
                        new User("David", 55),
                        new User("Eve", 5)
                )
                .toList();

        final List<User> myList = X.clone(jdkList);

        List<String> jdk = jdkList.stream()
                .peek(e -> {
                    if (e.getAge() > 17) {
                        e.setName(e.getName() + "123");
                    }
                })
                .map(User::getName)
                .toList();

        List<String> my = X.list(myList)
                .peekStream(e -> e
                        .filters(t -> t.getAge() > 17)
                        .forEach(t -> t.setName(t.getName() + "123"))
                )
                .map(User::getName)
                .toList();

        Assertions.assertEquals(jdk, my);
    }


}
