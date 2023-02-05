package space.jachen.yygh.cmn.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import space.jachen.yygh.cmn.service.DictService;
import space.jachen.yygh.common.result.JsonData;
import space.jachen.yygh.model.cmn.Dict;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 组织架构表 前端控制器
 * </p>
 *
 * @author jachen
 * @since 2023-01-29
 */
@Api(tags = "数据字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
public class DictController {

    @Autowired
    private DictService dictService;


    @ApiOperation(value = "根据dictCode获取下级节点")
    @GetMapping(value = "/findByDictCode/{dictCode}")
    public JsonData<HashMap<String, List<Dict>>> findByDictCode(
            @PathVariable String dictCode) {
        List<Dict> list = dictService.findByDictCode(dictCode);
        HashMap<String, List<Dict>> map = new HashMap<>();
        map.put("list",list);
        return JsonData.build(map,200,"查询成功");
    }

    @ApiOperation(value = "获取数据字典名称v2")
    @GetMapping(value = "/getName/{parentDictCode}/{value}")
    public String getName(@PathVariable("parentDictCode") String parentDictCode,
                          @PathVariable("value") String value) {
        return dictService.getNameByParentDictCodeAndValue(parentDictCode, value);
    }
    @ApiOperation(value = "获取数据字典名称v1")
    @GetMapping(value = "/getName/{value}")
    public String getName(@PathVariable("value") String value) {
        return dictService.getNameByParentDictCodeAndValue(null, value);
    }


    /**
     * MultipartFile是Spring中用于处理文件上传请求的类。
     * 它是org.springframework.web.multipart.MultipartFile接口的一个实现，
     * 并且通常用于处理使用multipart/form-data编码的HTTP POST请求。
     * 该类提供了一系列方法，用于访问上传文件的元数据，例如文件名、文件类型等，以及操作上传文件的内容，
     * 例如读取文件的内容或将其写入磁盘。
     * 在处理文件上传请求时，可以在控制器方法中接收MultipartFile对象，
     * 然后使用该对象的方法进行文件的读取和处理。
     *
     * @param file 上传的文件
     * @return 返回状态信息
     */
    @ApiOperation(value = "导入数据字典")
    @PostMapping("/importData")
    public JsonData<String> importData(MultipartFile file) {
        dictService.importData(file);
        return JsonData.ok("上传成功");
    }


    @ApiOperation(value="导出数据字典")
    @GetMapping(value = "/exportData")
    public void exportData(HttpServletResponse response) {
        dictService.exportData(response);
    }


    //根据数据id查询子数据列表
    @ApiOperation(value = "根据数据id查询子数据列表")
    @GetMapping("/findChildData/{id}")
    public JsonData<HashMap<String, List<Dict>>> findChildData(
            @PathVariable Long id) {

        List<Dict> list = dictService.findChlidData(id);
        HashMap<String, List<Dict>> hashMap = new HashMap<>();
        hashMap.put("list",list);

        return JsonData.ok(hashMap);
    }

}

