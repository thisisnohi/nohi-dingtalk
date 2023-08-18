package nohi.demo.mp.config;

import lombok.Data;
import nohi.demo.mp.dto.mp.token.TokenMeta;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author NOHI
 * @program: nohi-dd-minipro-server
 * @description:
 * @create 2021-01-03 20:32
 **/
@Data
@ConfigurationProperties(prefix = "dingtalk.mp")
@Configuration
public class MpConfig {
    private String agentId;
    private String appKey;
    private String appSecret;
    private String dingTalkServer;
    // 管理员Id，同步假期类型时需要
    private String managerId;

    private TokenMeta tokenMeta;
}
