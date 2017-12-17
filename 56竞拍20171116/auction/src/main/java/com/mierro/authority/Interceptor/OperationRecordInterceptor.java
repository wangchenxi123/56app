package com.mierro.authority.Interceptor;

import com.mierro.authority.controller.SystemModelHandler;
import com.mierro.authority.service.OperationRecordService;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 黄晓滨 simba on 2017/7/18.
 * Remarks：操作记录拦截器
 */
public class OperationRecordInterceptor implements HandlerInterceptor {

    @Resource
    private OperationRecordService operationRecordService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if (httpServletResponse.getStatus() == 200 ) {
            if (!(httpServletRequest.getMethod().contains("GET") || httpServletRequest.getMethod().contains("get"))){
                Long userId = SystemModelHandler.getCurrentUser().getId();
                operationRecordService.addOperationRecord(userId, httpServletRequest
                        , httpServletResponse);
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
