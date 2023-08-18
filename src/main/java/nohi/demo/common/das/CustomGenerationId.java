package nohi.demo.common.das;

import nohi.demo.mp.utils.IdUtils;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description: 自定义ID生成策略
 * @create 2021-01-06 21:23
 **/
public class CustomGenerationId implements Configurable, IdentifierGenerator {

    /**
     * id前缀
     */
    private String idPrefix;

    public CustomGenerationId() {};

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return getId();
    }

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        this.idPrefix = params.getProperty("idPrefix"); //	实体类中@Parameter注解，根据键值获取value
    }

    /**
     * 	该方法需要是线程安全的
     * @return
     */
    public String getId() {
        synchronized (CustomGenerationId.class) {
            return IdUtils.uuid32();
        }
    }
}
