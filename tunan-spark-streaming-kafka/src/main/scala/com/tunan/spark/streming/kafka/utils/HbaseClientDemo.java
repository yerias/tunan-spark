package com.tunan.spark.streming.kafka.utils;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Hbase 客户端测试
 *
 * @author Logan
 * @version 1.0.0
 * @createDate 2019-05-03
 */
public class HbaseClientDemo {

    /**
     * 向user表中插入数据
     */
    @Test
    public void put() {

        String tableName = "default:user";
        try {

            List<Put> puts = new ArrayList<Put>();
            Put put = HBaseCRUDUtils.createPut("key1005");
            HBaseCRUDUtils.addValueOnPut(put, "info", "name", "孙悟空");
            HBaseCRUDUtils.addValueOnPut(put, "info", "age", "500");
            HBaseCRUDUtils.addValueOnPut(put, "info", "address", "花果山");
            // HBaseCRUDUtils.put(tableName, put);
            puts.add(put);

            put = HBaseCRUDUtils.createPut("key1006");
            HBaseCRUDUtils.addValueOnPut(put, "info", "name", "沙悟净");
            HBaseCRUDUtils.addValueOnPut(put, "info", "age", "1000");
            HBaseCRUDUtils.addValueOnPut(put, "info", "address", "流沙河");
            puts.add(put);

            HBaseCRUDUtils.put(tableName, puts);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按rowKey批量查询user表中全部列簇全部列的值
     */
    @Test
    public void getAllFamily() {
        try {
            String tableName = "default:user";
            String[] rowKeys = {"key1001", "key1002", "key1003", "key1005", "key1006"};

            // 按表名和rowKey查询所有列
            Result[] results = HBaseCRUDUtils.get(tableName, rowKeys);
            for (Result result : results) {

                // 打印查询结果
                printResult(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按rowKey查询user表中指定列簇指定列的值
     */
    @Test
    public void get() {
        try {
            String tableName = "default:user";
            String rowKey = "key1005";

            Get get = HBaseCRUDUtils.createGet(rowKey);

            HBaseCRUDUtils.addColumnOnGet(get, "info", "name");
            HBaseCRUDUtils.addColumnOnGet(get, "info", "age");

            // 不存在的列，查询结果不显示
            HBaseCRUDUtils.addColumnOnGet(get, "info", "address");

            // 如果在增加列后增加已有的列簇，会返回该列簇的全部列数据，覆盖前边的增加列
            // HBaseCRUDUtils.addFamilyOnGet(get, "info");

            Result result = HBaseCRUDUtils.get(tableName, get);
            printResult(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void scan() {
        try {
            String tableName = "default:user";
            String startRow = "key1001";
            String stopRow = "key1006";
            ResultScanner resultScanner = HBaseCRUDUtils.scan(tableName, startRow, stopRow);
            for (Result result : resultScanner) {
                printResult(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印查询结果
     *
     * @param result 查询结果对象
     */
    private void printResult(Result result) {
        Cell[] cells = result.rawCells();

        // 从Result中读取 rowKey
        System.out.println(Bytes.toString(result.getRow()));

        String print = "%s\t %s:%s \t %s";
        for (Cell cell : cells) {

            // 从Cell中取rowKey
            String row = Bytes.toString(CellUtil.cloneRow(cell));
            String family = Bytes.toString(CellUtil.cloneFamily(cell));
            String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
            String value = Bytes.toString(CellUtil.cloneValue(cell));

            System.out.println(String.format(print, row, family, qualifier, value));

        }
    }

    /**
     * 删除指定列
     */
    @Test
    public void deleteColumn() {
        try {
            String tableName = "default:user";
            List<Delete> deletes = new ArrayList<Delete>();
            Delete delete = HBaseCRUDUtils.createDelete("key1005");
            HBaseCRUDUtils.addColumnOnDelete(delete, "info", "age");
            HBaseCRUDUtils.addColumnOnDelete(delete, "info", "address");
            // HBaseCRUDUtils.delete(tableName, delete);
            deletes.add(delete);

            delete = HBaseCRUDUtils.createDelete("key1006");
            HBaseCRUDUtils.addColumnOnDelete(delete, "info", "address");
            deletes.add(delete);

            HBaseCRUDUtils.delete(tableName, deletes);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定列簇
     */
    @Test
    public void deleteFamily() {
        try {
            String tableName = "default:user";
            List<Delete> deletes = new ArrayList<Delete>();
            Delete delete = HBaseCRUDUtils.createDelete("key1005");
            HBaseCRUDUtils.addFamilyOnDelete(delete, "info");
            // HBaseCRUDUtils.delete(tableName, delete);
            deletes.add(delete);

            delete = HBaseCRUDUtils.createDelete("key1006");
            HBaseCRUDUtils.addFamilyOnDelete(delete, "info");
            deletes.add(delete);

            HBaseCRUDUtils.delete(tableName, deletes);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}