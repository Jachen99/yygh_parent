package space.jachen.yygh.hosp.service;

import org.springframework.data.domain.Page;
import space.jachen.yygh.model.hosp.Department;
import space.jachen.yygh.vo.hosp.DepartmentVo;

import java.util.List;

/**
 * @author JaChen
 * @date 2023/2/2 14:46
 */
public interface DepartmentService {
    /**
     * 上传科室信息的方法
     * @param department
     */
    void saveById(Department department);


    Page<Department> findPage(Integer page, Integer limit,String hoscode);


    String remove(String hoscode, String depcode);

    /**
     * 根据hoscode查询医院科室的方法
     * @param hoscode 医院编码
     * @return 医院科室集合list
     */
    List<DepartmentVo> findDeptTree(String hoscode);
}
