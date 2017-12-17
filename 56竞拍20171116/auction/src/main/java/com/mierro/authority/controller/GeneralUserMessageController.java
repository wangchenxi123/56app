package com.mierro.authority.controller;

import com.mierro.authority.common.LoginType;
import com.mierro.authority.entity.AuthenticationInfo;
import com.mierro.authority.service.AuthorityService;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by 黄晓滨 simba on 2017/6/19.
 * Remarks：
 */
@RestController
public class GeneralUserMessageController {

    @Resource
    private AuthorityService authorityService;

    /**
     * 检测账号是否已经被注册
     * @param identifier 账号识别码
     * @return ResultMessage false(没有被注册)
     */
    @ApiOperation(value = "检测账号是否已经被注册",
            notes = "- identifier 账号识别码\n" +
                    "- loginType 登陆方式")
    @GetMapping(value = "/detection")
    public ResultMessage detection(@RequestParam("identifier") String identifier,
                                    @RequestParam("loginType") String loginType){
        LoginType loginType1= LoginType.parse(loginType);
        if (loginType1 == null){
            return new ResultMessage(ResponseCode.BAD_REQUEST,"没有相应登陆方式");
        }
        AuthenticationInfo authenticationInfo = authorityService.selectLoginMessageByIdentifier(identifier,loginType1);
        Boolean result = false ;
        if (authenticationInfo == null){
            result = true;
        }
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("result",result);
    }

    /**
     * 用户登出
     * @return resultMessage
     */
    @ApiOperation(value = "用户登出")
    @GetMapping(value = "/loginout")
    public ResultMessage loginout() {
        SecurityUtils.getSubject().logout();
        return new ResultMessage(ResponseCode.OK,"注销成功");
    }
}
