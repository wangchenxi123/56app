package com.mierro.authority.shiro;


import com.mierro.authority.common.SystemSettings;
import com.mierro.authority.service.AuthorityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * <p>Title:CustomPermissionCheckFilter </p> <p>Description:自定义权限过滤器 </p> <p>Company:Wteam </p>
 *
 * @author 黄晓滨
 */
public class CustomPermissionCheckFilter extends AccessControlFilter {

    /**
     *
     */
    private static Logger LOG = LogManager.getLogger(CustomPermissionCheckFilter.class);

    /**
     * 系统维护url
     */
    private String maintenanceUrl;

    /**
     * 404
     */
    private String notFoundUrl;

    /**
     * 认证不通过跳转的url
     */
    private String unauthenticatedUrl;

    /**
     * 认证通过无访问权限跳转的url
     */
    private String unauthorizedUrl;

    /**
     * 无须权限即可访问的地址
     */
    private String[] urlWithoutPermission;

    /**
     * 登录认证业务对象
     */
    @Resource
    private AuthorityService authorityService;



    /**
     * 系统权限缓存 为非法路径过滤设置的缓存 修改权限信息时手动调用updateUrlAllCache（）刷新此缓存 默认服务器运行后第一次使用此参数时查询一次数据库，之后靠手动刷新此缓存
     */
    private static Set<String> urlAllCache;

    /**
     * 刷新数据库缓存的方法
     */
    public void updateUrlAllCache() {
        urlAllCache = authorityService.selectAllAuthority();
    }

    public static void updateUrlAllCache(Set<String> newUrlAllCache) {
        urlAllCache = newUrlAllCache;
    }

    public String getUnauthenticatedUrl() {
        return unauthenticatedUrl;
    }

    public void setUnauthenticatedUrl(String unauthenticatedUrl) {
        this.unauthenticatedUrl = unauthenticatedUrl;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    public String[] getUrlWithoutPermission() {
        return urlWithoutPermission;
    }

    public void setUrlWithoutPermission(String[] urlWithoutPermission) {
        this.urlWithoutPermission = urlWithoutPermission;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
                                      Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response);
        String url = getPathWithinApplication(request);
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //检查服务器是否处于维护阶段
        if (SystemSettings.maintenance){
            //检测连接是否是后台管理系统，是则通过
            if (url.indexOf("client")<=3 && url.indexOf("client") > 0){
                // 无权限
                httpServletResponse.sendRedirect(
                        httpServletRequest.getContextPath() + "/" + maintenanceUrl);
            }
        }

        //判断传入的地址是否是无须权限即可访问的地址
        if (urlWithoutPermission != null) {
            for (String string : urlWithoutPermission) {
                if (url.equals(string)) {
                    return true;
                }
            }
        }

        //判断传入的地址是否是认证不通过跳转的url和认证通过无访问权限跳转的url
        if (url.equals("/" + unauthenticatedUrl) || url.equals("/" + unauthorizedUrl)) {
            LOG.error("退出CustomPermissionCheckFilter方法-------授权认证处理链接");
            return true;
        }

        //重新组装url
        url = UrlComposingRoom.getUrl(httpServletRequest,url);

        //如果权限列表为空,刷新系统权限缓存
        if (null == urlAllCache) {
            updateUrlAllCache();
        }

        //检测是否可以找到对应权限,如果没有,返回404
        if (!urlAllCache.contains(url)){
            httpServletResponse.sendRedirect(
                    httpServletRequest.getContextPath() + "/" + notFoundUrl);
            return false;
        }

        //进行用户是否有这个权限的认证
        if (subject.isAuthenticated()) {
            // 认证通过
            if (subject.isPermitted(url)) {
                // 拥有权限
                return true;
            } else {
                // 无权限
                httpServletResponse.sendRedirect(
                        httpServletRequest.getContextPath() + "/" + unauthorizedUrl);
                return false;
            }
        } else {
            // 认证不通过
            httpServletResponse.sendRedirect(
                    httpServletRequest.getContextPath() + "/" + unauthenticatedUrl);
            return false;
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
            throws Exception {
        return false;
    }

    public void setMaintenanceUrl(String maintenanceUrl) {
        this.maintenanceUrl = maintenanceUrl;
    }

    public String getNotFoundUrl() {
        return notFoundUrl;
    }

    public void setNotFoundUrl(String notFoundUrl) {
        this.notFoundUrl = notFoundUrl;
    }
}
