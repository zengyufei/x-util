package children.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
// Role 类
public final class Role implements Serializable {
    String roleName;
    Integer seqNo;
}