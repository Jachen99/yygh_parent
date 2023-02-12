package space.jachen.yygh.hosp.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import space.jachen.yygh.common.utils.MD5;
import space.jachen.yygh.hosp.service.HospitalSetService;
import space.jachen.yygh.model.hosp.HospitalSet;

/**
 * 验签工具
 *
 * @author JaChen
 * @date 2023/2/3 9:49
 */
public class SignUtils {

    /**
     * 对接医院与后台传输数据的验签小工具
     *
     * @param sign  签名sign
     * @param hoscode  医院编号
     * @param hospitalSetService  传入HospitalSetService服务
     * @return  返回验签状态boolean
     */
    public static Boolean verifyKeyOK(String sign, String hoscode, HospitalSetService hospitalSetService){

        String signKey = hospitalSetService.getOne(new QueryWrapper<HospitalSet>()
                .eq("hoscode", hoscode)).getSignKey();
        return MD5.encrypt(signKey).equals(sign);
    }

}
