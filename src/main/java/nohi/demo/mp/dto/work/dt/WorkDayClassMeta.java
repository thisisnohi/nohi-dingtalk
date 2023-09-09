package nohi.demo.mp.dto.work.dt;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

/**
 * 每日当班情况
 *    记录员工、上班/下班 打卡时间 所属项目
 * @author NOHI
 *  2021-01-18 17:35
 **/
@Data
public class WorkDayClassMeta {
    private String userNo;
    private String userName;

    /**
     * 工作日
     */
    private LocalDate workDay;
    /**
     * 打卡时间
     */
    private Date checkDate;
    /**
     * 打卡基准时间
     */
    private Date baseCheckTime;
    /**
     * 上班班类型
     * 打卡类型
     */
    private String checkType;


}
