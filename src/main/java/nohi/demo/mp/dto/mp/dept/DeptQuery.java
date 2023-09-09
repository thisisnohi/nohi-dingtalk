package nohi.demo.mp.dto.mp.dept;

import lombok.Data;
import nohi.demo.common.tx.BaseRequest;

/**
 * <h3>nohi-dd-miniprogram-server</h3>
 *
 * @author NOHI
 * @description <p>考勤信息</p>
 * @date 2023/09/03 21:11
 **/
@Data
public class DeptQuery {
    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 部门名称
     */
    private String deptName;
}
