package space.jachen.yygh.user.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import space.jachen.yygh.model.user.Patient;
import space.jachen.yygh.user.mapper.PatientMapper;
import space.jachen.yygh.user.service.PatientService;

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

}
