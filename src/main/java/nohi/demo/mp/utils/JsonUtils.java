package nohi.demo.mp.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-04 09:26
 **/
@Slf4j
public class JsonUtils {
    static SerializeConfig config = new SerializeConfig();
    static {
        config.put(Date.class, new DateJsonSerializer());
    }

    public static String toJson(Object data) {
        if (null == data) {
            return null;
        }
        return JSON.toJSONString(data, config);
    }

    public static void copyProperties(Object source, Object target){
        String json = JsonUtils.toJson(source);
        Object obj = JSON.parseObject(json, target.getClass());
        try {
            BeanUtils.copyProperties(obj, target);
        } catch(Exception e) {
          log.error("转换异常：", e.getMessage());
          log.debug("json:{}", json);
          log.debug("obj:{}", JsonUtils.toJson(obj));
        }
    }
}
