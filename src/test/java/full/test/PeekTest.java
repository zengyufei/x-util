package full.test;

import com.zyf.util.X;
import full.test.entity.User;
import org.junit.jupiter.api.*;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PeekTest {

    @Test
    @Order(1)
    public void peek测试() {
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
        final List<User> jdkList = X.listOf(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 17, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", 31, 158),
                        new User("Eve", 5, 158)
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
