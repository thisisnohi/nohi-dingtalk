package nohi.demo.mp.dto.work;

import lombok.Data;

import java.util.List;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-18 13:37
 **/
@Data
public class WorkSheetDTO {
    private String projectNo; // 项目号
    private String projectName; // 项目名
    private String office;  // 科室
    private String userNo;  // 员工号
    private String userName; // 员工名

    private List<WorkDayMeta> workDayList;

}
