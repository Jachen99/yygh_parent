package space.jachen.yygh.hosp.service;

import org.springframework.data.domain.Page;
import space.jachen.yygh.model.hosp.Hospital;
import space.jachen.yygh.vo.hosp.HospitalQueryVo;

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
}
