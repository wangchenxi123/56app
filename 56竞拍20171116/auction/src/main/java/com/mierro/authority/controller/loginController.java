package com.mierro.authority.controller;

import com.mierro.authority.common.AythorityType;
import com.mierro.authority.common.LoginType;
import com.mierro.authority.entity.AuthenticationInfo;
import com.mierro.authority.service.AuthorityService;
import com.mierro.authority.shiro.CustomUsernamePasswordToken;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.common.kaptcha.KaptchaCheckout;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by 黄晓滨 simba on 2017/7/24.
 * Remarks：权限管理员登录
 */
@RestController
@RequestMapping("/authority")
public class loginController {

    @Resource
    private AuthorityService authorityService;


    @PostMapping("/login")
    public ResultMessage login(@RequestParam("identifier")String identifier,
                               @RequestParam(value = "password",required = false) String password,
                               @RequestParam(value = "validate") String validate,
                               @ApiIgnore HttpServletRequest request){
        AuthenticationInfo authenticationInfo;
        //校验验证码
        if (!KaptchaCheckout.checkout(request,validate)){
            return new ResultMessage(ResponseCode.BAD_REQUEST,"验证码错误");
        }
        //检测是否已经登录，是--进行退出登录
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() || subject.isRemembered()) {
            subject.logout();
        }
        LoginType loginType = LoginType.ACCOUNT;
        authenticationInfo = authorityService.selectLoginMessageByIdentifier(identifier, LoginType.ACCOUNT);
        if (authenticationInfo == null){
            loginType = LoginType.TELEPHONE;
            authenticationInfo = authorityService.selectLoginMessageByIdentifier(identifier, LoginType.TELEPHONE);
        }
        if (authenticationInfo == null){
            return new ResultMessage(ResponseCode.BUSINESS,"没有查询到该账户，该登录方式只支持手机或者用户名登录");
        }
        if (authorityService.findAythorityTypeByUserId(authenticationInfo.getUserId(), AythorityType.INSIDER)){
            return new ResultMessage(ResponseCode.ACCESSDENIED,"您不是管理员，无法进行登录");
        }
        CustomUsernamePasswordToken token = new CustomUsernamePasswordToken(
                identifier.trim(), password, AythorityType.INSIDER, loginType);
        subject.login(token);
        return new ResultMessage(ResponseCode.OK,"登录成功");
    }

}
