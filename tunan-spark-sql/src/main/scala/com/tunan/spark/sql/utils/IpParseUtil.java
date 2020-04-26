package com.tunan.spark.sql.utils;

/**
 * @description: IP地址解析
 * @author: tunan
 * @create: 2020-02-16 01:34
 * @since: 1.0.0
 **/
public class IpParseUtil {
    private String country;
    private String province;
    private String city;
    private String area;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public static IpParseUtil IpParse(String ip){

        IpParseUtil parseIp = new IpParseUtil();

        String ips = GetIPRegion.GetipRegionInfo(ip);
        String[] split = ips.split("\\|");
        parseIp.country=split[0];
        parseIp.province=split[2];
        parseIp.city=split[3];
        parseIp.area=split[4];

        return parseIp;
    }

}



