package nohi.demo.mp.dt.dao.jpa;

import nohi.demo.mp.dt.entity.jpa.DtDept;
import nohi.demo.common.das.JpaDAO;

/**
 * <p>
 * 组织部门 Mapper 接口
 * </p>
 *
 * @author nohi
 * @date 2021-01-05
 */
public interface DtDeptDao extends JpaDAO<DtDept, String> {

    public DtDept findOneByDtDeptId(String dtDeptId);
}
