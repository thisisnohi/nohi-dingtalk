package nohi.demo.mp.dto.work;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-18 17:35
 **/
@Data
public class WorkDayMeta {
    private Date workStart;
    private Date workEnd;
    private BigDecimal workDays; // 工作量

    private String projectNo; // 项目号
    private String projectName; // 项目名
    private String userNo;  // 员工号
    private String userName; // 员工名


}
