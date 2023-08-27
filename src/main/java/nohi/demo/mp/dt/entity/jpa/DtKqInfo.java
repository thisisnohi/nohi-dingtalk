package nohi.demo.mp.dt.entity.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import java.util.Date;

import nohi.demo.common.das.OperationTracablePO;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 考勤数据
 * </p>
 *
 * @author nohi
 * @date 2021-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dt_kq_info")
public class DtKqInfo extends OperationTracablePO<String> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @Column(name = "ID", nullable = false)
    private String id;

    /**
     * 员工标识
     */
    @Column(name = "DT_USERID")
    private String dtUserid;

    /**
     * 工作日
     */
    @Column(name = "WORK_DATE")
    private Date workDate;

    /**
     * 打卡记录ID
     */
    @Column(name = "RECORD_ID")
    private String recordId;

    /**
     * 考勤组ID
     */
    @Column(name = "GROUP_ID")
    private String groupId;

    /**
     * 数据来源
     * ATM：考勤机
     * BEACON：IBeacon
     * DING_ATM：钉钉考勤机
     * USER：用户打卡
     * BOSS：老板改签
     * APPROVE：审批系统
     * SYSTEM：考勤系统
     * AUTO_CHECK：自动打卡
     */
    @Column(name = "SOURCE_TYPE")
    private String sourceType;

    /**
     * 实际打卡时间
     */
    @Column(name = "USERCHECK_TIME")
    private Date usercheckTime;

    /**
     * 计算迟到和早退，基准时间；也可作为排班打卡时间
     */
    @Column(name = "BASE_CHECK_TIME")
    private Date baseCheckTime;

    /**
     * 打卡结果 Normal：正常
     * Early：早退
     * Late：迟到
     * SeriousLate：严重迟到
     * Absenteeism：旷工迟到
     * NotSigned：未打卡
     */
    @Column(name = "TIME_RESULT")
    private String timeResult;

    /**
     * 位置结果 Normal：范围内
     * Outside：范围外
     * NotSigned：未打卡
     * checkType	String	OnDuty	考勤类型：
     * OnDuty：上班
     * OffDuty：下班
     */
    @Column(name = "LOCATION_RESULT")
    private String locationResult;

    /**
     * 考勤类型：
     * OnDuty：上班
     * OffDuty：下班
     */
    @Column(name = "CHECK_TYPE")
    private String checkType;


    /**
     * 关联的审批实例ID，当该字段非空时，表示打卡记录与请假、加班等审批有关。可以与获取审批实例详情配合使用。
     */
    @Column(name = "PROC_INST_ID")
    private String procInstId;

    /**
     * 关联的审批ID，当该字段非空时，表示打卡记录与请假、加班等审批有关。
     */
    @Column(name = "APPROVE_ID")
    private String approveId;

    /**
     * 排班ID
     */
    @Column(name = "PLAN_ID")
    private String planId;
}
