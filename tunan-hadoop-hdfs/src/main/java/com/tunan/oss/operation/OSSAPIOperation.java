package com.tunan.oss.operation;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.tunan.oss.utils.SecretUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class OSSAPIOperation {

    static String endpoint = "https://oss-cn-shenzhen.aliyuncs.com";

    static String bucketName = "tunan-bucket";

    static OSS ossClient = null;

    // 构造器中拿到OSS客户端
    public OSSAPIOperation(){
        ossClient = new OSSClientBuilder().build(endpoint, SecretUtils.AccessKeyID, SecretUtils.AccessKeySecret);
    }


    // 查询指定bucket下所有文件的信息
    public void listObjects(){
        ObjectListing listObjects = ossClient.listObjects(bucketName);
        for (OSSObjectSummary objectSummaries : listObjects.getObjectSummaries()){
            System.out.println(objectSummaries.getKey());
            System.out.println(objectSummaries.getETag());
            System.out.println(objectSummaries.getSize());
            System.out.println(objectSummaries.getBucketName());
            System.out.println(objectSummaries.getLastModified());
            System.out.println("==========================");
        }
    }

    // 查询所有的bucket
    public void listBucket(){
        List<Bucket> buckets = ossClient.listBuckets();
        for (Bucket bucket : buckets){
            System.out.println(bucket.getRegion());
            System.out.println(bucket.getLocation());
            System.out.println(bucket.getName());
            System.out.println(bucket.getStorageClass());
            System.out.println("==========================");
        }
    }

    // 上传文件
    public void putFiles(File files){
        ossClient.putObject(bucketName,"2/"+files.getName(),files);
    }

    // 下载文件
    public void getFiles(String key){
        OSSObject ossObject = ossClient.getObject(bucketName,key);
        BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
        String lines = null;
        try {
            while (((lines = reader.readLine()) != null)) {
                System.out.println(lines);
            };
        } catch (IOException e) {
            e.printStackTrace();
        }   finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // 下载Object到本地文件，并保存到指定的本地路径中。如果指定的本地文件存在会覆盖，不存在则新建。
    // 如果未指定本地路径，则下载后的文件默认保存到示例程序所属项目对应本地路径中。
    public void getFilesDownload(String key){
        ossClient.getObject(new GetObjectRequest(bucketName,key),new File("E:/Java/spark/tunan-spark/tunan-hadoop-hdfs/output/file.txt"));
    }

    // 删除文件
    public void deleteFiles(String key){
        ossClient.deleteObject(bucketName,key);
    }

    // 填写Bucket名称和file完整路径。file完整路径中不能包含Bucket名称。
    public void doesObjectExist(String key){
        System.out.println(ossClient.doesObjectExist(bucketName, key));
    }

    // 关闭OSS客户端
    public void closeOSSClient(OSS ossClient){
        ossClient.shutdown();
    }

    public static void main(String[] args) {
        String filePath = "E:\\Java\\spark\\tunan-spark\\tunan-hadoop-hdfs\\output\\file.txt";
//        String filePath = "E:/data/wc.txt/part-r-00000";
        OSSAPIOperation operation = new OSSAPIOperation();
//        operation.getFilesDownload("1/part-r-00000");
//        operation.getFiles("1/part-r-00000");
        operation.putFiles(new File(filePath));
        operation.closeOSSClient(ossClient);

    }
}