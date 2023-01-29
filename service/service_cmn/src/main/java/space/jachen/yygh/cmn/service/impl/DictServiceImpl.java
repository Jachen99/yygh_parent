package space.jachen.yygh.cmn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import space.jachen.yygh.cmn.mapper.DictMapper;
import space.jachen.yygh.cmn.service.DictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import space.jachen.yygh.model.cmn.Dict;

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

    /**
     * 根据数据id查询子数据列表
     * @param id 传入id值
     * @return  返回字典集合
     */
    @Override
    public List<Dict> findChlidData(Long id) {

        //1、传入parent_id作为查询条件
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);

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

        QueryWrapper<Dict> wrapper = new QueryWrapper<>();

        // 节点的id 与 子节点的父id进行比较
        wrapper.eq("parent_id",dictId);

        // 查询总记录数 通过记录数判断是否有子节点
        return baseMapper.selectCount(wrapper) > 0;

    }


}
