package nohi.demo.mp.dt.dao.jpa;

import nohi.demo.mp.dt.entity.jpa.DtAprFormInfo;
import nohi.demo.common.das.JpaDAO;

/**
 * <p>
 * 表单详情列表 Mapper 接口
 * </p>
 *
 * @author nohi
 * @date 2021-01-21
 */
public interface DtAprFormInfoDao extends JpaDAO<DtAprFormInfo, String> {
    /**
     * 根据流程实例ID
     * @param procInstId
     * @return
     */
    public int deleteByProcInstId(String procInstId);
}
