package nohi.demo.mp.service.mp;

import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.mp.config.MpConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-03 21:28
 **/
@Service
@Slf4j
public class MpHandler {
    private MpServiceImpl mpServiceImpl;
    @Autowired
    private MpConfig mpConfig;

    public MpServiceImpl getMpService() throws ApiException {
        if (null == mpServiceImpl) {
            mpServiceImpl = new MpServiceImpl(mpConfig);
        }
        mpServiceImpl.getToken();
        return mpServiceImpl;
    }
}
