package com.tunan.hadoop.platform.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Tunan
 */
public class HadoopMetrics {

    List<Map<String,Object>> beans =  new ArrayList<>();

    public Object getValue(String name){
        if (beans.isEmpty()){
            return null;
        }
        return beans.get(0).getOrDefault(name,null);
    }

    public List<Map<String, Object>> getBeans() {
        return beans;
    }

    public void setBeans(List<Map<String, Object>> beans) {
        this.beans = beans;
    }
}
