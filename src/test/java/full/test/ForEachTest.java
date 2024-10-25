package full.test;

import com.zyf.util.X;
import full.test.entity.User;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ForEachTest {

    @Test
    @Order(1)
    public void forEach测试() {
        final List<User> jdkList = X.list(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 17, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", null, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        final List<User> myList = X.clone(jdkList);

        List<Integer> jdkList2 = new ArrayList<>();
        List<Integer> myList2 = new ArrayList<>();

        jdkList.forEach(e -> {
            jdkList2.add(e.getAge());
        });

        X.list(myList)
                .forEach(e -> {
                    myList2.add(e.getAge());
                });

        Assertions.assertEquals(jdkList2, myList2);
    }


}
