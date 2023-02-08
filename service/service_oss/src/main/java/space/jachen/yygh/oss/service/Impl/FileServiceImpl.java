package space.jachen.yygh.oss.service.Impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import space.jachen.yygh.common.utils.UuidUtils;
import space.jachen.yygh.oss.service.FileService;
import space.jachen.yygh.oss.utils.ConstantOssPropertiesUtil;

import java.io.InputStream;

/**
 * @author JaChen
 * @date 2023/2/7 23:21
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    /**
     * 文件上传至阿里云
     * 将字符串上传到目标存储空间examplebucket中exampledir目录下
     * 的exampleobject.txt文件。
     */
    @Override
    public String upload(MultipartFile file) {

        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
        String accessKeyId = ConstantOssPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantOssPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ConstantOssPropertiesUtil.BUCKET_NAME;
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            String filename = file.getOriginalFilename();
            // 设置文件路径
            String font = new DateTime().toString("yyyy/MM/dd");
            // 设置文件名
            String resultFileName = font+UuidUtils.getUUID()+filename;
            InputStream inputStream = file.getInputStream();
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, resultFileName, inputStream);
            putObjectRequest.setProcess("true");
            // 上传字符串。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            // 如果上传成功，则返回200。
            log.info(""+result.getResponse().getStatusCode());
            String uri = result.getResponse().getUri();
            log.info("响应给前端的url图片路径"+uri);
            // 设置返回的文件路径
            return uri;
        } catch (Exception oe) {
            oe.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }
}
