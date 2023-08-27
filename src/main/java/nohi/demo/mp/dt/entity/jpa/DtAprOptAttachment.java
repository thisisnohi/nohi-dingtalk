package nohi.demo.mp.dt.entity.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import nohi.demo.common.das.OperationTracablePO;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 操作记录评论附件
 * </p>
 *
 * @author nohi
 * @date 2021-01-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dt_apr_opt_attachment")
public class DtAprOptAttachment extends OperationTracablePO<String> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "ID")
    @GenericGenerator(name = "ID", strategy = "nohi.demo.common.das.CustomGenerationId")
    @Column(name = "ID", nullable = false)
    private String id;

    /**
     * 流程实例ID
     */
    @Column(name = "PROC_INST_ID")
    private String procInstId;

    /**
     * 操作记录ID
     */
    @Column(name = "OPT_RECORD_ID")
    private String optRecordId;

    /**
     * 附件名称
     */
    @Column(name = "FILE_NAME")
    private String fileName;

    /**
     * 附件大小
     */
    @Column(name = "FILE_SIZE")
    private String fileSize;

    /**
     * 附件ID
     */
    @Column(name = "FILE_ID")
    private String fileId;

    /**
     * 附件类型
     */
    @Column(name = "FILE_TYPE")
    private String fileType;

}
