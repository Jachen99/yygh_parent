package space.jachen.yygh.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author JaChen
 * @date 2023/2/7 23:20
 */
public interface FileService {

    /**
     * 文件上传至阿里云
     */
    String upload(MultipartFile file);
}
