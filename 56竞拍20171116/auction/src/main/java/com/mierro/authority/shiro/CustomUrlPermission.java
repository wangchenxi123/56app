package com.mierro.authority.shiro;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.PatternMatcher;


/**
 * <p>Title:CustomUrlPermission </p> <p>Description:自定义shiro权限类 </p> <p>Company:Wteam </p>
 *
 * @author Wteam 李焕滨 86571705@qq.com
 */
public class CustomUrlPermission implements Permission {

    /**
     * 权限url
     */
    private String url;

    private String method;

    /**
     * 无参构造方法
     */
    public CustomUrlPermission() {

    }

    public CustomUrlPermission(String url,String method) {
        this.url = url;
        this.method = method;
    }

    /**
     * 带参构造方法
     */
    public CustomUrlPermission(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 权限字符串匹配方法
     */
    @Override
    public boolean implies(Permission permission) {
        System.out.println("---------------------------进入校验机制-----------------");
        if (!(permission instanceof CustomUrlPermission)) {
            return false;
        }
        CustomUrlPermission customUrlPermission = (CustomUrlPermission) permission;
        PatternMatcher patternMatcher = new AntPathMatcher();
        return patternMatcher.matches(this.getUrl(), customUrlPermission.getUrl());
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
