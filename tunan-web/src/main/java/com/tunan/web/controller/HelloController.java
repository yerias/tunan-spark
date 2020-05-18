package com.tunan.web.controller;


import com.tunan.web.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @RequestMapping("/sayHello")
    @ResponseBody
    public String sayHello(){
        return "Hello: 图南";
    }

    @Autowired
    private Student student;

    @RequestMapping("/testStudent")
    @ResponseBody
    public String testStudent(){
        System.out.println(student);
        return "============";
    }
}
