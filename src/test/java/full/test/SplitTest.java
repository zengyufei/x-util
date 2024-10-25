package full.test;

import com.zyf.util.X;
import full.test.entity.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SplitTest {

    @Test
    @Order(1)
    public void 切割list1() {
        final List<User> jdkList = X.list(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 17, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", null, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        X.list(jdkList)
                .split(1, list -> {
                    assert list.size() == 1;
                });
    }

    @Test
    @Order(1)
    public void 切割list2() {
        final List<User> jdkList = X.list(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 17, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", null, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        X.list(jdkList)
                .split(2, list -> {
                    assert list.size() == 2;
                });
    }

    @Test
    @Order(1)
    public void 切割list3() {
        final List<User> jdkList = X.list(
                        new User("Alice", 20, 168),  // 有变动
                        new User("Bob", 17, 178),
                        new User("Yama", 17, 201),
                        new User("Charlie", 19, 155), // 有变动
                        new User("David", null, 158),
                        new User("Eve", 5, 158)
                )
                .toList();

        X.list(jdkList)
                .split(3, list -> {
                    assert list.size() == 3;
                });
    }

    @Test
    @Order(1)
    public void 切割list4() {
        final List<Integer> dataList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            dataList.add(i);
        }
        X.list(dataList)
                .split(500, list -> {
                    assert list.size() == 500;
                });
    }


    @Test
    @Order(1)
    public void 切割list5() {
        final List<Integer> dataList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            dataList.add(i);
        }
        X.list(dataList)
                .split(1000, list -> {
                    assert list.size() == 1000;
                });
    }


    @Test
    @Order(1)
    public void 切割list6() {
        final List<Integer> dataList = new ArrayList<>();
        for (int i = 0; i < 1001; i++) {
            dataList.add(i);
        }
        X.list(dataList)
                .split(1000, list -> {
                    assert (list.size() == 1000) || (list.size() == 1);
                });
    }

    @Test
    @Order(1)
    public void 切割list7() {
        final List<Integer> dataList = new ArrayList<>();
        for (int i = 0; i < 1002; i++) {
            dataList.add(i);
        }
        X.list(dataList)
                .split(1000, list -> {
                    assert (list.size() == 1000) || (list.size() == 2);
                });
    }

    @Test
    @Order(1)
    public void 切割list8() {
        final List<Integer> dataList = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            dataList.add(i);
        }
        X.list(dataList)
                .split(1000, list -> {
                    assert list.size() == 1000;
                });
    }

    @Test
    @Order(1)
    public void 切割list9() {
        final List<Integer> dataList = new ArrayList<>();
        for (int i = 0; i < 2002; i++) {
            dataList.add(i);
        }
        X.list(dataList)
                .split(1000, list -> {
                    assert (list.size() == 1000) || (list.size() == 2);
                });
    }


}
