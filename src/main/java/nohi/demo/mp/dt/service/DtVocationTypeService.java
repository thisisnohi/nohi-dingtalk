package nohi.demo.mp.dt.service;

import com.dingtalk.api.response.OapiAttendanceVacationTypeListResponse;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.taobao.api.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.das.JpaCRUDService;
import nohi.demo.common.das.JpaDAO;
import nohi.demo.common.tx.BaseResponse;
import nohi.demo.mp.config.MpConfig;
import nohi.demo.mp.dt.dao.jpa.DtVocationTypeDao;
import nohi.demo.mp.dt.entity.jpa.DtVocationType;
import nohi.demo.mp.service.mp.MpHandler;
import nohi.demo.mp.utils.JsonUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 假期类型 服务实现类
 * </p>
 *
 * @author nohi
 * @date 2021-01-05
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DtVocationTypeService extends JpaCRUDService<DtVocationType, String> {
    private final DtVocationTypeDao dao;
    @Autowired
    private MpHandler mpHandler;
    @Autowired
    private MpConfig mpConfig;

    @Override
    public JpaDAO<DtVocationType, String> getDAO() {
        return dao;
    }


    public BaseResponse refresh() {
        return this.refreshByDept(mpConfig.getManagerId());
    }

    public BaseResponse refreshByDept(String operatId) {
        String title = String.format("刷新假期类型,操作人[%s]", operatId);
        try {
            OapiAttendanceVacationTypeListResponse response = mpHandler.getMpService().apiGetVocationType(operatId);
            log.debug("假期类型查询success[{}] msg[{}],数据:{}", response.getSuccess(), response.getMsg(), JsonUtils.toJson(response.getResult()));
            if (CollectionUtils.isEmpty(response.getResult())) {
                return BaseResponse.newCode("01", "没有假期数据");
            }
            // 循环用户列表
            response.getResult().forEach( item -> {
                this.syncVocationType(item);
            });

            int total = response.getResult().size();
            String msg = "假期类型总数[" + total + "]";
            return BaseResponse.suc(msg);
        } catch (ApiException e) {
            log.error("{} 异常:{}", title, e.getMessage(), e);
            return BaseResponse.error(e.getMessage());
        }
    }

    /**
     * 同步用户信息
     */
    public void syncVocationType(OapiAttendanceVacationTypeListResponse.Result item ) {
        String title = String.format("刷新假期数据[%s][%s]", item.getLeaveCode(), item.getLeaveName());
        // 查询用户信息
        String id = null;
        Optional<DtVocationType> rs = dao.findById(item.getLeaveCode());
        DtVocationType info = null;
        if (rs.isPresent()){
            info = rs.get();
        } else {
            info = new DtVocationType();
            info.setId(item.getLeaveCode());
        }
        info.setLeaveCode(item.getLeaveCode());
        info.setLeaveName(item.getLeaveName());
        info.setBizType(item.getBizType());
        info.setValidityType(item.getValidityType());
        info.setSource(item.getSource());
        info.setLeaveViewUnit(item.getLeaveViewUnit());
        info.setHoursInPerDay(item.getHoursInPerDay() == null ? "" : item.getHoursInPerDay() + "");
        info.setNaturalDayLeave(item.getNaturalDayLeave());

        info = dao.save(info);
        log.debug("{}, id:{}", title, info.getId());
    }
}
