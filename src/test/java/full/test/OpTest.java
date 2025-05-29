package full.test;

import com.zyf.util.Op;
import com.zyf.util.X;
import full.test.entity.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OpTest {

    @Test
    @Order(1)
    public void op获取不为空() {
        final List<User> jdkList = X.listOf(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 17, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", null, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        User jdk = jdkList.get(0);
        Optional<User> optional = Optional.ofNullable(jdk);
        optional.ifPresent(user -> {
            System.out.println("jdk ifPresent");
        });
        optional.ifPresentOrElse(user -> {
            System.out.println("jdk ifPresentOrElse ifPresent");
        }, () -> {
            System.out.println("jdk ifPresentOrElse else");
        });

        if (optional.isPresent()) {
            User user = optional.get();
            System.out.println("jdk isPresent");
        }

        Op<User> op = X.op(jdk);
        op.isNotBlank(user -> {
            System.out.println("my isNotBlank");
        });
        op.isNotBlankOrElse(user -> {
            System.out.println("my isNotBlankOrElse isNotBlank");
        }, () -> {
            System.out.println("my isNotBlankOrElse else");
        });
        if (op.isNotBlank()) {
            User my = op.get();
            System.out.println("my isNotBlank");
        }
    }

    @Test
    @Order(2)
    public void op获取为空() {
        final List<User> jdkList = X.listOf(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 17, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", null, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        Optional<User> optional = Optional.ofNullable(null);
        if (optional.isEmpty()) {
            System.out.println("jdk isEmpty");
        }

        Op<User> op = X.op(null);
        op.isBlank(user -> {
            System.out.println("my isBlank " + user);
        });
        op.isBlankOrElse(user -> {
            System.out.println("my isBlankOrElse isBlank " + user);
        }, () -> {
            System.out.println("my isBlankOrElse else");
        });
        if (op.isBlank()) {
            User my = op.get(jdkList.get(0));
            System.out.println("my isBlank get default value " + my);
        }
    }

    @Test
    @Order(3)
    public void op获取属性() {
        final List<User> jdkList = X.listOf(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 17, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", null, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        User jdk = jdkList.get(0);

        String name = X.op(jdk)
                .map(User::getName)
                .get();
        System.out.println(name);
    }


}
