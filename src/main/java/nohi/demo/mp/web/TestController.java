package nohi.demo.mp.web;

import com.taobao.api.ApiException;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.mp.service.TestService;
import nohi.demo.mp.service.mp.MpHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-03 20:59
 **/
@Api(value = "DEMO", tags = "demo", description = "demo 服务")
@RestController
@RequestMapping(value = "test")
@Slf4j
public class TestController {

    @Autowired
    private TestService testService;
    @Autowired
    private MpHandler mpHandler;

    @GetMapping("getDepts")
    public Object getDepts() throws ApiException {
        return mpHandler.getMpService().getDepts();
    }

    @GetMapping("getDeptsUser")
    public Object getDeptsUser() {
        try {
            return mpHandler.getMpService().getDeptsUser();
        } catch (ApiException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

}
