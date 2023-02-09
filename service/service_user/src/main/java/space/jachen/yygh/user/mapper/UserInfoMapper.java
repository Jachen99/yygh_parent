package space.jachen.yygh.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import space.jachen.yygh.model.user.UserInfo;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author jachen
 * @since 2023-02-06
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}
