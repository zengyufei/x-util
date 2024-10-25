package full.test;

import com.zyf.util.X;
import full.test.entity.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FiltersTest {

    @Test
    @Order(1)
    public void 过滤年龄不为空() {
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
                .filter(e -> e.getAge() != null)
                .count();

        long my = X.list(myList)
                .filters(e -> e.getAge() != null)
                .count();

        assert my == jdk;
    }

    @Test
    @Order(2)
    public void 过滤年龄不为空v2() {
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
                .filter(e -> e.getAge() != null)
                .count();

        long my = X.list(myList)
                .filterNotNull(User::getAge)
                .count();

        assert my == jdk;
    }

    @Test
    @Order(3)
    public void 过滤年龄不为空v3() {
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
                .filter(e -> e.getAge() != null)
                .count();

        long my = X.list(myList)
                .isNotNull(User::getAge)
                .count();

        assert my == jdk;
    }

    @Test
    @Order(4)
    public void 过滤年龄不为空且年龄大于17() {
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
                .filter(e -> e.getAge() != null)
                .filter(e -> e.getAge() > 17)
                .count();

        long my = X.list(myList)
                .filters(e -> e.getAge() != null,
                        e -> e.getAge() > 17)
                .count();

        assert my == jdk;
    }

    @Test
    @Order(5)
    public void 过滤年龄不为空且年龄大于17v2() {
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
                .filter(e -> e.getAge() != null)
                .filter(e -> e.getAge() > 17)
                .count();

        long my = X.list(myList)
                .filterNotNull(User::getAge)
                .filters(e -> e.getAge() > 17)
                .count();

        assert my == jdk;
    }

    @Test
    @Order(6)
    public void 过滤年龄不为空且年龄大于17v3() {
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
                .filter(e -> e.getAge() != null)
                .filter(e -> e.getAge() > 17)
                .count();

        long my = X.list(myList)
                .isNotNull(User::getAge)
                .filters(e -> e.getAge() > 17)
                .count();

        assert my == jdk;
    }

    @Test
    @Order(7)
    public void 过滤年龄不为空且年龄大于17v4() {
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
                .filter(e -> e.getAge() != null)
                .filter(e -> e.getAge() > 17)
                .count();

        long my = X.list(myList)
                .ands(e -> e.getAge() != null,
                        e -> e.getAge() > 17)
                .count();

        assert my == jdk;
    }

}
