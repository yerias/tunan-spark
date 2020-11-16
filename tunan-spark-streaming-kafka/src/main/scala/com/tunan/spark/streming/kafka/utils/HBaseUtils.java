package com.tunan.spark.streming.kafka.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RowFilter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * HBase连接的客户端工具类
 */
public class HBaseUtils {

    // pool就是存放hbase的连接对象的连接池
    private static LinkedList<Connection> pool = new LinkedList<>();

    // 初始化5条hbase的连接对象到连接池中
    static {
        try {
            Configuration conf = new Configuration();
            conf.set("hbase.rootdir", "hdfs://hadoop:9000");
//            conf.set("hbase.cluster.distributed", "true");
            conf.set("hbase.zookeeper.quorum", "hadoop");
            conf.set("hbase.regionserver.wal.codec", "org.apache.hadoop.hbase.regionserver.wal.IndexedWALEditCodec");
            for (int i = 0; i < 10; i++) {
                pool.push(ConnectionFactory.createConnection(conf));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取连接对象
    public static Connection getConnection() {
        while (pool.isEmpty()) {
            try {
                System.out.println("connection pool is null, please wait for a moment~~~");
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pool.poll();
    }

    // 释放连接对象，将连接对象归还给连接池
    public static void release(Connection connection) {
        pool.push(connection);
    }

    // 根据参数创建表
    public static Map<Integer, Long> getColValue(Connection connection, TableName tableName, byte[] rk, byte[] cf) {
        //1. 声明map存放最终结果
        Map<Integer, Long> partition2Offset = new HashMap<>();
        try {
            //2. 获取到表对象
            Table table = connection.getTable(tableName);
            Scan scan = new Scan();
            //3. 条件
            Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(rk));
            scan.setFilter(filter);
            //4. 创建扫描器
            ResultScanner scanner = table.getScanner(scan);
            //5. 遍历
            for (Result result : scanner) {
                List<Cell> cells = result.listCells(); // 获取到每一个cell(k,v)
                for (Cell cell : cells) {
                    //col
                    byte[] column = CellUtil.cloneQualifier(cell);
                    //value
                    byte[] values = CellUtil.cloneValue(cell);

                    int partition = Integer.valueOf(new String(column));
                    long offset = Long.valueOf(new String(values));

                    partition2Offset.put(partition, offset);
                }
            }
            return partition2Offset;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //将col和value设置到hbase
    public static void set(Connection connection, TableName tableName, byte[] rk, byte[] cf, byte[] col, byte[] value) {
        try {
            Table table = connection.getTable(tableName);
            Put put = new Put(rk);
            put.addColumn(cf, col, value);
            table.put(put);
            table.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}