package nohi.demo.mp.dto.mp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-15 13:53
 **/
@Data
@ApiModel(description = "考勤查询")
public class KqQueryReq {
    @ApiModelProperty(value = "部门ID", example = "1")
    private String deptId;
    @ApiModelProperty(value = "用户ID列表", example = "015919465020484799")
    private List<String> userIdList;
    @ApiModelProperty(value = "日期开始时间 yyyy-MM-dd HH:mm:ss",required=true, example = "2021-01-01 08:00:00")
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    private Date workDateFrom;
    @ApiModelProperty(value = "日期结束时间 yyyy-MM-dd HH:mm:ss",required=true, example = "2021-01-31 08:00:00")
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    private Date workDateTo;
}
