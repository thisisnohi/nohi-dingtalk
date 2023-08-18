package nohi.demo.common.tx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-06 20:42
 **/

@ControllerAdvice
@Slf4j
public class ExceptionCatch {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResponse exception(Exception e) {
        log.error("catch exception : {} \r\n===exception===", e.getMessage(), e);
        BaseResponse result = new BaseResponse(BaseResponse.ResCode.ERROR,e.getMessage());
        return result;
    }

}
