package space.jachen.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import space.jachen.yygh.cmn.listener.DictListener;
import space.jachen.yygh.cmn.mapper.DictMapper;
import space.jachen.yygh.cmn.service.DictService;
import space.jachen.yygh.common.handler.YyghException;
import space.jachen.yygh.model.cmn.Dict;
import space.jachen.yygh.vo.cmn.DictEeVo;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 组织架构表 服务实现类
 * </p>
 *
 * @author jachen
 * @since 2023-01-29
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Autowired
    DictService dictService;


    /**
     * 根据父id和唯一value获取数据字典的name
     * 如果value能唯一定位数据字典，parentDictCode可以传空，
     * 例如：省市区的value值能够唯一确定
     *
     * @param parentDictCode  上级编码
     * @param value 省市区的value值 具有唯一性
     * @return  返回字典数据的name
     */
    @Override
    public String getNameByParentDictCodeAndValue(String parentDictCode, String value) {

        // 只有医院等级的那个对象parentDictCode为空 我们就按照唯一的value查询
        if(StringUtils.isEmpty(parentDictCode)) {
            Dict dict = baseMapper.selectOne(
                    new LambdaQueryWrapper<Dict>().eq(Dict::getValue, value));
            if(null != dict) {
                return dict.getName();
            }
        } else {
            Dict parentDict = this.getDictByDictCode(parentDictCode);
            if(parentDict != null){
                Dict dict = baseMapper.selectOne(
                        new LambdaQueryWrapper<Dict>().
                                eq(Dict::getParentId,parentDict.getId()).
                                eq(Dict::getValue, value));
                // 如果dic存在 返回name  也就是"医院等级"
                if(null != dict) {
                    return dict.getName();
                }
            }
        }
        return null;
    }

    /**
     * 根据dict_code查询数据
     * @param dictCode  这里就是 Hostype
     * @return 返回数据对象 就是医院等级对象
     */
    private Dict getDictByDictCode(String dictCode) {
        LambdaQueryWrapper<Dict> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dict::getDictCode,dictCode);
        return baseMapper.selectOne(wrapper);
    }

    /**
     * 根据dictCode查询
     * @param dictCode 即Hostype
     * @return 返回字典树list集合
     */
    @Override
    public List<Dict> findByDictCode(String dictCode) {

        Dict codeDict = this.getDictByDictCode(dictCode);
        if(dictCode != null){
            return findChlidData(codeDict.getId());
        }
        return null;
    }

    /**
     * 根据id查询子数据列表
     * @param id 传入id值
     * @return  返回字典集合
     */
    //@Cacheable(value = "dict")
    @Override
    public List<Dict> findChlidData(Long id) {

        //1、传入parent_id作为查询条件
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        if (id==110000L||id==120000L||id==230000L|id==610000L){
            wrapper.eq("parent_id",id+100L);
        }else {
            wrapper.eq("parent_id",id);
        }
        // 2、调用baomidou的baseWrapper查询方法
        List<Dict> dictList = baseMapper.selectList(wrapper);
        // 3、遍历结果集 向list集合每个dict对象中设置hasChildren
        for (Dict dict : dictList) {
            // 拿到节点的id
            Long dictId = dict.getId();
            Boolean isChildren = isChildren(dictId);
            dict.setHasChildren(isChildren);
        }
        return dictList;
    }

    /**
     * 判断是否有子节点的方法
     * @param dictId 该节点id
     * @return 返回一个boolean值
     */
    public Boolean isChildren(Long dictId) {
        LambdaQueryWrapper<Dict> wrapper = new LambdaQueryWrapper<>();
        if (dictId==110000L||dictId==120000L||dictId==230000L||dictId==610000L){
            wrapper.eq(Dict::getParentId,dictId+100L);
        }else {
            // 节点的id 与 子节点的父id进行比较
            wrapper.eq(Dict::getParentId,dictId);
        }
        // 查询总记录数 通过记录数判断是否有子节点
        return baseMapper.selectCount(wrapper) > 0;
    }

    /**
     * 导入数据字典文件的方法
     * @param file  传入MultipartFile文件
     */
    //添加注解，添加数据字典数据时候，清空缓存
    //@CacheEvict(value = "dict",allEntries = true)
    @Override
    public void importData(MultipartFile file) {

        try {
            EasyExcel.read(file.getInputStream(),
                    DictEeVo.class,
                    new DictListener(dictService)).sheet().doRead();
        } catch (Exception e) {
            throw new YyghException(444,e.getMessage(),e);
        }
    }

    /**
     * 导出字典文件的方法
     * @param response 响应信息
     */
    @Override
    public void exportData(HttpServletResponse response) {
        try {
            // 1、设置响应数据类型 告诉浏览器传输的内容类型是Microsoft Excel文件。
            response.setContentType("application/vnd.ms-excel");
            String name = URLEncoder.encode("数据字典", "UTF-8");
            // 2、让浏览器将 响应内容作为一个附件 下载下来
            response.setHeader("Content-disposition", "attachment;filename="+ name + ".xlsx");
            // 3、获取数据集合List<Dict>赋给List<DictEeVo>
            List<Dict> dictList = baseMapper.selectList(null);
            List<DictEeVo> dictEeVoList = new ArrayList<>();
            for (Dict dict : dictList) {
                DictEeVo eeVo = new DictEeVo();
                BeanUtils.copyProperties(dict,eeVo);
                dictEeVoList.add(eeVo);
            }
            // 4、使用阿里的EasyExcel框架写入数据
            EasyExcel.write(response.getOutputStream(), DictEeVo.class)
                    .sheet("数据字典").doWrite(dictEeVoList);
        } catch (Exception e) {
            throw new YyghException(444,"导出数据字典文件失败");
        }

    }


}
