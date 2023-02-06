package space.jachen.yygh.hosp.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.yygh.common.result.JsonData;
import space.jachen.yygh.hosp.service.HospitalService;
import space.jachen.yygh.model.hosp.Hospital;
import space.jachen.yygh.vo.hosp.HospitalQueryVo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/2 20:36
 */
@Api(tags = "医院接口")
@RestController
@RequestMapping("/admin/hosp/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    /**
     * 根据id获取医院对象和预约规则
     * @param id  医院的id
     * @return 封装好的Map key为hospital和bookingRule
     */
    @ApiOperation(value = "获取医院详情")
    @GetMapping("/show/{id}")
    public JsonData<Map<String, Map<String, Object>>> show(
            @ApiParam(name = "id", value = "医院id", required = true)
            @PathVariable String id) {
        Map<String, Map<String, Object>> hashMap = new HashMap<>();
        Map<String, Object> map = hospitalService.show(id);
        hashMap.put("hospital",map);
        return JsonData.build(hashMap,200,"请求成功");
    }

    /**
     * 分页查询医院列表
     * @param page 当前页码
     * @param limit 每页记录数
     * @param hospitalQueryVo 查询条件
     */
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