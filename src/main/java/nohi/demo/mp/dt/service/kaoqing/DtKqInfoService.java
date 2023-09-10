package nohi.demo.mp.dt.service.kaoqing;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingtalk.api.response.OapiAttendanceListRecordResponse;
import com.dingtalk.api.response.OapiAttendanceListResponse;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.config.Knife4jConfig;
import nohi.demo.common.das.JpaCRUDService;
import nohi.demo.common.das.JpaDAO;
import nohi.demo.common.tx.BasePage;
import nohi.demo.common.tx.BaseResponse;
import nohi.demo.common.tx.PageUtils;
import nohi.demo.mp.config.MpConfig;
import nohi.demo.mp.dt.dao.jpa.DtKqDetailDao;
import nohi.demo.mp.dt.dao.jpa.DtKqInfoDao;
import nohi.demo.mp.dt.dao.mapper.DtKqInfoMapper;
import nohi.demo.mp.dt.entity.jpa.DtKqDetail;
import nohi.demo.mp.dt.entity.jpa.DtKqInfo;
import nohi.demo.mp.dt.entity.jpa.DtUser;
import nohi.demo.mp.dt.entity.jpa.DtUserDeptRel;
import nohi.demo.mp.dt.service.DtUserService;
import nohi.demo.mp.dto.mp.KqQueryReq;
import nohi.demo.mp.dto.mp.kq.SynUserKqRes;
import nohi.demo.mp.dto.mp.kq.UserKqInfoDTO;
import nohi.demo.mp.dto.work.EmpWorkSheet;
import nohi.demo.mp.dto.work.dt.WorkDayClassMeta;
import nohi.demo.mp.dto.work.dt.WorkDayMeta;
import nohi.demo.mp.service.mp.MpHandler;
import nohi.demo.mp.utils.DateUtils;
import nohi.demo.mp.utils.FileUtils;
import nohi.demo.mp.utils.IdUtils;
import nohi.demo.mp.utils.JsonUtils;
import nohi.doc.utils.ExcelUtils;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
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
    private final DtKqInfoMapper dtKqInfoMapper;
    private final MpConfig mpConfig;
    private final MpHandler mpHandler;

    private static final String EMPTY = "-";
    private static final int OFF_TIME = 18;
    private static final int SCALE = 2;
    private static final int SCALE_INT = 0;
    private static final BigDecimal WORK_TIME = new BigDecimal(8);
    private static final BigDecimal MINITUS_OF_HOURS = new BigDecimal(60);
    private static final BigDecimal MINITUS_OF_DAY = new BigDecimal(60 * 8);
    private static final BigDecimal MINITUS_30 = new BigDecimal("30");
    private static final Set HOLIDAY = Sets.newHashSet("1", "2");

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
                DtKqInfoService _tmpService = Knife4jConfig.SpringContextUtils.getBean(DtKqInfoService.class);
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
                DtKqInfoService _tmpService = Knife4jConfig.SpringContextUtils.getBean(DtKqInfoService.class);
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

    /**
     * 查询考勤信息
     *
     * @param req req
     */
    public BaseResponse findUserKq(KqQueryReq req) {
        log.info("查询考勤信息");
        BaseResponse response = BaseResponse.suc("");
        response.setData(dtKqInfoDao.findUserKq());
        return response;
    }

    /**
     * 考勤列表，JPA方式分页
     *
     * @param basePage 原始分页
     * @param info     查询对象
     * @return 响应数据
     */
    public List<UserKqInfoDTO> dingTalkPageList(BasePage basePage, KqQueryReq info) {
        log.info("查询考勤信息");

        // 转换请求参数
        // 日期
        if (null != info.getWorkDateFrom()) {
            info.setWorkDateFromStr(DateUtils.format(info.getWorkDateFrom(), DateUtils.SIMPLE_DATE));
        }
        if (null != info.getWorkDateTo()) {
            info.setWorkDateToStr(DateUtils.format(info.getWorkDateTo(), DateUtils.SIMPLE_DATE));
        }
        if (null != info.getQueryLocationResult() && !info.getQueryLocationResult().isEmpty()) {
            String tmp = Joiner.on("','").join(info.getQueryLocationResult());
            info.setQueryLocationResultSql("'" + tmp + "'");
        }
        if (null != info.getQueryTimeResult() && !info.getQueryTimeResult().isEmpty()) {
            String tmp = Joiner.on("','").join(info.getQueryTimeResult());
            info.setQueryTimeResultSql("'" + tmp + "'");
        }
        if (null != info.getQueryDeptIds() && !info.getQueryDeptIds().isEmpty()) {
            String tmp = Joiner.on("','").join(info.getQueryDeptIds());
            info.setQueryDeptSql("'" + tmp + "'");
        }
        Page<UserKqInfoDTO> page = PageUtils.buildPage(basePage, UserKqInfoDTO.class);
        List<UserKqInfoDTO> list = dtKqInfoMapper.dingTalkList(page, info);
        PageUtils.buildPage(basePage, page);
        return list;
    }

    /**
     * 获取考勤列表
     *
     * @param info
     * @return
     */
    public List<UserKqInfoDTO> dingTalkPageList(KqQueryReq info) {
        return dingTalkPageList(null, info);
    }

    /**
     * x
     * 统计工时信息
     */
    public List<Map> empWorkSheetDetail(KqQueryReq info) {
        log.info("查询条件:{}", JSONObject.toJSONString(info));
        LocalDate startDate = DateUtils.date2LocalDate(info.getWorkDateFrom());
        LocalDate endDate = DateUtils.date2LocalDate(info.getWorkDateTo());
        log.info("startDate:{}, endDate:{}", startDate, endDate);

        /** 查询考勤数据 **/
        List<UserKqInfoDTO> dingTalkList = this.dingTalkPageList(info);
        log.info("考勤[{}]条", dingTalkList.size());
        /**
         * 按日期每取每天数据
         *  表头： 员工号、员工名 类型(上班、下班、工作量) 日期T  T+1 T+2 ... 结束日期 上班天数 合计工作量
         *  例:   0006 丁龙海  上班  08:40 08:41 ...   20   -
         *        0006 丁龙海  下班 18:40 18:41 ...    20   -
         *        0006 丁龙海  工作量  1.25  1.3 ...     20   23
         */
        // 预处理每日员工工时 员工考勤
        UserWorkSheetKaoService service = new UserWorkSheetKaoService(dingTalkList);
        Map<String, EmpWorkSheet> empWorkSheetMap = service.getEmpSheetMap();
        log.info("empWorkSheetMap:{}", empWorkSheetMap);
        log.debug("================显示各个员工情况====================");
        List<Map> empSheetList = Lists.newArrayList();
        empWorkSheetMap.entrySet().stream().sorted((a, b) -> a.getKey().compareTo(b.getKey())).forEach(entity -> {
            EmpWorkSheet empWorkSheet = entity.getValue();
            String empNo = empWorkSheet.getEmpNo();
            String empName = empWorkSheet.getEmpName();
            log.debug("员工[{}-{}]", empNo, empName);

            // 工时列表
            Map<String, Object> sheetMap = Maps.newHashMap();
            sheetMap.put("empNo", empNo);
            sheetMap.put("empName", empName);

            Map<String, Object> dayMap = Maps.newHashMap();
            dayMap.put("empNo", empNo);
            dayMap.put("empName", empName);
            sheetMap.put("dayMap", dayMap);

            // 统计工时
            AtomicReference<BigDecimal> bd = new AtomicReference<>();
            bd.set(BigDecimal.ZERO);

            Map<String, WorkDayMeta> workDayMetaMap = empWorkSheet.getEmpWorkDayMap();
            workDayMetaMap.entrySet().stream().sorted((a, b) -> a.getKey().compareTo(b.getKey())).forEach(item -> {
                WorkDayMeta workDayMeta = item.getValue();
                LocalDate localDate = workDayMeta.getWorkDay();
                String workDayStr = localDate.toString();

                // 转换为Map
                Map tmpMap = new BeanMap(workDayMeta);
                // tmpMap无法新增key
                Map map = Maps.newHashMap();
                map.putAll(tmpMap);
                dayMap.put(workDayStr, map);

                // 工作量
                this.getDayWorkHard(map, workDayMeta);
                // 合计工作量
                Object tmp = map.get("workTotal");
                if (null != tmp && !EMPTY.equals(tmp)) {
                    bd.set(bd.get().add((BigDecimal) tmp));
                }
                log.debug("{} {}", localDate, this.printEmpDay(workDayMeta));
            });

            // 汇总
            sheetMap.put("total", bd.get());
            empSheetList.add(sheetMap);
        });
        return empSheetList;
    }
    private String printEmpDay(WorkDayMeta workDayMeta) {
        String msg = workDayMeta.getWorkDay().toString();
        msg += ", 上班:[" + workDayMeta.getUserName() + "]" + this.getWorkString(workDayMeta.getOnDuty().getCheckDate());
        msg += ", 下班:[" + workDayMeta.getUserName() + "]" + this.getWorkString(workDayMeta.getOffDuty().getCheckDate());
        return msg;
    }
    /**
     * 打印一天工时
     *
     * @param workDayMeta
     * @return
     */
    private String printDay(String projectNo, WorkDayMeta workDayMeta) {
        String msg = workDayMeta.getWorkDay().toString();
        msg += ", 上班:" + this.getWorkString(workDayMeta.getOnDuty().getCheckDate());
        msg += ", 下班:" + this.getWorkString(workDayMeta.getOffDuty().getCheckDate());
        return msg;
    }

    /**
     * 获取每天工作量
     *
     * @param workDayMeta
     * @return
     */
    private boolean getDayWorkHard(Map<String, Object> projectSheet, WorkDayMeta workDayMeta) {
        String workDayStr = workDayMeta.getWorkDay().toString();
        String msg = "";
        LocalDateTime startWork = null;
        LocalDateTime startBaseCheckTime = null;
        LocalDateTime endWork = null;
        LocalDateTime endBaseCheckTime = null;
        boolean inProject = false;

        msg += "上班:" + this.getWorkString(workDayMeta.getOnDuty().getCheckDate());
        startWork = DateUtils.date2LocalDateTime(workDayMeta.getOnDuty().getCheckDate());
        startBaseCheckTime = DateUtils.date2LocalDateTime(workDayMeta.getOnDuty().getBaseCheckTime());

        msg += " 下班:" + this.getWorkString(workDayMeta.getOffDuty().getCheckDate());
        endWork = DateUtils.date2LocalDateTime(workDayMeta.getOffDuty().getCheckDate());
        endBaseCheckTime = DateUtils.date2LocalDateTime(workDayMeta.getOffDuty().getBaseCheckTime());

        // 是否迟到
        boolean isLater = false;
        // 是否早退
        boolean isBeforeOff = false;
        // 上下班全在同一个项目
        if (startWork != null && endWork != null) {
            // 计算上下班
            if (startWork.isAfter(startBaseCheckTime)) {
                isLater = true;
            }
            if (endWork.isBefore(endBaseCheckTime)) {
                isBeforeOff = true;
            }
            // 计算上班时长
            Duration duration = Duration.between(startWork, endWork);
            long minutes = duration.toMinutes();
            if (minutes > 0) {
                BigDecimal workHour = new BigDecimal(minutes).divide(MINITUS_OF_HOURS, SCALE, RoundingMode.HALF_UP);
                BigDecimal shiChang = null;
                // 非工作日加班, 根据结束时间-开始时间计算工时
                if (HOLIDAY.contains(workDayMeta.getHolidayFlag())) {
                    shiChang = this.addingTime(minutes).divide(MINITUS_OF_DAY, SCALE, RoundingMode.HALF_UP);
                } else
                    // 如果工时大于8小时，则计算加班时长
                    if (workHour.compareTo(WORK_TIME) > 0 && endBaseCheckTime.getHour() <= OFF_TIME && startWork.getDayOfMonth() == endWork.getDayOfMonth()) {
                        duration = Duration.between(endBaseCheckTime, endWork);
                        long jiaBan = duration.toMinutes();
                        shiChang = this.addingTime(jiaBan).divide(MINITUS_OF_DAY, SCALE, RoundingMode.HALF_UP).add(BigDecimal.ONE);
                    } else {
                        // 如果基准时间在18点后，则计算完整时间
                        shiChang = workHour.divide(WORK_TIME, SCALE, RoundingMode.HALF_UP);
                    }


                workDayMeta.setWorkTotal(shiChang);
            } else {
                projectSheet.put(workDayStr, "-");
            }
        } else if (startWork != null || endWork != null) {
            workDayMeta.setWorkTotal(new BigDecimal("0.5"));
        }
        projectSheet.put("workTotal", workDayMeta.getWorkTotal());
        // 迟到
        projectSheet.put("later", isLater);
        // 早退
        projectSheet.put("beforeOff", isBeforeOff);
        // 上下班时间
        projectSheet.put("title", msg);
        return inProject;
    }

    /**
     * 以半小时统计
     * 多个半小时
     *
     * @param minutes
     * @return
     */
    public BigDecimal addingTime(long minutes) {
        BigDecimal time = new BigDecimal(minutes);
        time = time.divide(MINITUS_30, SCALE, RoundingMode.HALF_DOWN).setScale(SCALE_INT, RoundingMode.HALF_UP);
        time = new BigDecimal(time.intValue());
        time = time.multiply(MINITUS_30);
        return time;
    }

    private String getWorkString(Date date) {
        return DateUtils.format(date, "HH:mm");
    }


    /**
     * Excel标题
     *
     * @param searchParam
     * @return
     */
    public List<String> getTitle(KqQueryReq searchParam) {
        List<String> title = Lists.newArrayList();
        title.addAll(Lists.newArrayList("员工号", "员工名"));
        // 根据开始结束日期计算列数
        LocalDate start = DateUtils.date2LocalDate(searchParam.getWorkDateFrom());
        LocalDate end = DateUtils.date2LocalDate(searchParam.getWorkDateTo());
        while (!end.isBefore(start)) {
            title.add(DateUtils.localDateFormat(start, "M.d"));
            start = start.plusDays(1);
        }
        title.add("汇总");
        return title;
    }

    /**
     * 取得值
     *
     * @return
     */
    public static void setValue(Cell cell, Object rs) {
        if (rs == null) {
            cell.setCellValue("");
            return;
        }
        if (rs instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) rs).doubleValue());
        } else if (rs instanceof Date) {
            cell.setCellValue((Date) rs);
        } else if (rs instanceof Timestamp) {
            cell.setCellValue((Timestamp) rs);
        } else if (rs instanceof Integer) {
            cell.setCellValue((Integer) rs);
        } else {
            cell.setCellValue(rs.toString());
        }
    }

    /**
     * 导出项目工时列表
     */
    public String exportProjectSheet(KqQueryReq searchParam) {
        // 获取查询列表
        List<Map> workSheet = this.empWorkSheetDetail(searchParam);
        //
        String filename = IdUtils.timeFlow() + ".xlsx";
        String template = "template/emps_worksheet.xlsx";
        String outputFile = mpConfig.getFilePath() + File.separator + filename;
        try {
            FileUtils.createDir(mpConfig.getFilePath());
        } catch (Exception e) {
            log.error("创建目录[{}]异常:{}", mpConfig.getFilePath(), e.getMessage(), e);
        }
        log.info("template:{} outputFile:{}", template, outputFile);

        int colOffset = 2;
        //2,读取Excel模板
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(template);
             FileOutputStream fos = new FileOutputStream(outputFile);
        ) {
            // 读取Excel模板
            Workbook templatebook = WorkbookFactory.create(is);
            Sheet templateSheet = templatebook.getSheetAt(0);

            // title
            List<String> titleList = this.getTitle(searchParam);
            log.debug("titleList:{}", titleList);
            Row titleRow = templateSheet.getRow(0);
            for (int cellIndex = 0; cellIndex < titleList.size(); cellIndex++) {
                Cell cell = titleRow.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if (cellIndex > colOffset) {
                    // 拷贝样式
                    cell.setCellStyle(titleRow.getCell(colOffset).getCellStyle());
                }
                setValue(cell, titleList.get(cellIndex));
            }
            // 总天数：title - 5 (项目号、项目名、员工号、员工名、汇总)
            int totalDays = titleList.size() - (colOffset + 1);
            // 开始日期
            LocalDate startDate = DateUtils.date2LocalDate(searchParam.getWorkDateFrom());
            log.debug("开始日期:{},总天数:{}", startDate, totalDays);


            // 导出数据
            for (int i = 0; i < workSheet.size(); i++) {
                Map item = workSheet.get(i);
                // excel第二行，start with 0
                Row row = templateSheet.getRow(i + 1);

                // 大于第一行时拷贝样式
                if (i > 0) {
                    if (row == null) {
                        row = templateSheet.createRow(i + 1);
                    }
                    ExcelUtils.setRowStyle(ExcelUtils.getRow(templateSheet, 1), row);
                }

                setValue(row.getCell(0), item.get("empNo"));
                setValue(row.getCell(1), item.get("empName"));
                // 日期
                String columnLetter = null;
                LocalDate tmp = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth());
                for (int dayIndex = 0; dayIndex < totalDays; dayIndex++) {
                    Cell cell = row.getCell(colOffset + dayIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    if (i == 0) {
                        // 拷贝样式
                        cell.setCellStyle(row.getCell(colOffset).getCellStyle());
                    }
                    Map dayMap = (Map) item.get("dayMap");
                    BigDecimal bd = null;
                    if (null != dayMap && dayMap.get(tmp.toString()) != null) {
                        Map dayInfo = (Map) dayMap.get(tmp.toString());

                        Object wd = dayInfo.get("workTotal");
                        if (null != wd && !(wd instanceof String)) {
                            bd = (BigDecimal) wd;
                            setValue(cell, bd);
                        }
                    }
                    if (null != bd) {
                        setValue(cell, bd);
                    } else {
                        setValue(cell, null);
                    }
                    tmp = tmp.plusDays(1);
                    columnLetter = CellReference.convertNumToColString(colOffset + dayIndex);
                }
                // 设置汇总
                if (i == 0) {
                    // 最后一列为汇总
                    Cell cell = row.getCell(titleList.size() - 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    // 拷贝样式
                    cell.setCellStyle(row.getCell(colOffset).getCellStyle());
                    cell.setCellFormula("SUM(E2:" + columnLetter + "2)");
                }
                templateSheet.setForceFormulaRecalculation(true);
            }

            // 生成文件
            templatebook.write(fos);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return outputFile;
    }
}
