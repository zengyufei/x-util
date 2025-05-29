package full.test;

import com.zyf.util.Pair;
import com.zyf.util.X;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Map;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ZipTest {

    @Test
    @Order(1)
    public void zip测试() {
        final List<Integer> list1 = X.listOf(1, 2, 3)
                .toList();
        final List<Integer> list2 = X.listOf(11, 22, 33)
                .toList();

        final List<Pair<Integer, Integer>> list = X.list(list1)
                .zip(list2)
                .toList();

        for (final Pair<Integer, Integer> pair : list) {
            System.out.println(pair.getFirst() + " == " + pair.getSecond());
        }

        final Map<Integer, Integer> map = X.list(list1)
                .zip(list2)
                .toMap();

        for (final Map.Entry<Integer, Integer> entry : map.entrySet()) {
            final Integer key = entry.getKey();
            final Integer value = entry.getValue();
            System.out.println(key + " == " + value);
        }

    }


}
