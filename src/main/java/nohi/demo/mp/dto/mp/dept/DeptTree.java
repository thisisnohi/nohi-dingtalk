package nohi.demo.mp.dto.mp.dept;

import lombok.Data;
import nohi.demo.mp.dt.entity.jpa.DtDept;

import java.util.List;

/**
 * <h3>nohi-dd-miniprogram-server</h3>
 *
 * @author NOHI
 * @description <p>考勤信息</p>
 * @date 2023/09/03 21:11
 **/
@Data
public class DeptTree extends DtDept {
    /**
     * 上级部门名称
     */
    private String dtParDeptName;

    /**
     * 部门用户数
     */
    private Integer deptUserNum;

    /**
     * 下级部门
     */
    private List<DeptTree> children;
}
