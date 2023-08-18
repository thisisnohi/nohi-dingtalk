package nohi.demo.common.tx;

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
    private String reqId;
    private T t;
}
