package children;

import children.entity.Role;
import children.entity.User;
import com.zyf.util.X;

import java.util.List;

public class Data {

    // 测试数据
    public static List<User> 用户集合 = X.of(
            new User("Alice", 20),
            new User("Bob", 17),
            new User("Yama", 17),
            new User("Charlie", 19),
            new User("David", null),
            new User("Eve", 5)
    );

    public static List<Role> roleList = X.of(
            new Role("rootAdmin", 1),
            new Role("user", 2),
            new Role("rootUser", 3),
            new Role("admin", null),
            new Role("admin", 3)
    );
}
