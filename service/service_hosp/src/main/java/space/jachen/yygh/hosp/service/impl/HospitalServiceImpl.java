package space.jachen.yygh.hosp.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import space.jachen.yygh.hosp.repository.HospitalRepository;
import space.jachen.yygh.hosp.service.HospitalService;
import space.jachen.yygh.model.hosp.Hospital;
import space.jachen.yygh.vo.hosp.HospitalQueryVo;

/**
 * @author JaChen
 * @date 2023/2/2 10:38
 */
@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository repository;

    @Override
    public Page<Hospital> findPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        //排序
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //分页
        Pageable pageRequest = PageRequest.of(page-1, limit, sort);
        //创建Hospital对象
        Hospital hospital = new Hospital();
        //将HospitalQueryVo中的属性复制到Hospital对象中
        BeanUtils.copyProperties(hospitalQueryVo,hospital);
        //创建匹配器对象，即如何使用查询条件
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)//模糊查询
                .withIgnoreCase(true);
        //创建Example对象
        Example<Hospital> example = Example.of(hospital, exampleMatcher);
        return repository.findAll(example, pageRequest);
    }

    /**
     * 增加医院信息的方法
     * @param hospital
     */
    @Override
    public void saveById(Hospital hospital) {

        // 1、把logo中空格替换为+  因为在http传输中会默认把+变成空格 我们要转化回去
        String logoData = hospital.getLogoData();
        String replace = logoData.replace(" ", "+");
        hospital.setLogoData(replace);

        // 2、根据医院编码 查找本地数据库有没有该家医院
        Hospital isHospital = repository.findByHoscode(hospital.getHoscode());

        // 3、存入数据  如果没有直接插入
        if (isHospital==null){
            repository.insert(hospital);
        }else {
            // 如果存在该家医院  重置id进行更新操作
            hospital.setId(isHospital.getId());
            hospital.setUpdateTime(isHospital.getCreateTime());
            repository.save(hospital);
        }

    }


    /**
     * 查询医院的方法
     * @param hoscode
     * @return
     */
    @Override
    public Hospital getHospitalByHoscode(String hoscode) {
        return repository.findByHoscode(hoscode);
    }

}
