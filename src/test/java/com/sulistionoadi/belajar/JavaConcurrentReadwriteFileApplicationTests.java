package com.sulistionoadi.belajar;

import com.sulistionoadi.belajar.service.ExecutorService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class JavaConcurrentReadwriteFileApplicationTests {

    @Autowired
    private ExecutorService executorService;
    
    @Autowired
    @Qualifier("processingThreadPool")
    private ThreadPoolTaskExecutor taskExecutor;
    
    @Test
    public void testRun() {
        executorService.runJob();
    }

    @After
    public void shutdownHook() throws InterruptedException{
        while(taskExecutor.getActiveCount() > 0){
            System.out.println("Waiting for shutdown...");
            Thread.sleep(5000);
        }
    }
}
