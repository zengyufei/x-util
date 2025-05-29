package full.test;

import com.zyf.util.X;
import full.test.entity.Role;
import full.test.entity.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DiffTest {

    @Test
    @Order(1)
    public void diff同类型对比() {
        final List<User> oldList = X.listOf(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 17, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", null, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        final List<User> newList = X.listOf(
                        new User("Alice123", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 17, 201),
                        new User("Charlie123", 19, 155), // 有变动
                        new User("David", null, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        X.getDiff(oldList, newList, (oldUser, newUser) -> oldUser.getName().equals(newUser.getName()))
                .addConsumer((t, l) -> {
                    assert l.size() == 2;
                    List<String> addNames = Arrays.asList("Alice123", "Charlie123");
                    assert l.stream().allMatch(user -> addNames.contains(user.getName()));
                })
                .updateConsumer((l, map) -> {
                    assert map.size() == 4;
                    List<String> oldUpdateNames = Arrays.asList("Bob", "Yama", "David", "Eve");
                    for (Map.Entry<User, User> entry : map.entrySet()) {
                        final User left = entry.getKey();
                        final User right = entry.getValue();
                        System.out.println(left + "->" + right);
                        assert oldUpdateNames.contains(left.getName());
                    }
                })
                .delConsumer((t, l) -> {
                    assert l.size() == 2;
                    List<String> delNames = Arrays.asList("Alice", "Charlie");
                    assert l.stream().allMatch(user -> delNames.contains(user.getName()));
                });
    }

    @Test
    @Order(2)
    public void diff不同类型对比() {
        final String someName = "张三";
        final List<User> userList = X.listOf(
                        new User(someName, 20, 168),  // 名字相同
                        new User("Bob", 17, 178),
                        new User("Yama", 17, 201),
                        new User("Charlie", 19, 155),
                        new User("David", null, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        final List<Role> roleList = X.asList(
                new Role(someName, 1), // 名字相同
                new Role("user", 2),
                new Role("rootUser", 3),
                new Role("admin", null),
                new Role("admin", 3)
        );

        X.getDiff2(roleList, userList,
                        (role, user) -> role.getRoleName().equals(user.getName())
                )
                .addConsumer((t, l) -> {
                    assert l.size() == 5;
                    List<String> addNames = Arrays.asList("Bob", "Yama", "Charlie", "David", "Eve");
                    assert l.stream().allMatch(user -> addNames.contains(user.getName()));
                })
                .updateConsumer((l, map) -> {
                    assert map.size() == 1;
                    for (Map.Entry<Role, User> entry : map.entrySet()) {
                        final Role left = entry.getKey();
                        final User right = entry.getValue();
                        System.out.println(left + "->" + right);
                        assert someName.equals(left.getRoleName());
                        assert someName.equals(right.getName());
                    }
                })
                .delConsumer((t, l) -> {
                    assert l.size() == 4;
                    List<String> delNames = Arrays.asList("user", "rootUser", "admin");
                    assert l.stream().allMatch(role -> delNames.contains(role.getRoleName()));
                });

    }

}
