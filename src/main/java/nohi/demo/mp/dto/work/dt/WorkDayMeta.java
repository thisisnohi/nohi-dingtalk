package nohi.demo.mp.dto.work.dt;

import lombok.Data;
import nohi.demo.mp.consts.CommonConsts;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-18 17:35
 **/
@Data
public class WorkDayMeta {
    private String userNo;
    private String userName;

    /**
     * 节假日类型： 0-工作日 1-休息日 2-节假日
     */
    private String holidayFlag;
    /**
     * 工作日
     */
    private LocalDate workDay;
    /**
     * 工作量
     */
    private BigDecimal workTotal;

    /**
     * 上班
     */
    private WorkDayClassMeta onDuty;
    /**
     * 下班
     */
    private WorkDayClassMeta offDuty;

    public WorkDayClassMeta getOnDuty() {
        if (null == onDuty) {
            onDuty = new WorkDayClassMeta();
            onDuty.setCheckType(CommonConsts.CheckType.ONDUTY.getKey());
        }
        return onDuty;
    }

    public void setOnDuty(WorkDayClassMeta onDuty) {
        this.onDuty = onDuty;
    }

    public WorkDayClassMeta getOffDuty() {
        if (null == offDuty) {
            offDuty = new WorkDayClassMeta();
            offDuty.setCheckType(CommonConsts.CheckType.OFFDUTY.getKey());
        }
        return offDuty;
    }

}
