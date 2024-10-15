package children;

import children.entity.Role;
import children.entity.User;
import com.zyf.util.X;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Map;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DiffTest {

    @Test
    @Order(1)
    public void diff同类型对比() {
        final List<User> newList = X.l(Data.用户集合)
                .peek(e -> e.setName(e.getName() + "123"),
                        e -> e.isNotNull(User::getAge),
                        e -> e.gt(User::getAge, 17))
                .list();

        X.diff(Data.用户集合, newList, (oldUser, newUser) -> oldUser.getName().equals(newUser.getName()))
                .addList(l -> {
                    assert l.size() == 2;
                })
                .updateList((l, map) -> {
                    for (Map.Entry<User, User> entry : map.entrySet()) {
                        final User left = entry.getKey();
                        final User right = entry.getValue();
                        System.out.println(left.getName() + "->" + right.getName());
                    }
                })
                .delList(l -> {
                    assert l.size() == 2;
                });
    }

    @Test
    @Order(2)
    public void diff不同类型对比() {
        X.diff2(Data.roleList, Data.用户集合,
                        (role, user) -> role.getRoleName().equals(user.getName())
                )
                .addList(l -> {
                    assert l.size() == 6;
                })
                .updateList((l, map) -> {
                    for (Map.Entry<Role, User> entry : map.entrySet()) {
                        final Role left = entry.getKey();
                        final User right = entry.getValue();
                        System.out.println(left.getRoleName() + "->" + right.getName());
                    }
                })
                .delList(l -> {
                    assert l.size() == 5;
                });

    }

}
