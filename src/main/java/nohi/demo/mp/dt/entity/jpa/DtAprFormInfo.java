package nohi.demo.mp.dt.entity.jpa;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import nohi.demo.common.das.OperationTracablePO;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 表单详情列表
 * </p>
 *
 * @author nohi
 * @date 2021-01-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dt_apr_form_info")
public class DtAprFormInfo extends OperationTracablePO<String> {
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
     * 标签名
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 标签值
     */
    @Column(name = "VALUE")
    private String value;

    /**
     * 标签扩展值
     */
    @Column(name = "EXT_VALUE")
    private String extValue;

    /**
     * 组件ID
     */
    @Column(name = "COMPONENT_ID")
    private String componentId;

    /**
     * 组件类型
     */
    @Column(name = "COMPONENT_TYPE")
    private String componentType;

}
