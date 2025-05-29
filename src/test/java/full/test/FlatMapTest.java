package full.test;

import com.zyf.util.X;
import full.test.entity.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FlatMapTest {

    @Test
    @Order(1)
    public void flatMap测试() {
        List<List<Integer>> list = new ArrayList<>();
        list.add(X.asList(1, 2, 3, 4, 5));
        list.add(X.asList(6, 7, 8, 9, 10));
        list.add(X.asList(11, 12, 13, 14, 15));

        List<Integer> jdk = list.stream()
                .flatMap(Collection::stream)
                .toList();

        List<Integer> my = X.list(list)
                .flatMap(e -> e)
                .toList();

        for (int i = 0; i < jdk.size(); i++) {
            final Integer t1 = jdk.get(i);
            final Integer t2 = my.get(i);
            assert Objects.equals(t1, t2);
        }
    }

    @Test
    @Order(2)
    public void flatMap测试2() {
        List<List<List<Integer>>> list = new ArrayList<>();
        list.add(X.asList(X.asList(1, 2, 3, 4, 5), X.asList(6, 7, 8, 9, 10)));
        list.add(X.asList(X.asList(11, 12, 13, 14, 15), X.asList(16, 17, 18, 19, 20)));
        list.add(X.asList(X.asList(21, 22, 23, 24, 25), X.asList(26, 27, 28, 29, 30)));

        List<Integer> jdk = list.stream()
                .flatMap(Collection::stream)
                .flatMap(Collection::stream)
                .toList();

        List<Integer> my = X.list(list)
                .flatMap(e -> e)
                .flatMap(e -> e)
                .toList();

        for (int i = 0; i < jdk.size(); i++) {
            final Integer t1 = jdk.get(i);
            final Integer t2 = my.get(i);
            assert Objects.equals(t1, t2);
        }
    }

    @Test
    @Order(3)
    public void flatMap测试3() {
        List<List<User>> list = new ArrayList<>();
        list.add(X.asList(
                new User("Alice", 20, 168),  // 有变动
                new User("Bob", 17, 178),
                new User("Yama", 17, 201)));
        list.add(X.asList(
                new User("Charlie", 19, 155), // 有变动
                new User("David", null, 158),
                new User("Eve", 5, 158)));

        List<Integer> jdk = list.stream()
                .flatMap(Collection::stream)
                .map(User::getAge)
                .toList();

        List<Integer> my = X.list(list)
                .flatMap(e -> e)
                .map(User::getAge)
                .toList();

        for (int i = 0; i < jdk.size(); i++) {
            final Integer t1 = jdk.get(i);
            final Integer t2 = my.get(i);
            assert Objects.equals(t1, t2);
        }
    }

}
