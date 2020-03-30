package com.tunan.spark.utils;


import com.tunan.spark.ip.GetIPRegion;

/**
 * @description: IP地址解析
 * @author: tunan
 * @create: 2020-02-16 01:34
 * @since: 1.0.0
 **/
public class IpParseUtil {

    public static String[] IpParse(String ip){

        String[] list = new String[4];
        //指定纯真数据库的文件名，所在文件夹s
        String ips = GetIPRegion.GetipRegionInfo(ip);
        String[] split = ips.split("\\|");
        list[0]=split[0];
        list[1]=split[2];
        list[2]=split[3];
        list[3]=split[4];

        return list;
    }
}

