package com.piggod.common.utils;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Objects;


/**
 * 阿里云oss封装工具类
 * 根据阿里云文档进行封装
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "aliyun-oss")
@Getter
@Setter
public class AliyunOssUtils {

    private String endpoint;
    private String bucketName;
    private String region;
    private String cdn;

    public String upload(MultipartFile img) throws Exception {

        // 1.处理文件格式
        String imgPath = PathUtils.generateFilePath(Objects.requireNonNull(img.getOriginalFilename()));

        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如2025/sb666.jpg。
        String objectName = imgPath;


        // 创建OSSClient实例。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();

        try {
            // 获取所上传文件的输入流
            InputStream imgInputStream = img.getInputStream();

            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, imgInputStream);

            // 上传图片。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            // 返回 图片资源的url地址
            return cdn + "/" + objectName;

        } catch (
                OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
            log.error("Error Message:" + oe.getErrorMessage());
            log.error("Error Code:" + oe.getErrorCode());
            log.error("Request ID:" + oe.getRequestId());
            log.error("Host ID:" + oe.getHostId());
        } catch (
                ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.");
            log.error("Error Message:" + ce.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return "上传失败";
    }


}
