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
 * 部门人员关联表
 * </p>
 *
 * @author nohi
 * @date 2021-01-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "DT_USER_DEPT_REL")
public class DtUserDeptRel extends OperationTracablePO<String> {
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
     * 员工标识
     */
    @Column(name = "DT_USERID")
    private String dtUserid;

    /**
     * 部门ID
     */
    @Column(name = "DT_DEPT_ID")
    private String dtDeptId;

}
