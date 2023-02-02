package space.jachen.yygh.hosp.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import space.jachen.yygh.common.result.JsonData;
import space.jachen.yygh.hosp.service.HospitalService;
import space.jachen.yygh.model.hosp.Hospital;
import space.jachen.yygh.vo.hosp.HospitalQueryVo;

import java.util.HashMap;

/**
 * @author JaChen
 * @date 2023/2/2 20:36
 */
@Api(tags = "医院接口")
@RestController
@RequestMapping("/admin/hosp/hospital")
@CrossOrigin
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @ApiOperation(value = "获取分页列表")
    @GetMapping("/{page}/{limit}")
    public JsonData<HashMap<String, Page<Hospital>>> index(
            @PathVariable Integer page,
            @PathVariable Integer limit,
            HospitalQueryVo hospitalQueryVo) {
        //调用HospitalService中分页及带条件查询的方法
        Page<Hospital> pages = hospitalService.findPage(page, limit, hospitalQueryVo);
        //调用方法
        HashMap<String, Page<Hospital>> map = new HashMap<>();
        map.put("pages",pages);
        return JsonData.ok(map);
    }
}