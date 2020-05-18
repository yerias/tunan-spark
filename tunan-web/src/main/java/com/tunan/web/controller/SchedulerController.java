package com.tunan.web.controller;


import com.tunan.web.scheduler.AsyncTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SchedulerController {

    @Autowired
    private AsyncTask asyncTask;

    @RequestMapping("/task")
    @ResponseBody
    public String sayHello() throws Exception {
        long start = System.currentTimeMillis();

        asyncTask.task1();
        asyncTask.task2();
        asyncTask.task3();

        long end = System.currentTimeMillis();
        System.out.println("总共 花费时间: " + (end - start));


        return "Hello: 图南";
    }

}
