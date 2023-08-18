package nohi.demo.mp.dto.org;

import lombok.Builder;
import lombok.Data;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-17 18:27
 **/
@Data
@Builder
public class QueryWdReq {
    private String region;
    private String orgNo;
    private String orgName;
}
