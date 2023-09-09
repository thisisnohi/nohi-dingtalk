package nohi.demo.mp.dto.work;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description: 员工工作信息
 * @create 2021-01-18 13:37
 **/
@Data
public class EmpWorkDayMeta {
    @ExcelProperty("员工号")
    private String userNo;
    @ExcelProperty("员工名")
    private String userName;
    @ExcelProperty("工作日")
    @DateTimeFormat("M/d")
    private Date workDay;
    @DateTimeFormat("HH:mm")
    @ExcelProperty("开始时间")
    private Date workStart;
    @DateTimeFormat("HH:mm")
    @ExcelProperty("结束时间")
    private Date workEnd;
    @ExcelProperty("工作量")
    @NumberFormat("#.##")
    private BigDecimal workDays; // 工作量

    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    private String ignore;
}
