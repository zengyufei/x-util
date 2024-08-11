package children;

import children.entity.User;
import com.zyf.util.X;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Map;
import java.util.stream.Collectors;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class toMapTest {

    @Test
    @Order(1)
    public void toMap测试() {
        Map<String, User> jdk = Data.用户集合.stream()
                .collect(Collectors.toMap(User::getName, e -> e, (a, b) -> a));
        Map<String, User> my = X.l(Data.用户集合)
                .toMap(User::getName);

        for (Map.Entry<String, User> entry : my.entrySet()) {
            assert jdk.get(entry.getKey()).equals(entry.getValue());
        }
    }

}
