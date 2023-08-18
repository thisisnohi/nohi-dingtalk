package nohi.demo.mp.dt.service;

import com.dingtalk.api.response.OapiProcessinstanceGetResponse;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.das.JpaCRUDService;
import nohi.demo.common.das.JpaDAO;
import nohi.demo.mp.dt.dao.jpa.DtAprFormInfoDao;
import nohi.demo.mp.dt.entity.jpa.DtAprFormInfo;
import nohi.demo.mp.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 表单详情列表 服务实现类
 * </p>
 *
 * @author nohi
 * @date 2021-01-21
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DtAprFormInfoService extends JpaCRUDService<DtAprFormInfo, String> {
    private final DtAprFormInfoDao dtAprFormInfoDao;

    @Override
    public JpaDAO<DtAprFormInfo, String> getDAO(){
        return dtAprFormInfoDao;
    }

    /**
     * 同步操作记录
     * @param procInstId
     */
    public void syncForm(String procInstId, OapiProcessinstanceGetResponse.ProcessInstanceTopVo vo) {
        // 删除已经存在的操作记录表
        dtAprFormInfoDao.deleteByProcInstId(procInstId);

        List<OapiProcessinstanceGetResponse.FormComponentValueVo> list = vo.getFormComponentValues();
        if (null == list) {
            log.warn("操作记录为空");
            return;
        }
        List<DtAprFormInfo> saveList = Lists.newArrayList();
        for (int i = 0; i < list.size(); i++) {
            OapiProcessinstanceGetResponse.FormComponentValueVo item = list.get(i);
            DtAprFormInfo info = new DtAprFormInfo();
            JsonUtils.copyProperties(item, info);
            info.setId(procInstId + "_" + StringUtils.leftPad("" + i, 5, "0"));
            info.setProcInstId(procInstId);
            info.setComponentId(item.getId());

            saveList.add(info);
        }
        // 保存
        this.saveAll(saveList);
    }
}
