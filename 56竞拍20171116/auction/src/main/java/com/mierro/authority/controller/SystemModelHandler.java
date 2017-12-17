/*
 * 文件名：SystemModelHandler.java
 * 版权：Copyright by wteam团队
 * 描述：
 * 修改人：唐亮
 * 修改时间：2016年6月2日 下午3:10:55
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.mierro.authority.controller;

import com.mierro.authority.entity.AuthenticationInfo;
import com.mierro.authority.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;
import java.util.Set;

/**
 * Created by 黄晓滨 on 2016/6/2.
 * Remarks：将一些运行时要使用的信息填充进model中
 */
@ControllerAdvice
public class SystemModelHandler {
    /**
     * log4j实例对象.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(SystemModelHandler.class.getName());
    /**
     * 
     */
    public static final String CURRENT_USER = "currentUser";

    @ModelAttribute
    public void systemInfo(Map<String, Object> map) {
        map.put(CURRENT_USER, getCurrentUser());
    }
    
    public static User getCurrentUser() {
        AuthenticationInfo authenticationInfo = null;
        User user = new User();
        // 登录用户的信息
        Subject subject = SecurityUtils.getSubject();
        // 如果用户已经登录
        if (subject.isAuthenticated() || subject.isRemembered()) {
            Set<?> set = subject.getPrincipals().asSet();
            for (Object principal : set) {
                if (principal instanceof AuthenticationInfo) {
                    authenticationInfo = (AuthenticationInfo)principal;
                    break;
                }
            }
            if(authenticationInfo != null){
                user.setId(authenticationInfo.getUserId());
            }else{
                LOG.error("错误登陆，检测到已经登陆，却没有保存登陆信息");
            }
        }
        return user;
    }

    public static User getCurrentUser(Subject subject) {
        AuthenticationInfo authenticationInfo = null;
        User user = new User();
        // 如果用户已经登录
        if (subject.isAuthenticated() || subject.isRemembered()) {
            Set<?> set = subject.getPrincipals().asSet();
            for (Object principal : set) {
                if (principal instanceof AuthenticationInfo) {
                    authenticationInfo = (AuthenticationInfo)principal;
                    break;
                }
            }
            if(authenticationInfo != null){
                user.setId(authenticationInfo.getUserId());
            }
        }
        return user;
    }
}
