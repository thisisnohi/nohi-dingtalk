package nohi.demo.mp.dt.service;

import com.dingtalk.api.response.OapiProcessinstanceGetResponse;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.das.JpaCRUDService;
import nohi.demo.common.das.JpaDAO;
import nohi.demo.mp.dt.dao.jpa.DtAprTaskDao;
import nohi.demo.mp.dt.entity.jpa.DtAprTask;
import nohi.demo.mp.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 任务列表 服务实现类
 * </p>
 *
 * @author nohi
 * @date 2021-01-21
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DtAprTaskService extends JpaCRUDService<DtAprTask, String> {
    private final DtAprTaskDao dtAprTaskDao;

    @Override
    public JpaDAO<DtAprTask, String> getDAO() {
        return dtAprTaskDao;
    }

    /**
     * 同步任务列表
     *
     * @param procInstId
     * @param vo
     */
    public void syncTasks(String procInstId, OapiProcessinstanceGetResponse.ProcessInstanceTopVo vo) {
        // 删除已经存在的记录
        dtAprTaskDao.deleteByProcInstId(procInstId);

        List<OapiProcessinstanceGetResponse.TaskTopVo> list = vo.getTasks();
        if (null == list) {
            log.warn("操作记录为空");
            return;
        }
        List<DtAprTask> saveList = Lists.newArrayList();
        for (int i = 0; i < list.size(); i++) {
            OapiProcessinstanceGetResponse.TaskTopVo item = list.get(i);
            DtAprTask info = new DtAprTask();
            JsonUtils.copyProperties(item, info);
            info.setId(procInstId + "_" + StringUtils.leftPad("" + i, 5, "0"));
            info.setProcInstId(procInstId);

            saveList.add(info);
        }
        // 保存
        this.saveAll(saveList);
    }
}
