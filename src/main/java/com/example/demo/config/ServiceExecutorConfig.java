package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ServiceExecutorConfig implements AsyncConfigurer {
    @Bean("serviceExecutor")
    @Override
    public AsyncTaskExecutor getAsyncExecutor() {
        System.out.println("ok");
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("service-executor-async");
        executor.setCorePoolSize(2);
        executor.initialize();
        return executor;
    }
}