package com.mierro.authority.controller;

import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 黄晓滨
 * @date 17/2/18
 */
@Controller
@RequestMapping("/error")
public class ErrorController {
    /**
     * @function:返回无权限json串
     * @import:
     */
    @ResponseBody
    @RequestMapping(value = "/noAccess")
    public ResultMessage noAccess(){
        return new ResultMessage(ResponseCode.FORBIDDEN,"没有操作权限");
    }

    /**
     * @function:返回未登录json串
     * @import:
     */
    @ResponseBody
    @RequestMapping(value = "/notLogin")
    public ResultMessage notLogin(){
        return new ResultMessage(ResponseCode.UNAUTHORIZED,"请先登录");
    }

    /**
     * 禁止从该端登录
     * @return 操作码
     */
    @ResponseBody
    @RequestMapping(value = "/ban/login")
    public ResultMessage banLogin(){
        return new ResultMessage(ResponseCode.ACCESSDENIED,"无权从该端登录");
    }

    /**
     * @function:返回系统维护json串
     * @import:
     */
    @ResponseBody
    @RequestMapping(value = "/maintenance")
    public ResultMessage maintenance(){
        return new ResultMessage(ResponseCode.SYSTEM_MAINTENANCE,"系统维护中");
    }

    /**
     * 用户登陆被踢出，用户已经达到最大登陆数
     * @return 用户登陆被踢出，用户已经达到最大登陆数
     */
    @ResponseBody
    @RequestMapping(value = "/prohibit_landing")
    public ResultMessage prohibit_landing(){
        SecurityUtils.getSubject().logout();
        return new ResultMessage(ResponseCode.UNAUTHORIZED,"用户登陆被踢出，用户已经达到最大登陆数");
    }
}
