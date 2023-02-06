package space.jachen.yygh.hosp.service;

import org.springframework.data.domain.Page;
import space.jachen.yygh.model.hosp.Hospital;
import space.jachen.yygh.vo.hosp.HospitalQueryVo;

import java.util.List;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/2 10:37
 */
public interface HospitalService {
    /**
     * 新增医院
     * @param hospital
     */
    void saveById(Hospital hospital);

    /**
     * 查询医院信息的方法
     * @param hoscode
     * @return
     */
    Hospital getHospitalByHoscode(String hoscode);

    /**
     * 分页查询
     * @param page 当前页码
     * @param limit 每页记录数
     * @param hospitalQueryVo 查询条件
     */
    Page<Hospital> findPage(Integer page, Integer limit,
                            HospitalQueryVo hospitalQueryVo);

    Map<String, Object> show(String id);

    /**
     * 根据名字查询医院列表
     * @param hosname  医院名字
     * @return  返回医院列表
     */
    List<Hospital> findByHosname(String hosname);

    /**
     * 医院预约挂号详情
     * @param hoscode 医院编号
     * @return 返回一个封装好医院详情和预约规则的Map
     */
    Map<String, Object> item(String hoscode);
}
