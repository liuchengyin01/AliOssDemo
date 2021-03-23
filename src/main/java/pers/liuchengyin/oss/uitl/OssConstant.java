package pers.liuchengyin.oss.uitl;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName OssConstant
 * @Description
 * @Author 柳成荫
 * @Date 2021/3/23
 */
@Component
public class OssConstant implements InitializingBean {
    /** 从配置文件中读取bucketName */
    @Value("${aliyun.oss.file.bucketname}")
    private String bucketName;
    /** 从配置文件中读取存储文件的地址前缀 */
    @Value("${aliyun.oss.file.httpsprefix}")
    private String ossHttpsPrefix;

    public static String BUCKET_NAME;
    public static String OSS_HTTPS_PREFIX;

    @Override
    public void afterPropertiesSet() throws Exception {
        BUCKET_NAME = bucketName;
        OSS_HTTPS_PREFIX = ossHttpsPrefix;
    }

}
