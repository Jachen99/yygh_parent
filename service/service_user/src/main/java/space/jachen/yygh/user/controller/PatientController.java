package space.jachen.yygh.user.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import space.jachen.yygh.common.result.JsonData;
import space.jachen.yygh.common.utils.JwtHelper;
import space.jachen.yygh.model.user.Patient;
import space.jachen.yygh.user.service.PatientService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 就诊人表 前端控制器
 * </p>
 *
 * @author jachen
 * @since 2023-02-06
 */
@Api(tags = "就诊人管理接口")
@RestController
@RequestMapping("/yygh/user/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @ApiOperation(value = "获取全部就诊人信息")
    @GetMapping("/auth/findAll")
    public JsonData<Map<String, List<Patient>>> findAll(HttpServletRequest request){
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token))return null;
        Long userId = JwtHelper.getUserId(token);
        List<Patient> patientList = patientService.getAll(userId);
        Map<String, List<Patient>> hashMap = new HashMap<>();
        hashMap.put("list",patientList);
        return JsonData.ok(hashMap);
    }

    @ApiOperation(value = "根据id获取就诊人信息")
    @GetMapping("/auth/get/{id}")
    public JsonData<Map<String, Patient>> findOne(@PathVariable Long id){  // 注意rest风格@PathVariable不能省略
        Patient patient = patientService.getDetailById(id);
        Map<String, Patient> hashMap = new HashMap<>();
        hashMap.put("patient",patient);
        if (patient!=null){
            return JsonData.ok(hashMap);
        }else {
            return JsonData.ok(null);
        }
    }

    @ApiOperation(value = "添加就诊人信息")
    @PostMapping("/auth/save")
    public JsonData<Object> saveOne(@RequestBody Patient patient, HttpServletRequest request){
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token))return null;
        Long userId = JwtHelper.getUserId(token);
        patient.setUserId(userId);
        patientService.save(patient);
        return JsonData.ok().message("新增成功");
    }

    @ApiOperation("修改就诊人信息")
    @PutMapping("/auth/update")
    public JsonData<Object> updatePatient(@RequestBody Patient patient) {
        patientService.updateById(patient);
        return JsonData.ok();
    }

    @ApiOperation("删除就诊人信息")
    @DeleteMapping("/auth/remove/{id}")
    public JsonData<Object> removePatient(@PathVariable Long id) {
        patientService.removeById(id);
        return JsonData.ok();
    }

}

