import com.zyf.util.X;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class TestX {


    // 主方法用于测试
    public static void main(String[] args) {
        // 测试数据
        List<User> userList = X.of(
                new User("Alice", 20),
                new User("Bob", 17),
                new User("Yama", 17),
                new User("Charlie", 19),
                new User("David", null),
                new User("Eve", 5)
        );

        List<Role> roleList = X.of(
                new Role("rootAdmin", 1),
                new Role("user", 2),
                new Role("rootUser", 3),
                new Role("admin", null),
                new Role("admin", 3)
        );

        // 1. 获取大于18岁的用户的年龄
        List<User> ages = X.l(userList)
                .filterNotNull(User::getAge)
                .filters(e -> e.name != null,
                        e -> e.age > 18)
                .list();
        System.out.println("1 用户年龄大于18岁：" + ages);

        // 1.1 获取大于18岁的用户的年龄
        System.out.println("1 用户年龄大于18岁：" + X.l(userList)
                .filterNotNull(User::getAge)
                .filterNotNull(User::getName)
                .gt(User::getAge, 18)
                .list());

        // 2. 按照年龄分组大于11岁的用户
        Map<Integer, List<User>> groupedByAge = X.l(userList)
                .filters(e -> e.age != null,
                        e -> e.age > 11)
                .groupBy(User::getAge)
                .map();
        System.out.println("2 按年龄分组（年龄大于11岁）的用户：" + groupedByAge);

        // 2.1 按年龄分组，只取姓名
        final Map<Integer, List<String>> map = X.l(userList)
                .filters(e -> e.age != null,
                        e -> e.age > 11)
                .groupBy(User::getAge)
                .mapValues(e -> e.map(User::getName))
                .map();
        System.out.println("2.1 按年龄分组，只取姓名: " + map);

        // 3. 过滤roleName开头以root，并按照seqNo转为map
        Map<Integer, String> roleMap = X.l(roleList)
                .filters(e -> e.roleName.startsWith("root"))
                .toMap(Role::getSeqNo, Role::getRoleName);
        System.out.println("3 以“root”开头的角色：" + roleMap);

        // 4. 获取大于18岁或小于7岁的用户的年龄
        List<Integer> ages2 = X.l(userList)
                .isNotNull(User::getAge)
                .ors(e -> e.age > 18,
                        e -> e.age < 7)
                .map(e -> e.age)
                .list();
        System.out.println("4 用户年龄在18岁以上或7岁以下：" + ages2);

        // 4.1 获取大于18岁或小于7岁的用户的年龄
        System.out.println("4.1 用户年龄在18岁以上或7岁以下：" + X.l(userList)
                .filterNotNull(User::getAge)
                .or(
                        e -> e.gt(User::getAge, 18),
                        e -> e.lt(User::getAge, 7)
                )
                .map(e -> e.age)
                .list());

        // 5. 过滤Role集合中roleName不为空，且seqNo不为空的用户
        List<Role> newRoleList1 = X.l(roleList)
                .isNotBlank(Role::getRoleName)
                .filterNotBlank(Role::getSeqNo)
                .list();
        System.out.println("5 具有非空角色名称（roleName）和序列号（seqNo）的角色：" + newRoleList1);

        // 6. 收集User集合中所有年龄，去重
        List<Integer> distinctAges = X.l(userList)
                .map(User::getAge)
                .distinct()
                .list();
        System.out.println("6 不同用户的年龄：" + distinctAges);

        // 7. 去掉User集合中名字重复的实体
        List<User> distinctUsers = X.l(userList)
                .distinct(User::getName)
                .list();
        System.out.println("7 按姓名区分的用户：" + distinctUsers);

        // 7.1 去掉User集合中名字重复的实体，并且名字后面增加123
        List<User> peekDistinctUsers = X.l(userList)
                .peek(e -> e.name = e.name + "123")
                .distinct(User::getName)
                .list();
        System.out.println("7.1 按姓名区分的用户，并且名字后面增加123：" + peekDistinctUsers);

        // 8. 对RoleList集合进行倒序排序，并且null值排最后
        List<Role> sortedRoles = X.l(roleList)
                .sort(Role::getSeqNo, X.Sort.Asc, X.Sort.NullLast)
                .list();
        System.out.println("8 排序后的角色，空值在最后：" + sortedRoles);

        // 9. 获取第一个年龄
        final Integer first = X.l(userList)
                .map(User::getAge)
                .findFirst();
        System.out.println("9 获取第一个年龄：" + first);

        // 10. 按照 roleName 转为 BeanMap
        Map<String, Role> roleMap2 = X.l(roleList)
                .toMap(Role::getRoleName);
        System.out.println("10 按照seqNo转为 BeanMap" + roleMap2);

        // 11. 统计
        long roleCount = X.l(roleList)
                .map(Role::getRoleName)
                .count();
        System.out.println("11. 统计" + roleCount);

        // 12. 跳过前几个
        List<Role> skipRole = X.l(roleList)
                .skip(1)
                .list();
        System.out.println("12. 跳过前几个" + skipRole);

        // 13. 只要前几个
        List<Role> subList = X.l(roleList)
                .sub(3)
                .list();
        System.out.println("13. 只要前几个" + subList);

        // 14. 自由截取
        List<Role> subList2 = X.l(roleList)
                .sub(1, 3)
                .list();
        System.out.println("14. 自由截取" + subList2);

        // 15. 反转list
        List<Role> reversedList = X.l(roleList)
                .reversed()
                .list();
        System.out.println("15. 反转list" + reversedList);
        System.out.println("15. 反转list校验" + roleList);


        final List<User> newList = X.l(userList)
                .peek(e -> {
                    if (e.age != null && e.age >= 17) {
                        e.name = e.name + "123";
                    }
                })
                .list();

        System.out.println("15.1 对比");
        X.diff(userList, newList, (oldUser, newUser) -> oldUser.name.equals(newUser.name))
                .addList(System.out::println)
                .updateList(System.out::println)
                .delList(System.out::println);


        System.out.println("15.2 不同对象对比");
        X.diff(roleList, userList,
                        (oldRole, newRole) -> oldRole.getRoleName().equals(newRole.getRoleName()),
                        (role, user) -> role.getRoleName().equals(user.getName())
                )
                .addList(System.out::println)
                .updateList(System.out::println)
                .delList(System.out::println);


        X.TryRun(() -> System.out.print("16. success "))
                .andThen(() -> System.out.print(" andThen next "))
                .onFailure(error -> System.out.println("failure" + error.getMessage()))
                .andFinally(() -> {
                    System.out.println(" finally! ");
                });

        System.out.print("17. 吞异常自行处理 ");
        X.Try(() -> 1 / 0)
                .andThen(r -> System.out.println("and then " + r))
                .onFailure(error -> System.out.print(" failure" + error.getMessage()))
                .andFinally(() -> {
                    System.out.println(" finally! ");
                });

        System.out.print("18. try-finally 结构 ");
        X.Try(() -> 1 / 0)
                .andThen(r -> System.out.print("and then " + r))
                .andFinally(() -> {
                    System.out.println(" finally! ");
                });

        System.out.print("19. 抛异常 ");
        X.Try(() -> 1 / 0)
                .andThen(r -> System.out.println("and then " + r))
                .onFailure(true)
                .andFinally(() -> {
                    System.out.println(" finally! ");
                });

    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    // User 类
    public final static class User implements Serializable {
        String name;
        Integer age;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    // Role 类
    public final static class Role implements Serializable {
        String roleName;
        Integer seqNo;
    }
}
