package com.tunan.web.controller;

import com.tunan.web.domain.People;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Null;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tunan
 */
@RestController
public class MethodController {

    @GetMapping("/json")
    public People json(){

        return new People("老李","123456", "",new Date());
    }

    private Map<String,Object> params = new HashMap<>();

    //http://localhost:9997/tunan/1/2
    @GetMapping(path = "/{grade_id}/{class_id}")
    public Object find(@PathVariable("grade_id") String gradeId,
                       @PathVariable("class_id") String classId){
        params.clear();
        params.put("gradeId",gradeId);
        params.put("classId",classId);

        return params;
    }

    //http://localhost:9997/tunan/query?from=1&size=10
    @GetMapping(path = "/query")
    public Object query(int from,
                       int size){
        params.clear();
        params.put("from",from);
        params.put("size",size);

        return params;
    }

    //http://localhost:9997/tunan/query2?size=10&page=2
    @GetMapping(path = "/query2")
    public Object query2(@RequestParam(defaultValue = "1",name = "page") int from,
                        int size){
        params.clear();
        params.put("from",from);
        params.put("size",size);

        return params;
    }

    //http://localhost:9997/tunan/query2?size=10&page=2
    @PostMapping(path = "/login")
    public Object login(String name,String password){
        params.clear();
        params.put("name",name);
        params.put("password",password);

        return params;
    }
}