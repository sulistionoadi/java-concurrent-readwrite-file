package com.sulistionoadi.belajar;

import javax.annotation.PreDestroy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
public class JavaConcurrentReadwriteFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaConcurrentReadwriteFileApplication.class, args);
    }
    
    @Bean(name="processingThreadPool")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(0);
        pool.setMaxPoolSize(200);
        pool.setWaitForTasksToCompleteOnShutdown(true);
        pool.setQueueCapacity(500);
        return pool;
    }
    
    @PreDestroy
    public void shutdownHook() throws InterruptedException{
        while(taskExecutor().getActiveCount() > 0){
            System.out.println("Waiting for shutdown...");
            Thread.sleep(5000);
        }
    }
}
