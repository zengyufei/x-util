package full;

import com.zyf.util.X;
import full.test.entity.Role;
import full.test.entity.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class XTest {

    public static void main(String[] args) {

        final List<User> jdkList = X.list(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", null, 158),
                        new User("Eve", 5, 158),
                        new User("Jack", 5, 152)
                )
                .toList();
        final List<User> myList = X.clone(jdkList);

        X.list(myList).anyMatch(e -> e.getAge() > 19);
        X.list(myList).noneMatch(e -> e.getAge() > 119);

        final User clone = X.clone(myList.get(0));

        X.list(myList).count();

        X.list(myList)
                .map(User::getAge)
                .map(String::valueOf)
                .toList();

        X.list(jdkList)
                .concat(X.asList(new User("Yama", 17, 201)))
                .toList();

        X.list(myList)
                .isNotNull(User::getAge)
                .map(User::getName)
                .toList();
        X.list(myList)
                .isNotBlank(User::getAge)
                .map(User::getName)
                .toList();
        X.list(myList)
                .filterNotBlank(User::getAge)
                .map(User::getName)
                .toList();

        X.list(myList)
                .isNull(User::getAge)
                .map(User::getName)
                .toList();
        X.list(myList)
                .isBlank(User::getAge)
                .map(User::getName)
                .toList();
        X.list(myList)
                .filterBlank(User::getAge)
                .map(User::getName)
                .toList();

        X.list(myList)
                .distinct(User::getName)
                .toList();

        X.list(myList)
                .filterOrs(
                        e -> "Alice".equals(e.getName()),
                        e -> "Bob".equals(e.getName())
                )
                .count();

        X.list(myList)
                .filters(e -> e.getAge() != null,
                        e -> e.getAge() > 17)
                .count();

        X.list(myList)
                .findFirst();

        List<List<Integer>> list = new ArrayList<>();
        list.add(X.asList(1, 2, 3, 4, 5));
        list.add(X.asList(6, 7, 8, 9, 10));
        list.add(X.asList(11, 12, 13, 14, 15));

        X.list(list)
                .flatMap(e -> e)
                .toList();

        X.list(myList)
                .forEach(e -> System.out.println(e.getAge()));


        X.list(myList)
                .groupBy(User::getAge, User::getName)
                .toMap();


        X.list(myList)
                .groupBy(User::getAge)
                .valueStream(e -> e.map(User::getName).toList())
                .toMap();

        X.list(myList)
                .map(User::getName)
                .joining(",");

        X.list(myList)
                .limit(3)
                .toList();

        X.asList(1, 2, 3, 4);

        X.list(1, 2, 3, 4)
                .map(e -> e + 1)
                .toList();

        final Map<Integer, String> my = X.asMap(1, "2");

        final Map<Integer, String> my2 = X.map(1, "2")
                .put(2, "3")
                .toMap();

        final Map<Integer, String> my3 = X.map(1, "2")
                .put(2, "3")
                .hasKey(2, System.out::println) // 2
                .hasKey(4, "4", System.out::println) // 4
                .toMap();

        X.Op<User> op = X.op(myList.get(0));
        op.isNotBlank(user -> {
            System.out.println("my isNotBlank");
        });
        op.isNotBlankOrElse(user -> {
            System.out.println("my isNotBlankOrElse isNotBlank");
        }, () -> {
            System.out.println("my isNotBlankOrElse else");
        });
        if (op.isNotBlank()) {
            User my4 = op.get();
            System.out.println("my isNotBlank");
        }
        op.isBlank(user -> {
            System.out.println("my isBlank " + user);
        });
        op.isBlankOrElse(user -> {
            System.out.println("my isBlankOrElse isBlank " + user);
        }, () -> {
            System.out.println("my isBlankOrElse else");
        });
        if (op.isBlank()) {
            User my5 = op.get(jdkList.get(0));
            System.out.println("my isBlank get default value " + my);
        }
        String name = X.op(jdkList.get(0))
                .map(User::getName)
                .get();

        X.list(myList)
                .peek(e -> e.setName(e.getName() + "123"))
                .map(User::getName)
                .toList();

        List<String> my6 = X.list(myList)
                .peekStream(e -> e
                        .filters(t -> t.getAge() > 17)
                        .forEach(t -> t.setName(t.getName() + "123"))
                )
                .map(User::getName)
                .toList(); // 输出 myList 但是 t.getAge() > 17 的两个值被修改

        X.list(myList)
                .isNotNull(User::getAge)
                .map(User::getAge)
                .reduce(ArrayList::new, ArrayList::add);


        X.list(myList)
                .isNotNull(User::getAge)
                .reduce(ArrayList::new, User::getAge, ArrayList::add);


        X.list(myList)
                .sort(User::getAge, X.Sort.Asc)
                .reversed()
                .toList();

        X.list(myList)
                .skip(3)
                .toList();

        X.list(myList)
                .sortDesc(User::getAge)
                .map(User::getAge)
                .toList();
        X.list(myList)
                .sortAsc(User::getAge)
                .map(User::getAge)
                .toList();

        X.list(myList)
                .sort(
                        s -> s.createComparator(User::getAge, X.Sort.Desc, X.Sort.NullLast),
                        s -> s.createComparator(User::getHeight, X.Sort.Desc, X.Sort.NullLast)
                )
                .toList();

        X.list(myList)
                .sort(User::getAge, X.Sort.Desc, X.Sort.NullFirst)
                .toList();

        X.list(myList)
                .sort(Comparator.comparing(User::getAge))
                .toList();

        X.list(myList)
                .map(User::getAge)
                .sort(Integer::compareTo)
                .toList();

        X.list(jdkList)
                .split(1, l -> {
                    assert l.size() == 1;
                });

        X.list(myList)
                .sub(3)
                .toList();

        X.list(myList)
                .sub(2, 4)
                .toList();

        X.list(myList)
                .filterNotNull(User::getAge)
                .map(User::getAge)
                .sumInt();
        X.list(myList)
                .filterNotNull(User::getAge)
                .sumInt(User::getAge);
        X.list(jdkList)
                .filterNotNull(User::getAge)
                .map(User::getName)
                .sumInt();
        X.list(myList)
                .filterNotNull(User::getAge)
                .map(User::getAge)
                .sumDouble();
        X.list(myList)
                .filterNotNull(User::getAge)
                .sumDouble(User::getAge);
        X.list(jdkList)
                .filterNotNull(User::getAge)
                .map(User::getName)
                .sumDouble();
        X.list(myList)
                .filterNotNull(User::getAge)
                .map(User::getAge)
                .sumLong();
        X.list(myList)
                .filterNotNull(User::getAge)
                .sumLong(User::getAge);
        X.list(jdkList)
                .filterNotNull(User::getAge)
                .map(User::getName)
                .sumLong();

        final Map<String, User> map = X.list(myList)
                .toMap(User::getName);
        final Map<String, Integer> map1 = X.list(myList)
                .isNotNull(User::getAge)
                .toMap(User::getName, User::getAge);
        Map<String, Integer> map2 = X.list(myList)
                .isNotNull(User::getAge)
                .toMap(User::getName, User::getAge, (a, b) -> b);
        Map<String, Integer> map3 = X.list(myList)
                .isNotNull(User::getAge)
                .toLinkedMap(User::getName, User::getAge);

        X.tryBegin(() -> {
                    System.out.println("success");
                    return "123";
                })
                .andThen(() -> {
                    System.out.println("success2");
                })
                .andThen(val -> {
                    System.out.println(val);
                })
                .andFinally(() -> {
                    System.out.println("finally");
                })
                .onFailure(throwable -> {
                    System.out.println("error");
                })
                .get().equals("123");

        X.tryBegin(() -> {
                    System.out.println("success");
                    return "123";
                })
                .andThen(() -> {
                    throw new RuntimeException("1213");
                })
                .andThen(() -> {
                    System.out.println("success2");
                })
                .andThen(val -> {
                    System.out.println(val);
                })
                .andThen(() -> {
                    throw new RuntimeException("1213");
                })
                .andFinally(() -> {
                    System.out.println("finally");
                })
                .onFailure(throwable -> {
                    System.out.println("error");
                })
                .isFailure();


        X.getDiff(jdkList, myList, (oldUser, newUser) -> oldUser.getName().equals(newUser.getName()))
                .addConsumer((t, l) -> {
                    assert l.size() == 2;
                })
                .updateConsumer((l, changeMap) -> {
                    assert changeMap.size() == 4;
                })
                .delConsumer((t, l) -> {
                    assert l.size() == 2;
                });


        final List<Role> roleList = X.asList(
                new Role("Alice", 1), // 名字相同
                new Role("user", 2),
                new Role("rootUser", 3),
                new Role("admin", null),
                new Role("admin", 3)
        );

        X.getDiff2(roleList, jdkList,
                        (role, user) -> role.getRoleName().equals(user.getName())
                )
                .addConsumer((t, l) -> {
                    assert l.size() == 5;
                })
                .updateConsumer((l, changeMap) -> {
                    assert changeMap.size() == 1;
                })
                .delConsumer((t, l) -> {
                    assert l.size() == 4;
                });

        // 超长链式调用
        Map<Integer, List<String>> result = X.list(myList)
                .filter(user -> user.getAge() != null && user.getAge() > 10) // 过滤年龄大于10
                .isNotNull(User::getAge)
                .isNull(User::getAge)
                .ors(e -> e.getAge() > 10,
                        e -> e.getAge() < 100)
                .ands(e -> e.getAge() > 10,
                        e -> e.getAge() < 100)
                .skip(10)
                .sub(10)
                .sub(1, 10)
                .concat(X.asList(new User("zs", 50, 167)))
                .add(new User("zs", 50, 167))
                .reversed()
                .map(User::getName) // 获取名字
                .distinct() // 去重
                .sort(Comparator.naturalOrder()) // 排序
                .peek(n -> System.out.println("Processing name: " + n))
                .map(String::toUpperCase) // 转换为大写
                .filter(n -> n.startsWith("A")) // 过滤以"A"开头的名字
                .limit(5) // 限制结果数量
                .map(n -> new User(n, 50, 167))
                .groupBy(User::getAge)
                .valueStream(e -> e.map(User::getName).toList())
                .toMap();

    }


}
