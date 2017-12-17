package com.mierro.authority.controller;

import com.mierro.authority.entity.User;
import com.mierro.authority.entity.UserMessage;
import com.mierro.authority.service.UserService;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.common.common.VerifyException;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * Created by 黄晓滨 simba on 2017/7/20.
 * Remarks：
 */
@RestController
@RequestMapping("/authority")
public class UserController {

    @Resource
    private UserService userService ;

    /**
     * 查询自身信息或者其他管理员信息(详细)
     * @param userId userId
     * @return 信息详情
     */
    @GetMapping("/admin")
    public ResultMessage getMyself(@ModelAttribute(SystemModelHandler.CURRENT_USER)User user,
                                   @RequestParam(value = "userId",required = false) Long userId){
        UserMessage userMessage;

        if (userId == null){
            userMessage = userService.selectAdmin(user.getId());
        }else{
            userMessage = userService.selectAdmin(userId);
        }

        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",userMessage);
    }
}
