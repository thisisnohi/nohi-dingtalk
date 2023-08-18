package nohi.demo.mp.dt.dao.jpa;

import nohi.demo.common.das.JpaDAO;
import nohi.demo.mp.dt.entity.jpa.DtAprProcSubInst;

import java.util.List;

/**
 * <p>
 * 审批附属实例列表 Mapper 接口
 * </p>
 *
 * @author nohi
 * @date 2021-01-21
 */
public interface DtAprProcSubInstDao extends JpaDAO<DtAprProcSubInst, String> {
    List<DtAprProcSubInst> findByProcInstId(String procInstId);

    /**
     * 根据流程实例ID
     * @param procInstId
     * @return
     */
    int deleteByProcInstId(String procInstId);
}
