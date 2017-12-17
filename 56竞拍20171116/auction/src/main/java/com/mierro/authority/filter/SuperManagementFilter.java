package com.mierro.authority.filter;

import com.mierro.authority.conifg.AuthorityConfig;
import com.mierro.authority.controller.SystemModelHandler;
import com.mierro.authority.dao.UserRoleRelationshipDao;
import com.mierro.authority.entity.UserRoleRelationship;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
public class SuperManagementFilter extends AccessControlFilter {

    /**
     * 无权访问url
     */
    private String failUrl;

    /**
     * 未登入url
     */
    private String notLogin;

    /**
     * 登录认证业务对象
     */
    @Resource
    private UserRoleRelationshipDao userRoleRelationshipDao;

    public String getFailUrl() {
        return failUrl;
    }

    public void setFailUrl(String failUrl) {
        this.failUrl = failUrl;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        //获取操作用户
        Long id = SystemModelHandler.getCurrentUser().getId();
        if (id == null){
            //进行转发
            httpServletResponse.sendRedirect(
                    httpServletRequest.getContextPath() + "/" + notLogin);
            return false;
        }
        //查找该用户是否是超级管理员
        UserRoleRelationship userRoleRelationship =
                userRoleRelationshipDao.findByUserIdAndRoleId(id, AuthorityConfig.SUPER_ADMINISTRATOR);
        if (userRoleRelationship == null){
            //进行转发
            httpServletResponse.sendRedirect(
                    httpServletRequest.getContextPath() + "/" + failUrl);
            return false;
        }else{
            return true;
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return false;
    }


    public String getNotLogin() {
        return notLogin;
    }

    public void setNotLogin(String notLogin) {
        this.notLogin = notLogin;
    }
}
