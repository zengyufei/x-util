package full.test;


import com.zyf.util.X;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class newHashMapTest {
    @Test
    @Order(1)
    public void map创建() {
        final Map<Integer, String> jdk = new HashMap<>() {{
            put(1, "2");
            put(2, "3");
        }};
        final Map<Integer, String> my = X.asMap(1, "2");
        my.put(2, "3");
        Assertions.assertEquals(jdk, my);
    }

    @Test
    @Order(2)
    public void map创建v2() {
        final Map<Integer, String> jdk = new HashMap<>() {{
            put(1, "2");
            put(2, "3");
        }};
        final Map<Integer, String> my = X.map(1, "2")
                .put(2, "3")
                .toMap();
        Assertions.assertEquals(jdk, my);
    }

    @Test
    @Order(3)
    public void map创建v3() {
        final Map<Integer, String> jdk = new HashMap<>() {{
            put(1, "2");
            put(2, "3");
        }};
        final Map<Integer, String> my = X.map(1, "2")
                .put(2, "3")
                .hasKey(2, System.out::println)
                .hasKey(4, "4", System.out::println)
                .toMap();
        Assertions.assertEquals(jdk, my);
    }


}
