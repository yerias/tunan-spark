package com.tunan.oss.operation;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.UploadFileRequest;
import com.tunan.oss.utils.SecretUtils;

import java.io.File;
import java.io.IOException;

public class OSSUploadFile {

    static String endpoint = "https://oss-cn-shenzhen.aliyuncs.com";

    static String bucketName = "tunan-bucket";

    private static OSS ossClient = null;

    // 构造器中拿到OSS客户端
    static {
        ossClient = new OSSClientBuilder().build(endpoint, SecretUtils.AccessKeyID, SecretUtils.AccessKeySecret);
    }


    public static void main(String[] args) throws Throwable {
        try {
            getFile("F:/账户.txt");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
    }

    public static void getFile(String path) throws Throwable {
        File file = new File(path);
        if (file.isFile()){
            putCheckpointFile("", file);
        }else {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isFile()) {
                    String[] parentPaths = f.getParent().split("tunan-hadoop-hdfs");
                    if (parentPaths.length > 1) {
                        String parentPath = parentPaths[1].replace("\\", "/").substring(1, parentPaths[1].length()) + "/";
                        putCheckpointFile(parentPath, f);
                    } else {
                        putCheckpointFile("", f);
                    }
                }

                if (f.isDirectory()) {
                    getFile(f.getPath());
                }
            }
        }
    }

    public static void putFile(String parentPath, File file) {
        ossClient.putObject(bucketName, parentPath + file.getName(), file);
    }


    public static void putCheckpointFile(String parentPath, File file) throws Throwable {

        UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName,parentPath+file.getName());


        // 通过UploadFileRequest设置单个参数。
        // 填写Bucket名称。
        //uploadFileRequest.setBucketName("examplebucket");
        // 填写Object完整路径。Object完整路径中不能包含Bucket名称。
        //uploadFileRequest.setKey("exampleobject.txt");
        // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        uploadFileRequest.setUploadFile(file.getPath());
        // 指定上传并发线程数，默认值为1。
        uploadFileRequest.setTaskNum(5);
        // 指定上传的分片大小。
        uploadFileRequest.setPartSize(1 * 1024 * 1024);
        // 开启断点续传，默认关闭。
        uploadFileRequest.setEnableCheckpoint(true);
        // 记录本地分片上传结果的文件。上传过程中的进度信息会保存在该文件中。
        uploadFileRequest.setCheckpointFile("checkpointFile");

        // 断点续传上传。
        ossClient.uploadFile(uploadFileRequest);

    }
}
