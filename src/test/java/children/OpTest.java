package children;

import children.entity.User;
import com.zyf.util.X;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OpTest {

    @Test
    @Order(1)
    public void op获取不为空() {
        User jdk = Data.用户集合.get(0);
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

        X.Op<User> op = X.op(jdk);
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
        Optional<User> optional = Optional.ofNullable(null);
        if (optional.isEmpty()) {
            System.out.println("jdk isEmpty");
        }

        X.Op<User> op = X.op(null);
        op.isBlank(user -> {
            System.out.println("my isBlank " + user);
        });
        op.isBlankOrElse(user -> {
            System.out.println("my isBlankOrElse isBlank " + user);
        }, () -> {
            System.out.println("my isBlankOrElse else");
        });
        if (op.isBlank()) {
            User my = op.get(Data.用户集合.get(0));
            System.out.println("my isBlank get default value " + my);
        }
    }

    @Test
    @Order(3)
    public void op获取属性() {
        User jdk = Data.用户集合.get(0);

        String name = X.op(jdk)
                .map(User::getName)
                .get();
        System.out.println(name);
    }


}
