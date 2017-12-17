package com.mierro.robot.controller;

import com.mierro.common.common.DataCheck;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.robot.entity.view.AddRobotShowItemView;
import com.mierro.robot.service.RobotShowItemService;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 机器人晒单模块
 * Created by tlseek on 2017/8/25.
 */
@RestController
@RequestMapping("/admin/robot")
public class AdminRobotShowItemController {

    @Resource
    RobotShowItemService robotShowItemService;

    /**
     * 分页查看机器人未晒单中奖记录
     * @param username 用户名
     * @param itemName 商品名
     * @return
     */
    @GetMapping("/sealedBeforeShow")
    public ResultMessage findSealedBeforeShow(@RequestParam(value = "username",required = false) String username,
                                              @RequestParam(value = "itemName",required = false) String itemName,
                                              @RequestParam(value = "front_show",required = false) Boolean front_show,
                                              @RequestParam("pageNo") Integer pageNo,
                                              @RequestParam("pageSize") Integer pageSize){
        Page page = robotShowItemService.findSealedBeforeShow(username,itemName,front_show,pageNo,pageSize);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",page);
    }

    /**
     * 添加晒单
     * @param view
     * @param result
     * @return
     */
    @PostMapping("/showItem")
    public ResultMessage addUser(
            /*@RequestBody*/ @ModelAttribute("view") @Validated AddRobotShowItemView view, BindingResult result){
        DataCheck.returnError(result);
        robotShowItemService.addShowItem(view);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

}
