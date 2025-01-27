package spring.myproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {


    @Bean(name = "customAsyncExecutor")
    public Executor customAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 최소 스레드 개수
        executor.setMaxPoolSize(30); // 최대 스레드 개수
        executor.setQueueCapacity(100); // 작업 대기열 크기
        executor.setThreadNamePrefix("CustomExecutor-"); // 스레드 이름 접두사

        // 작업이 초과되었을 때 CallerRunsPolicy 설정
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize(); // 초기화
        return executor;
    }
}
