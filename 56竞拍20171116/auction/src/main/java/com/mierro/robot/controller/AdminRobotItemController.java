package com.mierro.robot.controller;

import com.mierro.common.common.DataCheck;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.robot.entity.RobotItemBean;
import com.mierro.robot.entity.valid.Update;
import com.mierro.robot.service.RobotItemService;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 机器人商品接口
 * Created by tlseek on 2017/8/22.
 */
@RestController
@RequestMapping("/admin/root")
public class AdminRobotItemController {

    @Resource
    RobotItemService robotItemService;

    /**
     * 分页查询机器人商品
     * @param name 用户名称
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 分页对象
     */
    @GetMapping("/items")
    public ResultMessage findAll(@RequestParam(value = "name",required = false) String name,
                                 @RequestParam("pageNo") Integer pageNo,
                                 @RequestParam("pageSize") Integer pageSize){
        Page page = robotItemService.find(name,pageNo,pageSize);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",page);
    }

    /**
     * 修改机器人商品信息
     * @param robotItem
     * @return
     */
    @PutMapping("/item")
    public ResultMessage update(
            /*@RequestBody*/ @ModelAttribute("robotItem") @Validated({Update.class})RobotItemBean robotItem, BindingResult result){
        DataCheck.returnError(result);
        robotItemService.update(robotItem);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }
}
