package com.tunan.spark.utils;

import org.apache.spark.SparkFiles;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;

import java.io.File;
import java.lang.reflect.Method;

public class GetIPRegion {

    public static String getipRegionInfo(String ip){
        String dbPath = GetIPRegion.class.getResource("/ip2region.db").getPath();
//        String dbPath = SparkFiles.get("ip2region.db");
//        String dbPath = "/home/hadoop/lib/ip2region.db";
//        String dbPath = "tunan-spark-core/src/main/resources/ip2region.db";
        File file = new File(dbPath);
        if (!file.exists()) {
            System.out.println("Error: Invalid ip2region.db file");
        }
        //查询算法
        int algorithm = DbSearcher.BTREE_ALGORITHM; //B-tree
        //DbSearcher.BINARY_ALGORITHM //Binary
        //DbSearcher.MEMORY_ALGORITYM //Memory
        DataBlock dataBlock = null;
        try{
            DbConfig config = new DbConfig();
            DbSearcher searcher = new DbSearcher(config, dbPath);

            //define the method
            Method method = null;
            switch ( algorithm )
            {
                case DbSearcher.BTREE_ALGORITHM:
                    method = searcher.getClass().getMethod("btreeSearch", String.class);
                    break;
                case DbSearcher.BINARY_ALGORITHM:
                    method = searcher.getClass().getMethod("binarySearch", String.class);
                    break;
                case DbSearcher.MEMORY_ALGORITYM:
                    method = searcher.getClass().getMethod("memorySearch", String.class);
                    break;
            }


            if ( Util.isIpAddress(ip) == false ) {
                System.out.println("Error: Invalid ip address");
            }

            dataBlock  = (DataBlock) method.invoke(searcher, ip);
            searcher.close();
            return dataBlock.getRegion();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
