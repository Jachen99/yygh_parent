package space.jachen.yygh.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import space.jachen.yygh.cmn.service.DictService;
import space.jachen.yygh.model.cmn.Dict;
import space.jachen.yygh.vo.cmn.DictEeVo;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author JaChen
 * @date 2023/1/30 14:50
 */
public class DictListener extends AnalysisEventListener<DictEeVo> {

    //通过构造传service
    @Resource
    private DictService dictService;

    public DictListener(DictService dictService) {
        this.dictService = dictService;
    }

    /**
     * 每隔2条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 2;
    /**
     * 缓存的数据
     */
    private List<Dict> cachedDataList =
            ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        //调用方法添加数据库
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo,dict);

        cachedDataList.add(dict);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            //调用保存方法
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        //批量添加
        dictService.saveBatch(cachedDataList);
    }
}
