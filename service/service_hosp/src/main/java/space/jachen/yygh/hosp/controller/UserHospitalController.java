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
import space.jachen.yygh.hosp.service.DepartmentService;
import space.jachen.yygh.hosp.service.HospitalService;
import space.jachen.yygh.hosp.service.ScheduleService;
import space.jachen.yygh.model.hosp.Hospital;
import space.jachen.yygh.vo.hosp.DepartmentVo;
import space.jachen.yygh.vo.hosp.HospitalQueryVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/6 10:27
 */
@Api(tags = "用户端医院显示接口")
@RestController
@RequestMapping("/user/hosp/hospital")
public class UserHospitalController {


    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "获取科室排班规则")
    @GetMapping("/schedule/{hoscode}/{depcode}")
    public JsonData<Map<String,Object>> getScheduleRule(
            @PathVariable String hoscode,
            @PathVariable String depcode
    ){
        Map<String,Object> map = scheduleService.getUserScheduleRule(hoscode,depcode);
        return JsonData.ok(map);
    }

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value = "获取科室列表")
    @GetMapping("/department/{hoscode}")
    public JsonData<Map<String, Object>> department(@PathVariable String hoscode) {
        List<DepartmentVo> list = departmentService.findDeptTree(hoscode);
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("list",list);
        return JsonData.ok(hashMap);
    }
    @ApiOperation(value = "医院预约挂号详情")
    @GetMapping("/{hoscode}")
    public JsonData<Map<String, Object>> show(@PathVariable String hoscode) {
        Map<String, Object> map = hospitalService.item(hoscode);
        return JsonData.ok(map);
    }

    @Autowired
    private HospitalService hospitalService;

    @ApiOperation(value = "根据医院名称获取医院列表")
    @GetMapping("/findByHosname/{hosname}")
    public JsonData<Map<String, Object>> findByHosname(
            @ApiParam(name = "hosname", value = "医院名称", required = true)
            @PathVariable String hosname) {
        List<Hospital> list = hospitalService.findByHosname(hosname);
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("list",list);
        return JsonData.ok(hashMap);
    }

    @ApiOperation(value = "获取分页列表")
    @GetMapping("/{page}/{limit}")
    public JsonData<Map<String, Object>> index(@PathVariable Integer page,
                          @PathVariable Integer limit,
                          HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> pageModel = hospitalService.findPage(page, limit, hospitalQueryVo);
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("pages",pageModel);
        return JsonData.ok(hashMap);
    }
}