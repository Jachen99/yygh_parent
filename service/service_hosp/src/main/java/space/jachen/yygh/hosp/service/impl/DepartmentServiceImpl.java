package space.jachen.yygh.hosp.service.impl;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import space.jachen.yygh.hosp.repository.DepartmentRepository;
import space.jachen.yygh.hosp.service.DepartmentService;
import space.jachen.yygh.model.hosp.Department;

import javax.annotation.Resource;

/**
 * @author JaChen
 * @date 2023/2/2 14:46
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Resource
    private DepartmentRepository repository;

    @Override
    public void saveById(Department department) {

        Department hasDepartment = repository
                .findByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        if (hasDepartment==null){
            repository.insert(department);
        }else {
            // 如果存在该家医院  重置id进行更新操作
            department.setId(hasDepartment.getId());
            department.setCreateTime(hasDepartment.getCreateTime());
            repository.save(department);
        }
    }

    @Override
    public Page<Department> findPage(Integer page, Integer limit,String hoscode) {
        //按照depcode升序排序
        Sort sort = Sort.by(Sort.Direction.ASC, "depcode");

        Department department = Department.builder().hoscode(hoscode).build();
        Example<Department> example = Example.of(department);
        //分页
        PageRequest pageRequest = PageRequest.of(page, limit, sort);
        //查询所有
        Page<Department> departments = repository.findAll(example,pageRequest);
        departments.forEach(System.out::println);
        return departments;
    }

    @Override
    public String remove(String hoscode,String depcode) {
        Department hasDepartment = repository
                .findByHoscodeAndDepcode(hoscode, depcode);
        if (hasDepartment!=null){
            repository.deleteById(hasDepartment.getId());
            return "删除成功";
        }else {
            return "不存在该科室";
        }
    }


}
