package nohi.demo.mp.dt.service.kaoqing;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.mp.consts.CommonConsts;
import nohi.demo.mp.dto.mp.kq.UserKqInfoDTO;
import nohi.demo.mp.dto.work.EmpWorkSheet;
import nohi.demo.mp.dto.work.dt.WorkDayClassMeta;
import nohi.demo.mp.dto.work.dt.WorkDayMeta;
import nohi.demo.mp.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


/**
 * <h3>nohi-dd-miniprogram-server</h3>
 *
 * @author NOHI
 * @description <p>员工工时统计</p>
 * @date 2023/09/09 10:13
 **/
@Service
@Slf4j
@Data
public class UserWorkSheetKaoService {

    List<UserKqInfoDTO> dingTalkList;

    /**
     * 项目号-员工工时考勤
     */
    private Map<String, EmpWorkSheet> empSheetMap = Maps.newHashMap();

    public UserWorkSheetKaoService(List<UserKqInfoDTO> dingTalkList) {
        this.dingTalkList = dingTalkList;
        this.init();
    }

    public void init() {
        // 转换考勤信息
        this.dtKq();
    }

    private String getCheckType(String noonType) {
        return CommonConsts.DtNoonType.AM.getKey().equals(noonType) ? CommonConsts.CheckType.ONDUTY.getKey() : CommonConsts.CheckType.OFFDUTY.getKey();
    }

    /**
     * 转换考勤信息
     */
    private void dtKq() {
        log.info("转换考勤信息，共[{}]条", null == dingTalkList ? 0 : dingTalkList.size());
        if (null == dingTalkList || dingTalkList.isEmpty()) {
            log.warn("转换工时信息，工时列表为空");
            return;
        }
        dingTalkList.forEach(item -> {
            LocalDate workDay = DateUtils.date2LocalDate(item.getWorkDate());
            String workDayStr = workDay.toString();
            String empNo = item.getDtUserId();
            String empName = item.getDtUserName();
            log.debug("{} {} {} {} {}", workDayStr, empNo, empName, item.getCheckType(), DateUtils.format(item.getUsercheckTime(), "HH:mm"));
            EmpWorkSheet empWorkSheet = null;
            // 获取员工工时
            if (!empSheetMap.containsKey(empNo)) {
                empWorkSheet = new EmpWorkSheet();
                empWorkSheet.setEmpNo(empNo);
                empWorkSheet.setEmpName(empName);
                empSheetMap.put(empNo, empWorkSheet);
            } else {
                empWorkSheet = empSheetMap.get(empNo);
            }

            WorkDayMeta workDayMeta = empWorkSheet.getEmpWorkDayMap().get(workDayStr);
            if (null == workDayMeta) {
                workDayMeta = new WorkDayMeta();
                workDayMeta.setWorkDay(workDay);
                empWorkSheet.getEmpWorkDayMap().put(workDayStr, workDayMeta);
            }
            workDayMeta.setUserNo(empNo);
            workDayMeta.setUserName(empName);
            // 考勤 -> 当班情况
            WorkDayClassMeta empWorkDayMeta = this.workDayMeta(workDayMeta, item);
            String key = empNo + "-" + workDayStr + "-" + empWorkDayMeta.getCheckType();
        });
    }


    /**
     * 转换考勤
     *
     * @param workDayMeta   工作日
     * @param userKqInfoDTO 考勤情况
     */
    private WorkDayClassMeta workDayMeta(WorkDayMeta workDayMeta, UserKqInfoDTO userKqInfoDTO) {
        WorkDayClassMeta empWorkDayMeta = null;
        // 上班
        if (CommonConsts.CheckType.ONDUTY.getKey().equals(userKqInfoDTO.getCheckType())) {
            empWorkDayMeta = workDayMeta.getOnDuty();
            workDayMeta.setOnDuty(empWorkDayMeta);
        } else { // 下班
            empWorkDayMeta = workDayMeta.getOffDuty();
            workDayMeta.setOffDuty(empWorkDayMeta);
        }
        // 工作日
        empWorkDayMeta.setWorkDay(workDayMeta.getWorkDay());
        empWorkDayMeta.setCheckDate(userKqInfoDTO.getUsercheckTime());
        empWorkDayMeta.setBaseCheckTime(userKqInfoDTO.getBaseCheckTime());
        empWorkDayMeta.setUserNo(workDayMeta.getUserNo());
        empWorkDayMeta.setUserNo(workDayMeta.getUserName());
        return empWorkDayMeta;
    }


}
