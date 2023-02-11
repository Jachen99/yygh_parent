package space.jachen.yygh.hosp.service;

import org.springframework.data.domain.Page;
import space.jachen.yygh.model.hosp.Schedule;
import space.jachen.yygh.vo.hosp.ScheduleOrderVo;

import java.util.List;
import java.util.Map;

/**
 * 排班服务接口
 *
 * @author JaChen
 * @date 2023/2/2 16:22
 */
public interface ScheduleService {

    /**
     * 修改排班
     * 用户下单后需要更新排班中可预约号的数量
     *
     * @param schedule 排班实体
     */
    void update(Schedule schedule);

    /**
     * 根据id获取排班的详细信息 订单需要
     *
     * @param scheduleId 排班id
     * @return ScheduleOrderVo
     */
    ScheduleOrderVo getScheduleOrderVo(String scheduleId);

    /**
     * 根据id获取排班的详细信息
     *
     * @param id id
     * @return Schedule
     */
    Schedule getById(String id);

    /**
     * 根据医院编号和排班编号查找分页后的Schedule排班信息
     *
     * @param page  第几页
     * @param limit  每页多少数据
     * @param hoscode  科室编号
     * @param hosScheduleId  排班编号
     * @return   分页后的Schedule排班信息
     */
    Page<Schedule> findPage(Integer page, Integer limit, String hoscode, String hosScheduleId);

    /**
     * 分页查询可预约排班规则
     *
     * @param page  当前页数
     * @param limit  每页多少条数据
     * @param hoscode  医院编号
     * @param depcode  科室编号
     * @return  返回分页后的可预约排班数据
     */
    Map<String, Object> getSubscribeScheduleRule(Integer page, Integer limit, String hoscode, String depcode);

    /**
     * 分页查询排班规则
     *
     * @param page  当前页数
     * @param limit  每页多少条数据
     * @param hoscode  医院编号
     * @param depcode  科室编号
     * @return  返回分页后的排班数据
     */
    Map<String, Object> getScheduleRule(long page, long limit, String hoscode, String depcode);

    /**
     * 查询排班详细信息
     *
     * @param hoscode 医院编号
     * @param depcode 科室编号
     * @param workDate 工作日期
     * @return 返回排班详情的list
     */
    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);

    /**
     * 根据医院编号和科室id删除
     *
     * @param hoscode  医院编号
     * @param hosScheduleId 排班编号
     * @return String 删除状态
     */
    String remove(String hoscode, String hosScheduleId);

    /**
     * 新增或更新科室信息
     *
     * @param schedule  传入科室对象
     */
    void saveSchedule(Schedule schedule);

}
