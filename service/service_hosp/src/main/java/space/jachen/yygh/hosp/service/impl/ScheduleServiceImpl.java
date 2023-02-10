package space.jachen.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
import org.springframework.util.CollectionUtils;
import space.jachen.yygh.common.handler.YyghException;
import space.jachen.yygh.common.result.ResultCodeEnum;
import space.jachen.yygh.hosp.repository.DepartmentRepository;
import space.jachen.yygh.hosp.repository.HospitalRepository;
import space.jachen.yygh.hosp.repository.ScheduleRepository;
import space.jachen.yygh.hosp.service.ScheduleService;
import space.jachen.yygh.hosp.utils.DateUtils;
import space.jachen.yygh.hosp.utils.WeekUtils;
import space.jachen.yygh.model.hosp.BookingRule;
import space.jachen.yygh.model.hosp.Department;
import space.jachen.yygh.model.hosp.Hospital;
import space.jachen.yygh.model.hosp.Schedule;
import space.jachen.yygh.vo.hosp.BookingScheduleRuleVo;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author JaChen
 * @date 2023/2/2 16:22
 */
@Service
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private MongoTemplate template;

    /**
     * 分页查询可预约排班规则
     * @param page  当前页数
     * @param limit  每页多少条数据
     * @param hoscode  医院编号
     * @param depcode  科室编号
     * @return  返回分页后的可预约排班数据
     */
    @Override
    public Map<String, Object> getSubscribeScheduleRule(Integer page, Integer limit, String hoscode, String depcode) {
        // 1.获取医院预约规则
        Hospital hospital = hospitalRepository.findByHoscode(hoscode);
        BookingRule bookingRule = hospital.getBookingRule();
        // 2.获取可预约日期分页数据
        IPage<Date> iPage = this.getListDate(page, limit, bookingRule);
        // 可预约的日期的list集合
        List<Date> dateList = iPage.getRecords();
        // 3.根据医院编号和科室编号查询的规则
        Criteria criteria = Criteria.where("hoscode").is(hoscode)
                .and("depcode").is(depcode).and("workDate").in(dateList);
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
                        .sum("availableNumber").as("availableNumber")
        );
        // 将Schedule.class output到 BookingScheduleRuleVo.class
        AggregationResults<BookingScheduleRuleVo> bookingScheduleRuleVos = template
                .aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> scheduleVoList = bookingScheduleRuleVos.getMappedResults();
        // 4.获取科室剩余预约数
        // 合并数据 将统计数据ScheduleVo根据"安排日期"合并到BookingRuleVo的map对象里
        // 为声明要转化为map呢？因为我们要把日期当作key value里面有医生的排班规则信息
        Map<Date, BookingScheduleRuleVo> scheduleVoMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(scheduleVoList)){
            scheduleVoMap = scheduleVoList.stream().collect(Collectors
                    .toMap(BookingScheduleRuleVo::getWorkDate,BookingScheduleRuleVo->BookingScheduleRuleVo));
        }
        // 5.封装可预约排班规则的list
        ArrayList<Object> bookingScheduleRuleVoList = new ArrayList<>();
        for (int index = 0; index < dateList.size(); index++) {
            // 获取当天
            Date date = dateList.get(index);
            BookingScheduleRuleVo bookingScheduleRuleVo = scheduleVoMap.get(date);
            // 当天没有医生
            if (bookingScheduleRuleVo == null){
                bookingScheduleRuleVo = new BookingScheduleRuleVo();
                // 设置就诊医生人数
                bookingScheduleRuleVo.setDocCount(0);
                // 设置科室剩余预约数(状态 0：正常 1：即将放号 -1：当天已停止挂号)
                bookingScheduleRuleVo.setAvailableNumber(-1);
            }
            // 可预约日期
            bookingScheduleRuleVo.setWorkDate(date);
            // 可预约日期,页面使用
            bookingScheduleRuleVo.setWorkDateMd(date);
            // 将今天日期转换为周几
            String dayOfWeek = WeekUtils.getDayOfWeek(new DateTime(date));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);
            // 设置显示 状态 0：正常 1：即将放号 -1：当天已停止挂号
            bookingScheduleRuleVo.setStatus(0);
            // 最后一页最后一条记录为即将预约
            if (page == iPage.getPages() && index == dateList.size()-1) {
                bookingScheduleRuleVo.setStatus(1);
            } else {
                bookingScheduleRuleVo.setStatus(0);
            }
            // 当天预约时间如果过了停号的时间 则不能预约
            if (page == 1 && index == 0){
                // getDateTime将Date日期（yyyy-MM-dd HH:mm）转换为DateTime
                DateTime stopTime = DateUtils.getDateTime(new Date(), bookingRule.getStopTime());
                if (stopTime.isBeforeNow())
                    bookingScheduleRuleVo.setStatus(-1);
            }


            bookingScheduleRuleVoList.add(bookingScheduleRuleVo);
        }
        // 6.封装结果集resultMap
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("bookingScheduleRuleList",bookingScheduleRuleVoList);
        resultMap.put("total",iPage.getTotal());
        Map<String, Object> baseMap = new HashMap<>();
        baseMap.put("hosname",hospital.getHosname());
        Department department = departmentRepository.findByHoscodeAndDepcode(hoscode, depcode);
        // 大科室名称
        baseMap.put("bigname",department.getBigname());
        // 科室名称
        baseMap.put("depname",department.getDepname());
        baseMap.put("workDateString",new DateTime().toString("yyyy年MM月"));
        // 放号日期
        baseMap.put("releaseTime",bookingRule.getReleaseTime());
        // 停号日期
        baseMap.put("stopTime",bookingRule.getStopTime());
        resultMap.put("baseMap",baseMap);
        return resultMap;
    }

    /*
    获取可预约日期分页数据
     */
    private IPage<Date> getListDate(int page, int limit, BookingRule bookingRule) {
        // 将放号时间转换为今天放号日期+时间 2023-02-10 08:30
        // 预约周期
        Integer cycle = bookingRule.getCycle();
        // 放号时间
        String releaseTime = bookingRule.getReleaseTime();
        DateTime dateTime = DateUtils.getDateTime(new Date(), releaseTime);
        // 如果此时已经过来预约时间，则周期+1 [标记预约周期最后一天为即将放号]
        if (dateTime.isBeforeNow()) {
            cycle+=1;
        }
        // 3.list集合封装预约周期
        List<Date> dateList = new ArrayList<>();
        for (int day = 0; day < cycle; day++) {
            String date = new DateTime().plusDays(day).toString("yyyy-MM-dd");
            dateList.add(new DateTime(date).toDate());
        }
        // 4、分页保存预约周期时间
        // 因为预约周期不一样，页面排版最多显示7天数据，多了就要进行份分页。
        List<Date> pageDateList = new ArrayList<>();
        // 设置循环的开始
        int begin = (page-1)*limit;
        // 设置循环结束
        int end = (page-1)*limit + limit;
        if (end > dateList.size()){
            end = dateList.size();
        }
        // 取出每页数据放到pageDateList
        for (int index = begin; index <end; index++) {
            pageDateList.add(dateList.get(index));
        }
        // 使用mp分页工具
        IPage<Date> mpPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page,7,dateList.size());
        mpPage.setRecords(pageDateList);
        return mpPage;
    }

    /**
     * 查询排班详细信息
     * @param hoscode 医院编号
     * @param depcode 科室编号
     * @param workDate 工作日期
     * @return 返回排班详情的list
     */
    @Override
    public List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate) {
        try {
            // 用DateTime替换
            // new SimpleDateFormat("yyyy-MM-dd").parse(workDate);
            Date date = new DateTime(workDate).toDate();
            return scheduleRepository.findByHoscodeAndDepcodeAndWorkDate(hoscode, depcode, date);
        } catch (Exception e) {
            throw new YyghException(ResultCodeEnum.DATA_ERROR.getCode(),"日期转换异常");
        }
    }

    /**
     *
     * @param page  当前页数
     * @param limit  每页多少条数据
     * @param hoscode  医院编号
     * @param depcode  科室编号
     * @return 返回map  key为bookingScheduleRuleList和total
     */
    @Override
    public Map<String, Object> getScheduleRule(long page, long limit, String hoscode, String depcode) {

        // 1、封装查询规则-BookingScheduleRuleVo
        // 1.1、根据医院编号和科室编号查询的规则
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);
        // 1.2、封装聚合查询后的可预约排班规则数据集合
        List<BookingScheduleRuleVo> scheduleRuleVoList = this.packageAggregation(criteria, page, limit);
        // 1.3、转换日期格式  加工scheduleRuleVoList
        for (BookingScheduleRuleVo bookingScheduleRuleVo : scheduleRuleVoList) {
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
        map.put("bookingScheduleRuleList",scheduleRuleVoList);
        map.put("total",total);
        return map;
    }

    /**
     * 聚合分组 封装可预约排班规则数据
     * @return List<BookingScheduleRuleVo>
     */
    public List<BookingScheduleRuleVo> packageAggregation(Criteria criteria,long page, long limit){
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
        return ruleBooking;
    }

    /**
     * 根据医院编号和排班编号查找分页后的Schedule排班信息
     * @param page  第几页
     * @param limit  每页多少数据
     * @param hoscode  科室编号
     * @param hosScheduleId  排班编号
     * @return   分页后的Schedule排班信息
     */
    @Override
    public Page<Schedule> findPage(Integer page, Integer limit, String hoscode, String hosScheduleId) {
        Sort sort = Sort.by(Sort.Direction.ASC,"hosScheduleId");
        Schedule schedule = Schedule.builder().hosScheduleId(hosScheduleId).hoscode(hoscode).build();
        Example<Schedule> example = Example.of(schedule);
        PageRequest pageRequest = PageRequest.of(page,limit,sort);
        return scheduleRepository.findAll(example,pageRequest);
    }

    /**
     * 根据医院编号和科室id删除
     * @param hoscode  医院编号
     * @param hosScheduleId 排班编号
     * @return String 删除状态
     */
    @Override
    public String remove(String hoscode, String hosScheduleId) {
        Schedule hasSchedule = scheduleRepository.findByHoscodeAndHosScheduleId(hoscode,hosScheduleId);
        if (hasSchedule!=null){
            scheduleRepository.deleteById(hasSchedule.getId());
            return "删除成功";
        }else {
            return "不存在该科室";
        }
    }

    /**
     * 新增或更新科室信息
     * @param schedule  传入科室对象
     */
    @Override
    public void saveSchedule(Schedule schedule) {
        Schedule hasSchedule = scheduleRepository.findByHoscodeAndHosScheduleId(
                schedule.getHoscode(), schedule.getHosScheduleId());
        if (hasSchedule==null){
            scheduleRepository.insert(schedule);
        }else {
            schedule.setId(hasSchedule.getId());
            schedule.setCreateTime(hasSchedule.getCreateTime());
            scheduleRepository.save(schedule);
        }
    }
}
