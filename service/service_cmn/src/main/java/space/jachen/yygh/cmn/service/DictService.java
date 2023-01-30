package space.jachen.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import space.jachen.yygh.model.cmn.Dict;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 组织架构表 服务类
 * </p>
 *
 * @author jachen
 * @since 2023-01-29
 */
public interface DictService extends IService<Dict> {

    /**
     * 导出
     * @param response 响应信息
     */
    void exportData(HttpServletResponse response);

    //根据数据id查询子数据列表
    List<Dict> findChlidData(Long id);
}
