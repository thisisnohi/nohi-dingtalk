package nohi.demo.mp.dto.mp.token;

import lombok.Data;

import java.util.Date;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-03 21:26
 **/
@Data
public class TokenMeta {
    private Long expiresIn; // 过期时间
    private String accessToken; // 过期时间
    private Date startDate;
}
