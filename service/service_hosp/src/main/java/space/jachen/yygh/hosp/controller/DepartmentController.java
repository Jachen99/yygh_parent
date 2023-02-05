package space.jachen.yygh.hosp.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.yygh.common.result.JsonData;
import space.jachen.yygh.hosp.service.DepartmentService;
import space.jachen.yygh.vo.hosp.DepartmentVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/5 10:34
 */
@RestController
@RequestMapping("/admin/hosp/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 根据医院编号，查询医院所有科室列表树
     * @param hoscode 医院编号
     * @return 医院的list树
     */
    @ApiOperation(value = "查询医院所有科室列表")
    @GetMapping("/getDeptList/{hoscode}")
    public JsonData<Map<String, Object>> getDeptList(@PathVariable String hoscode) {
        List<DepartmentVo> list = departmentService.findDeptTree(hoscode);
        System.out.println("list = " + list);
        Map<String, Object> map = new HashMap<>();
        map.put("list",list);
        return JsonData.build(map,200,"查询成功");
    }

}
