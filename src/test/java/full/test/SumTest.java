package full.test;

import com.zyf.util.X;
import full.test.entity.User;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Objects;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SumTest {

    @Test
    @Order(1)
    public void 年龄和int类型() {
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

        int jdk = jdkList.stream()
                .map(User::getAge)
                .filter(Objects::nonNull)
                .mapToInt(value -> Integer.parseInt(value.toString()))
                .sum();

        int my = X.list(myList)
                .filterNotNull(User::getAge)
                .map(User::getAge)
                .sumInt();

        assert my == jdk;
    }

    @Test
    @Order(2)
    public void 年龄和int类型v2() {
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

        int jdk = jdkList.stream()
                .map(User::getAge)
                .filter(Objects::nonNull)
                .mapToInt(value -> Integer.parseInt(value.toString()))
                .sum();

        int my = X.list(myList)
                .filterNotNull(User::getAge)
                .sumInt(User::getAge);

        assert my == jdk;
    }

    @Test
    @Order(3)
    public void error年龄和int() {
        final List<User> jdkList = X.listOf(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 17, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", null, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        Assertions.assertThrows(IllegalArgumentException.class, () -> X.list(jdkList)
                .filterNotNull(User::getAge)
                .map(User::getName)
                .sumInt());
    }

    @Test
    @Order(4)
    public void 年龄和double类型() {
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

        double jdk = jdkList.stream()
                .map(User::getAge)
                .filter(Objects::nonNull)
                .mapToDouble(value -> Double.parseDouble(value.toString()))
                .sum();

        Double my = X.list(myList)
                .filterNotNull(User::getAge)
                .map(User::getAge)
                .sumDouble();

        assert my == jdk;
    }

    @Test
    @Order(5)
    public void 年龄和double类型v2() {
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

        double jdk = jdkList.stream()
                .map(User::getAge)
                .filter(Objects::nonNull)
                .mapToDouble(value -> Double.parseDouble(value.toString()))
                .sum();

        double my = X.list(myList)
                .filterNotNull(User::getAge)
                .sumDouble(User::getAge);

        assert my == jdk;
    }

    @Test
    @Order(6)
    public void error年龄和double() {
        final List<User> jdkList = X.listOf(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 17, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", null, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        Assertions.assertThrows(IllegalArgumentException.class, () -> X.list(jdkList)
                .filterNotNull(User::getAge)
                .map(User::getName)
                .sumDouble());
    }


    @Test
    @Order(7)
    public void 年龄和long类型() {
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

        long jdk = jdkList.stream()
                .map(User::getAge)
                .filter(Objects::nonNull)
                .mapToLong(value -> Long.parseLong(value.toString()))
                .sum();

        long my = X.list(myList)
                .filterNotNull(User::getAge)
                .map(User::getAge)
                .sumLong();

        assert my == jdk;
    }

    @Test
    @Order(8)
    public void 年龄和long类型v2() {
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

        long jdk = jdkList.stream()
                .map(User::getAge)
                .filter(Objects::nonNull)
                .mapToLong(value -> Long.parseLong(value.toString()))
                .sum();

        long my = X.list(myList)
                .filterNotNull(User::getAge)
                .sumLong(User::getAge);

        assert my == jdk;
    }

    @Test
    @Order(9)
    public void error年龄和long() {
        final List<User> jdkList = X.listOf(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 17, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", null, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        Assertions.assertThrows(IllegalArgumentException.class, () -> X.list(jdkList)
                .filterNotNull(User::getAge)
                .map(User::getName)
                .sumLong());
    }


}
