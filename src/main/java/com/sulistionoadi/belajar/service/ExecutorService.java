/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sulistionoadi.belajar.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sulistionoadi.belajar.ReadWriteLock;
import com.sulistionoadi.belajar.model.JsonLogFile;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 *
 * @author adi
 */

@Service
public class ExecutorService {
    
    @Autowired
    @Qualifier("processingThreadPool")
    private ThreadPoolTaskExecutor taskExecutor;
    
    @Autowired
    private ObjectMapper mapper;
    
    private String pathlog = "/tmp/thread-write.log";
    
    public void runJob(){
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            String contentFile = IOUtils.toString(classLoader.getResourceAsStream("data.csv"), "UTF-8");
            String[] arrContent = contentFile.split("\n");
            
            ReadWriteLock fileLock = new ReadWriteLock();
            File logFile = new File(pathlog);
            if(!logFile.exists()){
                logFile.createNewFile();
            }

            JsonLogFile jsonLog = new JsonLogFile();

            fileLock.lockWrite();
            mapper.writerWithDefaultPrettyPrinter().writeValue(logFile, jsonLog);
            fileLock.unlockWrite();
            
            System.out.println("Running Process Insert into Batch Queue");
            for(String line : arrContent) {
                if(StringUtils.hasText(line)){
                    while(true){
                        try {
                            boolean inserted = taskExecutor.getThreadPoolExecutor().getQueue().offer(new WriteLogTask(line, logFile, fileLock), 60, TimeUnit.SECONDS);
                            if(inserted){
                                break;
                            }
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ExecutorService.class.getName()).log(Level.SEVERE, null, ex);
                            break;
                        }
                    }
                    taskExecutor.execute(taskExecutor.getThreadPoolExecutor().getQueue().poll());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ExecutorService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ExecutorService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class WriteLogTask implements Runnable {
        String name;
        File logfile;
        ReadWriteLock fileLock;
        
        public WriteLogTask(String name, File logfile, ReadWriteLock fileLock) {
            this.name = name;
            this.logfile = logfile;
            this.fileLock = fileLock;
        }

        public String getName() {
            return name;
        }

        @Override
        public void run() {
            try {
                this.fileLock.lockWrite();
                JsonLogFile jsonLog = mapper.readValue(this.logfile, JsonLogFile.class);
                jsonLog.getDataContent().add(this.name);
                jsonLog.setDataProcessed(Long.valueOf(jsonLog.getDataContent().size()));
                
                mapper.writerWithDefaultPrettyPrinter().writeValue(this.logfile, jsonLog);
                
                System.out.println(this.name + " processed");
                
                Thread.sleep(5000);
            } catch (InterruptedException | IOException ex) {
                Logger.getLogger(ExecutorService.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    this.fileLock.unlockWrite();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ExecutorService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}

