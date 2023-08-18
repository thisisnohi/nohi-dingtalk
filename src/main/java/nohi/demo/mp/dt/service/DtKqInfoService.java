package nohi.demo.mp.dt.service;

import com.dingtalk.api.response.OapiAttendanceListRecordResponse;
import com.dingtalk.api.response.OapiAttendanceListResponse;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.das.JpaCRUDService;
import nohi.demo.common.das.JpaDAO;
import nohi.demo.common.tx.BaseResponse;
import nohi.demo.mp.config.SpringContextUtils;
import nohi.demo.mp.dt.dao.jpa.DtKqDetailDao;
import nohi.demo.mp.dt.dao.jpa.DtKqInfoDao;
import nohi.demo.mp.dt.entity.jpa.DtKqDetail;
import nohi.demo.mp.dt.entity.jpa.DtKqInfo;
import nohi.demo.mp.dt.entity.jpa.DtUser;
import nohi.demo.mp.dt.entity.jpa.DtUserDeptRel;
import nohi.demo.mp.dto.mp.KqQueryReq;
import nohi.demo.mp.dto.mp.kq.SynUserKqRes;
import nohi.demo.mp.service.mp.MpHandler;
import nohi.demo.mp.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 考勤数据 服务实现类
 * </p>
 *
 * @author nohi
 * @date 2021-01-14
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class DtKqInfoService extends JpaCRUDService<DtKqInfo, String> {
    private final DtKqInfoDao dtKqInfoDao;
    private final DtKqDetailDao dtKqDetailDao;
    private final DtUserService dtUserService;
    @Autowired
    private MpHandler mpHandler;

    @Override
    public JpaDAO<DtKqInfo, String> getDAO() {
        return dtKqInfoDao;
    }

    public List<DtKqInfo> list(DtKqInfo info) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        return this.findByExample(info, sort);
    }

    public BaseResponse getKqAll(KqQueryReq req) {
        BaseResponse response = BaseResponse.suc("");
        try {
            List<OapiAttendanceListResponse.Recordresult> list = mpHandler.getMpService().getKqAll(req.getUserIdList(), req.getWorkDateFrom(), req.getWorkDateTo());
            response.setData(list);
        } catch (Exception e) {
            log.error("获取考勤异常:{}", e.getMessage(), e);
            return BaseResponse.error(e.getMessage());
        }
        return response;
    }

    /**
     * 同步部门下用户考勤信息
     *
     * @param req
     * @return
     */
    public BaseResponse synUserKq(KqQueryReq req) {
        List<String> userId = null;
        // 查询部门下
        if (StringUtils.isNotBlank(req.getDeptId())) {
            List<DtUserDeptRel> relList = dtUserService.findUserListByDeptId(req.getDeptId());
            if (null != relList && !relList.isEmpty()) {
                userId = relList.stream().map(item -> item.getDtUserid()).collect(Collectors.toList());
            }
        } else if (null != req.getUserIdList() && !req.getUserIdList().isEmpty()) {
            userId = req.getUserIdList();
        }
        if (null == userId || userId.isEmpty()) {
            List<DtUser> userLists = dtUserService.findAll();
            if (null != userLists && !userLists.isEmpty()) {
                userId = userLists.stream().map(item -> item.getDtUserid()).collect(Collectors.toList());
            }
        }
        log.debug("用户列表：{}", null == userId ? "is null" : userId.size());
        if (null == userId || userId.isEmpty()) {
            return BaseResponse.error("用户列表为空,请同步用户");
        }

        List<OapiAttendanceListResponse.Recordresult> rsList = Lists.newArrayList();
        AtomicInteger size = new AtomicInteger();
        // 分页同步
        List<List<String>> parts = Lists.partition(userId, 50);
        CountDownLatch countDownLatch = new CountDownLatch(parts.size());
        List<SynUserKqRes> synResList = Lists.newArrayList();

        for (List<String> userIdlist : parts) {
            String subTitle = String.format("第[%s]页，用户数[%s]", size.getAndIncrement(), userIdlist.size());
            log.info(subTitle);
            SynUserKqRes kqRes = SynUserKqRes.builder().countDownLatch(countDownLatch).userIdlist(userIdlist).build();
            synResList.add(kqRes);
            try {
                DtKqInfoService _tmpService = SpringContextUtils.getBean(DtKqInfoService.class);
                _tmpService.synUserKq(userIdlist, req.getWorkDateFrom(), req.getWorkDateTo(), kqRes);
            } catch (Exception e) {
                log.error("{},异常：{}", subTitle, e.getMessage(), e);
            }
        }
        try {
            countDownLatch.await(30, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("异步线程执行超时:{}", e.getMessage(), e);
        }
        log.info("异步线程结束....");
        int total = userId.size();
        int sucUsers = 0;
        int failUsers = 0;
        int synKqcount = 0;
        for (SynUserKqRes res : synResList) {
            if (res.isSuc()) {
                sucUsers += res.getUserIdlist().size();
                synKqcount += res.getKqList().size();
            } else {
                failUsers += res.getUserIdlist().size();
            }
        }
        String msg = "用户总数[" + total + "]";
        if (failUsers > 0) {
            msg += "失败用户数[" + failUsers + "]";
        }
        if (sucUsers > 0) {
            msg += "成功用户数[" + sucUsers + "],同步数据[" + synKqcount + "]条";
        }
        log.debug(msg);
        return BaseResponse.suc(msg);
    }

    /**
     * 按50分组查询用户数据
     * 同步数据库
     *
     * @return
     */
    @Async
    public SynUserKqRes synUserKq(List<String> userId, Date workStart, Date workTo, SynUserKqRes kqRes) throws Exception {
        if (null == userId) {
            kqRes.setSuc(false);
            kqRes.setMsg("查询用户列表为空");
            return kqRes;
        }
        log.info("查询[{}]用户考勤数据", userId.size());
        List<OapiAttendanceListResponse.Recordresult> rsList = Lists.newArrayList();
        AtomicInteger size = new AtomicInteger();
        try {
            for (List<String> userIdlist : Iterables.partition(userId, 50)) {
                log.info("第[{}]页，用户数[{}]", size.getAndIncrement(), userIdlist.size());
                List<OapiAttendanceListResponse.Recordresult> list = mpHandler.getMpService().getKqAll(userIdlist, workStart, workTo);
                log.debug("获取考勤数据:{}", list.size());
                rsList.addAll(list);
            }

            // 保存数据
            this.saveKqInfo(rsList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            kqRes.setSuc(false);
            kqRes.setMsg(e.getMessage());
            return kqRes;
        } finally {
            kqRes.getCountDownLatch().countDown();
        }
        kqRes.setSuc(true);
        kqRes.setMsg("同步完成");
        kqRes.setKqList(rsList);
        return kqRes;
    }
    @Transactional
    public void saveKqInfo(List<OapiAttendanceListResponse.Recordresult> kqList) {
        List<DtKqInfo> dtKqInfoList = Lists.newArrayList();
        List<String> idList = Lists.newArrayList();
        if (null != kqList && !kqList.isEmpty()) {
            kqList.forEach(item -> {
                DtKqInfo info = new DtKqInfo();
                JsonUtils.copyProperties(item, info);
                info.setDtUserid(item.getUserId());
                idList.add(info.getId());
                dtKqInfoList.add(info);
            });
        }
        log.debug("考勤数据:{}", JsonUtils.toJson(dtKqInfoList));
        // 清楚重复数据
        dtKqInfoDao.deleteByIdIn(idList);
        dtKqInfoDao.saveAll(dtKqInfoList);
    }

    /**
     * 同步部门下用户考勤详情
     *
     * @param req
     * @return
     */
    public BaseResponse synUserKqDetail(KqQueryReq req) {
        List<String> userId = null;
        // 查询部门下
        if (StringUtils.isNotBlank(req.getDeptId())) {
            List<DtUserDeptRel> relList = dtUserService.findUserListByDeptId(req.getDeptId());
            if (null != relList && !relList.isEmpty()) {
                userId = relList.stream().map(item -> item.getDtUserid()).collect(Collectors.toList());
            }
        } else if (null != req.getUserIdList() && !req.getUserIdList().isEmpty()) {
            userId = req.getUserIdList();
        }
        if (null == userId || userId.isEmpty()) {
            List<DtUser> userLists = dtUserService.findAll();
            if (null != userLists && !userLists.isEmpty()) {
                userId = userLists.stream().map(item -> item.getDtUserid()).collect(Collectors.toList());
            }
        }
        log.debug("用户列表：{}", null == userId ? "is null" : userId.size());
        if (null == userId || userId.isEmpty()) {
            return BaseResponse.error("用户列表为空,请同步用户");
        }

        List<OapiAttendanceListResponse.Recordresult> rsList = Lists.newArrayList();
        AtomicInteger size = new AtomicInteger();
        // 分页同步
        List<List<String>> parts = Lists.partition(userId, 50);
        CountDownLatch countDownLatch = new CountDownLatch(parts.size());
        List<SynUserKqRes> synResList = Lists.newArrayList();

        for (List<String> userIdlist : parts) {
            String subTitle = String.format("第[%s]页，用户数[%s]", size.getAndIncrement(), userIdlist.size());
            log.info(subTitle);
            SynUserKqRes kqRes = SynUserKqRes.builder().countDownLatch(countDownLatch).userIdlist(userIdlist).build();
            synResList.add(kqRes);
            try {
                DtKqInfoService _tmpService = SpringContextUtils.getBean(DtKqInfoService.class);
                _tmpService.synUserKqDetail(userIdlist, req.getWorkDateFrom(), req.getWorkDateTo(), kqRes);
            } catch (Exception e) {
                log.error("{},异常：{}", subTitle, e.getMessage(), e);
            }
        }
        try {
            countDownLatch.await(30, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("异步线程执行超时:{}", e.getMessage(), e);
        }
        log.info("异步线程结束....");
        int total = userId.size();
        int sucUsers = 0;
        int failUsers = 0;
        int synKqcount = 0;
        for (SynUserKqRes res : synResList) {
            if (res.isSuc()) {
                sucUsers += res.getUserIdlist().size();
                synKqcount += res.getKqDetailList().size();
            } else {
                failUsers += res.getUserIdlist().size();
            }
        }
        String msg = "用户总数[" + total + "]";
        if (failUsers > 0) {
            msg += "失败用户数[" + failUsers + "]";
        }
        if (sucUsers > 0) {
            msg += "成功用户数[" + sucUsers + "],同步数据[" + synKqcount + "]条";
        }
        log.debug(msg);
        return BaseResponse.suc(msg);
    }
    /**
     * 按50分组查询用户数据
     * 同步数据库
     *
     * @return
     */
    @Async
    public SynUserKqRes synUserKqDetail(List<String> userId, Date workStart, Date workTo, SynUserKqRes kqRes) throws Exception {
        if (null == userId) {
            kqRes.setSuc(false);
            kqRes.setMsg("查询用户列表为空");
            return kqRes;
        }
        log.info("查询[{}]用户考勤数据", userId.size());
        List<OapiAttendanceListRecordResponse.Recordresult> rsList = Lists.newArrayList();
        AtomicInteger size = new AtomicInteger();
        try {
            for (List<String> userIdlist : Iterables.partition(userId, 50)) {
                log.info("第[{}]页，用户数[{}]", size.getAndIncrement(), userIdlist.size());
                List<OapiAttendanceListRecordResponse.Recordresult> list = mpHandler.getMpService().getKqDetail(userIdlist, workStart, workTo);
                log.debug("获取考勤数据:{}", list.size());
                rsList.addAll(list);
            }

            // 保存数据
            this.saveKqDetail(rsList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            kqRes.setSuc(false);
            kqRes.setMsg(e.getMessage());
            return kqRes;
        } finally {
            kqRes.getCountDownLatch().countDown();
        }
        kqRes.setSuc(true);
        kqRes.setMsg("同步完成");
        kqRes.setKqDetailList(rsList);
        return kqRes;
    }
    @Transactional
    public void saveKqDetail(List<OapiAttendanceListRecordResponse.Recordresult> kqList) {
        List<DtKqDetail> dtKqInfoList = Lists.newArrayList();
        List<String> idList = Lists.newArrayList();
        if (null != kqList && !kqList.isEmpty()) {
            kqList.forEach(item -> {
                DtKqDetail info = new DtKqDetail();
                JsonUtils.copyProperties(item, info);
                idList.add(info.getId());
                dtKqInfoList.add(info);
            });
        }
        log.debug("考勤数据:{}", JsonUtils.toJson(dtKqInfoList));
        // 清楚重复数据
        dtKqDetailDao.deleteByIdIn(idList);
        dtKqDetailDao.saveAll(dtKqInfoList);
    }

}
