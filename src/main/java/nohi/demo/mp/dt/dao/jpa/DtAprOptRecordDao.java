package nohi.demo.mp.dt.dao.jpa;

import nohi.demo.mp.dt.entity.jpa.DtAprOptRecord;
import nohi.demo.common.das.JpaDAO;

import java.util.List;

/**
 * <p>
 * 操作记录列表 Mapper 接口
 * </p>
 *
 * @author nohi
 * @date 2021-01-21
 */
public interface DtAprOptRecordDao extends JpaDAO<DtAprOptRecord, String> {

    List<DtAprOptRecord> findByProcInstId(String procInstId);

    /**
     * 根据流程实例ID
     * @param procInstId
     * @return
     */
    int deleteByProcInstId(String procInstId);
}
