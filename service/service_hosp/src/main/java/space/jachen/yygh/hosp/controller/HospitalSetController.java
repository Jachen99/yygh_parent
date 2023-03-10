package space.jachen.yygh.hosp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import space.jachen.yygh.common.result.JsonData;
import space.jachen.yygh.common.utils.HttpRequestHelper;
import space.jachen.yygh.common.utils.UuidUtils;
import space.jachen.yygh.hosp.service.HospitalSetService;
import space.jachen.yygh.model.hosp.HospitalSet;
import space.jachen.yygh.vo.hosp.HospitalSetQueryVo;

import java.util.HashMap;
import java.util.List;

/**
 * 平台医院设置控制器
 *
 * @author jachen
 * @since 2023-01-13
 */

//@CrossOrigin // 解决跨域问题
@Api(tags = "医院设置接口")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    @Autowired
    HospitalSetService hospitalSetService;

    /**
     * 根据id和状态修改  直接调用IService方法
     * @param id  医院设置id
     * @param status  当前状态
     * @return    返回成功响应
     */
    @ApiOperation("锁定和解锁医院设置")
    @PutMapping("/lockHospitalSet/{id}/{status}")
    public JsonData<HospitalSet> lockHospitalSet(
            @PathVariable Long id,
            @PathVariable Integer status
    ){
        HospitalSet hospitalSet = new HospitalSet();
        hospitalSet.setId(id);
        hospitalSet.setStatus(status);
        boolean b = hospitalSetService.updateById(hospitalSet);
        if (b){
            return JsonData.ok();
        }else {
            return JsonData.fail();
        }

    }


    /**
     * 根据ids批量删除医院设置
     * @param ids  传入的id集合
     * @return 返回响应状态
     */
    @ApiOperation("批量删除医院设置")
    @DeleteMapping("/batchDelete")
    public JsonData<HospitalSet> batchDelete(@RequestBody List<Long> ids){
        boolean removeByIds = hospitalSetService.removeByIds(ids);
        if (removeByIds){
            return JsonData.ok();
        }else {
            return JsonData.fail();
        }
    }

    /**
     * 根据id查询医院设置
     * @param id 医院id
     * @return  返回医院设置对象
     */
    @ApiOperation("根据id查询医院设置")
    @GetMapping("/getHospSet/{id}")
    public JsonData<HospitalSet> getById(@PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);

        return JsonData.ok(hospitalSet);
    }


    /**
     * 新增医院设置
     * @param hospitalSet  传入医院设置对象
     * @return  返回影响状态
     */
    @ApiOperation(value = "医院设置新增")
    @PostMapping("/saveHospSet")
    public JsonData<HospitalSet> save(@RequestBody HospitalSet hospitalSet){

        hospitalSet.setSignKey(UuidUtils.getUUID());

        boolean save = hospitalSetService.save(hospitalSet);

        if (save){
            HashMap<String, Object> map = new HashMap<>();
            System.out.println(hospitalSet.getSignKey());
            map.put("sign",hospitalSet.getSignKey());
            map.put("hoscode",hospitalSet.getHoscode());
            HttpRequestHelper.sendRequest(map,hospitalSet.getApiUrl()
                    +"/hospSet/updateSignKey");
            return JsonData.ok();
        }else {
            return JsonData.fail();
        }

    }


    /**
     * 医院设置修改
     * @param hospitalSet 传入修改对象
     * @return   返回响应状态
     */
    @ApiOperation(value = "医院设置修改")
    @PutMapping("/updateHospSet")
    public JsonData<HospitalSet> updateById(@RequestBody HospitalSet hospitalSet){

        boolean updateById = hospitalSetService.updateById(hospitalSet);

        if (updateById){
            return JsonData.ok();
        }else {
            return JsonData.fail();
        }

    }


    /**
     * 带条件医院设置分页
     * @param page 当前的页数
     * @param limit  每页记录数
     * @return
     */
    @ApiOperation(value = "带条件医院设置分页")
    @GetMapping("/{page}/{limit}")
    public JsonData<Page<HospitalSet>> pageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "hospitalSetQueryVo", value = "查询对象", required = false)
            HospitalSetQueryVo hospitalSetQueryVo
    ){
        // 1、创建page对象
        Page<HospitalSet> pageParam = new Page<>(page, limit);

        // 2、创建QueryWrapper对象
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();

        // 判断name和id
        if (hospitalSetQueryVo == null){
            // 3、调用hospitalSetService的分页方法
            hospitalSetService.page(pageParam, queryWrapper);
        }else {
            String hosname = hospitalSetQueryVo.getHosname();
            String hoscode = hospitalSetQueryVo.getHoscode();
            if (!StringUtils.isEmpty(hosname)){
                queryWrapper.like("hosname",hosname);
            }
            if (!StringUtils.isEmpty(hoscode)){
                queryWrapper.eq("hoscode",hoscode);
            }
            hospitalSetService.page(pageParam, queryWrapper);
        }

        return JsonData.build(pageParam,200,"succeed");
    }


    /**
     * 测试获取全部医院设置信息
     * @return 返回医院设置list集合
     */
    @ApiOperation(value = "医院设置列表")
    @GetMapping("/getAll")
    public JsonData<List<HospitalSet>> getAll(){

        List<HospitalSet> list = hospitalSetService.list();

        return JsonData.build(list,200,"获取成功");

    }

    /**
     * 测试根据id删除医院设置的接口
     * @param id 要删除的id
     * @return 返回boolean
     */
    @ApiOperation(value = "医院设置删除")
    @DeleteMapping("/removeById/{id}")
    public JsonData<HospitalSet> removeById(
            @ApiParam(name = "id", value = "ID", required = true)
            @PathVariable Long id){

        boolean b = hospitalSetService.removeById(id);

        if (b){
            return JsonData.ok();
        }else {
            return JsonData.fail();
        }

    }

}

