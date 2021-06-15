package com.tunan.oss.operation;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.UploadFileRequest;
import com.tunan.oss.utils.SecretUtils;

import java.io.File;
import java.util.Date;

public class OSSDownloadFile {

    static String endpoint = "https://oss-cn-shenzhen.aliyuncs.com";

    static String bucketName = "tunan-bucket";

    private static OSS ossClient = null;

    // 构造器中拿到OSS客户端
    static {
        ossClient = new OSSClientBuilder().build(endpoint, SecretUtils.AccessKeyID, SecretUtils.AccessKeySecret);
    }


    public static void main(String[] args) throws Throwable {
        downloadFile("target/*");
    }

    public static void downloadFile(String path) throws Throwable {
        GetObjectRequest request = new GetObjectRequest(bucketName, path);
        // 设置限定条件。
        request.setUnmodifiedSinceConstraint(new Date());

        // 下载OSS文件到本地文件。
        ossClient.getObject(request, new File("F:/a.txt"));

    }
}
