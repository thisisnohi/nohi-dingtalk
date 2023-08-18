package nohi.demo.mp.dt.dao.jpa;

import nohi.demo.mp.dt.entity.jpa.DtAprOptAttachment;
import nohi.demo.common.das.JpaDAO;

/**
 * <p>
 * 操作记录评论附件 Mapper 接口
 * </p>
 *
 * @author nohi
 * @date 2021-01-21
 */
public interface DtAprOptAttachmentDao extends JpaDAO<DtAprOptAttachment, String> {
    /**
     * 根据流程实例ID
     * @param procInstId
     * @return
     */
    public int deleteByProcInstId(String procInstId);
}
