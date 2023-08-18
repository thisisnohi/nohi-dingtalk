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
 * 考勤组
 * </p>
 *
 * @author nohi
 * @date 2021-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dt_kq_group")
public class DtKqGroup extends OperationTracablePO<String> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "ID")
    @Column(name = "ID", nullable = false)
    private String id;

    /**
     * 考勤组名称
     */
    @Column(name = "GROUP_NAME")
    private String groupName;

    /**
     * 工作日
     */
    @Column(name = "CLASS_LIST")
    private String classList;

    /**
     * 考勤类型。
FIXED为固定排班
TURN为轮班排班
NONE为无班次
     */
    @Column(name = "TYPE")
    private String type;

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

}
