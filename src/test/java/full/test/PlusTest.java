package full.test;

import com.zyf.util.X;
import full.test.entity.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlusTest {

    @Test
    @Order(1)
    public void plus测试1() {

        final List<User> listA = X.listOf(
                        new User("Alice", 20, 168),
                        new User("Bob", 17, 178),
                        new User("Yama", 17, 201)
                )
                .toList();

        List<User> resultList = X.list(listA)
                .plus(new User("Charlie", 19, 155)) // listA + listB 并集为新 listAB
                .toList(); // 懒加载，只有 toList() 执行时，才会去执行 plus(listB) plus(listB)

        resultList.forEach(System.out::println);

    }


    @Test
    @Order(2)
    public void plus测试2() {

        final List<User> listA = X.listOf(
                        new User("Alice", 20, 168),
                        new User("Bob", 17, 178),
                        new User("Yama", 17, 201)
                )
                .toList();

        final List<User> listB = X.listOf(
                        new User("Charlie", 19, 155),
                        new User("David", 7, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        List<User> resultList = X.list(listA)
                .plus(listB) // listA + listB 并集为新 listAB
                .plus(listB) // listAB + listB 并集为新 listABB
                .toList(); // 懒加载，只有 toList() 执行时，才会去执行 plus(listB) plus(listB)

        resultList.forEach(System.out::println);

    }


}
