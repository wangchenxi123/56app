package com.mierro.main.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mierro.common.common.DataCheck;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.main.entity.SortBean;
import com.mierro.main.service.SortService;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;

/**
 * Created by 黄晓滨 simba on 2017/8/12.
 * Remarks：商品分类设置
 */
@RestController
@RequestMapping("/admin")
public class AdminSortController {

    @Resource
    private SortService sortService;

    /**
     * 根据条件查询所有商品分类
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 分类分页对象
     */
    @GetMapping("/sorts")
    public ResultMessage findAll(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        Page<SortBean> sortBeans = sortService.findAll(pageNo, pageSize);
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", sortBeans);
    }

    /**
     * 查询单个分类(会返回分类所属Item)
     * @param sortId 分类id
     * @return 分类对象
     * @throws IOException json转换错误
     */
    @GetMapping("/sort")
    public ResultMessage findOne(@RequestParam("sortId") Long sortId) throws IOException {
        SortBean sortBean = sortService.findOne(sortId);
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", sortBean);
    }

    /**
     * 添加一个商品分类
     * @param sortBean 商品分类对象
     * @return 操作码
     * @throws JsonProcessingException json转换错误
     */
    @PostMapping("/sort")
    public ResultMessage addSort(@ModelAttribute("sortBean") @Valid SortBean sortBean,
                                 BindingResult result) throws JsonProcessingException {
        DataCheck.returnError(result);
        sortService.addSort(sortBean);
        return new ResultMessage(ResponseCode.OK, "操作成功");
    }

    /**
     * 修改商品分类
     * @param sortBean 商品分类对象
     * @return 操作码
     * @throws JsonProcessingException json转换错误
     */
    @PutMapping("/sort")
    public ResultMessage updateSort(@ModelAttribute("sortBean") SortBean sortBean) throws JsonProcessingException {
        sortService.updateSort(sortBean);
        return new ResultMessage(ResponseCode.OK, "操作成功");
    }

    /**
     * 删除一个商品分类
     * @param sortId 分类对象id
     * @return 操作码
     */
    @DeleteMapping("/sort")
    public ResultMessage delete(@RequestParam("sortId") Long sortId) {
        sortService.deleteSort(sortId);
        return new ResultMessage(ResponseCode.OK, "操作成功");
    }

    /**
     * 更新商品分类禁用状态
     * @param sortId 商品id
     * @return 操作码
     */
    @PutMapping("/sort/state")
    public ResultMessage updateState(@RequestParam("sortId") Long sortId){
        sortService.updateState(sortId);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }
}
