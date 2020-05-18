package com.tunan.web.scheduler;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
//@EnableScheduling
public class SchedulerTask {


    @Scheduled(cron = "*/2 * * * * *")
    public void exec() throws Exception{
        System.out.println("定时调度任务，当前时间："+new Date());
    }

}
