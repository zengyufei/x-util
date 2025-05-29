package full.test;

import com.zyf.util.X;
import full.test.entity.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MapNotNullTest {

    @Test
    @Order(1)
    public void mapNotNull测试() {
        final List<User> jdkList = X.listOf(
                        null,
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 17, 201),
                        null,
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", null, 158),
                        null,
                        new User("Eve", 5, 158)
                )
                .toList();

        final List<User> myList = X.clone(jdkList);

        List<Integer> my = X.list(myList)
                .map(User::getAge)
                .mapNotNull(e -> e)
                .toList();

        System.out.println(my);

    }


}
