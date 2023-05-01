package com.sensedia.queueconsuming;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

// @Configuration
// @EnableAsync
// public class AppConfig implements AsyncConfigurer{

//     private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

//     @Override
//     public Executor getAsyncExecutor() {

//         logger.info("configuring thread...");
//         ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//         executor.setCorePoolSize(2);
//         executor.setMaxPoolSize(10);
//         executor.setQueueCapacity(100);
//         executor.setThreadNamePrefix("MyExecutor-");
//         executor.initialize();
//         return executor;
//     }
// }
@Configuration
@EnableScheduling
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {

        logger.info("configuring Scheduling...");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setThreadNamePrefix("default_task_executor_thread");
        executor.initialize();
        return executor;
    }
}