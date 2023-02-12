package space.jachen.yygh.hosp.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import space.jachen.yygh.hosp.repository.DepartmentRepository;
import space.jachen.yygh.hosp.service.DepartmentService;
import space.jachen.yygh.model.hosp.Department;
import space.jachen.yygh.vo.hosp.DepartmentVo;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author JaChen
 * @date 2023/2/2 14:46
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Resource
    private DepartmentRepository repository;

    /**
     * 根据医院编号获取分页的科室信息
     *
     * @param page  第几页
     * @param limit 每页多少条
     * @param hoscode  医院编号
     * @return  返回Page<Department>
     */
    @Override
    public Page<Department> findPage(Integer page, Integer limit,String hoscode) {
        // 按照depcode升序排序
        Sort sort = Sort.by(Sort.Direction.ASC, "depcode");
        Department department = Department.builder().hoscode(hoscode).build();
        Example<Department> example = Example.of(department);
        // 分页
        PageRequest pageRequest = PageRequest.of(page, limit, sort);
        // 查询所有
        Page<Department> departments = repository.findAll(example,pageRequest);
        departments.forEach(System.out::println);
        return departments;
    }

    /**
     * 根据hoscode查询医院科室的方法
     *
     * @param hoscode 医院编码
     * @return 医院科室集合list
     */
    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {

        // 0、创建封装结果集信息的对象
        List<DepartmentVo> resultList = new ArrayList<>();

        // 1、获取所有科室信息
        Department department = new Department();
        department.setHoscode(hoscode);
        Example<Department> example = Example.of(department);
        List<Department> departmentList = repository.findAll(example);

        // 2、根据bigcode大科室编号进行分组，得到map
        Map<String, List<Department>> listMap = departmentList.stream().collect(
                Collectors.groupingBy(Department::getBigcode)
        );
        // 3、根据分组查询出小科室的list集合
        for (Map.Entry<String, List<Department>> listEntry : listMap.entrySet()) {
            // 3.1 取出来全部信息
            String bigDepCode = listEntry.getKey();
            List<Department> samllDevVoList = listEntry.getValue();
            // 3.2 封装大科室信息
            DepartmentVo bigDepVo = new DepartmentVo();
            bigDepVo.setDepcode(bigDepCode);
            // value中随便拿一个 取出来big名字
            bigDepVo.setDepname(samllDevVoList.get(0).getBigname());
            // 3.3 封装小科室信息
            List<DepartmentVo> childrenList = new ArrayList<>();
            for (Department smallDev : samllDevVoList) {
                DepartmentVo smallDepVo = new DepartmentVo();
                BeanUtils.copyProperties(smallDev,smallDepVo);
                childrenList.add(smallDepVo);
            }
            // 4、将小科室list放入到DepartmentVo
            bigDepVo.setChildren(childrenList);
            // 5、创建List<DepartmentVo>，将查询结果存入并返回
            resultList.add(bigDepVo);
        }
        return resultList;
    }

    /**
     * 根据医院编号和科室编号删除科室
     *
     * @param hoscode  医院编号
     * @param depcode  科室编号
     * @return 返回删除状态
     */
    @Override
    public String remove(String hoscode,String depcode) {
        Department hasDepartment = repository.findByHoscodeAndDepcode(hoscode, depcode);
        if (hasDepartment!=null){
            repository.deleteById(hasDepartment.getId());
            return "删除成功";
        }else {
            return "不存在该科室";
        }
    }

    /**
     * 上传科室信息接口的实现方法
     *
     * @param department  传入的科室对象
     */
    @Override
    public void saveById(Department department) {
        Department hasDepartment = repository.findByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        if (hasDepartment==null){
            repository.insert(department);
        }else {
            // 如果存在该家医院 重置id进行更新操作
            department.setId(hasDepartment.getId());
            department.setCreateTime(hasDepartment.getCreateTime());
            repository.save(department);
        }
    }
}
