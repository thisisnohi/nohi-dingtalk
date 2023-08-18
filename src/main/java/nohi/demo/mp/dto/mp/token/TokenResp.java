package nohi.demo.mp.dto.mp.token;

import lombok.Data;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-03 21:18
 **/
@Data
public class TokenResp {
    private int errcode;
    private String errmsg; //
    private int expires_in; // 过期时间
    private String access_token; // 过期时间

}
