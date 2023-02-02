package space.jachen.yygh.hosp.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.yygh.common.handler.YyghException;
import space.jachen.yygh.common.result.JsonData;
import space.jachen.yygh.hosp.service.DepartmentService;
import space.jachen.yygh.hosp.service.HospitalService;
import space.jachen.yygh.model.hosp.Department;
import space.jachen.yygh.model.hosp.Hospital;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author JaChen
 * @date 2023/2/2 10:36
 */
@Api(tags = "医院管理API接口")
@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    @Resource
    private HospitalService hospitalService;

    @ApiOperation("上传医院信息")
    @PostMapping("/saveHospital")
    public JsonData<Hospital> saveHospital(Hospital hospital){
        hospitalService.saveById(hospital);
        return JsonData.ok();
    }

    @ApiOperation("获取医院信息")
    @PostMapping("/hospital/show")
    public JsonData<Hospital> queryHospital(HttpServletRequest request) {
        //获取医院编码
        String hoscode = request.getParameter("hoscode");
        if(StringUtils.isEmpty(hoscode)) {
            throw new YyghException(20001,"无该医院");
        }
        //签名校验 略
        Hospital hospital = hospitalService.getHospitalByHoscode(hoscode);
        return JsonData.ok(hospital);
    }

    @Resource
    private DepartmentService departmentService;

    @ApiOperation("上传科室信息")
    @PostMapping("/saveDepartment")
    public JsonData<Department> saveDepartment(
            Department department){

        departmentService.saveById(department);
        return JsonData.ok();
    }


    @ApiOperation("查询科室信息")
    @PostMapping("/department/list")
    public JsonData<Page<Department>> queryDepartment(HttpServletRequest request){

        Integer page = StringUtils.isEmpty(request.getParameter("page")) ?
                1 : Integer.parseInt(request.getParameter("page"));

        Integer limit = StringUtils.isEmpty(request.getParameter("limit")) ?
                0 : Integer.parseInt(request.getParameter("limit"));

        //调用DepartmentService中分页的方法
        Page<Department> departmentPage = departmentService.findPage(page,limit,request.getParameter("hoscode"));
        return JsonData.build(departmentPage,200,"查询成功");
    }

}
