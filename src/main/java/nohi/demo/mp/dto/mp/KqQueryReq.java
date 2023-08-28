package nohi.demo.mp.dto.mp;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "考勤查询", title = "dingtalk", description = "考勤查询")
public class KqQueryReq {
    @Schema(title = "部门ID", description = "流水号3", example = "1")
    private String deptId;
    @Schema(title = "用户ID列表", example = "[\"015919465020484799\"]")
    private List<String> userIdList;
    @Schema(title = "日期开始时间 yyyy-MM-dd HH:mm:ss", description = "日期开始时间 yyyy-MM-dd HH:mm:ss", example = "2021-01-01 08:00:00")
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    private Date workDateFrom;
    @Schema(title = "日期结束时间 yyyy-MM-dd HH:mm:ss", description = "日期结束时间 yyyy-MM-dd HH:mm:ss", example = "2021-01-31 08:00:00")
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    private Date workDateTo;
}
