package nohi.demo.mp.service.mp;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.taobao.api.ApiException;
import com.taobao.api.BaseTaobaoRequest;
import com.taobao.api.TaobaoResponse;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.mp.config.MpConfig;
import nohi.demo.mp.consts.DingTalkConsts;
import nohi.demo.mp.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-04 21:11
 **/
@Service
@Slf4j
public class DingTalkClientService {
    @Autowired
    private MpConfig mpConfig;

    public OapiGettokenResponse getToken(String url, OapiGettokenRequest req) throws ApiException {
        DefaultDingTalkClient client = new DefaultDingTalkClient(mpConfig.getDingTalkServer() + url);
        log.debug("获取Token请求[{}]，报文:{}", url, JsonUtils.toJson(req));
        OapiGettokenResponse response = client.execute(req);
        log.debug("获取Token请求[{}]，响应:{}", url, JsonUtils.toJson(response));
        return response;
    }

    public <T> T execute(String url, BaseTaobaoRequest req, Class<T> clz) throws ApiException {
        DefaultDingTalkClient client = new DefaultDingTalkClient(mpConfig.getDingTalkServer() + url);
        log.debug("请求[{}]，报文:{}", url, JsonUtils.toJson(req));
        TaobaoResponse response = client.execute(req, mpConfig.getTokenMeta().getAccessToken());
        log.debug("请求[{}]，响应:{}", url, JsonUtils.toJson(response));
        if (!DingTalkConsts.RespCode.SUC.getKey().equals(response.getErrorCode())) {
            throw new ApiException(response.getErrorCode() + ":" + response.getMsg());
        }
        return (T) response;
    }
}
