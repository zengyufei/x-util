package children;

import children.entity.User;
import com.zyf.util.X;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilterOrs {

    @Test
    @Order(1)
    public void 过滤年龄不为空且年龄大于18或小于7() {
        long jdk = Data.用户集合.stream()
                .filter(e -> e.getAge() != null && (e.getAge() > 18 || e.getAge() < 7))
                .count();
        long my = X.l(Data.用户集合)
                .isNotNull(User::getAge)
                .filterOrs(
                        e -> e.getAge() > 18,
                        e -> e.getAge() < 7
                )
                .count();
        assert my == jdk;
    }

    @Test
    @Order(2)
    public void 过滤年龄不为空且年龄大于18或小于7v2() {
        long jdk = Data.用户集合.stream()
                .filter(e -> e.getAge() != null && (e.getAge() > 18 || e.getAge() < 7))
                .count();
        long my = X.l(Data.用户集合)
                .isNotNull(User::getAge)
                .ors(
                        e -> e.getAge() > 18,
                        e -> e.getAge() < 7
                )
                .count();
        assert my == jdk;
    }

    @Test
    @Order(3)
    public void 过滤年龄不为空且年龄大于18或小于7v3() {
        long jdk = Data.用户集合.stream()
                .filter(e -> e.getAge() != null && (e.getAge() > 18 || e.getAge() < 7))
                .count();
        long my = X.l(Data.用户集合)
                .isNotNull(User::getAge)
                .or(
                        e -> e.gt(User::getAge, 18L),
                        e -> e.lt(User::getAge, 7)
                )
                .count();
        assert my == jdk;
    }

    @Test
    @Order(4)
    public void 过滤年龄不为空且年龄大于18或小于7v4() {
        long jdk = Data.用户集合.stream()
                .filter(e -> e.getAge() != null && (e.getAge() > 18 || e.getAge() < 7))
                .count();
        long my = X.l(Data.用户集合)
                .isNotNull(User::getAge)
                .filterOr(
                        e -> e.gt(User::getAge, 18L),
                        e -> e.lt(User::getAge, 7)
                )
                .count();
        assert my == jdk;
    }

}
