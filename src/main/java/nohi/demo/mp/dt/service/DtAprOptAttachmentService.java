package nohi.demo.mp.dt.service;

import com.dingtalk.api.response.OapiProcessinstanceGetResponse;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.das.JpaCRUDService;
import nohi.demo.common.das.JpaDAO;
import nohi.demo.mp.dt.dao.jpa.DtAprOptAttachmentDao;
import nohi.demo.mp.dt.entity.jpa.DtAprOptAttachment;
import nohi.demo.mp.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 操作记录评论附件 服务实现类
 * </p>
 *
 * @author nohi
 * @date 2021-01-21
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DtAprOptAttachmentService extends JpaCRUDService<DtAprOptAttachment, String> {
    private final DtAprOptAttachmentDao dtAprOptAttachmentDao;

    @Override
    public JpaDAO<DtAprOptAttachment, String> getDAO(){
        return dtAprOptAttachmentDao;
    }

    /**
     * 根据流程实例ID
     * @param procInstId
     * @return
     */
    public int deleteByProcInstId(String procInstId){
        return dtAprOptAttachmentDao.deleteByProcInstId(procInstId);
    }

    /**
     * 同步操作记录
     * @param procInstId
     */
    public void syncOperationRecords(String procInstId, String optRecordId, List<OapiProcessinstanceGetResponse.Attachment> list) {
        // 删除已经存在的操作记录表
        dtAprOptAttachmentDao.deleteByProcInstId(procInstId);

        if (null == list) {
            log.warn("操作记录为空");
            return;
        }
        List<DtAprOptAttachment> saveLit = Lists.newArrayList();
        for (int i = 0; i < list.size(); i++) {
            OapiProcessinstanceGetResponse.Attachment item = list.get(i);
            DtAprOptAttachment info = new DtAprOptAttachment();
            JsonUtils.copyProperties(item, info);
            info.setProcInstId(procInstId);
            info.setOptRecordId(optRecordId);

            saveLit.add(info);
        }
        // 保存
        this.saveAll(saveLit);
    }
}
