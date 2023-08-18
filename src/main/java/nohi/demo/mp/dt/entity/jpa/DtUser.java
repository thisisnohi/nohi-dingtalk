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
 * 人员表
 * </p>
 *
 * @author nohi
 * @date 2021-01-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "DT_USER")
public class DtUser extends OperationTracablePO<String> {
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
     * 名称
     */
    @Column(name = "DT_USERNAME")
    private String dtUsername;

    /**
     * UNIONID
     */
    @Column(name = "DT_UNIONID")
    private String dtUnionid;

    /**
     * 是否激活： Y-是  N-否
     */
    @Column(name = "DT_ACTIVE")
    private String dtActive;

    /**
     * 是否允许获取数据： Y-是  N-否
     */
    @Column(name = "GET_PRI_IND")
    private String getPriInd;

    /**
     * 企业员工编号
     */
    @Column(name = "EN_USERNO")
    private String enUserno;

}
