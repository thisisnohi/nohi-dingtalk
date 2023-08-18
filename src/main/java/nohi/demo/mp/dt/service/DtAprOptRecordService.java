package nohi.demo.mp.dt.service;

import com.dingtalk.api.response.OapiProcessinstanceGetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.das.JpaCRUDService;
import nohi.demo.common.das.JpaDAO;
import nohi.demo.mp.dt.dao.jpa.DtAprOptRecordDao;
import nohi.demo.mp.dt.entity.jpa.DtAprOptRecord;
import nohi.demo.mp.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 操作记录列表 服务实现类
 * </p>
 *
 * @author nohi
 * @date 2021-01-21
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DtAprOptRecordService extends JpaCRUDService<DtAprOptRecord, String> {
    private final DtAprOptRecordDao dtAprOptRecordDao;
    private final DtAprOptAttachmentService dtAprOptAttachmentService;

    @Override
    public JpaDAO<DtAprOptRecord, String> getDAO(){
        return dtAprOptRecordDao;
    }

    /**
     * 同步操作记录
     * @param procInstId
     * @param vo
     */
    public void syncOperationRecords(String procInstId, OapiProcessinstanceGetResponse.ProcessInstanceTopVo vo) {
        // 删除已经存在的操作记录表
        dtAprOptRecordDao.deleteByProcInstId(procInstId);
        // 删除所有附件
        dtAprOptAttachmentService.deleteByProcInstId(procInstId);


        List<OapiProcessinstanceGetResponse.OperationRecordsVo>  list = vo.getOperationRecords();
        if (null == list) {
            log.warn("操作记录为空");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            OapiProcessinstanceGetResponse.OperationRecordsVo item = list.get(i);
            DtAprOptRecord info = new DtAprOptRecord();
            JsonUtils.copyProperties(item, info);
            info.setId(procInstId + "_" + StringUtils.leftPad("" + i, 5, "0"));
            info.setProcInstId(procInstId);

            // 保存
            info = this.getDAO().save(info);

            // 同步附件
            dtAprOptAttachmentService.syncOperationRecords(procInstId, info.getId(), item.getAttachments());
        }
    }
}
