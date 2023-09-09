package nohi.demo.mp.dto.mp.kq;

import lombok.Data;

import java.util.Date;

/**
 * @author NOHI
 * :
 *  2021-01-23 17:23
 **/
@Data
public class UserKqInfoDTO {
    private String dtUserId;
    private String dtUserName;
    private String enUserNo;
    private Date workDate;
    private String checkType;
    private String checkTypeCn;
    private Date baseCheckTime;
    private Date usercheckTime;
    private String timeResult;
    private String timeResultCn;
    private String locationResult;
    private String locationResultCn;
    private String sourceType;
    private String sourceTypeCn;
    private String locationMethod;
    private String userAddress;
    private String procInstId;
    private String aprTitle;
    private String aprStatus;
    private String aprStatusCn;
    private String aprResult;
    private String aprResultCn;
    private String outsideRemark;
}
