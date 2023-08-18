package nohi.demo.mp.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Spring 上下文工具类
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }

    /**
     * 根据 Spring BeanID 取得 Spring Bean 实例
     * @param beanName Spring BeanID
     * @param <T>      Bean 类型
     * @return Spring Bean 实例，若找不到返回空
     */
    public static <T> T getBean(String beanName) {
        if(applicationContext.containsBean(beanName)){
            return (T) applicationContext.getBean(beanName);
        }else{
            return null;
        }
    }

    public static <T> T getBean(Class clzz) {
        return (T) applicationContext.getBean(clzz);
    }

    /**
     * 根据 Bean 类型取得 Spring Bean 实例
     * @param baseType Bean 类型
     * @param <T>      Bean 类型
     * @return Bean 实例 Map（key 为 Spring BeanID，val 为 Spring Bean 实例）
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> baseType){
        return applicationContext.getBeansOfType(baseType);
    }

}
