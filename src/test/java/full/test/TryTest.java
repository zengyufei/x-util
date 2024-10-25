package full.test;

import com.zyf.util.X;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TryTest {

    @Test
    @Order(1)
    public void try测试() {

        assert X.tryBegin(() -> {
            System.out.println("success");
            return true;
        }).isSuccess();

        assert X.tryBegin(() -> {
            throw new RuntimeException("1213");
        }).isFailure();

        assert X.tryRun(() -> {
            System.out.println("success");
        }).isSuccess();

        assert X.tryRun(() -> {
            throw new RuntimeException("1213");
        }).isFailure();

        assert X.trySupplier(() -> {
            System.out.println("success");
            return "1";
        }).isSuccess();

        assert X.trySupplier(() -> {
            throw new RuntimeException("1213");
        }).isFailure();

    }

    @Test
    @Order(2)
    public void try测试2() {

        assert X.tryBegin(() -> {
            System.out.println("success");
            return true;
        }).getCause() == null;

        assert X.tryBegin(() -> {
            throw new RuntimeException("1213");
        }).getCause() instanceof RuntimeException;

        assert X.tryRun(() -> {
            System.out.println("success");
        }).getCause() == null;

        assert X.tryRun(() -> {
            throw new RuntimeException("1213");
        }).getCause() instanceof RuntimeException;

        assert X.trySupplier(() -> {
            System.out.println("success");
            return "1";
        }).getCause() == null;

        assert X.trySupplier(() -> {
            throw new RuntimeException("1213");
        }).getCause() instanceof RuntimeException;


    }

    @Test
    @Order(3)
    public void try测试3() {

        assert X.tryBegin(() -> {
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

        assert X.tryBegin(() -> {
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

    }


}
