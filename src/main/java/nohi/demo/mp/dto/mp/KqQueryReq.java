package nohi.demo.mp.dto.mp;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @Schema(title = "日期开始时间 yyyy-MM-dd", description = "日期开始时间 yyyy-MM-dd", example = "2021-01-01")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat( pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date workDateFrom;
    @Schema(title = "日期结束时间 yyyy-MM-dd", description = "日期结束时间 yyyy-MM-dd", example = "2021-01-31")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat( pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date workDateTo;


    private String queryProjectId;
    private String queryProjectName;
    private String queryUserName;
    private String queryUserId;
    private String queryUserNo;
    private String queryProcInstId;
    private String workDateFromStr;
    private String workDateToStr;
    private String queryTimeResultSql;
    private String queryLocationResultSql;
    private String queryDeptSql;

    private List<String> queryDeptIds;
    private List<String> queryTimeResult;
    private List<String> queryLocationResult;

    // 查询待同步审批数据
    private String procInstSql;
}
