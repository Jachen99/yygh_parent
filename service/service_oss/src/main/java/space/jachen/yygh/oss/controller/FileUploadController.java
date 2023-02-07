package space.jachen.yygh.oss.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import space.jachen.yygh.common.result.JsonData;
import space.jachen.yygh.oss.service.FileService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/7 23:23
 */
@Api(tags="阿里云文件管理")
@RestController
@RequestMapping("/yygh/oss/file")
public class FileUploadController {
    @Resource
    private FileService fileService;
    /**
     * 文件上传
     */
    @ApiOperation(value = "文件上传")
    @PostMapping("upload")
    public JsonData<Map<String, String>> upload(
            @ApiParam(name = "file", value = "文件", required = true)
            @RequestParam("file") MultipartFile file) {
        String uploadUrl = fileService.upload(file);
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("url",uploadUrl);
        return JsonData.ok(hashMap).message("文件上传成功");
    }
}