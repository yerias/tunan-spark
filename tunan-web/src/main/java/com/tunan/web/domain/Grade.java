package com.tunan.web.domain;

import org.springframework.stereotype.Component;

@Component
public class Grade {
    private String name;
    private String desc;

    @Override
    public String toString() {
        return "Grade{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
