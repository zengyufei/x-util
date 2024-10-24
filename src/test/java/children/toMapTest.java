package children;

import children.entity.User;
import com.zyf.util.X;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class toMapTest {

    @Test
    @Order(1)
    public void toMap测试() {
        Map<String, User> jdk = Data.用户集合.stream()
                .collect(Collectors.toMap(User::getName, e -> e, (a, b) -> a));
        Map<String, User> my = X.list(Data.用户集合)
                .toMap(User::getName);

        for (Map.Entry<String, User> entry : my.entrySet()) {
            assert jdk.get(entry.getKey()).equals(entry.getValue());
        }
    }

    @Test
    @Order(1)
    public void toMap测试2() {
        Map<String, Integer> jdk = Data.用户集合.stream()
                .filter(e -> e.getAge() != null)
                .collect(Collectors.toMap(User::getName, User::getAge, (a, b) -> b));
        Map<String, Integer> my = X.list(Data.用户集合)
                .isNotNull(User::getAge)
                .toMap(User::getName, User::getAge);

        for (Map.Entry<String, Integer> entry : my.entrySet()) {
            assert jdk.get(entry.getKey()).equals(entry.getValue());
        }
    }


    @Test
    @Order(1)
    public void toMap测试3() {
        Map<String, Integer> jdk = Data.用户集合.stream()
                .filter(e -> e.getAge() != null)
                .collect(Collectors.toMap(User::getName, User::getAge, (a, b) -> a));
        Map<String, Integer> my = X.list(Data.用户集合)
                .isNotNull(User::getAge)
                .toMap(User::getName, User::getAge, (a, b) -> b);

        for (Map.Entry<String, Integer> entry : my.entrySet()) {
            assert jdk.get(entry.getKey()).equals(entry.getValue());
        }
    }


    @Test
    @Order(1)
    public void groupByMap测试() {
        Map<Integer, List<User>> jdk = Data.用户集合.stream()
                .filter(e -> e.getAge() != null)
                .collect(Collectors.groupingBy(User::getAge));
        Map<Integer, List<User>> my = X.list(Data.用户集合)
                .isNotNull(User::getAge)
                .groupBy(User::getAge)
                .toMap();

        for (Map.Entry<Integer, List<User>> entry : my.entrySet()) {
            assert jdk.get(entry.getKey()).equals(entry.getValue());
        }
    }

    @Test
    @Order(1)
    public void groupByMap测试2() {
        Map<Integer, List<String>> jdk = Data.用户集合.stream()
                .filter(e -> e.getAge() != null)
                .collect(Collectors.groupingBy(User::getAge, Collectors.mapping(User::getName, Collectors.toList())));
        Map<Integer, List<String>> my = X.list(Data.用户集合)
                .isNotNull(User::getAge)
                .groupBy(User::getAge, User::getName)
                .toMap();

        for (Map.Entry<Integer, List<String>> entry : my.entrySet()) {
            assert jdk.get(entry.getKey()).equals(entry.getValue());
        }
    }

}
