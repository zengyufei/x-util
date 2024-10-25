package full.test;

import com.zyf.util.X;
import full.test.entity.User;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CloneTest {

    @Test
    @Order(1)
    public void clone测试() {
        final User user = new User("Alice", 20, 168);

        final User clone = X.clone(user);

        Assertions.assertNotSame(user, clone);
        Assertions.assertEquals(user, clone);
    }


}
