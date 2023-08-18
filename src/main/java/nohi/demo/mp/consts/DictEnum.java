package nohi.demo.mp.consts;

import java.io.Serializable;

/**
 * 字典枚举类描述接口
 */
public interface DictEnum extends Serializable {

    /**
     * 获取字典名
     * @return 字典名称
     */
    String getName();

    /**
     * 获取字典项 KEY
     * @return 字典项 KEY
     */
    String getKey();

    /**
     * 获取字典项 VAL
     * @return 字典项 VAL
     */
    Object getVal();

}
