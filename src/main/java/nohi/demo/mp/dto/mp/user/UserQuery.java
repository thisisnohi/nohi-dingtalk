package nohi.demo.mp.dto.mp.user;

import lombok.Data;

/**
 * <h3>nohi-dd-miniprogram-server</h3>
 *
 * @author NOHI
 * @description <p>User</p>
 * @date 2023/09/05 13:35
 **/
@Data
public class UserQuery {
    /**
     * 部门ID
     */
    private String deptId;
    /**
     * 用户号
     */
    private String dtUserid;
    /**
     * 用户名
     */
    private String dtUsername;
}
