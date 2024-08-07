package children;

import children.entity.User;
import com.zyf.util.X;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SumTest {

    @Test
    @Order(1)
    public void 年龄和int类型() {
        int jdk = Data.用户集合.stream()
                .filter(e -> e.getAge() != null)
                .map(User::getAge)
                .mapToInt(value -> Integer.parseInt(value.toString()))
                .sum();
        int my = X.l(Data.用户集合)
                .filterNotNull(User::getAge)
                .map(User::getAge)
                .sumInt();
        assert my == jdk;
    }

    @Test
    @Order(2)
    public void 年龄和int类型v2() {
        int jdk = Data.用户集合.stream()
                .filter(e -> e.getAge() != null)
                .map(User::getAge)
                .mapToInt(value -> Integer.parseInt(value.toString()))
                .sum();
        int my = X.l(Data.用户集合)
                .filterNotNull(User::getAge)
                .sumInt(User::getAge);
        assert my == jdk;
    }

    @Test
    @Order(3)
    public void error年龄和int() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            X.l(Data.用户集合)
                    .filterNotNull(User::getAge)
                    .sumInt(User::getName);
        });
    }

    @Test
    @Order(4)
    public void 年龄和double类型() {
        double jdk = Data.用户集合.stream()
                .filter(e -> e.getAge() != null)
                .map(User::getAge)
                .mapToDouble(value -> Double.parseDouble(value.toString()))
                .sum();
        Double my = X.l(Data.用户集合)
                .filterNotNull(User::getAge)
                .map(User::getAge)
                .sumDouble();
        assert my == jdk;
    }

    @Test
    @Order(5)
    public void 年龄和double类型v2() {
        double jdk = Data.用户集合.stream()
                .filter(e -> e.getAge() != null)
                .map(User::getAge)
                .mapToDouble(value -> Double.parseDouble(value.toString()))
                .sum();
        Double my = X.l(Data.用户集合)
                .filterNotNull(User::getAge)
                .sumDouble(User::getAge);
        assert my == jdk;
    }

    @Test
    @Order(6)
    public void error年龄和double() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            X.l(Data.用户集合)
                    .filterNotNull(User::getAge)
                    .sumDouble(User::getName);
        });
    }


    @Test
    @Order(7)
    public void 年龄和long类型() {
        long jdk = Data.用户集合.stream()
                .filter(e -> e.getAge() != null)
                .map(User::getAge)
                .mapToLong(value -> Long.parseLong(value.toString()))
                .sum();
        long my = X.l(Data.用户集合)
                .filterNotNull(User::getAge)
                .map(User::getAge)
                .sumLong();
        assert my == jdk;
    }

    @Test
    @Order(8)
    public void 年龄和long类型v2() {
        long jdk = Data.用户集合.stream()
                .filter(e -> e.getAge() != null)
                .map(User::getAge)
                .mapToLong(value -> Long.parseLong(value.toString()))
                .sum();
        long my = X.l(Data.用户集合)
                .filterNotNull(User::getAge)
                .sumLong(User::getAge);
        assert my == jdk;
    }

    @Test
    @Order(9)
    public void error年龄和long() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            X.l(Data.用户集合)
                    .filterNotNull(User::getAge)
                    .sumLong(User::getName);
        });
    }


}
