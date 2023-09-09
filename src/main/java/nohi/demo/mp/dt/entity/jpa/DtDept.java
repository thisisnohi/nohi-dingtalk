package nohi.demo.mp.dt.entity.jpa;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * 组织部门
 * </p>
 *
 * @author nohi
 * @date 2021-01-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "DT_DEPT")
public class DtDept extends OperationTracablePO<String> {
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
     * 部门ID
     */
    @Column(name = "DT_DEPT_ID")
    private String dtDeptId;

    /**
     * 部门名称
     */
    @Column(name = "DT_DEPT_NAME")
    private String dtDeptName;

    /**
     * 企业部门编号
     */
    @Column(name = "EN_DEPT_NO")
    private String enDeptNo;

    /**
     * 父部门ID
     */
    @Column(name = "DT_PAR_DEPT_ID")
    private String dtParDeptId;

}
