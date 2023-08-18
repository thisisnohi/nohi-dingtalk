package nohi.demo.mp.service.mp;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.mp.config.MpConfig;
import nohi.demo.mp.config.SpringContextUtils;
import nohi.demo.mp.consts.DingTalkConsts;
import nohi.demo.mp.dto.mp.token.TokenMeta;
import nohi.demo.mp.utils.DateUtils;
import nohi.demo.mp.utils.JsonUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-03 21:28
 **/
@Slf4j
public class MpServiceImpl {
    private MpConfig mpConfig;
    private DingTalkClientService client;

    public MpServiceImpl(MpConfig mpConfig) {
        this.mpConfig = mpConfig;
        this.client = SpringContextUtils.getBean(DingTalkClientService.class);
    }

    private boolean checkToken() {
        if (null == mpConfig) {
            log.error("请检查应用配置是否存在");
            throw new IllegalArgumentException("请检查应用配置是否存在");
        }
        if (null == mpConfig.getTokenMeta()) {
            log.error("token不存在，重新获取...");
            return false;
        }
        Date now = new Date();
        Date src = mpConfig.getTokenMeta().getStartDate();
        if (src != null) {
            if (mpConfig.getTokenMeta().getExpiresIn() - (now.getTime() - src.getTime()) / 1000 > 10) {
                return true;
            } else {
                log.warn("token在10秒内过期(startDate:{})，需要重新刷新", DateUtils.format(mpConfig.getTokenMeta().getStartDate()));
                return false;
            }
        }
        return true;
    }

    /**
     * 检查token,如果快过期，重新获取
     */
    public TokenMeta getToken() throws ApiException {
        // 判断是否存在token, 以及token是否快过期
        if (!checkToken()) {
            OapiGettokenRequest req = new OapiGettokenRequest();
            req.setAppkey(mpConfig.getAppKey());
            req.setAppsecret(mpConfig.getAppSecret());
            req.setHttpMethod(DingTalkConsts.HTTP_METHOD_GET);
            OapiGettokenResponse rsp = this.client.getToken(DingTalkConsts.API_GETTOKEN, req);
            log.debug("resp:{}", JSONObject.toJSONString(rsp));
            if (rsp.getErrorCode().equals(DingTalkConsts.RespCode.SUC.getKey())) { // 响应成功
                TokenMeta tokenMeta = new TokenMeta();
                mpConfig.setTokenMeta(tokenMeta);
                tokenMeta.setAccessToken(rsp.getAccessToken());
                tokenMeta.setExpiresIn(rsp.getExpiresIn());
                tokenMeta.setStartDate(new Date());

                return tokenMeta;
            }
        }
        return mpConfig.getTokenMeta();
    }

    /**
     * 获取部门数据
     *
     * @return
     */
    public List<OapiDepartmentListResponse.Department> getDepts() throws ApiException {
        OapiDepartmentListRequest req = new OapiDepartmentListRequest();
        req.setHttpMethod(DingTalkConsts.HTTP_METHOD_GET);
        OapiDepartmentListResponse rsp = client.execute(DingTalkConsts.API_DEPARTMENT_LIST, req, OapiDepartmentListResponse.class);
        log.debug("rsp:{}", JsonUtils.toJson(rsp));
        return rsp.getDepartment();
    }

    /**
     * 获取部门用户
     *
     * @return
     * @throws ApiException
     */
    public Object getDeptsUser() throws ApiException {
        OapiV2UserListRequest req = new OapiV2UserListRequest();
        req.setDeptId(1L);
        req.setCursor(0L);
        req.setSize(1L);
        OapiV2UserListResponse rsp = client.execute(DingTalkConsts.API_USER_LIST, req, OapiV2UserListResponse.class);
        log.debug("rsp:{}", JsonUtils.toJson(rsp));
        return rsp;
    }

    /**
     * 获取部门用户
     *
     * @return
     * @throws ApiException
     */
    public List<OapiV2UserListResponse.ListUserResponse> getDeptsAllUser(Long deptId) throws ApiException {
        long cursor = 0L;
        long size = 10L;
        List<OapiV2UserListResponse.ListUserResponse> list = Lists.newArrayList();
        while (true) {
            OapiV2UserListResponse.PageResult result = this.getDeptsUser(deptId, cursor, size);
            if (null != result && null != result.getList()) {
                list.addAll(result.getList());
            }
            if (null == result || !result.getHasMore()) {
                break;
            }
            // 下一游标
            cursor = result.getNextCursor();
        }
        return list;
    }

    /**
     * 获取分页数据
     *
     * @param deptId
     * @param cursor
     * @param size
     * @return
     * @throws ApiException
     */
    public OapiV2UserListResponse.PageResult getDeptsUser(Long deptId, long cursor, long size) throws ApiException {
        OapiV2UserListRequest req = new OapiV2UserListRequest();
        req.setDeptId(deptId);
        req.setCursor(cursor);
        req.setSize(size);
        OapiV2UserListResponse rsp = client.execute(DingTalkConsts.API_USER_LIST, req, OapiV2UserListResponse.class);
        log.debug("rsp:{}", JsonUtils.toJson(rsp));
        return rsp.getResult();
    }


    /**
     * 获取部门用户
     *
     * @return
     * @throws ApiException
     */
    public List<OapiAttendanceGetsimplegroupsResponse.AtGroupForTopVo> getKqGroup() throws ApiException {
        long cursor = 0L;
        long size = 10L;
        List<OapiAttendanceGetsimplegroupsResponse.AtGroupForTopVo> list = Lists.newArrayList();
        while (true) {
            OapiAttendanceGetsimplegroupsResponse.AtGroupListForTopVo result = this.getKqGroup(cursor, size);
            if (null != result && null != result.getGroups()) {
                list.addAll(result.getGroups());
            }
            if (null == result || !result.getHasMore()) {
                break;
            }
            // 下一游标
            cursor = list.size();
        }
        return list;
    }

    /**
     * 获取考勤组
     *
     * @param cursor
     * @param size
     * @return
     * @throws ApiException
     */
    public OapiAttendanceGetsimplegroupsResponse.AtGroupListForTopVo getKqGroup(long cursor, long size) throws ApiException {
        OapiAttendanceGetsimplegroupsRequest req = new OapiAttendanceGetsimplegroupsRequest();
        req.setOffset(cursor);
        req.setSize(size);
        OapiAttendanceGetsimplegroupsResponse rsp = client.execute(DingTalkConsts.API_ATTENDANCE_GET_SIMPLE_GROUPS, req, OapiAttendanceGetsimplegroupsResponse.class);
        log.debug("rsp:{}", JsonUtils.toJson(rsp));
        return rsp.getResult();
    }

    /**
     * 获取考勤数据
     *
     * @return
     */
    public List<OapiAttendanceListResponse.Recordresult> getKqAll(List<String> userIdList, Date workDateFrom, Date workDateTo) throws Exception {

        if (null == userIdList || userIdList.isEmpty()) {
            throw new Exception("用户列表不能为空");
        }
        if (null == workDateFrom || null == workDateTo) {
            throw new Exception("起止日期不能为空");
        }
        List<OapiAttendanceListResponse.Recordresult> list = Lists.newArrayList();
        // 转换日期
        LocalDate start = DateUtils.date2LocalDate(workDateFrom);
        LocalDate end = DateUtils.date2LocalDate(workDateTo);
        // 2021-01-03 08:00:00 YYYYMMddHHmmssSSS
        DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("YYYY-MM-dd");
        while (!end.isBefore(start)) {
            String workDateFromStr = start.format(dateformat) + " 00:00:00";
            String workDateToStr = null;
            start = start.plusDays(6);
            if (start.isBefore(end)) {
                workDateToStr = start.format(dateformat);
            } else {
                workDateToStr = end.format(dateformat);
            }
            start = start.plusDays(1);
            workDateToStr += " 23:59:59";
            log.info(String.format("[%s]-[%s]", workDateFromStr, workDateToStr));

            // 获取日期内所有数据
            list.addAll(this.getKqAllByDate(userIdList, workDateFromStr, workDateToStr));

        }

        return list;
    }

    /**
     * 获取考勤数据
     *
     * @return
     */
    public List<OapiAttendanceListResponse.Recordresult> getKqAllByDate(List<String> userIdList, String workDateFrom, String workDateTo) throws Exception {
        if (userIdList.size() > 50) {
            log.error("查询用户数量不能超过50");
            throw new Exception("查询用户数量不能超过50");
        }
        List<OapiAttendanceListResponse.Recordresult> list = Lists.newArrayList();
        long offset = 0L;
        long size = 10L;
        while (true) {
            OapiAttendanceListResponse result = this.getKq(offset, size, userIdList, workDateFrom, workDateTo);
            if (null != result && null != result.getRecordresult()) {
                list.addAll(result.getRecordresult());
            }
            if (null == result || !result.getHasMore()) {
                break;
            }
            // 下一游标
            offset = list.size();
        }
        return list;
    }

    /**
     * 获取考勤数据
     *
     * @return
     */
    public OapiAttendanceListResponse getKq(long offset, long limit, List<String> userIdList, String workDateFrom, String workDate) throws ApiException {
        OapiAttendanceListRequest req = new OapiAttendanceListRequest();
        req.setLimit(limit);
        req.setOffset(offset);
        req.setWorkDateFrom(workDateFrom);
        req.setWorkDateTo(workDate);
        req.setUserIdList(userIdList);
        OapiAttendanceListResponse rsp = client.execute(DingTalkConsts.API_ATTENDANCE_LIST, req, OapiAttendanceListResponse.class);
        log.debug("rsp:{}", JsonUtils.toJson(rsp));
        return rsp;
    }


    /**
     * 获取考勤详情
     *
     * @return
     */
    public List<OapiAttendanceListRecordResponse.Recordresult> getKqDetail(List<String> userIdList, Date workDateFrom, Date workDateTo) throws Exception {
        if (null == userIdList || userIdList.isEmpty()) {
            throw new Exception("用户列表不能为空");
        }
        if (null == workDateFrom || null == workDateTo) {
            throw new Exception("起止日期不能为空");
        }
        List<OapiAttendanceListRecordResponse.Recordresult> list = Lists.newArrayList();
        // 转换日期
        LocalDate start = DateUtils.date2LocalDate(workDateFrom);
        LocalDate end = DateUtils.date2LocalDate(workDateTo);
        // 2021-01-03 08:00:00 YYYYMMddHHmmssSSS
        DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("YYYY-MM-dd");
        while (!end.isBefore(start)) {
            String workDateFromStr = start.format(dateformat) + " 00:00:00";
            String workDateToStr = null;
            start = start.plusDays(6);
            if (start.isBefore(end)) {
                workDateToStr = start.format(dateformat);
            } else {
                workDateToStr = end.format(dateformat);
            }
            start = start.plusDays(1);
            workDateToStr += " 23:59:59";
            log.info(String.format("[%s]-[%s]", workDateFromStr, workDateToStr));

            // 获取日期内所有数据
            list.addAll(this.getKqDetailAllByDate(userIdList, workDateFromStr, workDateToStr));

        }

        return list;
    }

    /**
     * 获取考勤数据
     *
     * @return
     */
    public List<OapiAttendanceListRecordResponse.Recordresult> getKqDetailAllByDate(List<String> userIdList, String workDateFrom, String workDateTo) throws Exception {
        if (userIdList.size() > 50) {
            log.error("查询用户数量不能超过50");
            throw new Exception("查询用户数量不能超过50");
        }
        List<OapiAttendanceListRecordResponse.Recordresult> list = Lists.newArrayList();
        OapiAttendanceListRecordResponse result = this.getKqDetail(userIdList, workDateFrom, workDateTo);
        if (null != result && null != result.getRecordresult()) {
            list.addAll(result.getRecordresult());
        }
        return list;
    }

    /**
     * 获取考勤详情
     *
     * @return
     */
    public OapiAttendanceListRecordResponse getKqDetail(List<String> userIdList, String workDateFrom, String workDate) throws ApiException {
        OapiAttendanceListRecordRequest req = new OapiAttendanceListRecordRequest();
        req.setUserIds(userIdList);
        req.setCheckDateFrom(workDateFrom);
        req.setCheckDateTo(workDate);
        req.setIsI18n(false);
        OapiAttendanceListRecordResponse rsp = client.execute(DingTalkConsts.API_ATTENDANCE_LIST_RECORD, req, OapiAttendanceListRecordResponse.class);
        log.debug("rsp:{}", JsonUtils.toJson(rsp));
        return rsp;
    }


    /**
     * 获取考勤详情
     *
     * @return
     */
    public OapiProcessinstanceGetResponse.ProcessInstanceTopVo getProcessInstanceInfo(String processInstanceId) throws ApiException {
        OapiProcessinstanceGetResponse resp = this.getProcessinstance(processInstanceId);
        if (null == resp) {
            log.error("返回为空");
            throw new ApiException(resp.getErrmsg());
        }
        if (DingTalkConsts.RespCode.SUC.getKey().equals(resp.getErrorCode())) {
            return resp.getProcessInstance();
        } else {
            log.error("error:{}", resp.getErrmsg());
            throw new ApiException(resp.getErrmsg());
        }
    }

    /**
     * 获取考勤详情
     *
     * @return
     */
    public OapiProcessinstanceGetResponse getProcessinstance(String processInstanceId) throws ApiException {
        OapiProcessinstanceGetRequest req = new OapiProcessinstanceGetRequest();
        req.setProcessInstanceId(processInstanceId);
        return client.execute(DingTalkConsts.API_GET_PROCESS_INSTANCE, req, OapiProcessinstanceGetResponse.class);
    }

    /**
     * 获取用户所有请休假
     * @param userIdList
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    public List<OapiAttendanceGetleavestatusResponse.LeaveStatusVO> geAlltLeavesSatus(List<String> userIdList, Date startTime, Date endTime) throws Exception {
        if (userIdList.size() > 100) {
            log.error("查询用户数量不能超过100");
            throw new Exception("查询用户数量不能超过100");
        }
        List<OapiAttendanceGetleavestatusResponse.LeaveStatusVO> list = Lists.newArrayList();
        long offset = 0L;
        long size = 10L;
        while (true) {
            OapiAttendanceGetleavestatusResponse.LeaveStatusListVO result = this.getLeavesSatus(offset, size, userIdList, startTime.getTime(), endTime.getTime());
            if (null != result && null != result.getLeaveStatus()) {
                list.addAll(result.getLeaveStatus());
            }
            if (null == result || !result.getHasMore()) {
                break;
            }
            // 下一游标
            offset = list.size();
        }
        return list;
    }

    /**
     * 获取考勤详情
     *
     * @return
     */
    public OapiAttendanceGetleavestatusResponse.LeaveStatusListVO getLeavesSatus(long offset, long size, List<String> userIdList, long startTime, long endTime) throws ApiException {
        OapiAttendanceGetleavestatusResponse resp = this.apiGtLeavesSatus(offset, size, userIdList, startTime, endTime);
        if (null == resp) {
            log.error("返回为空");
            throw new ApiException(resp.getErrmsg());
        }
        if (DingTalkConsts.RespCode.SUC.getKey().equals(resp.getErrorCode())) {
            return resp.getResult();
        } else {
            log.error("error:{}", resp.getErrmsg());
            throw new ApiException(resp.getErrmsg());
        }
    }
    /**
     * 获取考勤详情
     *
     * @return
     */
    public OapiAttendanceGetleavestatusResponse apiGtLeavesSatus(long offset, long size, List<String> userIdList, long startTime, long endTime) throws ApiException {
        OapiAttendanceGetleavestatusRequest req = new OapiAttendanceGetleavestatusRequest();
        req.setUseridList(Joiner.on(",").join(userIdList));
        req.setStartTime(startTime);
        req.setEndTime(endTime);
        req.setOffset(offset);
        req.setSize(size);
        return client.execute(DingTalkConsts.API_ATTENDANCE_GET_LEAVES_STATUS, req, OapiAttendanceGetleavestatusResponse.class);
    }

    /**
     * 获取假期类型
     *
     * @return 列表
     */
    public OapiAttendanceVacationTypeListResponse apiGetVocationType(String operatId) throws ApiException {
        OapiAttendanceVacationTypeListRequest req = new OapiAttendanceVacationTypeListRequest();
        req.setOpUserid(operatId);
        // 所有假期类型
        req.setVacationSource("all");
        return client.execute(DingTalkConsts.API_ATTENDANCE_GET_VOCATION_TYPE_, req, OapiAttendanceVacationTypeListResponse.class);
    }
}
