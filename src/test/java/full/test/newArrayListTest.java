package full.test;


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
    @Order(1)
    public void list创建2() {
        final List<Integer> jdk = Arrays.asList(2, 3, 4, 5);
        final List<Integer> my = X.list(1, 2, 3, 4)
                .map(e -> e + 1)
                .toList();
        Assertions.assertEquals(jdk, my);
    }


}
