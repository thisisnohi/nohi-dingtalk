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
 * 考勤打卡详情
 * </p>
 *
 * @author nohi
 * @date 2021-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dt_kq_detail")
public class DtKqDetail extends OperationTracablePO<String> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @Column(name = "ID", nullable = false)
    private String id;

    /**
     * 用户打卡定位精度
     */
    @Column(name = "USER_ACCURACY")
    private String userAccuracy;

    /**
     * 用户打卡纬度
     */
    @Column(name = "USER_LATITUDE")
    private String userLatitude;

    /**
     * 用户打卡经度
     */
    @Column(name = "USER_LONGITUDE")
    private String userLongitude;

    /**
     * 用户打卡地址
     */
    @Column(name = "USER_ADDRESS")
    private String userAddress;

    /**
     * 打卡设备ID
     */
    @Column(name = "DEVICE_ID")
    private String deviceId;

    /**
     * 定位方法
     */
    @Column(name = "LOCATION_METHOD")
    private String locationMethod;

    /**
     * 是否合法
     */
    @Column(name = "IS_LEGAL")
    private String isLegal;

    /**
     * 实际打卡时间
     */
    @Column(name = "USERCHECK_TIME")
    private Date usercheckTime;

    /**
     * 计算迟到和早退，基准时间；也可作为排班打卡时间
     */
    @Column(name = "BASE_CHECK_TIME")
    private Date baseCheckTime;

    /**
     * 打卡结果 Normal：正常
     * Early：早退
     * Late：迟到
     * SeriousLate：严重迟到
     * Absenteeism：旷工迟到
     * NotSigned：未打卡
     */
    @Column(name = "TIME_RESULT")
    private String timeResult;

    /**
     * 位置结果 Normal：范围内
     * Outside：范围外
     * NotSigned：未打卡
     * checkType	String	OnDuty	考勤类型：
     * OnDuty：上班
     * OffDuty：下班
     */
    @Column(name = "LOCATION_RESULT")
    private String locationResult;

    /**
     * 数据来源
     * ATM：考勤机
     * BEACON：IBeacon
     * DING_ATM：钉钉考勤机
     * USER：用户打卡
     * BOSS：老板改签
     * APPROVE：审批系统
     * SYSTEM：考勤系统
     * AUTO_CHECK：自动打卡
     */
    @Column(name = "SOURCE_TYPE")
    private String sourceType;

    /**
     * 打卡人的userid
     */
    @Column(name = "USER_ID")
    private String userId;

    /**
     * 工作日
     */
    @Column(name = "WORK_DATE")
    private Date workDate;

    /**
     * 考勤组ID
     */
    @Column(name = "GROUP_ID")
    private String groupId;

    /**
     * 用户打卡wifi SSID
     */
    @Column(name = "USER_SSID")
    private String userSsid;

    /**
     * 用户打卡wifi Mac地址
     */
    @Column(name = "USER_MAC_ADDR")
    private String userMacAddr;

    /**
     * 基准地址
     */
    @Column(name = "BASE_ADDRESS")
    private String baseAddress;

    /**
     * 基准经度
     */
    @Column(name = "BASE_LONGITUDE")
    private String baseLongitude;

    /**
     * 基准纬度
     */
    @Column(name = "BASE_LATITUDE")
    private String baseLatitude;

    /**
     * 基准定位精度
     */
    @Column(name = "BASE_ACCURACY")
    private String baseAccuracy;

    /**
     * 基准wifi ssid
     */
    @Column(name = "BASE_SSID")
    private String baseSsid;

    /**
     * 基准Mac地址
     */
    @Column(name = "BASE_MAC_ADDR")
    private String baseMacAddr;

    /**
     * 打卡创建时间
     */
    @Column(name = "GMT_CREATE")
    private Date gmtCreate;

    /**
     * 打卡备注
     */
    @Column(name = "OUTSIDE_REMARK")
    private String outsideRemark;

    /**
     * 打卡设备序列号
     */
    @Column(name = "DEVICE_SN")
    private String deviceSn;

}
