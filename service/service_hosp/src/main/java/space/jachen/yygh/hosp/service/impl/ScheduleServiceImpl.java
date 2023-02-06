package space.jachen.yygh.hosp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import space.jachen.yygh.hosp.repository.ScheduleRepository;
import space.jachen.yygh.hosp.service.ScheduleService;
import space.jachen.yygh.hosp.utils.WeekUtils;
import space.jachen.yygh.model.hosp.Schedule;
import space.jachen.yygh.vo.hosp.BookingScheduleRuleVo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/2 16:22
 */
@Service
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository repository;

    @Autowired
    private MongoTemplate template;

    @Override
    public Map<String, Object> getUserScheduleRule(String hoscode, String depcode) {

        // 1、封装查询规则-BookingScheduleRuleVo
        // 1.1、根据医院编号和科室编号查询的规则
        Criteria criteria = Criteria.where("hoscode").is(hoscode)
                .and("depcode").is(depcode);
        // 分组：根据工作如wordDate进行分组的规则
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
                        .first("workDate").as("workDate")
                        // 统计同一天的医生数量
                        .count().as("docname")
                        // 科室可预约数
                        .sum("reservedNumber").as("reservedNumber")
                        // 科室剩余预约数
                        .sum("availableNumber").as("availableNumber"),
                // 排序 根据日期降序
                Aggregation.sort(Sort.Direction.ASC,"workDate")
        );
        // 将Schedule.class output到 BookingScheduleRuleVo.class
        AggregationResults<BookingScheduleRuleVo> bookingScheduleRuleVos = template
                .aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> ruleBooking = bookingScheduleRuleVos.getMappedResults();
        log.info("ruleBooking:"+ruleBooking);
        // 转换日期格式
        for (BookingScheduleRuleVo bookingScheduleRuleVo : ruleBooking) {
            String dayOfWeek = bookingScheduleRuleVo.getDayOfWeek();
            String week = WeekUtils.getDayOfWeek(new DateTime(dayOfWeek));
            bookingScheduleRuleVo.setDayOfWeek(week);
        }

        // 2、封装分组查询总页数-total
        Aggregation totalAgg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        AggregationResults<BookingScheduleRuleVo> totalRuleVos = template
                .aggregate(totalAgg, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> ruleTotal = totalRuleVos.getMappedResults();
        int total = ruleTotal.size();
        log.info("total:"+total);
        // 3、封装map集合返回
        HashMap<String, Object> map = new HashMap<>();
        map.put("bookingScheduleRuleList",ruleBooking);
        map.put("total",total);
        return map;
    }

    @Override
    public List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate) {

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(workDate);
           return repository.findByHoscodeAndDepcodeAndWorkDate(hoscode, depcode, date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public Map<String, Object> getScheduleRule(long page, long limit, String hoscode, String depcode) {

        // 1、封装查询规则-BookingScheduleRuleVo
        // 1.1、根据医院编号和科室编号查询的规则
        Criteria criteria = Criteria.where("hoscode").is(hoscode)
                .and("depcode").is(depcode);
        // 分组：根据工作如wordDate进行分组的规则
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
                        .first("workDate").as("workDate")
                        // 统计同一天的医生数量
                        .count().as("docname")
                        // 科室可预约数
                        .sum("reservedNumber").as("reservedNumber")
                        // 科室剩余预约数
                        .sum("availableNumber").as("availableNumber"),
                // 排序 根据日期降序
                Aggregation.sort(Sort.Direction.ASC,"workDate"),
                // 进行分页
                Aggregation.skip((page-1)*limit),
                Aggregation.limit(limit)
        );
        // 将Schedule.class output到 BookingScheduleRuleVo.class
        AggregationResults<BookingScheduleRuleVo> bookingScheduleRuleVos = template
                .aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> ruleBooking = bookingScheduleRuleVos.getMappedResults();
        log.info("ruleBooking:"+ruleBooking);
        // 转换日期格式
        for (BookingScheduleRuleVo bookingScheduleRuleVo : ruleBooking) {
            String dayOfWeek = bookingScheduleRuleVo.getDayOfWeek();
            String week = WeekUtils.getDayOfWeek(new DateTime(dayOfWeek));
            bookingScheduleRuleVo.setDayOfWeek(week);
        }

        // 2、封装分组查询总页数-total
        Aggregation totalAgg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        AggregationResults<BookingScheduleRuleVo> totalRuleVos = template
                .aggregate(totalAgg, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> ruleTotal = totalRuleVos.getMappedResults();
        int total = ruleTotal.size();
        log.info("total:"+total);
        // 3、封装map集合返回
        HashMap<String, Object> map = new HashMap<>();
        map.put("bookingScheduleRuleList",ruleBooking);
        map.put("total",total);
        return map;

    }

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
