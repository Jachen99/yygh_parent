package space.jachen.yygh.hosp.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.yygh.common.handler.YyghException;
import space.jachen.yygh.common.result.JsonData;
import space.jachen.yygh.common.result.ResultCodeEnum;
import space.jachen.yygh.hosp.service.DepartmentService;
import space.jachen.yygh.hosp.service.HospitalService;
import space.jachen.yygh.hosp.service.HospitalSetService;
import space.jachen.yygh.hosp.service.ScheduleService;
import space.jachen.yygh.hosp.utils.SignUtils;
import space.jachen.yygh.model.hosp.Department;
import space.jachen.yygh.model.hosp.Hospital;
import space.jachen.yygh.model.hosp.Schedule;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 医院管理 前端控制器
 * 用于对接医院管理的API接口的控制器
 *
 * @author JaChen
 * @date 2023/2/2 10:36
 */
@Api(tags = "医院管理API接口")
@RestController
@RequestMapping("/api/hosp")
public class ApiController {
    @Resource
    private DepartmentService departmentService;
    @Resource
    private HospitalService hospitalService;
    @Autowired
    private HospitalSetService hospitalSetService;
    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation("上传医院信息")
    @PostMapping("/saveHospital")
    public JsonData<Hospital> saveHospital(Hospital hospital,HttpServletRequest request){

        // 签名 比较后台与前台的hoscode值获取signkey
        String sign = request.getParameter("sign");
        // 专属验签小工具 用于校验对接医院签名
        Boolean keyOK = SignUtils.verifyKeyOK(sign, hospital.getHoscode(), hospitalSetService);
        if (keyOK){
            hospitalService.saveById(hospital);
        }else {
            throw new YyghException(ResultCodeEnum.SIGN_REQUEST.getCode(),"验签失败");
        }
        return JsonData.ok();
    }

    @ApiOperation("获取医院信息")
    @PostMapping("/hospital/show")
    public JsonData<Hospital> queryHospital(HttpServletRequest request) {
        // 获取医院编码
        String hoscode = request.getParameter("hoscode");
        if(StringUtils.isEmpty(hoscode)) {
            throw new YyghException(20001,"无该医院");
        }
        String sign = request.getParameter("sign");
        Boolean keyOK = SignUtils.verifyKeyOK(sign, hoscode, hospitalSetService);
        if (!keyOK){
            throw new YyghException(ResultCodeEnum.SIGN_REQUEST.getCode(),"验签失败");
        }
        // 调用医院服务进行查询展示医院的数据
        Hospital hospital = hospitalService.getHospitalByHoscode(hoscode);
        return JsonData.ok(hospital);
    }

    @ApiOperation("上传科室信息")
    @PostMapping("/saveDepartment")
    public JsonData<Department> saveDepartment(Department department,HttpServletRequest request){
        String sign = request.getParameter("sign");
        Boolean keyOK = SignUtils.verifyKeyOK(sign, department.getHoscode(), hospitalSetService);
        if (!keyOK){
            throw new YyghException(ResultCodeEnum.SIGN_REQUEST.getCode(),"验签失败");
        }
        departmentService.saveById(department);
        return JsonData.ok();
    }

    @ApiOperation("删除科室信息")
    @PostMapping("/department/remove")
    public JsonData<String> removeDepartment(HttpServletRequest request){
        String sign = request.getParameter("sign");
        String hoscode = request.getParameter("hoscode");
        Boolean keyOK = SignUtils.verifyKeyOK(sign, hoscode, hospitalSetService);
        if (!keyOK){
            throw new YyghException(ResultCodeEnum.SIGN_REQUEST.getCode(),"验签失败");
        }
        String remove = departmentService.remove(hoscode, request.getParameter("depcode"));
        return JsonData.ok(remove);
    }

    @ApiOperation("查询科室信息")
    @PostMapping("/department/list")
    public JsonData<Page<Department>> queryDepartment(HttpServletRequest request){
        Map<String,Integer> pageParam = this.getPageParam(request);
        String sign = request.getParameter("sign");
        String hoscode = request.getParameter("hoscode");
        Boolean keyOK = SignUtils.verifyKeyOK(sign, hoscode, hospitalSetService);
        if (!keyOK){
            throw new YyghException(ResultCodeEnum.SIGN_REQUEST.getCode(),"验签失败");
        }
        Integer page = pageParam.get("page");
        Integer limit = pageParam.get("limit");
        // 调用DepartmentService中分页的方法
        Page<Department> departmentPage = departmentService.findPage(page,limit,request.getParameter("hoscode"));
        return JsonData.build(departmentPage,200,"查询成功");
    }

    @ApiOperation("上传排班信息")
    @PostMapping("/saveSchedule")
    public JsonData<Schedule> saveSchedule(Schedule schedule,HttpServletRequest request){
        String sign = request.getParameter("sign");
        String hoscode = request.getParameter("hoscode");
        Boolean keyOK = SignUtils.verifyKeyOK(sign, hoscode, hospitalSetService);
        if (!keyOK){
            throw new YyghException(ResultCodeEnum.SIGN_REQUEST.getCode(),"验签失败");
        }
        scheduleService.saveSchedule(schedule);
        return JsonData.ok();
    }

    @ApiOperation("删除排班信息")
    @PostMapping("/schedule/remove")
    public JsonData<String> removeSchedule(HttpServletRequest request){
        String sign = request.getParameter("sign");
        String hoscode = request.getParameter("hoscode");
        Boolean keyOK = SignUtils.verifyKeyOK(sign, hoscode, hospitalSetService);
        if (!keyOK){
            throw new YyghException(ResultCodeEnum.SIGN_REQUEST.getCode(),"验签失败");
        }
        String remove = scheduleService.remove(hoscode, request.getParameter("hosScheduleId"));
        return JsonData.ok(remove);
    }

    @ApiOperation("查询排班信息")
    @PostMapping("/schedule/list")
    public JsonData<Page<Schedule>> querySchedule(HttpServletRequest request){
        // 获取参数
        String sign = request.getParameter("sign");
        String hoscode = request.getParameter("hoscode");
        Boolean keyOK = SignUtils.verifyKeyOK(sign, hoscode, hospitalSetService);
        if (!keyOK){
            throw new YyghException(ResultCodeEnum.SIGN_REQUEST.getCode(),"验签失败");
        }
        Map<String,Integer> pageParam = this.getPageParam(request);
        Integer page = pageParam.get("page");
        Integer limit = pageParam.get("limit");
        Page<Schedule> servicePage = scheduleService
                .findPage(page,limit,hoscode,request.getParameter("hosScheduleId"));
        return JsonData.build(servicePage,200,"查询成功");
    }

    /**
     * 用于获取分页参数的公用方法
     *
     * @param request  HttpServletRequest
     * @return  Map<String,Integer>
     */
    private Map<String,Integer> getPageParam(HttpServletRequest request) {
        Integer page =
                StringUtils.isEmpty(request.getParameter("page"))
                        ? 1 : Integer.parseInt( request.getParameter("page"));
        Integer limit =
                StringUtils.isEmpty(request.getParameter("limit"))
                        ? 0 : Integer.parseInt( request.getParameter("limit"));
        return new HashMap<String,Integer>(){{
            put("page",page);
            put("limit",limit);
        }};
    }

}
