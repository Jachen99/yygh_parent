package space.jachen.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import space.jachen.yygh.model.user.Patient;

import java.util.List;

/**
 * <p>
 * 就诊人表 服务类
 * </p>
 *
 * @author jachen
 * @since 2023-02-06
 */
public interface PatientService extends IService<Patient> {

    /**
     * 根据用户登录userId获取就诊人列表
     * @param userId  用户登录唯一表示userId
     * @return  List<Patient>
     */
    List<Patient> getAll(Long userId);

    /**
     * 根据id获取就诊人详细信息
     * @param id 就诊id
     * @return 返回Patient
     */
    Patient getDetailById(Long id);

}
