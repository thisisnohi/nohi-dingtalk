package nohi.demo.mp.dt.entity.jpa;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nohi.demo.common.das.OperationTracablePO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

/**
 * Database Table Remarks:
 * 假期状态
 * <p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "DT_VOCATION_STATUS")
public class DtVocationStatus extends OperationTracablePO<String> {
    /**
     * 主键
     * DT_VOCATION_TYPE.ID
     */
    @Id
    private String id;

    /**
     * 假期类型code
     */
    @Column(name = "LEAVE_CODE")
    private String leaveCode;

    @Column(name = "DURATION_PERCENT")
    private Long durationPercent;
    /**
     * 请假单位：“percent_day”表示天，“percent_hour”表示小时
     */
    @Column(name = "duration_unit")
    private String durationUnit;
    /**
     * 请假结束时间，时间戳
     */
    @Column(name = "END_TIME")
    private Date endTime;

    /**
     * 请假开始时间，时间戳
     */
    @Column(name = "START_TIME")
    private Date startTime;
    /**
     * 用户id
     */
    @Column(name = "USERID")
    private String userid;

}
