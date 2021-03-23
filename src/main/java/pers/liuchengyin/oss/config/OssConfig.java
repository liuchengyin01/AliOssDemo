package pers.liuchengyin.oss.config;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pers.liuchengyin.oss.uitl.OssConstant;

/**
 * @ClassName OssConfig
 * @Description
 * @Author 柳成荫
 * @Date 2021/3/23
 */
@Configuration
public class OssConfig {
    /** 从配置文件中读取endPoint */
    @Value("${aliyun.oss.file.endpoint}")
    private String ossEndpoint;
    /** 从配置文件中读取KeyId */
    @Value("${aliyun.oss.file.keyid}")
    private String keyId;
    /** 从配置文件中读取KeySecret */
    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;

    @Bean
    public OSSClient ossClient(){
        return (OSSClient) new OSSClientBuilder().build(ossEndpoint, keyId, keySecret);
    }
}
