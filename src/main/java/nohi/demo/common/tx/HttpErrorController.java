package nohi.demo.common.tx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-06 20:26
 **/
@Slf4j
@RestController
public class HttpErrorController implements ErrorController {

    private final static String ERROR_PATH = "/error";

    @ResponseBody
    @RequestMapping(path  = ERROR_PATH )
    public BaseResponse error(HttpServletRequest request, HttpServletResponse response){
        String uri = request.getRequestURI();
        log.info("访问[{}] error 错误代码:{}", uri, response.getStatus());
        BaseResponse result = new BaseResponse(BaseResponse.ResCode.ERROR,"HttpErrorController error:"+response.getStatus());
        return result;
    }
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
