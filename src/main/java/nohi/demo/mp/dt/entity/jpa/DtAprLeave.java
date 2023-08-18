package nohi.demo.mp.dt.entity.jpa;

import javax.persistence.Entity;
import javax.persistence.Column;
import java.math.BigDecimal;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import java.util.Date;
import nohi.demo.common.das.OperationTracablePO;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 请假信息
 * </p>
 *
 * @author nohi
 * @date 2021-01-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dt_apr_leave")
public class DtAprLeave extends OperationTracablePO<String> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "ID")
    @GenericGenerator(name = "ID", strategy = "uuid2")
    @Column(name = "ID", nullable = false)
    private String id;

    /**
     * 员工标识
     */
    @Column(name = "DT_USERID")
    private String dtUserid;

    /**
     * 请假单位：
percent_day：表示天
percent_hour：表示小时
     */
    @Column(name = "DURATION_UNIT")
    private String durationUnit;

    /**
     * 假期时长*100，例如用户请假时长为1天，该值就等于100。
     */
    @Column(name = "DURATION_PERCENT")
    private BigDecimal durationPercent;

    /**
     * 成员人数
     */
    @Column(name = "MEMBER_COUNT")
    private Integer memberCount;

    /**
     * 考勤组主负责人
     */
    @Column(name = "OWNER_USER_ID")
    private String ownerUserId;

    /**
     * 请假开始时间，Unix时间戳。
     */
    @Column(name = "START_TIME")
    private Date startTime;

    /**
     * 请假结束时间，Unix时间戳。
     */
    @Column(name = "END_TIME")
    private Date endTime;

}