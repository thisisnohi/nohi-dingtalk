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
 * 机构表
 * </p>
 *
 * @author nohi
 * @date 2021-01-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "c_org")
public class COrg extends OperationTracablePO<String> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "ID")
    @GenericGenerator(name = "ID", strategy = "uuid2")
    @Column(name = "ID", nullable = false)
    private String id;

    /**
     * 机构号
     */
    @Column(name = "ORG_NO")
    private String orgNo;

    /**
     * 机构名
     */
    @Column(name = "ORG_NAME")
    private String orgName;

    /**
     * 上级机构号
     */
    @Column(name = "PAR_ORG_NO")
    private String parOrgNo;

    /**
     * 上级机构名
     */
    @Column(name = "PAR_ORG_NAME")
    private String parOrgName;

    /**
     * 机构级别
     */
    @Column(name = "ORG_LVL")
    private String orgLvl;

    /**
     * 省分行
     */
    @Column(name = "ORG_LVL_ONE")
    private String orgLvlOne;

    /**
     * 市分行
     */
    @Column(name = "ORG_LVL_TWO")
    private String orgLvlTwo;

    /**
     * 支行
     */
    @Column(name = "ORG_LVL_THR")
    private String orgLvlThr;

    /**
     * 网点
     */
    @Column(name = "ORG_LVL_FOUR")
    private String orgLvlFour;

    /**
     * 省
     */
    @Column(name = "PROV")
    private String prov;

    /**
     * 市
     */
    @Column(name = "CITY")
    private String city;

    /**
     * 区县
     */
    @Column(name = "REGION")
    private String region;

    /**
     * 机构地址
     */
    @Column(name = "ORG_ADDR")
    private String orgAddr;

    /**
     * 机构电话
     */
    @Column(name = "ORG_PHONE")
    private String orgPhone;

    /**
     * 联系人名称
     */
    @Column(name = "LINKMAN_NAME")
    private String linkmanName;

    /**
     * 联系人员工号
     */
    @Column(name = "LINKMAN_NO")
    private String linkmanNo;

    /**
     * 联系人手机
     */
    @Column(name = "LINKMAN_PHONE")
    private String linkmanPhone;

}
