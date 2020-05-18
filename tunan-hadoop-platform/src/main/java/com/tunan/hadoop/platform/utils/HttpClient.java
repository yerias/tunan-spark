package com.tunan.hadoop.platform.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tunan.hadoop.platform.domain.HadoopMetrics;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HttpClient {

    private static RequestConfig requestConfig = null;

    static {
        // 设置请求和传输超时时间
        requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
    }

    public static HadoopMetrics httpGet(String url) {
        HadoopMetrics metrics = new HadoopMetrics();
        // get请求返回结果
        JSONObject jsonResult = null;
        CloseableHttpClient client = HttpClients.createDefault();
        // 发送get请求
        HttpGet request = new HttpGet(url);
        request.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = client.execute(request);

            // 请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 读取服务器返回过来的json字符串数据
                HttpEntity entity = response.getEntity();
                String strResult = EntityUtils.toString(entity, "utf-8");
                // 把json字符串转换成json对象
                jsonResult = JSONObject.parseObject(strResult);
                JSONArray beans = jsonResult.getJSONArray("beans");

                List<Map<String, Object>> listObjectSec = JSONArray.parseObject(beans.toString(), List.class);
//                System.out.println("利用JSONArray中的parseObject方法并指定返回类型来解析json数组字符串");
//                for(Map<String,Object> mapList : listObjectSec){
//                    for (Map.Entry entry : mapList.entrySet()){
//                        System.out.println( entry.getKey()  + "  " +entry.getValue());
//                    }
//                }
                metrics.setBeans(listObjectSec);

            } else {
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            request.releaseConnection();
        }

        return metrics;
    }
}
