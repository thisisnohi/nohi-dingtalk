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
 * 审批实例详情
 * </p>
 *
 * @author nohi
 * @date 2021-01-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dt_apr_proc_inst")
public class DtAprProcInst extends OperationTracablePO<String> {
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
     * 审批实例标题
     */
    @Column(name = "TITLE")
    private String title;

    /**
     * 开始时间
     */
    @Column(name = "CREATE_TIME")
    private Date createTime;

    /**
     * 结束时间
     */
    @Column(name = "FINISH_TIME")
    private Date finishTime;

    /**
     * 发起人的userid
     */
    @Column(name = "ORIGINATOR_USERID")
    private String originatorUserid;

    /**
     * 发起人的部门
     */
    @Column(name = "ORIGINATOR_DEPT_ID")
    private String originatorDeptId;

    /**
     * 审批状态：
NEW：新创建
RUNNING：审批中
TERMINATED：被终止
COMPLETED：完成
CANCELED：取消
     */
    @Column(name = "STATUS")
    private String status;

    /**
     * 审批结果：
agree：同意
refuse：拒绝
     */
    @Column(name = "RESULT")
    private String result;

    /**
     * 审批实例业务编号
     */
    @Column(name = "BUSINESS_ID")
    private String businessId;

    /**
     * 发起部门
     */
    @Column(name = "ORIGINATOR_DEPT_NAME")
    private String originatorDeptName;

    /**
     * 审批实例业务动作：
MODIFY：表示该审批实例是基于原来的实例修改而来
REVOKE：表示该审批实例是由原来的实例撤销后重新发起的
NONE表示正常发起
     */
    @Column(name = "BIZ_ACTION")
    private String bizAction;

    /**
     * 主流程实例标识
     */
    @Column(name = "MAIN_PROCESS_INSTANCE_ID")
    private String mainProcessInstanceId;

}
