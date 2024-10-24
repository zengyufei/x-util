package children;

import children.entity.User;
import com.zyf.util.X;
import org.junit.jupiter.api.*;

import java.util.Comparator;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReversedTest {

    @Test
    @Order(1)
    public void reversed测试() {
        final List<User> jdkList = X.list(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 10, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", 44, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        final List<User> myList = X.clone(jdkList);

        List<User> jdk = jdkList.stream()
                .sorted(Comparator.comparing(User::getAge).reversed())
                .toList();

        List<User> my = X.list(myList)
                .sort(User::getAge, X.Sort.Asc)
                .reversed()
                .toList();

        Assertions.assertEquals(jdk, my);
    }


}
