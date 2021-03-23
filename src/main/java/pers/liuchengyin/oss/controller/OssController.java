package pers.liuchengyin.oss.controller;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pers.liuchengyin.oss.uitl.OssConstant;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName OssController
 * @Description OSS相关操作
 * @Author 柳成荫
 * @Date 2021/3/23
 */
@RestController
@RequestMapping("/oss")
public class OssController {
    @Autowired
    private OSSClient ossClient;

    /**
     * 上传图片到OSS
     * @param file 图片文件
     * @return 图片访问地址
     */
    @PostMapping("/upload")
    public String ossUpload(MultipartFile file){
        // 获取原始文件名(我一般都是用UUID生成新的文件名)
        String fileName = file.getOriginalFilename();
        // 文件夹名(可以用模块名)
        String folder = "liuchengyin";
        // 文件夹名(根据日期来存储)
        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        // 文件名 - 使用UUID生成
        String fileNameUUID = UUID.randomUUID().toString().replaceAll("-", "");
        if (fileName != null){
            // 获取原始文件名的后缀，如.jpg .png
            fileNameUUID = folder+ "/" + format + "/" + fileNameUUID + fileName.substring(fileName.lastIndexOf("."));
        } else {
            // 一般来说这种情况是不存在的
            fileNameUUID = folder+ "/" + format + "/" + fileNameUUID + ".jpg";
        }
        try {
            ossClient.putObject(OssConstant.BUCKET_NAME, fileNameUUID, file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 返回文件访问地址 - 这里可以封装一个对象，返回访问地址和文件名(也就是fileNameUUID)
        // 这个文件名(fileNameUUID)可以用于查询是否存在、删除等操作
        return OssConstant.OSS_HTTPS_PREFIX + "/" + fileNameUUID;
    }

    /**
     * 删除OSS里的文件/文件夹(文件夹内不能有文件)
     * @param fileName 文件名 - 完整文件名包括文件夹名，如：liuchengyin/2021-03-23/f624048f7ca8466881f825365e3308d4.jpg
     * @return
     */
    @PostMapping("/delete")
    public String ossDelete(String fileName){
        ossClient.deleteObject(OssConstant.BUCKET_NAME, fileName);
        return "删除成功！";
    }

    /**
     * 批量删除OSS里的文件
     * @param fileNames 文件名集合 - 完整文件名包括文件夹名，如：liuchengyin/2021-03-23/f624048f7ca8466881f825365e3308d4.jpg
     * @return
     */
    @PostMapping("/deleteBatch")
    public List<String> ossDeleteBatch(@RequestBody List<String> fileNames){
        DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(OssConstant.BUCKET_NAME).withKeys(fileNames));
        // 返回的就是删除成功的图片名集合(图片不存在，也会返回)
        return deleteObjectsResult.getDeletedObjects();
    }

    /**
     * 查询OSS里的文件是否存在
     * @param fileName 文件名 - 完整文件名包括文件夹名，如：liuchengyin/2021-03-23/f624048f7ca8466881f825365e3308d4.jpg
     * @return 是否存在:true or false
     */
    @GetMapping("/exist")
    public Boolean isExist(String fileName) {
        return ossClient.doesObjectExist(OssConstant.BUCKET_NAME, fileName);
    }
}
