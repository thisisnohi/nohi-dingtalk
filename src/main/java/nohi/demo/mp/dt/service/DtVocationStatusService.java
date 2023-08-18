package nohi.demo.mp.dt.service;

import com.dingtalk.api.response.OapiAttendanceGetleavestatusResponse;
import com.dingtalk.api.response.OapiAttendanceListResponse;
import com.dingtalk.api.response.OapiAttendanceVacationTypeListResponse;
import com.google.common.collect.Lists;
import com.taobao.api.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.das.JpaCRUDService;
import nohi.demo.common.das.JpaDAO;
import nohi.demo.common.tx.BaseResponse;
import nohi.demo.mp.config.MpConfig;
import nohi.demo.mp.config.SpringContextUtils;
import nohi.demo.mp.dt.dao.jpa.DtVocationStatusDao;
import nohi.demo.mp.dt.entity.jpa.DtUser;
import nohi.demo.mp.dt.entity.jpa.DtUserDeptRel;
import nohi.demo.mp.dt.entity.jpa.DtVocationStatus;
import nohi.demo.mp.dt.entity.jpa.DtVocationType;
import nohi.demo.mp.dto.mp.KqQueryReq;
import nohi.demo.mp.dto.mp.kq.SynUserKqRes;
import nohi.demo.mp.service.mp.MpHandler;
import nohi.demo.mp.utils.JsonUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 假请类型 服务实现类
 * </p>
 *
 * @author nohi
 * @date 2021-01-05
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DtVocationStatusService extends JpaCRUDService<DtVocationStatus, String> {
    private final DtVocationStatusDao dao;
    @Autowired
    private MpHandler mpHandler;
    @Autowired
    private MpConfig mpConfig;
    @Autowired
    private DtUserService dtUserService;

    @Override
    public JpaDAO<DtVocationStatus, String> getDAO() {
        return dao;
    }

    public BaseResponse refreshVocationStatus(KqQueryReq req) {
        if (null == req) {
            return BaseResponse.error("同步请求参数为空");
        }
        String title = String.format("假请状态");
        Date startTime = req.getWorkDateFrom();
        Date endTime = req.getWorkDateTo();

        try {

            List<String> userId = Lists.newArrayList();
            // 查询部门下
            if (StringUtils.isNotBlank(req.getDeptId())) {
                List<DtUserDeptRel> relList = dtUserService.findUserListByDeptId(req.getDeptId());
                if (null != relList && !relList.isEmpty()) {
                    userId = relList.stream().map(DtUserDeptRel::getDtUserid).collect(Collectors.toList());
                }
            } else if (null != req.getUserIdList() && !req.getUserIdList().isEmpty()) {
                userId = req.getUserIdList();
            }
            // 如果用户id为空，则同步所有人
            if (userId.isEmpty()) {
                List<DtUser> userLists = dtUserService.findAll();
                if (null != userLists && !userLists.isEmpty()) {
                    userId = userLists.stream().map(DtUser::getDtUserid).collect(Collectors.toList());
                }
            }
            log.info("待同步假请用户数[{}]", userId.size());

            AtomicInteger size = new AtomicInteger();
            // 分页同步
            List<List<String>> parts = Lists.partition(userId, 50);
            int total = 0;
            for (List<String> userIdlist : parts) {
                String subTitle = String.format("第[%s]页，用户数[%s]", size.getAndIncrement(), userIdlist.size());
                log.info(subTitle);
                try {
                    List<OapiAttendanceGetleavestatusResponse.LeaveStatusVO> response = mpHandler.getMpService().geAlltLeavesSatus(userIdlist, startTime, endTime);
                    log.debug("假请状态[{}]", null == response ? "IS NULL" : response.size());
                    if (CollectionUtils.isEmpty(response)) {
                        return BaseResponse.newCode("01", "没有假请数据");
                    }
                    total += response.size();
                    // 循环用户列表
                    response.forEach(this::syncVocationStatus);
                } catch (Exception e) {
                    log.error("{},异常：{}", subTitle, e.getMessage(), e);
                }
            }
            String msg = "假请类型总数[" + total + "]";
            return BaseResponse.suc(msg);
        } catch (Exception e) {
            log.error("{} 异常:{}", title, e.getMessage(), e);
            return BaseResponse.error(e.getMessage());
        }
    }

    public String getKey(OapiAttendanceGetleavestatusResponse.LeaveStatusVO vo) {
        return vo.getUserid() + "-" + vo.getStartTime();
    }

    /**
     * 同步用户信息
     */
    public void syncVocationStatus(OapiAttendanceGetleavestatusResponse.LeaveStatusVO item) {
        String title = String.format("刷新假请数据[%s][%s]", item.getLeaveCode(), item.getUserid());
        // 查询用户信息
        String id = this.getKey(item);
        Optional<DtVocationStatus> rs = dao.findById(id);
        DtVocationStatus info = null;
        if (rs.isPresent()) {
            info = rs.get();
        } else {
            info = new DtVocationStatus();
            info.setId(id);
        }
        info.setLeaveCode(item.getLeaveCode());
        info.setDurationPercent(item.getDurationPercent());
        info.setDurationUnit(item.getDurationUnit());
        info.setUserid(item.getUserid());
        info.setStartTime(new Date(item.getStartTime()));
        info.setEndTime(new Date(item.getEndTime()));
        // 保存
        info = dao.save(info);
        log.debug("{}, id:{} 结束", title, info.getId());
    }

    public BaseResponse findUserVocation(KqQueryReq req) {
        log.info("查询考勤信息");
        BaseResponse response = BaseResponse.suc("");
        response.setData(dao.findUserVocation());
        return response;
    }
}
