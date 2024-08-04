package children;

import children.entity.User;
import com.zyf.util.X;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Filters {

    @Test
    @Order(1)
    public void 过滤年龄不为空() {
        long jdk = Data.用户集合.stream()
                .filter(e -> e.getAge() != null)
                .count();
        long my = X.l(Data.用户集合)
                .filters(e -> e.getAge() != null)
                .count();
        assert my == jdk;
    }

    @Test
    @Order(2)
    public void 过滤年龄不为空v2() {
        long jdk = Data.用户集合.stream()
                .filter(e -> e.getAge() != null)
                .count();
        long my = X.l(Data.用户集合)
                .filterNotNull(User::getAge)
                .count();
        assert my == jdk;
    }

    @Test
    @Order(3)
    public void 过滤年龄不为空v3() {
        long jdk = Data.用户集合.stream()
                .filter(e -> e.getAge() != null)
                .count();
        long my = X.l(Data.用户集合)
                .isNotNull(User::getAge)
                .count();
        assert my == jdk;
    }

    @Test
    @Order(4)
    public void 过滤年龄不为空且年龄大于17() {
        long jdk = Data.用户集合.stream()
                .filter(e -> e.getAge() != null)
                .filter(e -> e.getAge() > 17)
                .count();
        long my = X.l(Data.用户集合)
                .filters(e -> e.getAge() != null,
                        e -> e.getAge() > 17)
                .count();
        assert my == jdk;
    }

    @Test
    @Order(5)
    public void 过滤年龄不为空且年龄大于17v2() {
        long jdk = Data.用户集合.stream()
                .filter(e -> e.getAge() != null)
                .filter(e -> e.getAge() > 17)
                .count();
        long my = X.l(Data.用户集合)
                .filterNotNull(User::getAge)
                .filters(e -> e.getAge() > 17)
                .count();
        assert my == jdk;
    }

    @Test
    @Order(6)
    public void 过滤年龄不为空且年龄大于17v3() {
        long jdk = Data.用户集合.stream()
                .filter(e -> e.getAge() != null)
                .filter(e -> e.getAge() > 17)
                .count();
        long my = X.l(Data.用户集合)
                .isNotNull(User::getAge)
                .filters(e -> e.getAge() > 17)
                .count();
        assert my == jdk;
    }

}
