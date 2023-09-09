package nohi.demo.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <h3>nohi-dd-miniprogram-server</h3>
 *
 * @author NOHI
 * @description <p>ThreadPool</p>
 * @date 2023/09/08 21:38
 **/
@Configuration
public class ThreadPoolConfiguration {

    @Bean
    public ThreadPoolExecutor dingTalkPool(){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                10 ,
                20 ,
                60,
                TimeUnit.SECONDS,
                // 指定队列大小
                new ArrayBlockingQueue<Runnable>(10),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
        return executor;
    }
}
