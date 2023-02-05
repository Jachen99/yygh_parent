package space.jachen.yygh.hosp.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.yygh.common.result.JsonData;
import space.jachen.yygh.hosp.service.ScheduleService;
import space.jachen.yygh.model.hosp.Schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/5 15:24
 */
@Api(tags = "排班接口")
@RestController
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService service;


    @ApiOperation(value = "查询排班详细信息")
    @GetMapping("/getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public JsonData<Map<String, Object>> getScheduleDetail(@PathVariable String hoscode,
                                    @PathVariable String depcode,
                                    @PathVariable String workDate) {
        List<Schedule> list = service.getDetailSchedule(hoscode,depcode,workDate);
        Map<String, Object> map = new HashMap<>();
        map.put("list",list);
        System.out.println("map = " + map);
        return JsonData.ok(map);
    }

    @ApiOperation(value = "查询排班规则")
    @GetMapping("/getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public JsonData<Map<String,Object>> getScheduleRule(
            @PathVariable long page,
            @PathVariable long limit,
            @PathVariable String hoscode,
            @PathVariable String depcode
    ){
        Map<String,Object> map = service.getScheduleRule(page,limit,hoscode,depcode);
        return JsonData.ok(map);
    }


}
