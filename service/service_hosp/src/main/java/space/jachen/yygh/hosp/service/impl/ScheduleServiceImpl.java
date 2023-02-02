package space.jachen.yygh.hosp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import space.jachen.yygh.hosp.repository.ScheduleRepository;
import space.jachen.yygh.hosp.service.ScheduleService;
import space.jachen.yygh.model.hosp.Schedule;

/**
 * @author JaChen
 * @date 2023/2/2 16:22
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository repository;
    @Override
    public void saveSchedule(Schedule schedule) {
        Schedule hasSchedule = repository
                .findByHoscodeAndHosScheduleId(schedule.getHoscode()
                        , schedule.getHosScheduleId());
        if (hasSchedule==null){
            repository.insert(schedule);
        }else {
            schedule.setId(hasSchedule.getId());
            schedule.setCreateTime(hasSchedule.getCreateTime());
            repository.save(schedule);
        }
    }

    @Override
    public Page<Schedule> findPage(Integer page, Integer limit, String hoscode, String hosScheduleId) {
        Sort sort = Sort.by(Sort.Direction.ASC,"hosScheduleId");
        Schedule schedule = Schedule.builder().hosScheduleId(hosScheduleId).hoscode(hoscode).build();
        Example<Schedule> example = Example.of(schedule);
        PageRequest pageRequest = PageRequest.of(page,limit,sort);
        return repository.findAll(example,pageRequest);
    }

    @Override
    public String remove(String hoscode, String hosScheduleId) {
        Schedule hasSchedule = repository
                .findByHoscodeAndHosScheduleId(hoscode,hosScheduleId);
        if (hasSchedule!=null){
            repository.deleteById(hasSchedule.getId());
            return "删除成功";
        }else {
            return "不存在该科室";
        }
    }
}
