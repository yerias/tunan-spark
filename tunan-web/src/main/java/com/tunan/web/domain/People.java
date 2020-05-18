package com.tunan.web.domain;

import com.fasterxml.jackson.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Date;

//@Component
public class People {

    @JsonProperty("account")  //name的别名
    private String name;
    @JsonIgnore
    private String password;
//    @JsonInclude(JsonInclude.Include.NON_NULL)        //排除值为Null
    @JsonInclude(JsonInclude.Include.NON_EMPTY)         //排除值为空
    private String phone;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh",timezone = "GMT +8")
    private Date birthday;

    public People() {
    }

    public People(String name, String password, String phone, Date birthday) {
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
