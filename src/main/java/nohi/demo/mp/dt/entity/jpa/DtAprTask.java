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
 * 任务列表
 * </p>
 *
 * @author nohi
 * @date 2021-01-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dt_apr_task")
public class DtAprTask extends OperationTracablePO<String> {
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
     * 任务状态：
	NEW：未启动
	RUNNING：处理中
	PAUSED：暂停
	CANCELED：取消
	COMPLETED：完成
	TERMINATED：终止
     */
    @Column(name = "TASK_STATUS")
    private String taskStatus;

    /**
     * 结果：
	AGREE：同意
	REFUSE：拒绝
	REDIRECTED：转交
     */
    @Column(name = "TASK_RESULT")
    private String taskResult;

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
     * 任务节点ID
     */
    @Column(name = "TASKID")
    private String taskid;

    /**
     * 任务URL
     */
    @Column(name = "URL")
    private String url;

}
