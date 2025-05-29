package full.test;

import com.zyf.util.X;
import full.test.entity.User;
import org.junit.jupiter.api.*;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnyMatchTest {

    @Test
    @Order(1)
    public void anyMatch测试() {
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

        boolean jdk = jdkList.stream()
                .anyMatch(e -> e.getAge() > 19);

        boolean my = X.list(myList)
                .anyMatch(e -> e.getAge() > 19);

        Assertions.assertEquals(jdk, my);
    }

    @Test
    @Order(1)
    public void anyMatch测试2() {
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

        boolean jdk = jdkList.stream()
                .anyMatch(e -> e.getAge() > 119);

        boolean my = X.list(myList)
                .anyMatch(e -> e.getAge() > 119);

        Assertions.assertEquals(jdk, my);
    }


}
