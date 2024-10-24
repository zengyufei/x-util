package children;

import children.entity.Role;
import children.entity.User;
import com.zyf.util.X;

import java.util.List;
import java.util.Map;

public class Data {

    public static final Map<String, List<Role>> grouptByMap = X
            .map("1", X.asList(new Role("rootAdmin", 1), new Role("user", 2)))
            .put("2", X.asList(new Role("rootUser", 3), new Role("admin", null)))
            .put("3", X.asList(new Role("admin", null), new Role("admin", 3)))
            .map();
    public static final List<String> map = X
            .list("rootAdmin")
            .add("user")
            .add("rootUser")
            .add("admin")
            .toList();
    // 测试数据
    public static List<User> 用户集合 = X.asList(
            new User("Alice", 20, 168),
            new User("Bob", 17, 178),
            new User("Yama", 17, 201),
            new User("Charlie", 19, 155),
            new User("David", null, 158),
            new User("Eve", 5, 158)
    );
    public static List<Role> roleList = X.asList(
            new Role("rootAdmin", 1),
            new Role("user", 2),
            new Role("rootUser", 3),
            new Role("admin", null),
            new Role("admin", 3)
    );

}
