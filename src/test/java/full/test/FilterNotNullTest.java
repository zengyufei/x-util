package full.test;

import com.zyf.util.X;
import full.test.entity.User;
import org.junit.jupiter.api.*;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilterNotNullTest {

    @Test
    @Order(1)
    public void filterNotNull测试() {
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

        List<User> jdk = jdkList.stream()
                .filter(e -> e.getAge() != null)
                .toList();

        List<User> my = X.list(myList)
                .filterNotNull(User::getAge)
                .toList();

        Assertions.assertEquals(jdk, my);
    }

    @Test
    @Order(2)
    public void filterNotNull测试2() {
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

        List<User> jdk = jdkList.stream()
                .filter(e -> e.getAge() != null)
                .toList();

        List<User> my = X.list(myList)
                .isNotNull(User::getAge)
                .toList();

        Assertions.assertEquals(jdk, my);
    }


    @Test
    @Order(3)
    public void filterNotNull测试3() {
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

        List<User> jdk = jdkList.stream()
                .filter(e -> e.getAge() != null)
                .toList();

        List<User> my = X.list(myList)
                .isNotBlank(User::getAge)
                .toList();

        Assertions.assertEquals(jdk, my);
    }


}
