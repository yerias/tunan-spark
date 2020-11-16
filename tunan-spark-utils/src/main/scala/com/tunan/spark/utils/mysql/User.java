package com.tunan.spark.utils.mysql;

public class User {
    //User(name:String,age:Int,birth_day:String)
    String name;
    Integer age;
    String birth_day;

    public User() {
    }

    public User(String name, Integer age, String birth_day) {
        this.name = name;
        this.age = age;
        this.birth_day = birth_day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBirth_day() {
        return birth_day;
    }

    public void setBirth_day(String birth_day) {
        this.birth_day = birth_day;
    }
}
