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
     * 根据医院编号获取分页的科室信息
     * @param page  第几页
     * @param limit 每页多少条
     * @param hoscode  医院编号
     * @return  返回Page<Department>
     */
    Page<Department> findPage(Integer page, Integer limit,String hoscode);


    /**
     * 根据hoscode查询医院科室的方法
     * @param hoscode 医院编码
     * @return 医院科室集合list
     */
    List<DepartmentVo> findDeptTree(String hoscode);

    /**
     * 根据医院编号和科室编号删除科室
     * @param hoscode  医院编号
     * @param depcode  科室编号
     * @return 返回删除状态
     */
    String remove(String hoscode, String depcode);

    /**
     * 上传科室信息的方法
     * @param department  传入的科室对象
     */
    void saveById(Department department);
}
