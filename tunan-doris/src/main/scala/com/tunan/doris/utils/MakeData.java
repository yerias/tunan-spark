package com.tunan.doris.utils;

import com.tunan.doris.domain.User;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MakeData {

    static String[] userNames = {"胡歌","彭于晏","李易峰","刘德华","张旭","小七","图南","狗蛋"};
    static String[] phones = {"135","150","185","176"};
    static String strDateFormat = "yyyy-MM-dd HH:mm:ss";
    static String[] provinces = {"湖南","湖北","广东","广西","四川","福建","北京","上海","湖北","内蒙古","西藏"};
    static String[] citys = {"长沙","武汉","株洲","上海","厦门","无锡","北京"};
    static String[] registerExamNames = {"一级建造师","一级消防工程师","二级造价工程师","证券从业及专项","教师资格笔试","中级会计师","安全工程师","银行从业","社会工作者"};
    static String[] originSources = {"直接访问","tongyong","znjjcy","233wangxiao_360","App Store","233wangxiao_oppo","233wangxiao_vivo","233wangxiao_mi"};
    static String[] platforms = {"iOS","Android","pc","wap"};
    static String[] registerChannels = {"233wangxiao_huawei","App Store","233wangxiao","233wangxiao_oppo","233wangxiao_vivo","233wangxiao_mi","www.baidu.com","直接访问"};
    static String[] urlTypes = {"app首页","题库首页","我的","签到首页","我的课程","app签到完成页"};
    static String[] registerTypes = {"手机快捷注册","微信小程序","手机正常注册","微信","qq","百度小程序","Apple"};
    static String[] columnTypes = {"章节练习","每日一练","点击签到","签到奖励积分","历年真题","全部","考试日历","模拟试题"};
    static String[] isPays = {"是","否"};


    public static void main(String[] args) throws IOException, SQLException {

        Random r = new Random(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat(strDateFormat);

        Connection conn = null;
        PreparedStatement pstate = null;

//        conn = MySQLUtils.getConnection();
        conn.setAutoCommit(false);
        pstate = conn.prepareStatement("INSERT INTO user_profile(userId,userName,phone,registerTime,location,registerExamName," +
                "province,city,originSource,platform,registerChannel,focusExamName,urlType,originUrl,registerType,columnType,lastLoginTime,lastOrderTime," +
                "lastPayTime,isPay,payNum,realPay,isCoursePay,coursePayNum,courseRealPay) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

        for (int i = 0; i < 1*1500; i++) {
            String userId = String.valueOf(r.nextInt(10000000));

            String userName = userNames[r.nextInt(8)]+r.nextInt(10);

            String phone = phones[r.nextInt(4)]+r.nextInt(10)+r.nextInt(10)+r.nextInt(10)+r.nextInt(10)+r.nextInt(10)+r.nextInt(10)+r.nextInt(10)+r.nextInt(10);

            String registerTime = format.format(getRandomTime(r));

            String location = citys[r.nextInt(7)];

            String registerExamName = registerExamNames[r.nextInt(9)];

            String province = provinces[r.nextInt(11)];

            String city = citys[r.nextInt(7)];

            String originSource = originSources[r.nextInt(8)];

            String platform = platforms[r.nextInt(4)];

            String registerChannel = registerChannels[r.nextInt(8)];

            String focusExamName = registerExamNames[r.nextInt(9)];

            String urlType =urlTypes[r.nextInt(6)];

            String originUrl = originSources[r.nextInt(8)];

            String registerType = registerTypes[r.nextInt(7)];

            String columnType = columnTypes[r.nextInt(8)];

            String lastLoginTime = format.format(getRandomTime(r));

            String lastOrderTime = format.format(getRandomTime(r));

            String lastPayTime = format.format(getRandomTime(r));

            String isPay = isPays[r.nextInt(2)];

            int payNum = r.nextInt(10);

            double realPay = r.nextInt(10000);

            String isCoursePay = isPays[r.nextInt(2)];

            int coursePayNum = r.nextInt(10);

            double courseRealPay = r.nextInt(10000);


            pstate.setString(1,userId);
            pstate.setString(2,userName);
            pstate.setString(3,phone);
            pstate.setString(4,registerTime);
            pstate.setString(5,location);
            pstate.setString(6,registerExamName);
            pstate.setString(7,province);
            pstate.setString(8,city);
            pstate.setString(9,originSource);
            pstate.setString(10,platform);
            pstate.setString(11,registerChannel);
            pstate.setString(12,focusExamName);
            pstate.setString(13,urlType);
            pstate.setString(14,originUrl);
            pstate.setString(15,registerType);
            pstate.setString(16,columnType);
            pstate.setString(17,lastLoginTime);
            pstate.setString(18,lastOrderTime);
            pstate.setString(19,lastPayTime);
            pstate.setString(20,isPay);
            pstate.setInt(21,payNum);
            pstate.setBigDecimal(22,BigDecimal.valueOf(realPay));
            pstate.setString(23,isCoursePay);
            pstate.setInt(24,coursePayNum);
            pstate.setBigDecimal(25, BigDecimal.valueOf(courseRealPay));
            pstate.addBatch();

            if(i % 300 == 0){
                pstate.executeBatch();
                System.out.println("1111111111111");
                conn.commit();
                pstate.clearBatch();

            }
        }
        pstate.executeBatch();
        conn.commit();

        if(null != pstate){
            pstate.close();
        }

        if(null != conn){
            conn.close();
        }
    }

    public static Date getRandomTime(Random r){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-r.nextInt(50));
        calendar.add(Calendar.HOUR,-r.nextInt(24));
        calendar.add(Calendar.MINUTE,-r.nextInt(60));
        calendar.add(Calendar.SECOND,-r.nextInt(60));
        return calendar.getTime();
    }
}



