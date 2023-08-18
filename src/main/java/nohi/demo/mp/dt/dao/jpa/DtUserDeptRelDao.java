package nohi.demo.mp.dt.dao.jpa;

import nohi.demo.mp.dt.entity.jpa.DtUserDeptRel;
import nohi.demo.common.das.JpaDAO;

import java.util.List;

/**
 * <p>
 * 部门人员关联表 Mapper 接口
 * </p>
 *
 * @author nohi
 * @date 2021-01-05
 */
public interface DtUserDeptRelDao extends JpaDAO<DtUserDeptRel, String> {
    /**
     * 根据用户ID、部门ID查询数据
     * @param dtUserId
     * @param dtDeptId
     * @return
     */
    DtUserDeptRel findOneByDtUseridAndDtDeptId(String dtUserId, String dtDeptId);
    List<DtUserDeptRel> findListByDtDeptId(String dtDeptId);
}
