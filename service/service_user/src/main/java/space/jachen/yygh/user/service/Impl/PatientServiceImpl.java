package space.jachen.yygh.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.jachen.yygh.cmn.DictFeignClient;
import space.jachen.yygh.enums.DictEnum;
import space.jachen.yygh.model.user.Patient;
import space.jachen.yygh.user.mapper.PatientMapper;
import space.jachen.yygh.user.service.PatientService;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 就诊人表 服务实现类
 * </p>
 *
 * @author jachen
 * @since 2023-02-06
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Resource
    private PatientMapper patientMapper;
    @Autowired
    private DictFeignClient dictFeignClient;

    /**
     * 根据id获取就诊人详细信息
     * @param id 就诊id
     * @return 返回Patient
     */
    @Override
    public Patient getDetailById(Long id) {
        Patient patient = baseMapper.selectById(id);
        this.packagePatient(patient);
        return patient;
    }

    /**
     * 根据用户登录userId获取就诊人列表
     * @param userId  用户登录唯一表示userId
     * @return  List<Patient>
     */
    @Override
    public List<Patient> getAll(Long userId) {

        LambdaQueryWrapper<Patient> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Patient::getUserId,userId);
        List<Patient> patientList = patientMapper.selectList(lambdaQueryWrapper);
        // 还要封装远程调用查询的用户信息 根据certificatesType获取身份证获取户口本
        patientList.forEach(this::packagePatient);
        return patientList;
    }

    public void packagePatient(Patient patient){
        // 获取证据类型的name 证件类型 - > 身份证 、户口本
        String certificatesTypeName = dictFeignClient.getName(
                DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getCertificatesType());
        // 获取省市区
        String province = dictFeignClient.getName(patient.getProvinceCode());
        String city = dictFeignClient.getName(patient.getCityCode());
        String district = dictFeignClient.getName(patient.getDistrictCode());
        // 封装param
        patient.getParam().put("certificatesTypeString", certificatesTypeName);
        patient.getParam().put("provinceString", province);
        patient.getParam().put("cityString", city);
        patient.getParam().put("districtString", district);
        patient.getParam().put("fullAddress", province + city + district + patient.getAddress());
    }
}
