package children;

import com.zyf.util.X;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class newArrayListTest {

    @Test
    @Order(1)
    public void list创建() {
        final List<Integer> jdk = Arrays.asList(1, 2, 3, 4);
        final List<Integer> my = X.asList(1, 2, 3, 4);
        Assertions.assertEquals(jdk, my);
    }

    @Test
    @Order(2)
    public void list创建v2() {
        final List<Integer> jdk = Arrays.asList(1, 2, 3, 4);
        final List<Integer> my = X.warpperList(2, 3, 4, 5)
                .map(e -> e - 1)
                .list();
        Assertions.assertEquals(jdk, my);
    }


}
