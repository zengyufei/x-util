package full.test;

import com.zyf.util.X;
import full.test.entity.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PartitionEveryOneTest {

    @Test
    @Order(1)
    public void partitionEveryOne测试() {
        final List<User> list = X.listOf(
                        new User("Alice", 20, 168),
                        new User("Bob", 17, 178),
                        new User("Yama", 17, 201),
                        new User("Charlie", 19, 155),
                        new User("David", 7, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        List<List<User>> resultList = X.list(list)
                .partitionEveryOne(
                        e -> e.getAge() <= 18,
                        e -> e.getAge() > 18,
                        e -> e.getHeight() <= 170,
                        e -> e.getHeight() > 170
                );

        final List<User> 年龄小于等于18 = resultList.get(0);
        final List<User> 年龄大于18 = resultList.get(1);

        final List<User> 身高小于等于170 = resultList.get(2);
        final List<User> 身高大于170 = resultList.get(3);

        System.out.println("-------------------------------------------------");
        年龄小于等于18.forEach(e -> System.out.println("年龄小于等于18：" + e.getName() + " == " + e.getAge()));
        System.out.println("-------------------------------------------------");
        年龄大于18.forEach(e -> System.out.println("年龄大于18：" + e.getName() + " == " + e.getAge()));
        System.out.println("-------------------------------------------------");
        身高小于等于170.forEach(e -> System.out.println("身高小于等于170：" + e.getName() + " == " + e.getHeight()));
        System.out.println("-------------------------------------------------");
        身高大于170.forEach(e -> System.out.println("身高大于170：" + e.getName() + " == " + e.getHeight()));

    }


}
