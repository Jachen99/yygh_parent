package space.jachen.yygh.cmn.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@CrossOrigin
public class DictController {

    @Autowired
    private DictService dictService;


    @ApiOperation(value="导出")
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

