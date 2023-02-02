package space.jachen.yygh.hosp.service;

import org.springframework.data.domain.Page;
import space.jachen.yygh.model.hosp.Schedule;

/**
 * @author JaChen
 * @date 2023/2/2 16:22
 */
public interface ScheduleService {
    void saveSchedule(Schedule schedule);
    Page<Schedule> findPage(Integer page, Integer limit, String hoscode, String hosScheduleId);

    String remove(String hoscode, String hosScheduleId);
}
