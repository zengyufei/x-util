package full.test;

import com.zyf.util.X;
import full.test.entity.User;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReduceTest {

    @Test
    @Order(1)
    public void reduce统计年龄和() {
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

        List<Integer> jdk = jdkList.stream()
                .map(User::getAge)
                .filter(Objects::nonNull)
                .reduce(new ArrayList<>(), (list, a) -> {
                    list.add(a);
                    return list;
                }, (o, o2) -> o);

        List<Integer> my = X.list(myList)
                .isNotNull(User::getAge)
                .map(User::getAge)
                .reduce(ArrayList::new, ArrayList::add);

        Assertions.assertEquals(jdk, my);
    }

    @Test
    @Order(2)
    public void reduce统计年龄和v2() {
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

        List<Integer> jdk = jdkList.stream()
                .map(User::getAge)
                .filter(Objects::nonNull)
                .reduce(new ArrayList<>(), (list, a) -> {
                    list.add(a);
                    return list;
                }, (o, o2) -> o);

        List<Integer> my = X.list(myList)
                .isNotNull(User::getAge)
                .reduce(ArrayList::new, User::getAge, ArrayList::add);

        for (Integer integer : my) {
            assert jdk.contains(integer);
        }
    }


}
