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
 * 医院服务的实现类
 *
 * @author JaChen
 * @date 2023/2/2 10:38
 */
@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository repository;
    // 注入远程调用数据字典
    @Resource
    private DictFeignClient dictFeignClient;

    /**
     * 远程调用查询字典的方法
     *
     * @param hospital hospital对象
     * @return Hospital
     */
    private Hospital packHospital(Hospital hospital) {
        // 获取医院等级
        String hostypeName = dictFeignClient.getName(DictEnum.HOSTYPE.getDictCode(), hospital.getHostype());
        // 获取省
        String provinceName = dictFeignClient.getName(hospital.getProvinceCode());
        // 获取市
        String cityName = dictFeignClient.getName(hospital.getCityCode());
        // 获取区
        String districtName = dictFeignClient.getName(hospital.getDistrictCode());
        // 封装数据
        hospital.getParam().put("hostypeString",hostypeName);
        hospital.getParam().put("fullAddress",provinceName+cityName+districtName+hospital.getAddress());
        return hospital;
    }

    /**
     * 根据医院编号获取医院对象和预约规则
     *
     * @param hoscode 医院编号
     * @return  封装好的Map
     */
    @Override
    public Map<String, Object> item(String hoscode) {
        Map<String, Object> result = new HashMap<>();
        // 医院详情
        Hospital hospital = this.packHospital(repository.findByHoscode(hoscode));
        result.put("hospital", hospital);
        // 预约规则
        result.put("bookingRule", hospital.getBookingRule());
        // 不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }


    /**
     * 根据id获取医院对象和预约规则
     *
     * @param id  医院的id
     * @return 封装好的Map key为hospital和bookingRule
     */
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
     * 分页查询医院信息
     *
     * @param page 当前页码
     * @param limit 每页记录数
     * @param hospitalQueryVo 查询条件
     * @return 返回Page<Hospital>
     */
    @Override
    public Page<Hospital> findPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        // 排序
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        // 分页
        Pageable pageRequest = PageRequest.of(page-1, limit, sort);
        // 创建Hospital对象
        Hospital hospital = new Hospital();
        hospital.setHosname(hospitalQueryVo.getHosname());
        /*
        修复客户端不能按条件展示医院列表的bug ---> 没有把传入条件赋给hospital
         */
        hospital.setHostype(hospitalQueryVo.getHostype());
        hospital.setDistrictCode(hospitalQueryVo.getDistrictCode());
        // 创建匹配器对象，即如何使用查询条件
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)//模糊查询
                .withIgnoreCase(true);
        // 创建Example对象
        Example<Hospital> example = Example.of(hospital, exampleMatcher);
        Page<Hospital> hospitals = repository.findAll(example, pageRequest);
        // 获取Page里的医院信息
        List<Hospital> hospitalList = hospitals.getContent();
        // 遍历
        hospitalList.forEach(this::packHospital);
        return hospitals;
    }

    /**
     * 根据医院名字获取医院list集合
     *
     * @param hosname  医院名字
     * @return List<Hospital>
     */
    @Override
    public List<Hospital> findByHosname(String hosname) {
        return repository.findHospitalByHosnameLike(hosname);
    }

    /**
     * 简单的查询医院接口的实现方法
     * 通过调用MongoRepository底层实现的方法直接进行查询mongoDB数据库
     *
     * @param hoscode  医院编号
     * @return  医院对象
     */
    @Override
    public Hospital getHospitalByHoscode(String hoscode) {
        return repository.findByHoscode(hoscode);
    }

    /**
     * 增加医院信息的方法
     * 这里是增加对接医院信息接口的实现方法，即把对接医院的信息增加到后台mongoDB数据库中。
     * 如果mongoDB中没有该家医院的hoscode，我们进行insert插入操作；
     * 如果存在该家医院的hoscode，则要把本地数据库中医院的id赋给新增医院，最后进行save更新操作。
     *
     * @param hospital 传入医院对象
     */
    @Override
    public void saveById(Hospital hospital) {
        /*
          由于在http传输中会默认把+变成空格 我们要转化回去，
          因此，这里把logo中空格替换为+  。
         */
        String logoData = hospital.getLogoData();
        String replace = logoData.replace(" ", "+");
        hospital.setLogoData(replace);
        Hospital isHospital = repository.findByHoscode(hospital.getHoscode());
        if (isHospital==null){
            repository.insert(hospital);
        }else {
            // 如果存在该家医院 需要重置id进行更新操作
            hospital.setId(isHospital.getId());
            // mongoDB时间跟新注解无法对二次更新的数据保留创建时间，因此此处要重置创建时间。
            hospital.setCreateTime(isHospital.getCreateTime());
            repository.save(hospital);
        }
    }
}
