package com.mierro.authority.Interceptor;

import com.mierro.authority.controller.SystemModelHandler;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 黄晓滨 simba on 2017/7/20.
 * Remarks：对需要登录的接口进行是否登录检测
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        Long userId = SystemModelHandler.getCurrentUser().getId();
        if (userId == null){
            httpServletResponse.sendRedirect(
                    httpServletRequest.getContextPath() + "/error/notLogin");
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
