package com.sulistionoadi.belajar;

import com.sulistionoadi.belajar.service.ExecutorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class JavaConcurrentReadwriteFileApplicationTests {

    @Autowired
    private ExecutorService executorService;
    
    @Test
    public void testRun() {
        executorService.runJob();
    }
    
}
