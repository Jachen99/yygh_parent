package space.jachen.yygh.hosp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import space.jachen.yygh.cmn.DictFeignClient;
import space.jachen.yygh.enums.DictEnum;
import space.jachen.yygh.hosp.repository.HospitalRepository;
import space.jachen.yygh.hosp.service.HospitalService;
import space.jachen.yygh.model.hosp.Hospital;
import space.jachen.yygh.vo.hosp.HospitalQueryVo;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/2 10:38
 */
@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository repository;

    //注入远程调用数据字典
    @Resource
    private DictFeignClient dictFeignClient;


    @Override
    public Map<String, Object> item(String hoscode) {
        Map<String, Object> result = new HashMap<>();
        //医院详情
        Hospital hospital = this.packHospital(repository.findByHoscode(hoscode));
        result.put("hospital", hospital);
        //预约规则
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }

    @Override
    public List<Hospital> findByHosname(String hosname) {
        return repository.findHospitalByHosnameLike(hosname);
    }

    /**
     * 分页查询医院信息
     * @param page 当前页码
     * @param limit 每页记录数
     * @param hospitalQueryVo 查询条件
     * @return
     */
    @Override
    public Page<Hospital> findPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        //排序
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //分页
        Pageable pageRequest = PageRequest.of(page-1, limit, sort);
        //创建Hospital对象
        Hospital hospital = new Hospital();
        hospital.setHosname(hospitalQueryVo.getHosname());

        /*
        修复客户端不能按条件展示医院列表的bug ---> 没有把传入条件赋给hospital
         */
        hospital.setHostype(hospitalQueryVo.getHostype());
        hospital.setDistrictCode(hospitalQueryVo.getDistrictCode());

        //创建匹配器对象，即如何使用查询条件
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)//模糊查询
                .withIgnoreCase(true);
        //创建Example对象
        Example<Hospital> example = Example.of(hospital, exampleMatcher);
        Page<Hospital> hospitals = repository.findAll(example, pageRequest);

        //获取Page里的医院信息
        List<Hospital> hospitalList = hospitals.getContent();

        //遍历
        hospitalList.forEach(this::packHospital);
        return hospitals;
    }

    @Override
    public Map<String, Object> show(String id) {
        Map<String, Object> result = new HashMap<>();
        Hospital hospital = this.packHospital(repository.findById(id).get());
        //医院基本信息（包含医院等级）
        result.put("hospital",hospital);
        //单独处理更直观
        result.put("bookingRule", hospital.getBookingRule());
        //医院里不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }

    /**
     * 远程调用查询字典的方法
     * @param hospital hospital对象
     * @return Hospital
     */
    private Hospital packHospital(Hospital hospital) {
        //获取医院等级
        String hostypeName = dictFeignClient.getName(DictEnum.HOSTYPE.getDictCode(), hospital.getHostype());
        //获取省
        String provinceName = dictFeignClient.getName(hospital.getProvinceCode());
        //获取市
        String cityName = dictFeignClient.getName(hospital.getCityCode());
        //获取区
        String districtName = dictFeignClient.getName(hospital.getDistrictCode());
        //封装数据
        hospital.getParam().put("hostypeString",hostypeName);
        hospital.getParam().put("fullAddress",provinceName+cityName+districtName+hospital.getAddress());
        return hospital;
    }

    /**
     * 增加医院信息的方法
     * @param hospital 传入医院对象
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
     * @param hoscode  医院编号
     * @return 医院对象
     */
    @Override
    public Hospital getHospitalByHoscode(String hoscode) {
        return repository.findByHoscode(hoscode);
    }


}
