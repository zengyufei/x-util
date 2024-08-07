package children;

import children.entity.User;
import com.zyf.util.X;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReduceTest {

    @Test
    @Order(1)
    public void reduce统计年龄和() {
        List<Integer> jdk = Data.用户集合.stream()
                .map(User::getAge)
                .filter(Objects::nonNull)
                .reduce(new ArrayList<>(), (list, a) -> {
                    list.add(a);
                    return list;
                }, (o, o2) -> o);
        List<Integer> my = X.l(Data.用户集合)
                .isNotNull(User::getAge)
                .map(User::getAge)
                .reduce(ArrayList::new, ArrayList::add);
        Assertions.assertEquals(jdk, my);
    }

    @Test
    @Order(2)
    public void reduce统计年龄和v2() {
        List<Integer> jdk = Data.用户集合.stream()
                .map(User::getAge)
                .filter(Objects::nonNull)
                .reduce(new ArrayList<>(), (list, a) -> {
                    list.add(a);
                    return list;
                }, (o, o2) -> o);
        List<Integer> my = X.l(Data.用户集合)
                .isNotNull(User::getAge)
                .reduce(ArrayList::new, User::getAge, ArrayList::add);
        for (Integer integer : my) {
            assert jdk.contains(integer);
        }
    }



}
