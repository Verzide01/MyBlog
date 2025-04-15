package com.piggod.blogfront;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.*;
import com.piggod.common.utils.AliyunOssUtils;
import com.piggod.common.utils.RedisCache;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Random;

import static org.apache.naming.SelectorContext.prefix;

@SpringBootTest
@Component
//@ConfigurationProperties(prefix = "aliyun-oss")
class BlogFrontApplicationTests {

    @Autowired
    private AliyunOssUtils aliyunOssUtils;

    private String endpoint;
    private String bucketName;
    private String region;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
    }

    @Test
    void testDriver() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("MySQL驱动加载成功");
    }

    @Test
    void testRedis() throws ClassNotFoundException {
        redisCache.setCacheObject("test", "test");
    }

    @Test
    void testSecurityPassWordEncrypt() throws ClassNotFoundException {
        String encode1 = passwordEncoder.encode("1234");
        System.out.println(encode1);

    }

    


//    @Test
    void testAliyunOss() throws Exception {
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

        // 创建OSSClient实例。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();
        String objectName = "666.jpg";

        try {
            // 获取所上传文件的输入流

            InputStream fileInputStream = new FileInputStream("C:\\Users\\hasee\\Pictures\\一寸照.jpg");

            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, fileInputStream);

            // 上传图片。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            // 返回 图片资源的url地址

        } catch (
                OSSException oe) {

        } catch (
                ClientException ce) {

        } catch (Exception e) {
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
