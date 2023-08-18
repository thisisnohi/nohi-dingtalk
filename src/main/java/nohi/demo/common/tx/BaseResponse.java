package nohi.demo.common.tx;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-06 20:20
 **/
@Data
public class BaseResponse<T> implements Serializable {
    public interface ResCode {
        String SUC = "0"; // 成功
        String ERROR = "1"; // 失败
    }
    private String reqId;
    private String resCode;
    private String resMsg;
    private Date respDate;
    private T data;

    public BaseResponse() {

    }
    public BaseResponse(String resCode, String resMsg) {
        this.resCode = resCode;
        this.resMsg = resMsg;
    }
    public static BaseResponse error(String resMsg) {
        return new BaseResponse<String>(ResCode.ERROR, resMsg);
    }
    public static BaseResponse suc(String resMsg) {
        return new BaseResponse<String>(ResCode.SUC, resMsg);
    }
    public static BaseResponse newCode(String resCode, String resMsg) {
        if (StringUtils.isBlank(resCode)) {
            return new BaseResponse<String>(ResCode.SUC, resMsg);
        }
        return new BaseResponse<String>(resCode, resMsg);
    }
}
