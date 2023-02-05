package space.jachen.yygh.hosp.service;

import org.springframework.data.domain.Page;
import space.jachen.yygh.model.hosp.Schedule;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/2 16:22
 */
public interface ScheduleService {
    void saveSchedule(Schedule schedule);
    Page<Schedule> findPage(Integer page, Integer limit, String hoscode, String hosScheduleId);

    String remove(String hoscode, String hosScheduleId);

    /**
     * 分页查询排班规则
     * @param page  当前页数
     * @param limit  每页多少条数据
     * @param hoscode  医院编号
     * @param depcode  科室编号
     * @return  返回分页后的排班数据
     */
    Map<String, Object> getScheduleRule(long page, long limit, String hoscode, String depcode);
}
