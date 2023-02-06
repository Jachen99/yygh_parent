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
 * 用户登陆前可见的客户端页面
 * @author JaChen
 * @date 2023/2/6 10:27
 */
@Api(tags = "用户端医院显示接口")
@RestController
@RequestMapping("/user/hosp/hospital")
public class UserHospitalController {


    @Autowired
    private ScheduleService scheduleService;

    /**
     * 根据医院编号和科室编号获取用户端的排班规则
     * @param hoscode  医院编号
     * @param depcode  科室编号
     * @return  map对象Map<String, Object>
     */
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

    /**
     * 根据医院编号获取科室对象
     * @param hoscode 医院编号
     * @return  List<DepartmentVo>
     */
    @ApiOperation(value = "获取科室列表")
    @GetMapping("/department/{hoscode}")
    public JsonData<Map<String, Object>> department(@PathVariable String hoscode) {
        List<DepartmentVo> list = departmentService.findDeptTree(hoscode);
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("list",list);
        return JsonData.ok(hashMap);
    }

    /**
     * 根据科室编号获取约挂号详情
     * @param hoscode 医院编号
     * @return  Map<String, Object>
     */
    @ApiOperation(value = "医院预约挂号详情")
    @GetMapping("/{hoscode}")
    public JsonData<Map<String, Object>> show(@PathVariable String hoscode) {
        Map<String, Object> map = hospitalService.item(hoscode);
        return JsonData.ok(map);
    }

    @Autowired
    private HospitalService hospitalService;

    /**
     * 医院分页的列表  模糊查询
     * @param hosname 医院的名字
     * @return  返回List<Hospital>
     */
    @ApiOperation(value = "医院分页列表")
    @GetMapping("/findByHosname/{hosname}")
    public JsonData<Map<String, Object>> findByHosname(
            @ApiParam(name = "hosname", value = "医院名称", required = true)
            @PathVariable String hosname) {
        List<Hospital> list = hospitalService.findByHosname(hosname);
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("list",list);
        return JsonData.ok(hashMap);
    }

    /**
     * 用户端医院显示
     * @param page 页数
     * @param limit 每页显示条数
     * @param hospitalQueryVo 查询条件
     * @return 将Page<Hospital>封装到map返回
     */
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