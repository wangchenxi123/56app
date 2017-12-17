package com.mierro.authority.Interceptor;

import com.mierro.authority.shiro.ShiroOnlineLimited;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
public class OnlineUsersDetectionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //判断用户是否被踢出
        if (httpServletRequest.getSession().getAttribute("kickout") != null){
            httpServletResponse.sendRedirect(
                    httpServletRequest.getContextPath() + "/" + ShiroOnlineLimited.kickoutUrl);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
