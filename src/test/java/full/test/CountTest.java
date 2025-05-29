package full.test;

import com.zyf.util.X;
import full.test.entity.User;
import org.junit.jupiter.api.*;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CountTest {

    @Test
    @Order(1)
    public void count测试() {
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

        long jdk = jdkList.size();

        long my = X.list(myList)
                .count();

        Assertions.assertEquals(jdk, my);
    }


}
