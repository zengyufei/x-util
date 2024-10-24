package children;

import children.entity.User;
import com.zyf.util.X;
import org.junit.jupiter.api.*;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SkipTest {

    @Test
    @Order(1)
    public void peek测试() {
        final List<User> jdkList = X.list(
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
                .skip(3)
                .toList();

        List<User> my = X.list(myList)
                .skip(3)
                .toList();

        Assertions.assertEquals(jdk, my);
    }


}
