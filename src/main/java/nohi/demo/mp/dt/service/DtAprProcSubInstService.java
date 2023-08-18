package nohi.demo.mp.dt.service;

import com.dingtalk.api.response.OapiProcessinstanceGetResponse;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.das.JpaCRUDService;
import nohi.demo.common.das.JpaDAO;
import nohi.demo.mp.dt.dao.jpa.DtAprProcSubInstDao;
import nohi.demo.mp.dt.entity.jpa.DtAprProcSubInst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 审批附属实例列表 服务实现类
 * </p>
 *
 * @author nohi
 * @date 2021-01-21
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DtAprProcSubInstService extends JpaCRUDService<DtAprProcSubInst, String> {
    private final DtAprProcSubInstDao dtAprProcSubInstDao;

    @Override
    public JpaDAO<DtAprProcSubInst, String> getDAO(){
        return dtAprProcSubInstDao;
    }

    /**
     * 同步任务列表
     *
     * @param procInstId
     * @param vo
     */
    public void syncSubInst(String procInstId, OapiProcessinstanceGetResponse.ProcessInstanceTopVo vo) {
        // 删除已经存在的记录
        dtAprProcSubInstDao.deleteByProcInstId(procInstId);

        List<String> list = vo.getAttachedProcessInstanceIds();
        if (null == list) {
            log.warn("操作记录为空");
            return;
        }
        List<DtAprProcSubInst> saveList = Lists.newArrayList();
        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i);
            DtAprProcSubInst info = new DtAprProcSubInst();
            info.setId(procInstId + "_" + StringUtils.leftPad("" + i, 5, "0"));
            info.setProcInstId(procInstId);
            info.setProcInstSubId(item);

            saveList.add(info);
        }
        // 保存
        this.saveAll(saveList);
    }
}
