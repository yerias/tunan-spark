package com.tunan.doris.domain;

import scala.Serializable;

public class User implements Serializable {

    private String userId;
    private String userName;
    private String phone;
    private String registerTime;
    private String location;
    private String registerExamName;
    private String province;
    private String city;
    private String originSource;
    private String platform;
    private String registerChannel;
    private String focusExamName;
    private String urlType;
    private String originUrl;
    private String registerType;
    private String columnType;
    private String lastLoginTime;
    private String lastOrderTime;
    private String lastPayTime;
    private String isPay;
    private int payNum;
    private double realPay;
    private String isCoursePay;
    private int coursePayNum;
    private double courseRealPay;

    public User() {
    }

    public User(String userId, String userName, String phone, String registerTime, String location, String registerExamName, String province, String city, String originSource, String platform, String registerChannel, String focusExamName, String urlType, String originUrl, String registerType, String columnType, String lastLoginTime, String lastOrderTime, String lastPayTime, String isPay, int payNum, double realPay, String isCoursePay, int coursePayNum, double courseRealPay) {
        this.userId = userId;
        this.userName = userName;
        this.phone = phone;
        this.registerTime = registerTime;
        this.location = location;
        this.registerExamName = registerExamName;
        this.province = province;
        this.city = city;
        this.originSource = originSource;
        this.platform = platform;
        this.registerChannel = registerChannel;
        this.focusExamName = focusExamName;
        this.urlType = urlType;
        this.originUrl = originUrl;
        this.registerType = registerType;
        this.columnType = columnType;
        this.lastLoginTime = lastLoginTime;
        this.lastOrderTime = lastOrderTime;
        this.lastPayTime = lastPayTime;
        this.isPay = isPay;
        this.payNum = payNum;
        this.realPay = realPay;
        this.isCoursePay = isCoursePay;
        this.coursePayNum = coursePayNum;
        this.courseRealPay = courseRealPay;
    }

    @Override
    public String toString() {
        return userId +
                "," + userName +
                "," + phone+
                "," + registerTime+
                "," + location+
                "," + registerExamName+
                "," + province+
                "," + city+
                "," + originSource+
                "," + platform+
                "," + registerChannel+
                "," + focusExamName+
                "," + urlType+
                "," + originUrl+
                "," + registerType+
                "," + columnType+
                "," + lastLoginTime+
                "," + lastOrderTime+
                "," + lastPayTime+
                "," + isPay+
                "," + payNum+
                "," + realPay+
                "," + isCoursePay+
                "," + coursePayNum+
                "," + courseRealPay;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getregisterExamName() {
        return registerExamName;
    }

    public void setregisterExamName(String registerExamName) {
        this.registerExamName = registerExamName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOriginSource() {
        return originSource;
    }

    public void setOriginSource(String originSource) {
        this.originSource = originSource;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getRegisterChannel() {
        return registerChannel;
    }

    public void setRegisterChannel(String registerChannel) {
        this.registerChannel = registerChannel;
    }

    public String getFocusExamName() {
        return focusExamName;
    }

    public void setFocusExamName(String focusExamName) {
        this.focusExamName = focusExamName;
    }

    public String getUrlType() {
        return urlType;
    }

    public void setUrlType(String urlType) {
        this.urlType = urlType;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }

    public String getRegisterType() {
        return registerType;
    }

    public void setRegisterType(String registerType) {
        this.registerType = registerType;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastOrderTime() {
        return lastOrderTime;
    }

    public void setLastOrderTime(String lastOrderTime) {
        this.lastOrderTime = lastOrderTime;
    }

    public String getLastPayTime() {
        return lastPayTime;
    }

    public void setLastPayTime(String lastPayTime) {
        this.lastPayTime = lastPayTime;
    }

    public String getIsPay() {
        return isPay;
    }

    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }

    public int getPayNum() {
        return payNum;
    }

    public void setPayNum(int payNum) {
        this.payNum = payNum;
    }

    public double getRealPay() {
        return realPay;
    }

    public void setRealPay(double realPay) {
        this.realPay = realPay;
    }

    public String getIsCoursePay() {
        return isCoursePay;
    }

    public void setIsCoursePay(String isCoursePay) {
        this.isCoursePay = isCoursePay;
    }

    public int getCoursePayNum() {
        return coursePayNum;
    }

    public void setCoursePayNum(int coursePayNum) {
        this.coursePayNum = coursePayNum;
    }

    public double getCourseRealPay() {
        return courseRealPay;
    }

    public void setCourseRealPay(double courseRealPay) {
        this.courseRealPay = courseRealPay;
    }
}
