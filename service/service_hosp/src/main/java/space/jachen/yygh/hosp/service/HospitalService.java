package space.jachen.yygh.hosp.service;

import space.jachen.yygh.model.hosp.Hospital;

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
}
