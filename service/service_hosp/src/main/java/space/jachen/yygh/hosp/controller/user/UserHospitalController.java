package space.jachen.yygh.hosp.controller.user;

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
import space.jachen.yygh.model.hosp.Schedule;
import space.jachen.yygh.vo.hosp.DepartmentVo;
import space.jachen.yygh.vo.hosp.HospitalQueryVo;
import space.jachen.yygh.vo.hosp.ScheduleOrderVo;

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
    private HospitalService hospitalService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "根据排班id获取订单需要的排班信息")
    @GetMapping("/getScheduleOrderVo/{scheduleId}")
    public ScheduleOrderVo getScheduleOrderVo(@PathVariable String scheduleId){
        return scheduleService.getScheduleOrderVo(scheduleId);
    }

    @ApiOperation(value = "用户获取挂号时排班详情")
    @GetMapping("/auth/getSchedule/{id}")
    public JsonData<HashMap<String, Schedule>> getScheduleList(
            @PathVariable String id) {
        Schedule schedule = scheduleService.getById(id);
        HashMap<String, Schedule> hashMap = new HashMap<>();
        hashMap.put("schedule",schedule);
        return JsonData.ok(hashMap);
    }

    @ApiOperation(value = "获取排班数据详情")
    @GetMapping("/auth/findScheduleList/{hoscode}/{depcode}/{workDate}")
    public JsonData<Map<String, List<Schedule>>> findScheduleList(
            @PathVariable String hoscode,
            @PathVariable String depcode,
            @PathVariable String workDate) {
        List<Schedule> scheduleList = scheduleService.getDetailSchedule(hoscode, depcode, workDate);
        Map<String, List<Schedule>> hashMap = new HashMap<>();
        hashMap.put("scheduleList",scheduleList);
        return JsonData.ok(hashMap);
    }

    @ApiOperation(value = "获取可预约排班数据")
    @GetMapping("/auth/getBookingScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public JsonData<Map<String, Object>> getBookingSchedule(
            @PathVariable Integer page,
            @PathVariable Integer limit,
            @PathVariable String hoscode,
            @PathVariable String depcode) {
        Map<String, Object> map = scheduleService
                .getSubscribeScheduleRule(page, limit, hoscode, depcode);
        return JsonData.ok(map);
    }

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

    /**
     * 医院分页的列表  模糊查询
     * @param hosname 医院的名字
     * @return  返回List<Hospital>
     */
    @ApiOperation(value = "根据名字获取医院列表")
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