package full.test;

import com.zyf.util.X;
import full.test.entity.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConcatTest {

    @Test
    @Order(1)
    public void concat测试() {
        final List<User> list = X.asList(new User("Alice", 20, 168));

        List<User> my = X.list(list)
                .toList();
        assert my.size() == 1;

        List<User> my2 = X.list(list)
                .addAll(X.asList(new User("Bob", 17, 178)))
                .toList();
        assert my.size() == 2;
        assert my2.size() == 2;

        List<User> my3 = X.list(list)
                .concat(X.asList(new User("Yama", 17, 201)))
                .toList();
        assert my.size() == 3;
        assert my2.size() == 3;
        assert my3.size() == 3;

    }


}
