package children;

import children.entity.User;
import com.zyf.util.X;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SplitTest {

    @Test
    @Order(1)
    public void 切割list() {
        X.l(Data.用户集合)
                .split(2, list -> {
                    System.out.println("============================");
                    for (User user : list) {
                        System.out.println(user.getName());
                    }
                });
    }


}
