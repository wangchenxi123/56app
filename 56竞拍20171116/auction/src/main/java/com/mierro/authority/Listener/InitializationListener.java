package com.mierro.authority.Listener;

import com.mierro.authority.Listener.AuthorityInitSerivce.AuthorityInitSerivce;
import com.mierro.authority.Listener.Thread.AuthorityThread;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by Simba on 2017/3/31.
 */
@WebListener
public class InitializationListener implements ServletContextListener {

    private Integer mark= 1;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
        AuthorityInitSerivce initService = (AuthorityInitSerivce)springContext.getBean("AuthorityInitSerivce");
        if (mark == 1){
            System.out.println("/************************初始化权限模块数据***************************/");
            initService.addSuperAdmin();
            initService.addClientRols();
            //证明进入初始化函数(保证只开启一个线程)
            if (mark == 1) {
                Thread thread = new AuthorityThread(initService);
                thread.start();
                mark = 2;
            }
            System.out.println("/***********************启动权限模块守护线程***************************/");
            System.out.println("/************************权限模块初始化完成***************************/");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("/*************************权限模块销毁完成****************************/");
    }
}
