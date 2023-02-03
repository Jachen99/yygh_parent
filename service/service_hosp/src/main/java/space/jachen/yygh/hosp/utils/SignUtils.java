package space.jachen.yygh.hosp.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import space.jachen.yygh.common.utils.MD5;
import space.jachen.yygh.hosp.service.HospitalSetService;
import space.jachen.yygh.model.hosp.HospitalSet;

/**
 *
 * 验签工具
 * @author JaChen
 * @date 2023/2/3 9:49
 */
public class SignUtils {

    public static Boolean verifyKeyOK(String sign, String hoscode, HospitalSetService hospitalSetService){

        String signKey = hospitalSetService.getOne(new QueryWrapper<HospitalSet>()
                .eq("hoscode", hoscode)).getSignKey();
        return MD5.encrypt(signKey).equals(sign);
    }

}
