package nohi.demo.mp.dto.work;

import com.google.common.collect.Maps;
import lombok.Data;
import nohi.demo.mp.dto.work.dt.WorkDayMeta;

import java.util.Map;

/**
 * 员工工时
 *
 * @author NOHI
 * 2021-01-28 17:51
 **/
@Data
public class EmpWorkSheet {
    /**
     * 员工
     */
    private String empNo;
    private String empName;
    /**
     * 每天工时信息
     * key: 工作日(yyyy-MM-dd)
     */
    private Map<String, WorkDayMeta> empWorkDayMap = Maps.newHashMap();
}
