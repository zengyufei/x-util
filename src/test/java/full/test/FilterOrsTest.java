package full.test;

import com.zyf.util.X;
import full.test.entity.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilterOrsTest {

    @Test
    @Order(1)
    public void 过滤年龄不为空且年龄大于18或小于7() {
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

        long jdk = jdkList.stream()
                .filter(e -> e.getAge() != null && (e.getAge() > 18 || e.getAge() < 7))
                .count();

        long my = X.list(myList)
                .isNotNull(User::getAge)
                .filterOrs(
                        e -> e.getAge() > 18,
                        e -> e.getAge() < 7
                )
                .count();

        assert my == jdk;
    }

    @Test
    @Order(2)
    public void 过滤年龄不为空且年龄大于18或小于7v2() {
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

        long jdk = jdkList.stream()
                .filter(e -> e.getAge() != null && (e.getAge() > 18 || e.getAge() < 7))
                .count();

        long my = X.list(myList)
                .isNotNull(User::getAge)
                .ors(
                        e -> e.getAge() > 18,
                        e -> e.getAge() < 7
                )
                .count();

        assert my == jdk;
    }

}
