package com.tunan.java.filter;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * 布隆过滤器判断存在 -> 不一定存在
 * 布隆过滤器判断不存在 -> 一定不存在
 */
public class RedissonBloomFilter {

    public static void main(String[] args) {

        // 配置环境
        Config config = new Config();
        config.useSingleServer().setAddress("redis://aliyun:16379");

        // 构造Redis客户端
        RedissonClient client = Redisson.create(config);

        // 构建一个布隆过滤器
        RBloomFilter<Object> bloomFilter = client.getBloomFilter("PhoneList");

        // 初始化,初始值100000000,误差率0.01
        bloomFilter.tryInit(100000000L,0.01);

        //将号码10086插入到布隆过滤器中
        bloomFilter.add("10086");

        //判断下面号码是否在布隆过滤器中
        System.out.println(bloomFilter.contains("123456"));//false
        System.out.println(bloomFilter.contains("10086"));//true

    }
}
