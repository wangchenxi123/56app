package com.mierro.main.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mierro.common.common.DataCheck;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.main.common.RunningProgram;
import com.mierro.main.entity.ItemBean;
import com.mierro.main.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/8/8.
 * Remarks：商品管理
 */
@RestController
@RequestMapping("/admin")
public class AdminItemController {

    @Resource
    private ItemService itemService;


    /**
     * 添加一个商品
     * @param itemBean 商品bean
     * @return 操作码
     * @throws JsonProcessingException json转换错误
     */
    @PostMapping("/item")
    public ResultMessage addItem(@ModelAttribute("itemBean") @Valid ItemBean itemBean, BindingResult result) throws JsonProcessingException {
        DataCheck.returnError(result);
        itemService.addItem(itemBean);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 根据条件查询商品
     * @param name 商品名称
     * @param itemType 商品类型
     * @param disable 是否禁用
     * @param runningProgram 运行方案
     * @param plusCode 每轮加价
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 商品分页对象
     */
    @GetMapping("/items")
    public ResultMessage findAll(@RequestParam(value = "name",required = false) String name,
                                 @RequestParam(value = "item_type",required = false) String itemType,
                                 @RequestParam(value = "disable",required = false) Boolean disable,
                                 @RequestParam(value = "runningProgram",required = false) RunningProgram runningProgram,
                                 @RequestParam(value = "plus_code",required = false) Integer plusCode,
                                 @RequestParam(value = "pageNo") Integer pageNo,
                                 @RequestParam(value = "pageSize") Integer pageSize) {
        Page<ItemBean> itemBeans =  itemService.findAll(name,itemType,disable,runningProgram,plusCode,pageNo,pageSize);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",itemBeans);
    }

    /**
     * 查询单个商品
     * @param itemId 商品id
     * @return 操作码
     */
    @GetMapping("/item")
    public ResultMessage findOne(@RequestParam("itemId") Long itemId) {
        ItemBean itemBeans =  itemService.findOne(itemId);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",itemBeans);
    }

    /**
     * 获取机器人分类
     * @return 用户分类详情
     */
    @GetMapping("/running/program/type")
    public ResultMessage getRunningProgramType(){
        List list = RunningProgram.getRunningProgramName();
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",list);
    }

    /**
     * 强制删除一个商品
     * @param itemId 商品id
     * @return 操作码
     *
     */
    @DeleteMapping("/forced/item")
    public ResultMessage ForcedDelete(@RequestParam("itemId") Long itemId){
        itemService.forcedDelete(itemId);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 删除一个商品
     * @param itemId 商品id
     * @return 操作码
     *
     */
    @DeleteMapping("/item")
    public ResultMessage delete(@RequestParam("itemId") Long itemId){
        itemService.delete(itemId);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 更新一个商品(不进行禁用更新)
     * @param itemBean 商品bean
     * @return 操作码
     */
    @PutMapping("/item")
    public ResultMessage update(@ModelAttribute("itemBean") ItemBean itemBean) throws JsonProcessingException {
        itemService.updateItem(itemBean);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 更新商品禁用状态
     * @param itemId 商品id
     * @return 操作码
     */
    @PutMapping("/item/state")
    public ResultMessage updateState(Long itemId){
        itemService.updateItemState(itemId);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 更新商品是否前端展示
     * @param itemId 商品id
     * @return 操作码
     */
    @PutMapping("/item/show")
    public ResultMessage updateShow(Long itemId){
        itemService.updateItemShow(itemId);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 更新商品是否是新手
     * @param itemId 商品id
     * @return 操作码
     */
    @PutMapping("/item/novice")
    public ResultMessage updateNovice(Long itemId){
        itemService.updateItemNovice(itemId);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }
}
