package space.jachen.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
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

    void importData(MultipartFile file);

    /**
     * 根据上级字典编码获取与值获取字典的名称
     * @param parentDictCode  上级编码
     * @param value 值
     * @return
     */
    String getNameByParentDictCodeAndValue(String parentDictCode, String value);

    List<Dict> findByDictCode(String dictCode);
}
