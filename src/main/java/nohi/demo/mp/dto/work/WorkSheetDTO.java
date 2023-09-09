package nohi.demo.mp.dto.work;

import lombok.Data;
import nohi.demo.mp.dto.work.dt.WorkDayMeta;

import java.util.List;

/**
 * 员工-工时列表
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-18 13:37
 **/
@Data
public class WorkSheetDTO {
    // 员工号
    private String userNo;
    // 员工名
    private String userName;

    private List<WorkDayMeta> workDayList;

}
