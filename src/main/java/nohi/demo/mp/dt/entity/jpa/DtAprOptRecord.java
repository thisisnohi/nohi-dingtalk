package nohi.demo.mp.dt.entity.jpa;

import javax.persistence.Entity;
import javax.persistence.Column;
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
 * 操作记录列表
 * </p>
 *
 * @author nohi
 * @date 2021-01-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dt_apr_opt_record")
public class DtAprOptRecord extends OperationTracablePO<String> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @Column(name = "ID", nullable = false)
    private String id;

    /**
     * 流程实例ID
     */
    @Column(name = "PROC_INST_ID")
    private String procInstId;

    /**
     * 操作人
     */
    @Column(name = "DT_USERID")
    private String dtUserid;

    /**
     * 操作时间
     */
    @Column(name = "DATE")
    private Date date;

    /**
     * 操作类型：
EXECUTE_TASK_NORMAL：正常执行任务
EXECUTE_TASK_AGENT：代理人执行任务
APPEND_TASK_BEFORE：前加签任务
APPEND_TASK_AFTER：后加签任务
REDIRECT_TASK：转交任务
START_PROCESS_INSTANCE：发起流程实例
TERMINATE_PROCESS_INSTANCE：终止(撤销)流程实例
FINISH_PROCESS_INSTANCE：结束流程实例
ADD_REMARK：添加评论
redirect_process：审批退回
     */
    @Column(name = "OPERATION_TYPE")
    private String operationType;

    /**
     * 操作结果：
AGREE：同意
REFUSE：拒绝
NONE
     */
    @Column(name = "OPERATION_RESULT")
    private String operationResult;

    /**
     * 评论内容。审批操作附带评论时才返回该字段。
     */
    @Column(name = "REMARK")
    private String remark;

}
