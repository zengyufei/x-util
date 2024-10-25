package full.test;

import com.zyf.util.X;
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
    @Order(1)
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

}
