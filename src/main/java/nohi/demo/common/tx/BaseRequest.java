package nohi.demo.common.tx;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-06 20:19
 **/
@Data
public class BaseRequest<T> implements Serializable {
    private String traceId;
    private String traceTime;
    private T data;
    private BasePage page;
}
