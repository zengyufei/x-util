package children;

import com.zyf.util.X;
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
        X.list(Data.用户集合)
                .split(1, list -> {
                    assert list.size() == 1;
                });
    }

    @Test
    @Order(1)
    public void 切割list2() {
        X.list(Data.用户集合)
                .split(2, list -> {
                    assert list.size() == 2;
                });
    }

    @Test
    @Order(1)
    public void 切割list3() {
        X.list(Data.用户集合)
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
