package nohi.demo.mp.dt.service;

import com.dingtalk.api.response.OapiProcessinstanceGetResponse;
import com.taobao.api.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.config.Knife4jConfig;
import nohi.demo.common.das.JpaCRUDService;
import nohi.demo.common.das.JpaDAO;
import nohi.demo.common.tx.BaseResponse;
import nohi.demo.mp.dt.dao.jpa.DtAprProcInstDao;
import nohi.demo.mp.dt.entity.jpa.DtAprProcInst;
import nohi.demo.mp.dt.service.kaoqing.DtKqInfoService;
import nohi.demo.mp.dto.mp.KqQueryReq;
import nohi.demo.mp.dto.mp.kq.UserKqInfoDTO;
import nohi.demo.mp.service.mp.MpHandler;
import nohi.demo.mp.utils.DateUtils;
import nohi.demo.mp.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 审批实例详情 服务实现类
 * </p>
 *
 * @author nohi
 * @date 2021-01-21
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DtAprProcInstService extends JpaCRUDService<DtAprProcInst, String> {
    private final DtAprProcInstDao dtAprProcInstDao;
    private final DtAprOptRecordService dtAprOptRecordService;
    private final DtAprTaskService dtAprTaskService;
    private final DtAprProcSubInstService dtAprProcSubInstService;
    private final DtAprFormInfoService dtAprFormInfoService;
    private final DtKqInfoService dtKqInfoService;

    @Autowired
    private MpHandler mpHandler;

    @Override
    public JpaDAO<DtAprProcInst, String> getDAO() {
        return dtAprProcInstDao;
    }

    public List<DtAprProcInst> list(DtAprProcInst info) {
        Sort sort = Sort.by(Sort.Direction.ASC, "dtUserid").and(Sort.by(Sort.Direction.ASC, "createTime"));
        return this.findByExample(info, sort);
    }

    /**
     * 获取审批实例信息
     *
     * @param procInstId
     * @return
     * @throws ApiException
     */
    public OapiProcessinstanceGetResponse.ProcessInstanceTopVo getProcessinstance(String procInstId) throws ApiException {
        return mpHandler.getMpService().getProcessInstanceInfo(procInstId);
    }

    /**
     * 同步实例详情
     * @param procInstId
     * @return
     */
    @Transactional
    public BaseResponse syncProcInfo(String procInstId) {
        try {
            OapiProcessinstanceGetResponse.ProcessInstanceTopVo vo = this.getProcessinstance(procInstId);
            // 同步实例信息
            DtAprProcInst inst = this.syncProcInst(procInstId, vo);
            // 同步 操作记录列表
            dtAprOptRecordService.syncOperationRecords(procInstId, vo);
            // 同步任务列表
            dtAprTaskService.syncTasks(procInstId, vo);
            // 同步子实例列表
            dtAprProcSubInstService.syncSubInst(procInstId, vo);
            // 同步表单
            dtAprFormInfoService.syncForm(procInstId, vo);
        } catch (Exception e) {
            log.error("流程实例[{}] 异常:{}", procInstId, e.getMessage() ,e);
            return BaseResponse.error(e.getMessage());
        }

        return null;
    }

    /**
     * 同步实例信息
     * @param procInstId
     * @param vo
     */
    public DtAprProcInst syncProcInst(String procInstId, OapiProcessinstanceGetResponse.ProcessInstanceTopVo vo) {
        // 查询流程实例是否存在
        DtAprProcInst inst = this.findOne(procInstId);
        if (null == inst) {
            inst = new DtAprProcInst();
        }
        JsonUtils.copyProperties(vo, inst);
        inst.setDtUserid(vo.getOriginatorUserid());
        inst.setId(procInstId);
        inst = this.getDAO().saveAndFlush(inst);
        return inst;
    }

    /**
     * 同步考虑审批数据
     *
     * @param info     查询对象
     * @return 响应数据
     */
    public void syncProcInstByData(KqQueryReq info) {
        log.info("查询考勤信息");

        // 转换请求参数
        // 日期
        if (null != info.getWorkDateFrom()) {
            info.setWorkDateFromStr(DateUtils.format(info.getWorkDateFrom(), DateUtils.SIMPLE_DATE));
        }
        if (null != info.getWorkDateTo()) {
            info.setWorkDateToStr(DateUtils.format(info.getWorkDateTo(), DateUtils.SIMPLE_DATE));
        }
        info.setProcInstSql(" di.PROC_INST_ID is not null and ( pi.RESULT != 'agree' or pi.RESULT is null )");
        /** 查询流程数据 **/
        List<UserKqInfoDTO> list = dtKqInfoService.dingTalkPageList(info);
        log.info("执行同步审批数据[{}]", list.size());
        CountDownLatch cd = new CountDownLatch(list.size());
        // 循环列表，查询审批数据
        list.parallelStream().forEach( item -> {
            String subTitle = String.format("工作日[%s][%s]", item.getWorkDate(), item.getProcInstId());
            try {
                DtAprProcInstService service = Knife4jConfig.SpringContextUtils.getBean(DtAprProcInstService.class);
                service.syncProcInfo(item.getProcInstId());
            } catch (Exception e) {
                log.error("{} 异常:{}", subTitle, e.getMessage());
            } finally {
                cd.countDown();
            }
        });

        try {
            boolean flag = cd.await(30, TimeUnit.SECONDS);
            log.debug("flag:{}", flag);
        } catch (Exception e) {
            log.error("同步审批数据异常:{}", e.getMessage());
        }

        log.info("执行同步审批数据结束");
    }


}
