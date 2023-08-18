package nohi.demo.mp.dt.service;

import com.dingtalk.api.response.OapiAttendanceGetsimplegroupsResponse;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.taobao.api.ApiException;
import lombok.RequiredArgsConstructor;
import nohi.demo.common.das.JpaCRUDService;
import nohi.demo.common.das.JpaDAO;
import nohi.demo.common.tx.BaseResponse;
import nohi.demo.mp.dt.dao.jpa.DtKqGroupDao;
import nohi.demo.mp.dt.entity.jpa.DtKqGroup;
import nohi.demo.mp.service.mp.MpHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 考勤组 服务实现类
 * </p>
 *
 * @author nohi
 * @date 2021-01-14
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DtKqGroupService extends JpaCRUDService<DtKqGroup, String> {
    private final DtKqGroupDao dtKqGroupDao;

    @Autowired
    private MpHandler mpHandler;

    @Override
    public JpaDAO<DtKqGroup, String> getDAO(){
        return dtKqGroupDao;
    }

    public List<DtKqGroup> listDepts(DtKqGroup info) {
        Sort sort =  Sort.by(Sort.Direction.ASC, "id");
        return this.findByExample(info, sort);
    }

    /**
     * 刷新考勤组
     * @return
     */
    public BaseResponse refresh() throws ApiException {

        List<OapiAttendanceGetsimplegroupsResponse.AtGroupForTopVo> groupList = mpHandler.getMpService().getKqGroup();
        if (null != groupList && !groupList.isEmpty()) {
            // 删除所有考勤组
            this.getDAO().deleteAll();
            // 添加新考勤组
            List<DtKqGroup> dtKqGroupList = Lists.newArrayList();
            groupList.forEach(item -> {
                DtKqGroup group = new DtKqGroup();
                group.setId(String.valueOf(item.getGroupId()));
                group.setGroupName(item.getGroupName());
                group.setClassList(Joiner.on(",").join(item.getClassesList()));
                group.setType(item.getType());
                group.setMemberCount(null == item.getMemberCount() ? 0 : item.getMemberCount().intValue());
                group.setOwnerUserId(item.getOwnerUserId());

                dtKqGroupList.add(group);
            });
            // 保存
            this.saveAll(dtKqGroupList);
        }
        return BaseResponse.suc("同步成功");
    }
}
