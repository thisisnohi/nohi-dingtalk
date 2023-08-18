package nohi.demo.mp.dt.dao.jpa;

import nohi.demo.common.das.JpaDAO;
import nohi.demo.mp.dt.entity.jpa.DtAprTask;

import java.util.List;

/**
 * <p>
 * 任务列表 Mapper 接口
 * </p>
 *
 * @author nohi
 * @date 2021-01-21
 */
public interface DtAprTaskDao extends JpaDAO<DtAprTask, String> {
    List<DtAprTask> findByProcInstId(String procInstId);

    /**
     * 根据流程实例ID
     * @param procInstId
     * @return
     */
    int deleteByProcInstId(String procInstId);
}
